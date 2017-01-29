package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public class OfferingBox extends ModBlockContainer
{
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0.5/16.0, 0, 0.5D/16.0, 15.5D/16.0, 3.0D/16.0, 15.5D/16.0);

    public OfferingBox()
    {
        super();
        this.setHardness(1.0f);
        this.isBlockContainer = true;
        this.setLightOpacity(0);
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return null;
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
    private boolean canPlaceOn(World worldIn, BlockPos pos) {

        IBlockState state = worldIn.getBlockState(pos);
        boolean canPlaceAt = (state.getBlock().isBlockSolid(worldIn, pos, EnumFacing.UP)
                || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos))
                && state.getBlock().isFullBlock(state);

        BlockPos adjustedPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

        // Can't place if there is already an offering box who has claimed this
        if (canPlaceAt) {
            // Check for adjacent totem
            for (EnumFacing direction : EnumFacing.HORIZONTALS) {
                Vec3i dirVec = direction.getDirectionVec();
                BlockPos posToCheck = new BlockPos(adjustedPos.getX() + dirVec.getX(), adjustedPos.getY(), adjustedPos.getZ() + dirVec.getZ());
                TileEntity te = worldIn.getTileEntity(posToCheck);

                if (te != null
                        && te instanceof TotemTileEntity
                        && ((TotemTileEntity) te).getMaster() != null) {

                    TileEntity master = (TileEntity)((TotemTileEntity) te).getMaster();
                    FMLLog.log(Level.ERROR, "masterPos: " + master.getPos() + " adjustedPos: " + adjustedPos);
                    if(!master.getPos().equals(adjustedPos)) {
                        canPlaceAt = false;
                        break;
                    }
                }
            }
        }
        return canPlaceAt;
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
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TileEntity te = world.getTileEntity(pos);
        if(te != null
                && te instanceof OfferingBoxTileEntity) {
            for(BlockPos slavePos : ((OfferingBoxTileEntity) te).getSlaves()) {
                TileEntity te2 = world.getTileEntity(slavePos);
                if(te2 instanceof TotemTileEntity) {
                    ((TotemTileEntity) te2).setMaster(null);
                }
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    public boolean checkForDrop(World world, BlockPos pos, IBlockState state)
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


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof OfferingBoxTileEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (OfferingBoxTileEntity)te);
        }
        world.removeTileEntity(pos);
    }

    // TE
    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta)
    {
        return new OfferingBoxTileEntity();
    }
}
