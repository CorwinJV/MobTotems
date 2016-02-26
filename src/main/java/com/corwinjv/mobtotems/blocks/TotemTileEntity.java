package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.datamodel.INBTAuthor;
import com.corwinjv.mobtotems.datamodel.TotemTileEntityData;
import com.corwinjv.mobtotems.items.TotemStencil;
import com.corwinjv.mobtotems.utils.TotemConsts;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 2/18/2016.
 */
public class TotemTileEntity extends TileEntity implements ITickable, INBTAuthor
{
    private static int SLAVE_COUNT = 2;

    private TotemTileEntityData mData = new TotemTileEntityData(this);

    public TotemTileEntity() {}

    public static TotemTileEntity getTotemTileEntity(World world, BlockPos pos)
    {
        TileEntity entity = world.getTileEntity(pos);
        if(entity == null
                || !(entity instanceof TotemTileEntity))
        {
            return null;
        }
        return (TotemTileEntity)entity;
    }

    public TotemTileEntityData getData()
    {
        return mData;
    }

    // INBTAuthor
    public void markForUpdate()
    {
        worldObj.markBlockForUpdate(pos);
        markDirty();
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        mData.writeToNBT(nbtTagCompound);
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        mData.readFromNBT(nbtTagCompound);
    }

        @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        // FMLLog.log(Level.WARN, "getDescriptionPacket() - nbtTagCompound: " + nbtTagCompound);
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

                    TotemTileEntityData data = masterEntity.getData();
                    data.setIsMaster(true);
                    data.setMasterPos(masterEntity.pos);
                    data.setSlaveOne(slaveOneEntity.getPos());
                    data.setSlaveTwo(slaveTwoEntity.getPos());

                    data = slaveOneEntity.getData();
                    data.setIsMaster(false);
                    data.setMasterPos(masterEntity.pos);
                    data.setSlaveOne(slaveOneEntity.getPos());
                    data.setSlaveTwo(slaveTwoEntity.getPos());

                    data = slaveTwoEntity.getData();
                    data.setIsMaster(false);
                    data.setMasterPos(masterEntity.pos);
                    data.setSlaveOne(slaveOneEntity.getPos());
                    data.setSlaveTwo(slaveTwoEntity.getPos());
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
                    || ((TotemTileEntity) tileEntity).getData().getMasterPos() != null)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update()
    {
        if(mData.getMasterPos()!= null)
        {
            // Client
            if(worldObj.isRemote)
            {
                spawnParticleEffects();
            }
            // Server
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
            TotemTileEntity entity = null;
            if(mData.getMasterPos() != null)
            {
                entity = getTotemTileEntity(worldObj, mData.getMasterPos());
                if(entity != null)
                {
                    entity.getData().clearData();
                }
            }

            if(mData.getSlaveOne() != null)
            {
                entity = getTotemTileEntity(worldObj, mData.getSlaveOne());
                if(entity != null)
                {
                    entity.getData().clearData();
                }
            }

            if(mData.getSlaveTwo() != null)
            {
                entity = getTotemTileEntity(worldObj, mData.getSlaveTwo());
                if(entity != null)
                {
                    entity.getData().clearData();
                }
            }
        }
    }

    public void performTotemEffect()
    {
        // TODO: Refactor this class heavily, but especially this method

        // Set up effect list
        ArrayList<Integer> effectList = new ArrayList<Integer>();

        TileEntity totemTileEntity = worldObj.getTileEntity(mData.getMasterPos());
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            effectList.add(((TotemTileEntity) totemTileEntity).getData().getTotemType());
        }

        totemTileEntity = worldObj.getTileEntity(mData.getSlaveOne());
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            effectList.add(((TotemTileEntity) totemTileEntity).getData().getTotemType());
        }

        totemTileEntity = worldObj.getTileEntity(mData.getSlaveTwo());
        if(totemTileEntity != null
                && totemTileEntity instanceof TotemTileEntity)
        {
            effectList.add(((TotemTileEntity) totemTileEntity).getData().getTotemType());
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
                Class creeperClass = (Class) EntityList.stringToClassMapping.get("Creeper");
                List entitiesWithinAABB = worldObj.getEntitiesWithinAABB(creeperClass,
                        AxisAlignedBB.fromBounds(pos.getX() - range,
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

            switch(mData.getTotemType())
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
