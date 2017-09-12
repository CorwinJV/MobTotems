package com.corwinjv.mobtotems.blocks.tiles.base;

import com.corwinjv.mobtotems.interfaces.IMultiblock;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public abstract class ModMultiblockTileEntity<T> extends ModTileEntity implements IMultiblock<T> {
    protected boolean isMaster = false;
    protected List<BlockPos> slaves = new ArrayList<BlockPos>();
    protected BlockPos masterPos = null;

    public ModMultiblockTileEntity() {
        super();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        tagCompound = super.writeToNBT(tagCompound);
        tagCompound.setBoolean(IS_MASTER, isMaster);

        NBTTagList slaveList = new NBTTagList();
        for (BlockPos slavePos : slaves) {
            slaveList.appendTag(new NBTTagLong(slavePos.toLong()));
        }
        tagCompound.setTag(SLAVES, slaveList);

        if (masterPos != null) {
            tagCompound.setLong(MASTER_POS, masterPos.toLong());
        }
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        isMaster = tagCompound.getBoolean(IS_MASTER);

        NBTTagList tagList = tagCompound.getTagList(SLAVES, Constants.NBT.TAG_LONG);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTBase tag = tagList.get(i);
            if (tag instanceof NBTTagLong) {
                BlockPos blockPos = BlockPos.fromLong(((NBTTagLong) tag).getLong());
                slaves.add(blockPos);
            }
        }

        masterPos = BlockPos.fromLong(tagCompound.getLong(MASTER_POS));
    }

    @Override
    public boolean getIsMaster() {
        return isMaster;
    }

    @Override
    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
        markDirty();
    }

    public void setSlaves(List<BlockPos> slaves) {
        this.slaves = slaves;
        markDirty();
    }

    @Override
    public void setMaster(IMultiblock<T> master) {
        if (master instanceof TileEntity) {
            masterPos = ((TileEntity) master).getPos();
        } else {
            masterPos = null;
        }
        markDirty();
    }

    @Override
    public IMultiblock<T> getMaster() {
        if (masterPos != null) {
            try {
                TileEntity te = world.getTileEntity(masterPos);
                if (te != null
                        && te instanceof IMultiblock) {
                    return (IMultiblock) te;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
