package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import com.corwinjv.mobtotems.KeyBindings;
import com.corwinjv.mobtotems.items.BaubleItem;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;


/**
 * Created by CorwinJV on 1/31/2016.
 */
public class WolfTotemBauble extends BaubleItem {
    private static final String WOLF_SUMMONED = "WOLF_SUMMONED";
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

        initNbtData(stack);
    }

    @Override
    protected void initNbtData(ItemStack stack)
    {
        super.initNbtData(stack);
        NBTTagCompound tagCompound = stack.getTagCompound();
        tagCompound.setBoolean(WOLF_SUMMONED, false);

        stack.setTagCompound(tagCompound);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity)
    {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if(nbtTagCompound == null)
        {
            return;
        }

        if(entity instanceof EntityPlayer)
        {
            if(KeyBindings.keys[KeyBindings.ACTIVATE_BAUBLE_KEY].isPressed())
            {
                EntityPlayer player = (EntityPlayer) entity;
                if(!nbtTagCompound.getBoolean(WOLF_SUMMONED)
                        && nbtTagCompound.getInteger(CHARGE_LEVEL) > 0)
                {
                    Vec3i facingVec = player.getHorizontalFacing().getDirectionVec();
                    double posX = player.posX + (facingVec.getX() * SPAWN_DISTANCE);
                    double posY = player.posY + (facingVec.getY() * SPAWN_DISTANCE);
                    double posZ = player.posZ + (facingVec.getZ() * SPAWN_DISTANCE);

                    mSpiritWolf = (EntityWolf)ItemMonsterPlacer.spawnCreature(player.worldObj, "Wolf", posX, posY, posZ);
                    // tameSpiritWolf(player);
                    nbtTagCompound.setBoolean(WOLF_SUMMONED, true);
                }
                else if(nbtTagCompound.getBoolean(WOLF_SUMMONED))
                {
                    if(mSpiritWolf != null)
                    {
                        mSpiritWolf.setDead();
                        mSpiritWolf = null;
                    }
                    nbtTagCompound.setBoolean(WOLF_SUMMONED, false);
                }
            }
        }
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
