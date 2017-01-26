package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class EnderLogic extends TotemLogic {
    @Override
    public List<ItemStack> getCost() {
        {
            List<ItemStack> cost = new ArrayList<>();
            return cost;
        }
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }
}
