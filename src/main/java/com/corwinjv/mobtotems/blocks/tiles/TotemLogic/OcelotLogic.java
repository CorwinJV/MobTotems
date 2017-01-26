package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class OcelotLogic extends TotemLogic {
    @Override
    public List<ItemStack> getCost() {
        {
            List<ItemStack> cost = new ArrayList<>();
            cost.add(new ItemStack(Items.FISH, 2, 0));
            return cost;
        }
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.MODIFIER;
    }

    @Nonnull
    @Override
    public Modifiers adjustModifiers(Modifiers modifiers) {
        modifiers.range += 1.0f;
        return modifiers;
    }
}
