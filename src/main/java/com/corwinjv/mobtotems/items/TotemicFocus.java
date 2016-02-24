package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.blocks.BaseBlock;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemicFocus extends BaseItem
{
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote)
        {
            Block targetBlock = BlockUtils.getBlock(world, pos);
            if (targetBlock != null
                    && targetBlock instanceof BlockLog)
            {
                world.destroyBlock(pos, false);
                BaseBlock blockToPlace = ModBlocks.getBlock(ModBlocks.TOTEM_WOOD);
                if(blockToPlace != null)
                {
                    world.setBlockState(pos, blockToPlace.getDefaultState());
                }
            }
        }
        return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
    }
}
