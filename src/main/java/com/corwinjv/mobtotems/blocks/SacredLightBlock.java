package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.SacredLightTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by CorwinJV on 7/13/2016.
 */
public class SacredLightBlock extends ModBlock implements ITileEntityProvider {
    public SacredLightBlock(Block.Properties properties) {
        super(properties);
//        super(Material.CIRCUITS);
//        this.setHardness(2.0F);
//        this.setSoundType(SoundType.WOOD);
//
//        this.isBlockContainer = true;
    }

    // Rendering stuff
    public boolean isVisuallyOpaque() {
        return false;
    }

    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
        return false;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;

        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double) Direction.UP.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) Direction.UP.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public int getLightValue(@Nullable BlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos) {
        return 15;
    }

    // Can only place on the tops of solid things
    private boolean canPlaceOn(World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        if (state.isSideSolid(worldIn, pos, Direction.UP)) {
            return true;
        } else {
            return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
        }
    }

    // If block below is a totem that is a slave to an offering box, invalidate the multiblock
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, BlockState state) {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
        TileEntity te = worldIn.getTileEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
        if (te != null
                && te instanceof TotemTileEntity) {
            TileEntity master = (TileEntity) ((TotemTileEntity) te).getMaster();
            if (master != null
                    && master instanceof OfferingBoxTileEntity) {
                ((OfferingBoxTileEntity) master).invalidateSlaves();
                ((OfferingBoxTileEntity) master).verifyMultiblock();
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        return this.canPlaceAt(worldIn, pos, Direction.UP);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, Direction facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        return this.canPlaceOn(worldIn, blockpos);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }

    // Okay, so I see that neighborChanged is deprecated but it appears that onNeighborChange(IBlockAccess, BlockPos, BlockPos)
    // doesn't get called when a sand block is broken under the SacredLightBlock
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        this.checkForDrop(worldIn, pos, state);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, BlockState state) {
        if (this.canPlaceAt(worldIn, pos, Direction.UP)) {
            return true;
        } else {
            if (worldIn.getBlockState(pos).getBlock() == this) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new SacredLightTileEntity();
    }
}
