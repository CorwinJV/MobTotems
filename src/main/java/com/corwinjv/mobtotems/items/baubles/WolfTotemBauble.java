package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import com.corwinjv.mobtotems.entities.EntitySpiritWolf;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by CorwinJV on 1/31/2016.
 */
public class WolfTotemBauble extends BaubleItem
{
    private static final String WOLF_ID = "WOLF_ID";
    private static final String WOLF_TOTEM_COMPOUND = "WOLF_TOTEM_COMPOUND";
    private static final int SPAWN_DISTANCE = 3;
    private static final long UPDATE_TICKS = 20;

    private static final int CHARGE_COST_PER_TICK = 1;

    public WolfTotemBauble()
    {
        super();
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack)
    {
        return BaubleType.AMULET;
    }


    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        super.onCreated(stack, world, player);
    }

    @Override
    protected NBTTagCompound initNbtData(ItemStack stack)
    {
        super.initNbtData(stack);
        NBTTagCompound tagCompound = stack.getTagCompound();

        if(tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }

        NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
        wolfTotemCompound.setString(WOLF_ID, "");
        tagCompound.setTag(WOLF_TOTEM_COMPOUND, wolfTotemCompound);

        stack.setTagCompound(tagCompound);
        return tagCompound;
    }

    private boolean hasValidNbt(ItemStack stack)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null)
        {
            FMLLog.log(Level.WARN, "WolfTotemBauble onBaubleActivated() no tagCompound");
            return false;
        }
        return true;
    }

    private void setWolfId(@Nonnull ItemStack stack, @Nonnull String wolfId)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }
        NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
        wolfTotemCompound.setString(WOLF_ID, wolfId);
    }

    @Nullable
    private EntitySpiritWolf getWolf(@Nonnull ItemStack stack, @Nonnull World world)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null)
        {
            NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
            String wolfId = wolfTotemCompound.getString(WOLF_ID);
            if (!StringUtils.isNullOrEmpty(wolfId))
            {
                final UUID wolfUUID = UUID.fromString(wolfId);
                Predicate<EntitySpiritWolf> spiritWolfForUUID = new Predicate<EntitySpiritWolf>() {
                    @Override
                    public boolean apply(@Nullable EntitySpiritWolf input)
                    {
                        return (input != null && input.getUniqueID().equals(wolfUUID));
                    }
                };

                List<EntitySpiritWolf> entities = new ArrayList<EntitySpiritWolf>(world.getEntities(EntitySpiritWolf.class, spiritWolfForUUID));
                if(entities.size() > 0)
                {
                    return entities.get(0);
                }
            }
        }
        return null;
    }

    @Override
    public void onBaubleActivated(ItemStack stack, EntityPlayer player)
    {
        if(!player.worldObj.isRemote)
        {
            if(!hasValidNbt(stack))
            {
                return;
            }

            NBTTagCompound tagCompound = stack.getTagCompound();
            NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);

            EntitySpiritWolf spiritWolf = getWolf(stack, player.worldObj);
            if(spiritWolf == null
                    && tagCompound.getInteger(CHARGE_LEVEL) > 0)
            {
                //FMLLog.log(Level.INFO, "spiritWolf is null and charge is greater than zero");
                Vec3i facingVec = player.getHorizontalFacing().getDirectionVec();
                double posX = player.posX + (facingVec.getX() * SPAWN_DISTANCE);
                double posY = player.posY + (facingVec.getY() * SPAWN_DISTANCE);
                double posZ = player.posZ + (facingVec.getZ() * SPAWN_DISTANCE);

                spiritWolf = spawnSpiritWolf(player.worldObj, posX, posY, posZ);
                tameSpiritWolf(spiritWolf, player);

                wolfTotemCompound.setString(WOLF_ID, spiritWolf.getPersistentID().toString());
            }
            else if(spiritWolf != null)
            {
                //FMLLog.log(Level.INFO, "spiritWolf ain't dead. Killing and saving now.");
                spiritWolf.setDead();
                wolfTotemCompound.setString(WOLF_ID, "");
            }
            tagCompound.setTag(WOLF_TOTEM_COMPOUND, wolfTotemCompound);
        }
    }

    // Spirit Wolf Spawning
    private EntitySpiritWolf spawnSpiritWolf(World worldIn, double x, double y, double z)
    {
        EntityLiving entityliving = new EntitySpiritWolf(worldIn);
        entityliving.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        entityliving.rotationYawHead = entityliving.rotationYaw;
        entityliving.renderYawOffset = entityliving.rotationYaw;
        entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
        worldIn.spawnEntityInWorld(entityliving);
        entityliving.playLivingSound();

        return (EntitySpiritWolf)entityliving;
    }

    // TODO: Move to custom SpiritWolf Entity when I get around to writing one
    private void tameSpiritWolf(EntitySpiritWolf spiritWolf, EntityPlayer ownerPlayer)
    {
        spiritWolf.setTamed(true);
        spiritWolf.getNavigator().clearPathEntity();
        spiritWolf.setAttackTarget(null);
        spiritWolf.getAISit().setSitting(false);
        spiritWolf.setHealth(20.0F);
        spiritWolf.setOwnerId(ownerPlayer.getUniqueID());
        spiritWolf.worldObj.setEntityState(spiritWolf, (byte)7);
        spiritWolf.setInitialized(true);
    }

    /**
     * This method is called once per tick if the bauble is being worn by a player
     */
    public void onWornTick(ItemStack stack, EntityLivingBase player)
    {
        super.onWornTick(stack, player);
        World world = player.getEntityWorld();
        if(!world.isRemote)
        {
            long worldTime = world.getWorldInfo().getWorldTime();
            EntitySpiritWolf spiritWolf = getWolf(stack, world);

            if(worldTime % UPDATE_TICKS == 0)
            {
                if(spiritWolf != null)
                {
                    decrementChargeLevel(stack, CHARGE_COST_PER_TICK);
                }
            }

            // Should wolf still be summoned? This should happen every tick
            if(spiritWolf != null
                    && getChargeLevel(stack) == 0)
            {
                spiritWolf.setDead();
                setWolfId(stack, "");
            }
        }
    }

    /**
     * This method is called when the bauble is unequipped by a player
     */
    public void onUnequipped(ItemStack stack, EntityLivingBase player)
    {
        if(!player.getEntityWorld().isRemote)
        {
            EntitySpiritWolf spiritWolf = getWolf(stack, player.getEntityWorld());
            if(spiritWolf != null)
            {
                spiritWolf.setDead();
            }
            setWolfId(stack, "");
        }
    }

    Predicate<EntityItem> wolfTotemBaublePredicate = new Predicate<EntityItem>() {
        @Override
        public boolean apply(@Nullable EntityItem input) {
            return (input != null) && (input.getEntityItem().getItem() instanceof WolfTotemBauble);
        }
    };

}
