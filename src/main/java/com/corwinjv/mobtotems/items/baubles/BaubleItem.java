package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.corwinjv.mobtotems.interfaces.IChargeable;
import com.corwinjv.mobtotems.items.ModItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by CorwinJV on 1/31/2016.
 */
@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class BaubleItem extends ModItem implements IBauble, IChargeable {
    protected static final String CHARGE_LEVEL = "CHARGE_LEVEL";

    public BaubleItem() {
        super();
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        initNbtData(stack);
    }

    protected NBTTagCompound initNbtData(ItemStack stack) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if (nbtTagCompound == null) {
            nbtTagCompound = new NBTTagCompound();
        }
        nbtTagCompound.setInteger(CHARGE_LEVEL, 0);

        stack.setTagCompound(nbtTagCompound);
        return nbtTagCompound;
    }

    @SuppressWarnings("all")
    @Override
    public int getChargeLevel(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = initNbtData(stack);
        }
        return tagCompound.getInteger(CHARGE_LEVEL);
    }

    @Override
    public void setChargeLevel(ItemStack stack, int chargeLevel) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = initNbtData(stack);
        }
        tagCompound.setInteger(CHARGE_LEVEL, chargeLevel);
    }

    @Override
    public void decrementChargeLevel(ItemStack stack, int amount) {
        int chargeLevel = getChargeLevel(stack);
        chargeLevel -= amount;
        if (chargeLevel < 0) {
            chargeLevel = 0;
        }

        setChargeLevel(stack, chargeLevel);
    }

    @Override
    public void incrementChargeLevel(ItemStack stack, int amount) {
        int chargeLevel = getChargeLevel(stack);
        chargeLevel += amount;
        if (chargeLevel > getMaxChargeLevel()) {
            chargeLevel = getMaxChargeLevel();
        }
        setChargeLevel(stack, chargeLevel);
    }

    @Override
    public int getMaxChargeLevel() {
        return 16;
    }

    public void onBaubleActivated(ItemStack stack, PlayerEntity player) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.TRINKET;
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
