package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockTileEntity;

/**
 * Created by CorwinJV on 2/18/2016.
 */
public class TotemTileEntity extends ModMultiblockTileEntity
{
    public TotemTileEntity() { }

    @Override
    public boolean getIsMaster() {
        return false;
    }
}
