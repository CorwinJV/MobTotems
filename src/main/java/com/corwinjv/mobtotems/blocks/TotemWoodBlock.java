package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
    public static final PropertyEnum<TotemType> TOTEM_TYPE = PropertyEnum.create("totem_type", TotemType.class);

    TotemWoodBlock()
    {
        super(Material.WOOD);
        this.isBlockContainer = true;
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(getDefaultState().withProperty(TOTEM_TYPE, TotemType.NONE));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {TOTEM_TYPE});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TOTEM_TYPE, TotemType.fromMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TotemType)state.getValue(TOTEM_TYPE)).getMeta();
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
