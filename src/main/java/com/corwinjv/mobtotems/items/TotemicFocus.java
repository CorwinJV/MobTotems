package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemicFocus extends ModItem {
    public TotemicFocus(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getWorld().isRemote) {
            Block targetBlock = BlockUtils.getBlock(context.getWorld(), context.getPos());
            if (targetBlock != null
                    && targetBlock instanceof LogBlock) {
                context.getWorld().destroyBlock(context.getPos(), false);
                Block blockToPlace = ModBlocks.TOTEM_WOOD;
                if (blockToPlace != null) {
                    context.getWorld().setBlockState(context.getPos(), blockToPlace.getDefaultState());
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    // TODO: Not sure if this is required at all anymore...
    public boolean canItemEditBlocks() {
        return true;
    }
}
