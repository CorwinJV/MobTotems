package com.corwinjv.mobtotems.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemWood extends BaseBlock implements ITileEntityProvider
{
    public TotemWood()
    {
        super(Material.wood);
        this.isBlockContainer = true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        if(!world.isRemote)
        {
            TileEntity totemTileEntity = world.getTileEntity(pos);
            if(totemTileEntity != null
                    && totemTileEntity instanceof TotemTileEntity)
            {
                ((TotemTileEntity)totemTileEntity).setupMultiBlock();
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TotemTileEntity();
    }


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState)
    {
        TileEntity totemTileEntity = world.getTileEntity(pos);
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            ((TotemTileEntity)totemTileEntity).onBreakBlock();
        }
    }

}
