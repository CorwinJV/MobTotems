package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import com.corwinjv.mobtotems.KeyBindings;
import com.corwinjv.mobtotems.items.BaubleItem;
import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.UUID;


/**
 * Created by CorwinJV on 1/31/2016.
 */
public class WolfTotemBauble extends BaubleItem {
    private static final String WOLF_ID = "WOLF_ID";
    private static final String WOLF_TOTEM_COMPOUND = "WOLF_TOTEM_COMPOUND";
    private static final int SPAWN_DISTANCE = 3;

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
        //FMLLog.log(Level.WARN, "WolfTotemBauble onCreated()");
        initNbtData(stack);
    }

    @Override
    protected void initNbtData(ItemStack stack)
    {
        super.initNbtData(stack);
        NBTTagCompound tagCompound = stack.getTagCompound();

        NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
        if(wolfTotemCompound == null)
        {
            wolfTotemCompound = new NBTTagCompound();
            wolfTotemCompound.setString(WOLF_ID, "");
            tagCompound.setTag(WOLF_TOTEM_COMPOUND, wolfTotemCompound);
        }

        stack.setTagCompound(tagCompound);
    }

    private EntityWolf getWolf(ItemStack stack, World world)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
        if (wolfTotemCompound != null)
        {
            String wolfId = wolfTotemCompound.getString(WOLF_ID);
            if (!StringUtils.isNullOrEmpty(wolfId))
            {
                final UUID wolfUUID = UUID.fromString(wolfId);
                Predicate<EntityWolf> searchPredicate = new Predicate<EntityWolf>() {
                    @Override
                    public boolean apply(@Nullable EntityWolf input)
                    {
                        if(input != null)
                        {
                            return input.getUniqueID().equals(wolfUUID);
                        }
                        return false;
                    }
                };

                for(EntityWolf entity : world.getEntities(EntityWolf.class, searchPredicate))
                {
                    //FMLLog.log(Level.INFO, "Found entityWolf with matching UUID, returning wolf");
                    return (EntityWolf)entity;
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
            NBTTagCompound tagCompound = stack.getTagCompound();
            if(tagCompound == null)
            {
                FMLLog.log(Level.WARN, "WolfTotemBauble onBaubleActivated() no tagCompound");
                return;
            }
            NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
            if(wolfTotemCompound == null)
            {
                FMLLog.log(Level.WARN, "WolfTotemBauble onBaubleActivated() no wolfTotemCompound");
                return;
            }

            EntityWolf spiritWolf = getWolf(stack, player.worldObj);
            if(spiritWolf == null
                    && tagCompound.getInteger(CHARGE_LEVEL) > 0)
            {
                //FMLLog.log(Level.INFO, "spiritWolf is null and charge is greater than zero");
                Vec3i facingVec = player.getHorizontalFacing().getDirectionVec();
                double posX = player.posX + (facingVec.getX() * SPAWN_DISTANCE);
                double posY = player.posY + (facingVec.getY() * SPAWN_DISTANCE);
                double posZ = player.posZ + (facingVec.getZ() * SPAWN_DISTANCE);

                spiritWolf = (EntityWolf)ItemMonsterPlacer.spawnCreature(player.worldObj, "Wolf", posX, posY, posZ);
                tameSpiritWolf(spiritWolf, player);

                //FMLLog.log(Level.INFO, "Saving spiritWolf id: " + spiritWolf.getPersistentID().toString());
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


    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity)
    {
    }

    // TODO: Move to custom SpiritWolf Entity when I get around to writing one
    private void tameSpiritWolf(EntityWolf spiritWolf, EntityPlayer ownerPlayer)
    {
        spiritWolf.setTamed(true);
        spiritWolf.getNavigator().clearPathEntity();
        spiritWolf.setAttackTarget(null);
        spiritWolf.getAISit().setSitting(true);
        spiritWolf.setHealth(20.0F);
        spiritWolf.setOwnerId(ownerPlayer.getUniqueID().toString());
        spiritWolf.worldObj.setEntityState(spiritWolf, (byte)7);
    }
}
