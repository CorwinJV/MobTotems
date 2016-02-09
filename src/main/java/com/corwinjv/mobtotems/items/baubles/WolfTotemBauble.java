package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import com.corwinjv.mobtotems.KeyBindings;
import com.corwinjv.mobtotems.items.BaubleItem;
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

import java.util.UUID;


/**
 * Created by CorwinJV on 1/31/2016.
 */
public class WolfTotemBauble extends BaubleItem {
    private static final String WOLF_ID = "WOLF_ID";
    private static final String WOLF_TOTEM_COMPOUND = "WOLF_TOTEM_COMPOUND";
    private static final int SPAWN_DISTANCE = 3;

    private EntityWolf mSpiritWolf = null;

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
        FMLLog.log(Level.WARN, "WolfTotemBauble onCreated()");

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

    private void initWolfEntity(ItemStack stack, World world)
    {
        if(!world.isRemote
                && mSpiritWolf == null)
        {
            NBTTagCompound tagCompound = stack.getTagCompound();
            NBTTagCompound wolfTotemCompound = tagCompound.getCompoundTag(WOLF_TOTEM_COMPOUND);
            if (wolfTotemCompound != null)
            {
                String wolfId = wolfTotemCompound.getString(WOLF_TOTEM_COMPOUND);
                FMLLog.log(Level.INFO, "wolfId: " + wolfId);
                if (!StringUtils.isNullOrEmpty(wolfId))
                {
                    UUID wolfUUID = UUID.fromString(wolfId);
                    for (Entity entity : world.getLoadedEntityList())
                    {
                        if (entity instanceof EntityWolf
                                && entity.getPersistentID() == wolfUUID)
                        {
                            FMLLog.log(Level.INFO, "Found entityWolf with matching UUID, setting mSpiritWolf");
                            mSpiritWolf = (EntityWolf) entity;
                        }
                    }
                }
            }
        }
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

            if(mSpiritWolf == null
                    && tagCompound.getInteger(CHARGE_LEVEL) > 0)
            {
                FMLLog.log(Level.INFO, "mSpiritWolf is null and charge is greater than zero");

                Vec3i facingVec = player.getHorizontalFacing().getDirectionVec();
                double posX = player.posX + (facingVec.getX() * SPAWN_DISTANCE);
                double posY = player.posY + (facingVec.getY() * SPAWN_DISTANCE);
                double posZ = player.posZ + (facingVec.getZ() * SPAWN_DISTANCE);

                mSpiritWolf = (EntityWolf)ItemMonsterPlacer.spawnCreature(player.worldObj, "Wolf", posX, posY, posZ);
                tameSpiritWolf(player);
                wolfTotemCompound.setString(WOLF_ID, mSpiritWolf.getPersistentID().toString());
            }
            else if(mSpiritWolf != null)
            {
                FMLLog.log(Level.INFO, "mSpiritWolf ain't dead. Killing and saving now.");

                mSpiritWolf.setDead();
                mSpiritWolf = null;
                wolfTotemCompound.setString(WOLF_ID, "");
            }
            tagCompound.setTag(WOLF_TOTEM_COMPOUND, wolfTotemCompound);
        }
    }


    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity)
    {
        initWolfEntity(stack, entity.worldObj);
    }

    // TODO: Move to custom SpiritWolf Entity when I get around to writing one
    private void tameSpiritWolf(EntityPlayer ownerPlayer)
    {
        mSpiritWolf.setTamed(true);
        mSpiritWolf.getNavigator().clearPathEntity();
        mSpiritWolf.setAttackTarget(null);
        mSpiritWolf.getAISit().setSitting(true);
        mSpiritWolf.setHealth(20.0F);
        mSpiritWolf.setOwnerId(ownerPlayer.getUniqueID().toString());
        mSpiritWolf.worldObj.setEntityState(mSpiritWolf, (byte)7);
    }
}
