package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.blocks.BaseBlock;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemicFocus extends BaseItem
{
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote)
        {
            Block targetBlock = getBlock(world, pos);
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

    //TODO: Move this somewhere sensible
    public static Block getBlock(World world, BlockPos pos)
    {
        Block targetBlock = null;
        if (pos != null)
        {
            Chunk targetChunk = world.getChunkFromBlockCoords(pos);
            if (targetChunk != null)
            {
                targetBlock = targetChunk.getBlock(pos);
            }
        }
        return targetBlock;
    }
}
