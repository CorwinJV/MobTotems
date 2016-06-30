package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.TotemStencil;
import com.corwinjv.mobtotems.utils.TotemConsts;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

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
    private static String TOTEM_TYPE_TAG = "totem_type_tag";

    private boolean mIsMaster = false;
    private BlockPos mMasterPos = null;
    private BlockPos mSlaveOnePos = null;
    private BlockPos mSlaveTwoPos = null;
    private int mTotemType = TotemStencil.NULL_STENCIL_META;

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

    public int getTotemType()
    {
        return mTotemType;
    }

    public void setTotemType(int totemType)
    {
        FMLLog.log(Level.WARN, "setTotemType: " + totemType);
        mTotemType = totemType;
        markForUpdate();
    }

    public void markForUpdate()
    {
        // TODO: find way to do this in 1.9 ? Maybe not needed anymore?
        // I can't figure out how to get the blocks to send their packets to the client
        //worldObj.markBlockForUpdate(pos);
        markDirty();
    }

    public void clearData()
    {
        mIsMaster = false;
        mMasterPos = null;
        mSlaveOnePos = null;
        mSlaveTwoPos = null;
        mTotemType = TotemStencil.NULL_STENCIL_META;
        markForUpdate();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(IS_MASTER_TAG, mIsMaster);
        saveBlockPos(nbtTagCompound, MASTER_POS_TAG, mMasterPos);
        saveBlockPos(nbtTagCompound, SLAVE_ONE_TAG, mSlaveOnePos);
        saveBlockPos(nbtTagCompound, SLAVE_TWO_TAG, mSlaveTwoPos);
        nbtTagCompound.setInteger(TOTEM_TYPE_TAG, mTotemType);

        return nbtTagCompound;
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

    // TODO: Reimplement TileEntity syncing?
    // It looks like forge has changed a lot of this networking code. Commenting out to get it running.
//    @Override
//    public Packet getDescriptionPacket()
//    {
//        NBTTagCompound nbtTagCompound = new NBTTagCompound();
//        writeToNBT(nbtTagCompound);
//        FMLLog.log(Level.WARN, "getDescriptionPacket() - nbtTagCompound: " + nbtTagCompound);
//        return new SPacketUpdateTileEntity(this.pos, 0, nbtTagCompound);
//    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet)
    {
        FMLLog.log(Level.WARN, "onDataPacket() called... packet: " + packet.toString());
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
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
                    || !(tileEntity instanceof TotemTileEntity)
                    || ((TotemTileEntity) tileEntity).getMasterPos() != null)
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
        if(mMasterPos != null)
        {
            if(worldObj.isRemote)
            {
                spawnParticleEffects();
            }
            else
            {
                performTotemEffect();
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

    public void performTotemEffect()
    {
        // TODO: Refactor this class heavily, but especially this method

        // Set up effect list
        ArrayList<Integer> effectList = new ArrayList<Integer>();

        TileEntity totemTileEntity = worldObj.getTileEntity(mMasterPos);
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            effectList.add(((TotemTileEntity) totemTileEntity).getTotemType());
        }

        totemTileEntity = worldObj.getTileEntity(mSlaveOnePos);
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            effectList.add(((TotemTileEntity) totemTileEntity).getTotemType());
        }

        totemTileEntity = worldObj.getTileEntity(mSlaveTwoPos);
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            effectList.add(((TotemTileEntity) totemTileEntity).getTotemType());
        }

        // Set how often we should perform the effect
        int tickMod = 20;
        if(effectList.contains(TotemStencil.RABBIT_STENCIL_META))
        {
            tickMod = 10;
        }

        // Set our range
        int range = TotemConsts.DEFAULT_RANGE;
        if(effectList.contains(TotemStencil.SLIME_STENCIL_META))
        {
            range += TotemConsts.SLIME_RANGE_MODIFIER;
        }

        // Perform effect
        if(worldObj.getWorldTime() % tickMod == 0)
        {
            // Active effect groups
            if(effectList.contains(TotemStencil.CREEPER_STENCIL_META))
            {
                Class creeperClass = (Class) EntityList.getClassFromID(EntityList.getIDFromString("Creeper"));
                List entitiesWithinAABB = worldObj.getEntitiesWithinAABB(creeperClass,
                        new AxisAlignedBB(pos.getX() - range,
                                pos.getY() - range,
                                pos.getZ() - range,
                                pos.getX() + range,
                                pos.getY() + range,
                                pos.getZ() + range));

                // FMLLog.log(Reference.MOD_NAME, Level.INFO, String.format("Entities found: %d", entitiesWithinAABB.size()));

                for(int i = 0; i < entitiesWithinAABB.size(); i++)
                {
                    EntityLivingBase creeperToBurn = (EntityLivingBase)entitiesWithinAABB.get(i);
                    if(!creeperToBurn.isBurning())
                    {
                        creeperToBurn.setFire(3);
                    }
                }
            }
            if(effectList.contains(TotemStencil.WOLF_STENCIL_META))
            {
                // TODO: Implement wolf totem block
                // If an enemy comes within range
                // And the totem multiblock has enough mana
                // And a wolf is not already summoned by this multiblock
                // Summon wolf in an aggressive stance to fight off the enemy
                    // This wolf disappears if it runs out of range
                    // This wolf drains mana while it is summoned
                    // This wolf disappears when the totem multiblock no longer has enough mana
            }
        }
    }

    public void spawnParticleEffects()
    {
        // This stuff is so totally temp it's not even funny.
        // I just wanted to procastinate on doing a special entity renderer w/ custom model that I used
        // minecraft's particles in a few weird ways.
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

            switch(mTotemType)
            {
                case TotemStencil.CREEPER_STENCIL_META:
                {
                    worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ(), offX, 0.0, offZ);
                    break;
                }
                case TotemStencil.RABBIT_STENCIL_META:
                {
                    worldObj.spawnParticle(EnumParticleTypes.REDSTONE, spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ(), offX, 0.5, offZ);
                    worldObj.spawnParticle(EnumParticleTypes.REDSTONE, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), offX, 0.5, offZ);
                    break;
                }
                case TotemStencil.SLIME_STENCIL_META:
                {
                    if(worldObj.getWorldTime() % 10 == 0
                            && worldObj.rand.nextInt(10) < 5)
                    {
                        worldObj.spawnParticle(EnumParticleTypes.SLIME, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), offX, 0.0, offZ);
                    }
                    break;
                }
                case TotemStencil.WOLF_STENCIL_META:
                {
                    if(worldObj.getWorldTime() % 20 == 0
                            && worldObj.rand.nextInt(10) < 3)
                    {
                        worldObj.spawnParticle(EnumParticleTypes.HEART, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), offX, 0.0, offZ);
                    }
                    break;
                }
                default:
                {
                    worldObj.spawnParticle(EnumParticleTypes.PORTAL, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), offX, 0.0, offZ);
                    break;
                }
            }
        }
    }
}