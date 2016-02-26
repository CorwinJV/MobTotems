package com.corwinjv.mobtotems.datamodel;

import com.corwinjv.mobtotems.items.TotemStencil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

/**
 * Created by CorwinJV on 2/25/2016.
 */
public class TotemTileEntityData
{
    private static String IS_MASTER_TAG = "is_master";
    private static String MASTER_POS_TAG = "master_pos";
    private static String SLAVE_ONE_TAG = "slave_one";
    private static String SLAVE_TWO_TAG = "slave_two";
    private static String TOTEM_TYPE_TAG = "totem_type_tag";

    private boolean mIsMaster = false;
    private BlockPos mMasterPos = null;
    private BlockPos mSlaveOnePos = null;
    private BlockPos mSlaveTwoPos = null;
    private int mTotemType = TotemStencil.NULL_STENCIL_META;

    private INBTAuthor mAuthor = null;

    public TotemTileEntityData(INBTAuthor author)
    {
        mAuthor = author;
    }

    public boolean getIsMaster()
    {
        return mIsMaster;
    }

    public void setIsMaster(boolean isMaster)
    {
        mIsMaster = isMaster;
        markForUpdate();
    }

    public BlockPos getMasterPos()
    {
        return mMasterPos;
    }

    public void setMasterPos(BlockPos masterPos)
    {
        mMasterPos = masterPos;
        markForUpdate();
    }

    public BlockPos getSlaveOne()
    {
        return mSlaveOnePos;
    }

    public void setSlaveOne(BlockPos slaveOnePos)
    {
        mSlaveOnePos = slaveOnePos;
        markForUpdate();
    }

    public BlockPos getSlaveTwo()
    {
        return mSlaveOnePos;
    }

    public void setSlaveTwo(BlockPos slaveTwoPos)
    {
        mSlaveTwoPos = slaveTwoPos;
        markForUpdate();
    }

    public int getTotemType()
    {
        return mTotemType;
    }

    public void setTotemType(int totemType)
    {
        mTotemType = totemType;
        markForUpdate();
    }

    private void markForUpdate()
    {
        if(mAuthor != null)
        {
            mAuthor.markForUpdate();
        }
    }

    public void clearData()
    {
        mIsMaster = false;
        mMasterPos = null;
        mSlaveOnePos = null;
        mSlaveTwoPos = null;
        mTotemType = TotemStencil.NULL_STENCIL_META;
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setBoolean(IS_MASTER_TAG, mIsMaster);
        writeBlockPos(nbtTagCompound, MASTER_POS_TAG, mMasterPos);
        writeBlockPos(nbtTagCompound, SLAVE_ONE_TAG, mSlaveOnePos);
        writeBlockPos(nbtTagCompound, SLAVE_TWO_TAG, mSlaveTwoPos);
        nbtTagCompound.setInteger(TOTEM_TYPE_TAG, mTotemType);
    }

    private void writeBlockPos(NBTTagCompound nbtTagCompound, String key, BlockPos pos)
    {
        if(pos != null)
        {
            nbtTagCompound.setLong(key, pos.toLong());
        }
        else
        {
            nbtTagCompound.setLong(key, 0);
        }
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        mIsMaster = nbtTagCompound.getBoolean(IS_MASTER_TAG);
        mMasterPos = readBlockPos(nbtTagCompound, MASTER_POS_TAG);
        mSlaveOnePos = readBlockPos(nbtTagCompound, SLAVE_ONE_TAG);
        mSlaveTwoPos = readBlockPos(nbtTagCompound, SLAVE_TWO_TAG);
        mTotemType = nbtTagCompound.getInteger(TOTEM_TYPE_TAG);
    }

    private BlockPos readBlockPos(NBTTagCompound nbtTagCompound, String key)
    {
        BlockPos retBlockPos = null;
        long serializedBlockPos = nbtTagCompound.getLong(key);
        if(serializedBlockPos != 0)
        {
            retBlockPos = BlockPos.fromLong(serializedBlockPos);
        }
        return retBlockPos;
    }
}
