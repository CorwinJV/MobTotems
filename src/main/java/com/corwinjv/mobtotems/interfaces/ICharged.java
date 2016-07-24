package com.corwinjv.mobtotems.interfaces;

import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public interface ICharged
{
    int getChargeLevel(ItemStack stack);
    void setChargeLevel(ItemStack stack, int chargeLevel);
    void decrementChargeLevel(ItemStack stack, int amount);
    void incrementChargeLevel(ItemStack stack, int amount);
}
