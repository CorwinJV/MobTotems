package com.corwinjv.mobtotems.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 7/13/2016.
 */
public class ModTileEntity extends TileEntity implements ITickable
{
    @Nonnull
    @Override
    public final NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return new SPacketUpdateTileEntity(pos, 0, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet)
    {
        super.onDataPacket(networkManager, packet);
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public void update()
    {

    }

    public void markForUpdate()
    {
        // TODO: find way to do this in 1.9+? Maybe not needed anymore?
        // I can't figure out how to get the blocks to send their packets to the client
        //worldObj.markBlockForUpdate(pos);
        markDirty();
    }
}
