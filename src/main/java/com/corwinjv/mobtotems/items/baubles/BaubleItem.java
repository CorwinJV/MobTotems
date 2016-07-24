package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.corwinjv.mobtotems.interfaces.ICharged;
import com.corwinjv.mobtotems.items.ModItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by CorwinJV on 1/31/2016.
 */
@Optional.Interface(modid = "Baubles", iface = "baubles.api.IBauble")
public class BaubleItem extends ModItem implements IBauble, ICharged
{
    protected static final String CHARGE_LEVEL = "CHARGE_LEVEL";
    protected static final int MAX_CHARGE_LEVEL = 16;
    protected static final int INIT_CHARGE_LEVEL = 0;

    public BaubleItem()
    {
        super();
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        initNbtData(stack);
    }

    protected void initNbtData(ItemStack stack)
    {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if(nbtTagCompound == null)
        {
            nbtTagCompound = new NBTTagCompound();
        }
        nbtTagCompound.setInteger(CHARGE_LEVEL, INIT_CHARGE_LEVEL);

        stack.setTagCompound(nbtTagCompound);
    }

    @Override
    public int getChargeLevel(ItemStack stack)
    {
        int chargeLevel = 0;
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }
        if(tagCompound.hasKey(CHARGE_LEVEL))
        {
            chargeLevel = tagCompound.getInteger(CHARGE_LEVEL);
        }
        return chargeLevel;
    }

    @Override
    public void setChargeLevel(ItemStack stack, int chargeLevel)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }

        tagCompound.setInteger(CHARGE_LEVEL, chargeLevel);
    }

    @Override
    public void decrementChargeLevel(ItemStack stack, int amount)
    {
        int chargeLevel = getChargeLevel(stack);
        chargeLevel -= amount;
        if(chargeLevel < 0)
        {
            chargeLevel = 0;
        }

        setChargeLevel(stack, chargeLevel);
    }

    @Override
    public void incrementChargeLevel(ItemStack stack, int amount)
    {
        int chargeLevel = getChargeLevel(stack);
        chargeLevel += amount;
        if(chargeLevel > MAX_CHARGE_LEVEL)
        {
            chargeLevel = MAX_CHARGE_LEVEL;
        }
        setChargeLevel(stack, chargeLevel);
    }

    public void onBaubleActivated(ItemStack stack, EntityPlayer player)
    {
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        if(stack.getTagCompound() == null)
        {
            initNbtData(stack);
        }

        // TODO: Use locale strings
        int chargeLevel = getChargeLevel(stack);
        tooltip.add("Charge Level: " + chargeLevel + "/" + MAX_CHARGE_LEVEL);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
