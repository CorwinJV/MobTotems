package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.tiles.SacredLightTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 7/13/2016.
 */
public class SacredLightBlock extends ModBlock implements ITileEntityProvider
{
    public SacredLightBlock()
    {
        super(Material.CIRCUITS);
        MinecraftForge.EVENT_BUS.register(new EntityJoinWorldHandler());
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

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    // Can only place on the tops of solid things
    private boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.isSideSolid(worldIn, pos, EnumFacing.UP))
        {
            return true;
        }
        else
        {
            return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
        }
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

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    // Okay, so I see that neighborChanged is deprecated but it appears that onNeighborChange(IBlockAccess, BlockPos, BlockPos)
    // doesn't get called when a sand block is broken under the SacredLightBlock
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.canPlaceAt(worldIn, pos, EnumFacing.UP))
        {
            return true;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }

    // This block limits what entities can spawn near it, we subscribe to the EntityJoinWorldEvent in order to stop entity spawning
    // based on proximity to TileEntities of our block's type
    public class EntityJoinWorldHandler
    {
        @SubscribeEvent
        public void onEntityJoinWorldEvent(EntityJoinWorldEvent e)
        {
            if(!e.getWorld().isRemote)
            {
                for(TileEntity tileEntity : e.getWorld().loadedTileEntityList)
                {
                    if(tileEntity instanceof SacredLightTileEntity)
                    {
                        if(!((SacredLightTileEntity)tileEntity).canSpawnMobHere(e.getEntity()))
                        {
                            e.setCanceled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta)
    {
        return new SacredLightTileEntity();
    }
}
