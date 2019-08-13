package com.corwinjv.mobtotems.blocks.tiles.base;

import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import com.corwinjv.mobtotems.interfaces.IMultiblock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
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

    public ModMultiblockInventoryTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        tagCompound = super.write(tagCompound);
        tagCompound.putBoolean(IS_MASTER, isMaster);

        ListNBT slaveList = new ListNBT();
        for (BlockPos slavePos : slaves) {
            slaveList.add(new LongNBT(slavePos.toLong()));
        }
        tagCompound.put(SLAVES, slaveList);
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        isMaster = tagCompound.getBoolean(IS_MASTER);

        slaves.clear();
        ListNBT tagList = tagCompound.getList(SLAVES, Constants.NBT.TAG_LONG);
        for (int i = 0; i < tagList.size(); i++) {
            INBT tag = tagList.get(i);
            BlockPos blockPos = BlockPos.fromLong(((LongNBT) tag).getLong());
            slaves.add(blockPos);
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
            if(hasWorld()) {
                TileEntity te = world.getTileEntity(slavePos);
                if (te != null
                        && te instanceof TotemTileEntity) {
                    ((TotemTileEntity) te).setMaster(null);
                }
            }
        }
        setSlaves(new ArrayList<>());
        setIsMaster(false);
    }
}
