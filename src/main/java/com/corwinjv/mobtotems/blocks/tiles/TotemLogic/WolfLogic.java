package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class WolfLogic extends TotemLogic {
    @Override
    public List<ItemStack> getCost() {
        List<ItemStack> cost = new ArrayList<>();
        cost.add(new ItemStack(Items.BONE, 4, null));
        return cost;
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }
}
