package com.corwinjv.mobtotems.items.baubles;

import baubles.api.BaubleType;
import com.corwinjv.mobtotems.items.BaubleItem;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 1/31/2016.
 */
public class WolfTotemBauble extends BaubleItem {
    public WolfTotemBauble()
    {
        super();
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack)
    {
        return BaubleType.AMULET;
    }
}
