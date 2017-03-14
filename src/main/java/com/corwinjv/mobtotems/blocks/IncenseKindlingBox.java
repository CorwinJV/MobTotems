package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public class IncenseKindlingBox extends ModBlock implements ITileEntityProvider {
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0.2D, 0.D, 0.2D, 0.8D, 0.5D, 0.8D);

    public IncenseKindlingBox() {
        super();
        this.setHardness(2.0F);
        this.isBlockContainer = true;
    }

    // Collision
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return boundingBox;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    // Rendering stuff
    public boolean isVisuallyOpaque() {
        return false;
    }

    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    // Can only place on the tops of solid things
    private boolean canPlaceOn(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return (state.getBlock().isBlockSolid(worldIn, pos, EnumFacing.UP)
                || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos))
                && state.getBlock().isFullBlock(state);
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        return this.canPlaceAt(worldIn, pos, EnumFacing.UP);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        return this.canPlaceOn(worldIn, blockpos);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        this.checkForDrop(worldIn, pos, state);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (this.canPlaceAt(worldIn, pos, EnumFacing.UP)) {
            return true;
        } else {
            if (worldIn.getBlockState(pos).getBlock() == this) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }

    // TE
    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new IncenseKindlingBoxTileEntity();
    }

    // Consumable block!
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

}
