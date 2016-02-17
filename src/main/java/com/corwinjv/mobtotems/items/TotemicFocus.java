package com.corwinjv.mobtotems.items;

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
                // TODO: Spawn TotemWood instead of glass
                world.destroyBlock(pos, false);
                world.setBlockState(pos, Blocks.glass.getDefaultState());
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
