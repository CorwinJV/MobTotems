package com.corwinjv.mobtotems.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public interface IChargeable
{
    int getChargeLevel(ItemStack stack);
    void setChargeLevel(ItemStack stack, int chargeLevel);
    void decrementChargeLevel(ItemStack stack, int amount);
    void incrementChargeLevel(ItemStack stack, int amount);
    int getMaxChargeLevel();
}
