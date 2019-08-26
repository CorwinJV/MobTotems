package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class LlamaLogic extends TotemLogic {
    public static final int DAMAGE_MODIFIER = 2;

    @Override
    public List<ItemStack> getCost() {
        List<ItemStack> cost = new ArrayList<>();
        // TODO: After the 'flattening' how can I make the cost be a any of a set of items? Any carpet should do for this offering
        cost.add(new ItemStack(Blocks.WHITE_CARPET, 3, null));
        return cost;
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.MODIFIER;
    }

    @Nonnull
    @Override
    public Modifiers adjustModifiers(Modifiers modifiers) {
        modifiers.damage += 1.0f;
        return modifiers;
    }
}
