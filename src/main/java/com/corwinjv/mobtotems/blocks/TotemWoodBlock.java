package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.items.TotemWoodItemBlock;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemWoodBlock extends ModBlock implements ITileEntityProvider
{
    public static final PropertyEnum<TotemType> TOTEM_TYPE = PropertyEnum.create("totem_type", TotemType.class);
    public static final int MAX_MULTIBLOCK_RANGE = 9;

    TotemWoodBlock()
    {
        super(Material.WOOD);
        this.isBlockContainer = true;
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(getDefaultState().withProperty(TOTEM_TYPE, TotemType.NONE));
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for(int i = 0; i < 11; i++)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
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
        return state.getValue(TOTEM_TYPE).getMeta();
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta)
    {
        return new TotemTileEntity(TotemType.fromMeta(meta));
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        for(int i = worldIn.loadedTileEntityList.size() - 1; i >= 0; i--)
        {
            TileEntity te = worldIn.loadedTileEntityList.get(i);
            if(te instanceof OfferingBoxTileEntity
                    && te.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < MAX_MULTIBLOCK_RANGE)
            {
                ((OfferingBoxTileEntity)te).verifyMultiblock();
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        for(int i = worldIn.loadedTileEntityList.size() - 1; i >= 0; i--)
        {
            TileEntity te = worldIn.loadedTileEntityList.get(i);
            if(te instanceof OfferingBoxTileEntity
                && te.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < MAX_MULTIBLOCK_RANGE)
            {
                ((OfferingBoxTileEntity)te).verifyMultiblock();
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
        for(int i = worldIn.loadedTileEntityList.size() - 1; i >= 0; i--)
        {
            TileEntity te = worldIn.loadedTileEntityList.get(i);
            if(te instanceof OfferingBoxTileEntity
                    && te.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < MAX_MULTIBLOCK_RANGE)
            {
                ((OfferingBoxTileEntity)te).verifyMultiblock();
            }
        }
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
