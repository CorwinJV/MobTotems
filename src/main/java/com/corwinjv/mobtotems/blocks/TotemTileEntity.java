package com.corwinjv.mobtotems.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

/**
 * Created by CorwinJV on 2/18/2016.
 */
public class TotemTileEntity extends TileEntity implements ITickable
{
    private static int SLAVE_COUNT = 2;

    // TODO: Move these to an NBTHelper Class?
    private static String IS_MASTER_TAG = "is_master";
    private static String MASTER_POS_TAG = "master_pos";
    private static String SLAVE_ONE_TAG = "slave_one";
    private static String SLAVE_TWO_TAG = "slave_two";

    private boolean mIsMaster = false;
    private BlockPos mMasterPos = null;
    private BlockPos mSlaveOnePos = null;
    private BlockPos mSlaveTwoPos = null;

    public TotemTileEntity()
    {
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

    public void markForUpdate()
    {
        worldObj.markBlockForUpdate(pos);
        markDirty();
    }

    public void clearData()
    {
        mIsMaster = false;
        mMasterPos = null;
        mSlaveOnePos = null;
        mSlaveTwoPos = null;
        markForUpdate();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(IS_MASTER_TAG, mIsMaster);
        saveBlockPos(nbtTagCompound, MASTER_POS_TAG, mMasterPos);
        saveBlockPos(nbtTagCompound, SLAVE_ONE_TAG, mSlaveOnePos);
        saveBlockPos(nbtTagCompound, SLAVE_TWO_TAG, mSlaveTwoPos);
    }

    private void saveBlockPos(NBTTagCompound nbtTagCompound, String key, BlockPos pos)
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

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        mIsMaster = nbtTagCompound.getBoolean(IS_MASTER_TAG);
        mMasterPos = readBlockPos(nbtTagCompound, MASTER_POS_TAG);
        mSlaveOnePos = readBlockPos(nbtTagCompound, SLAVE_ONE_TAG);
        mSlaveTwoPos = readBlockPos(nbtTagCompound, SLAVE_TWO_TAG);
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

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
//        FMLLog.log(Level.WARN, "getDescriptionPacket() - nbtTagCompound: " + nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.pos, 0, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    public void setupMultiBlock()
    {
        for(int y = pos.getY(); y >= pos.getY() - SLAVE_COUNT; y--)
        {
            TileEntity totemTileEntity = worldObj.getTileEntity(new BlockPos(pos.getX(), y, pos.getZ()));
            if(totemTileEntity != null
                    && totemTileEntity instanceof TotemTileEntity)
            {
                if(((TotemTileEntity) totemTileEntity).canBeMaster())
                {
                    // TODO: I kinda don't like the way I wrote this. Make this stuff more generalized.
                    TotemTileEntity masterEntity = (TotemTileEntity)totemTileEntity;
                    TotemTileEntity slaveOneEntity = (TotemTileEntity)worldObj.getTileEntity(totemTileEntity.getPos().add(0, 1, 0));
                    TotemTileEntity slaveTwoEntity = (TotemTileEntity)worldObj.getTileEntity(totemTileEntity.getPos().add(0, 2, 0));

                    masterEntity.setIsMaster(true);
                    masterEntity.setMasterPos(masterEntity.pos);
                    masterEntity.setSlaveOne(slaveOneEntity.getPos());
                    masterEntity.setSlaveTwo(slaveTwoEntity.getPos());

                    slaveOneEntity.setIsMaster(false);
                    slaveOneEntity.setMasterPos(masterEntity.pos);
                    slaveOneEntity.setSlaveOne(slaveOneEntity.getPos());
                    slaveOneEntity.setSlaveTwo(slaveTwoEntity.getPos());

                    slaveTwoEntity.setIsMaster(false);
                    slaveTwoEntity.setMasterPos(masterEntity.pos);
                    slaveTwoEntity.setSlaveOne(slaveOneEntity.getPos());
                    slaveTwoEntity.setSlaveTwo(slaveTwoEntity.getPos());
                    return;
                }
            }
        }
    }

    public boolean canBeMaster()
    {
        for(int y = pos.getY(); y <= pos.getY() + SLAVE_COUNT; y++)
        {
            TileEntity tileEntity = worldObj.getTileEntity(new BlockPos(pos.getX(), y, pos.getZ()));
            if(tileEntity == null
                    || !(tileEntity instanceof TotemTileEntity))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update()
    {
        // Client side only particle effects
        if(worldObj.isRemote)
        {
            if(mMasterPos != null)
            {
                ArrayList<BlockPos> spawnPoints = new ArrayList<BlockPos>();
                spawnPoints.add(pos.add(0.0, 0.0, 0.0));
                spawnPoints.add(pos.add(1.0, 0.0, 0.0));
                spawnPoints.add(pos.add(0.0, 0.0, 1.0));
                spawnPoints.add(pos.add(1.0, 0.0, 1.0));

                for(int i = 0; i < spawnPoints.size(); i++)
                {
                    BlockPos spawnPos = spawnPoints.get(i);
                    double offsetAmount = 0.05f;
                    double offX = offsetAmount;
                    double offZ = offsetAmount;
                    if(i % 2 == 0)
                    {
                        offX = -offsetAmount;
                    }
                    if(i == 0 || i == 1)
                    {
                       offZ = -offsetAmount;
                    }
                    worldObj.spawnParticle(EnumParticleTypes.PORTAL, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), offX, 0.0, offZ);
                }
            }
        }
    }


    public void onBreakBlock()
    {
        if(worldObj != null)
        {
            TileEntity masterEntity = null;
            if(mMasterPos != null)
            {
                masterEntity = worldObj.getTileEntity(mMasterPos);
            }

            TileEntity slaveOneEntity = null;
            if(mSlaveOnePos != null)
            {
                slaveOneEntity = worldObj.getTileEntity(mSlaveOnePos);
            }

            TileEntity slaveTwoEntity = null;
            if(mSlaveTwoPos != null)
            {
                slaveTwoEntity = worldObj.getTileEntity(mSlaveTwoPos);
            }

            if(masterEntity != null
                    && masterEntity instanceof TotemTileEntity)
            {
                ((TotemTileEntity)masterEntity).clearData();
            }

            if(slaveOneEntity != null
                    && slaveOneEntity instanceof TotemTileEntity)
            {
                ((TotemTileEntity)slaveOneEntity).clearData();
            }

            if(slaveTwoEntity != null
                    && slaveTwoEntity instanceof TotemTileEntity)
            {
                ((TotemTileEntity)slaveTwoEntity).clearData();
            }
        }
    }
}
