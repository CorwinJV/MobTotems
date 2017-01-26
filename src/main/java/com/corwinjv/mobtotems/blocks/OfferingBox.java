package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
public class OfferingBox extends ModBlockContainer
{
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0.2D, 0.D, 0.2D, 0.8D, 0.5D, 0.8D);

    public OfferingBox()
    {
        super();
        this.setHardness(2.0F);
        this.isBlockContainer = true;
        this.setLightOpacity(0);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean retVal = false;

        if(!worldIn.isRemote) {
            playerIn.openGui(MobTotems.instance, Reference.GUI_ID.OFFERING_BOX.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return retVal;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
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


    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isVisuallyOpaque()
    {
        return false;
    }

    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }

    // Can only place on the tops of solid things
    private boolean canPlaceOn(World worldIn, BlockPos pos)
    {
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
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos)
    {
        return this.canPlaceAt(worldIn, pos, EnumFacing.UP);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        return this.canPlaceOn(worldIn, blockpos);
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        this.checkForDrop(world, pos, state);

        TileEntity te = world.getTileEntity(pos);
        if(te instanceof OfferingBoxTileEntity)
        {
            ((OfferingBoxTileEntity)te).verifyMultiblock();
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    private boolean checkForDrop(World world, BlockPos pos, IBlockState state)
    {
        if (this.canPlaceAt(world, pos, EnumFacing.UP))
        {
            return true;
        }
        else
        {
            if (world.getBlockState(pos).getBlock() == this)
            {
                TileEntity te = world.getTileEntity(pos);
                if(te instanceof OfferingBoxTileEntity)
                {
                    InventoryHelper.dropInventoryItems(world, pos, (OfferingBoxTileEntity)te);
                }
                this.dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
            }

            return false;
        }
    }

    // TE
    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta)
    {
        return new OfferingBoxTileEntity();
    }
}
