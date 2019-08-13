package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public class OfferingBox extends ModBlockContainer {
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0.5 / 16.0, 0, 0.5D / 16.0, 15.5D / 16.0, 3.0D / 16.0, 15.5D / 16.0);

    public OfferingBox(Block.Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return null;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        boolean retVal = true;
        if (!worldIn.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return null;
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    //return new OfferingBox();
                    return null;
                }
            }, pos);
//            NetworkHooks.openGui(MobTotems.instance, Reference.GUI_ID.OFFERING_BOX.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return retVal;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    // Collision
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess world, BlockPos pos) {
        return boundingBox;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return super.getCollisionShape(state, worldIn, pos, context);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return super.getRenderLayer();
    }

    // Rendering stuff
    @Override
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isVisuallyOpaque() {
        return false;
    }

    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
        return false;
    }

    // Can only place on the tops of solid things
    private boolean canPlaceOn(World worldIn, BlockPos pos) {

        BlockState state = worldIn.getBlockState(pos);
        boolean canPlaceAt = (state.getBlock().canSpawnInBlock()
                || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos))
                && state.getBlock().isFullBlock(state);

        BlockPos adjustedPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

        // Can't place if there is already an offering box who has claimed this
        if (canPlaceAt) {
            // Check for adjacent totem
            for (Direction direction : Direction.HORIZONTALS) {
                Vec3i dirVec = direction.getDirectionVec();
                BlockPos posToCheck = new BlockPos(adjustedPos.getX() + dirVec.getX(), adjustedPos.getY(), adjustedPos.getZ() + dirVec.getZ());
                TileEntity te = worldIn.getTileEntity(posToCheck);

                if (te != null
                        && te instanceof TotemTileEntity
                        && ((TotemTileEntity) te).getMaster() != null) {

                    TileEntity master = (TileEntity) ((TotemTileEntity) te).getMaster();
                    //FMLLog.log(Level.ERROR, "masterPos: " + master.getPos() + " adjustedPos: " + adjustedPos);
                    if (!master.getPos().equals(adjustedPos)) {
                        canPlaceAt = false;
                        break;
                    }
                }
            }
        }
        return canPlaceAt;
    }

    @Override
    public boolean canPlaceTorchOnTop(BlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }


    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        return this.canPlaceAt(worldIn, pos, Direction.UP);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, Direction facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        return this.canPlaceOn(worldIn, blockpos);
    }

    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        this.checkForDrop(world, pos, state);

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof OfferingBoxTileEntity) {
            ((OfferingBoxTileEntity) te).verifyMultiblock();
        }
    }

    @Override
    public boolean isSideSolid(BlockState base_state, IBlockAccess world, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null
                && te instanceof OfferingBoxTileEntity) {
            for (BlockPos slavePos : ((OfferingBoxTileEntity) te).getSlaves()) {
                TileEntity te2 = world.getTileEntity(slavePos);
                if (te2 instanceof TotemTileEntity) {
                    ((TotemTileEntity) te2).setMaster(null);
                }
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        this.checkForDrop(worldIn, pos, state);
    }

    public boolean checkForDrop(World world, BlockPos pos, BlockState state) {
        if (this.canPlaceAt(world, pos, Direction.UP)) {
            return true;
        } else {
            if (world.getBlockState(pos).getBlock() == this) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof OfferingBoxTileEntity) {
                    InventoryHelper.dropInventoryItems(world, pos, (OfferingBoxTileEntity) te);
                }
                this.dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
            }

            return false;
        }
    }


    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof OfferingBoxTileEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (OfferingBoxTileEntity) te);
        }
        world.removeTileEntity(pos);
    }

    // TE
    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new OfferingBoxTileEntity();
    }
}
