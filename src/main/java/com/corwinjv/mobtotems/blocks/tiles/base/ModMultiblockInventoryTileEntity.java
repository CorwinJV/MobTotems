package com.corwinjv.mobtotems.blocks.tiles.base;

import com.corwinjv.mobtotems.interfaces.IMultiblock;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public abstract class ModMultiblockInventoryTileEntity extends ModInventoryTileEntity implements IMultiblock {
    protected boolean isMaster = false;

    public ModMultiblockInventoryTileEntity()
    {
        super();
    }

    @Override
    public boolean getIsMaster() {
        return isMaster;
    }

    @Override
    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }
}
