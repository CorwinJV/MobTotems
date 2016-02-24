package com.corwinjv.mobtotems.utils;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Created by CorwinJV on 2/24/2016.
 */
public class BlockUtils
{
    // Returns an instance of the block at the given position in the given world
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
