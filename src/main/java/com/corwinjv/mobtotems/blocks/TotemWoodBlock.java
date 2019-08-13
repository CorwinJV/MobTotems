package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemWoodBlock extends ModBlock implements ITileEntityProvider {
    public static final PropertyEnum<TotemType> TOTEM_TYPE = PropertyEnum.create("totem_type", TotemType.class);
    public static final int MAX_MULTIBLOCK_RANGE = 9;

    TotemWoodBlock(Block.Properties properties) {
        super(properties);
//        this.isBlockContainer = true;
//        this.setHardness(2.0F);
//        this.setSoundType(SoundType.WOOD);
//        this.setDefaultState(getDefaultState().withProperty(TOTEM_TYPE, TotemType.NONE));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < TotemType.values().length; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{TOTEM_TYPE});
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TOTEM_TYPE, TotemType.fromMeta(meta));
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(TOTEM_TYPE).getMeta();
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new TotemTileEntity(TotemType.fromMeta(meta));
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        //FMLLog.log(Level.ERROR, "onNeighborChange at pos" + pos);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state) {
        for (int i = worldIn.loadedTileEntityList.size() - 1; i >= 0; i--) {
            TileEntity te = worldIn.loadedTileEntityList.get(i);
            if (te instanceof OfferingBoxTileEntity
                    && te.getPos().getDistance(pos.getX(), pos.getY(), pos.getZ()) < MAX_MULTIBLOCK_RANGE) {
                ((OfferingBoxTileEntity) te).verifyMultiblock();

                BlockPos offeringBoxPos = te.getPos();
                Block offeringBoxBlock = worldIn.getBlockState(offeringBoxPos).getBlock();
                if (offeringBoxBlock instanceof OfferingBox) {
                    ((OfferingBox) offeringBoxBlock).checkForDrop(worldIn, offeringBoxPos, worldIn.getBlockState(offeringBoxPos));
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, BlockState state) {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
        for (int i = worldIn.loadedTileEntityList.size() - 1; i >= 0; i--) {
            TileEntity te = worldIn.loadedTileEntityList.get(i);
            if (te instanceof OfferingBoxTileEntity
                    && te.getPos().getDistance(pos.getX(), pos.getY(), pos.getZ()) < MAX_MULTIBLOCK_RANGE) {
                ((OfferingBoxTileEntity) te).invalidateSlaves();
                ((OfferingBoxTileEntity) te).verifyMultiblock();
            }
        }
    }

    @Override
    public int damageDropped(BlockState state) {
        return getMetaFromState(state);
    }

    // Rendering stuff
    public boolean isVisuallyOpaque() {
        return false;
    }

    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState blockState) {
        super.breakBlock(world, pos, blockState);
    }
}
