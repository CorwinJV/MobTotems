package com.corwinjv.mobtotems.blocks;


import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class ModBlockContainer extends ContainerBlock {
    public ModBlockContainer(Properties props) {
        super(props);
        this.init();
    }

    public void init() {
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader iBlockReader) {
        return null;
    }
}
