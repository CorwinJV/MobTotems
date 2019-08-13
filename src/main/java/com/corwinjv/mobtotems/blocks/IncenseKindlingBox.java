package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.FaceDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public class IncenseKindlingBox extends ModBlock {
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0.2D, 0.D, 0.2D, 0.8D, 0.5D, 0.8D);

    public IncenseKindlingBox(Block.Properties properties) {
        super(properties);
        //this.setHardness(2.0F);
        //this.isBlockContainer = true;
    }


//    // Collision
//    @Nonnull
//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess world, BlockPos pos) {
//        return boundingBox;
//    }

    // Rendering stuff
    public boolean isVisuallyOpaque() {
        return false;
    }

//    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
//        return false;
//    }

    // Can only place on the tops of solid things
    private boolean canPlaceOn(World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        return (state.getBlock().canSpawnInBlock());
//                || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos))
//                && state.getBlock().full(state);
    }

//    @Override
//    public boolean canPlaceTorchOnTop(BlockState state, IBlockAccess world, BlockPos pos) {
//        return false;
//    }
//
//    @Override
//    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
//        return this.canPlaceAt(worldIn, pos, Direction.UP);
//    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, Direction facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        return this.canPlaceOn(worldIn, blockpos);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
//        this.checkForDrop(worldIn, pos, state);
//    }
//
//    @Override
//    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
//        super.onNeighborChange(world, pos, neighbor);
//    }

    private boolean checkForDrop(World worldIn, BlockPos pos, BlockState state) {
        if (this.canPlaceAt(worldIn, pos, Direction.UP)) {
            return true;
        } else {
            if (worldIn.getBlockState(pos).getBlock() == this) {
                //this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.removeBlock(pos, false);
            }

            return false;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IncenseKindlingBoxTileEntity();
    }

    //    // Consumable block!
//    @Override
//    public int quantityDropped(Random random) {
//        return 0;
//    }
//
}
