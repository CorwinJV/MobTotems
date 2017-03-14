package com.corwinjv.mobtotems.blocks.tiles.base;

import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
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
public abstract class ModMultiblockInventoryTileEntity<T> extends ModInventoryTileEntity implements IMultiblock<T> {

    protected boolean isMaster = false;
    protected List<BlockPos> slaves = new ArrayList<>();

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        tagCompound = super.writeToNBT(tagCompound);
        tagCompound.setBoolean(IS_MASTER, isMaster);

        NBTTagList slaveList = new NBTTagList();
        for (BlockPos slavePos : slaves) {
            slaveList.appendTag(new NBTTagLong(slavePos.toLong()));
        }
        tagCompound.setTag(SLAVES, slaveList);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        isMaster = tagCompound.getBoolean(IS_MASTER);

        slaves.clear();
        NBTTagList tagList = tagCompound.getTagList(SLAVES, Constants.NBT.TAG_LONG);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTBase tag = tagList.get(i);
            if (tag instanceof NBTTagLong) {
                BlockPos blockPos = BlockPos.fromLong(((NBTTagLong) tag).getLong());
                slaves.add(blockPos);
            }
        }
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

    public void invalidateSlaves() {
        for (BlockPos slavePos : getSlaves()) {
            TileEntity te = world.getTileEntity(slavePos);
            if (te != null
                    && te instanceof TotemTileEntity) {
                ((TotemTileEntity) te).setMaster(null);
            }
        }
        setSlaves(new ArrayList<>());
        setIsMaster(false);
    }
}
