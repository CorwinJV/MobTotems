package com.corwinjv.mobtotems.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CorwinJV on 2/24/2016.
 */
public class BlockUtils {
    // Returns an instance of the block at the given position in the given world
    public static Block getBlock(World world, BlockPos pos) {
        Block targetBlock = null;
        if (pos != null) {
            targetBlock = world.getBlockState(pos).getBlock();
        }
        return targetBlock;
    }

    public static boolean isAreaSolid(EntityPlayer player, BlockPos centerPos) {
        for (double x = centerPos.getX() - 1; x <= centerPos.getX() + 1; x++) {
            for (double z = centerPos.getZ() - 1; z <= centerPos.getZ() + 1; z++) {
                IBlockState blockState = player.world.getBlockState(new BlockPos(x, player.posY, z));
                if (blockState.getMaterial().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }
}
