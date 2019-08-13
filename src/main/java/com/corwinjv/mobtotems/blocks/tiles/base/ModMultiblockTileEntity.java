package com.corwinjv.mobtotems.blocks.tiles.base;

import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.interfaces.IMultiblock;
import net.minecraft.nbt.CompoundNBT;
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
public abstract class ModMultiblockTileEntity<T> extends ModTileEntity implements IMultiblock<T> {
    protected boolean isMaster = false;
    protected List<BlockPos> slaves = new ArrayList<BlockPos>();
    protected BlockPos masterPos = null;

    public ModMultiblockTileEntity(TileEntityType<?> type) {
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

        if (masterPos != null) {
            tagCompound.putLong(MASTER_POS, masterPos.toLong());
        }
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        isMaster = tagCompound.getBoolean(IS_MASTER);

        ListNBT tagList = tagCompound.getList(SLAVES, Constants.NBT.TAG_LONG);
        for (int i = 0; i < tagList.size(); i++) {
            BlockPos blockPos = BlockPos.fromLong(((LongNBT) tagList.get(i)).getLong());
            slaves.add(blockPos);
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
