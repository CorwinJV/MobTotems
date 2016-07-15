package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemWoodBlock extends ModBlock implements ITileEntityProvider
{
    TotemWoodBlock()
    {
        super(Material.WOOD);
        this.isBlockContainer = true;
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta)
    {
        return new TotemTileEntity();
    }

    // Rendering stuff
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState blockState)
    {
        super.breakBlock(world, pos, blockState);
    }

}
