package com.corwinjv.mobtotems.blocks.tiles.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 7/13/2016.
 */
public class ModTileEntity extends TileEntity {
    public ModTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        return super.write(tagCompound);
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
    }

    @Nonnull
    @Override
    public final CompoundNBT getUpdateTag() {
        return write(super.getUpdateTag());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        super.onDataPacket(networkManager, packet);
        read(packet.getNbtCompound());

        final BlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    // TODO: What's this. Double check that my TileEntity netcode is still valid
//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull BlockState oldState, @Nonnull BlockState newState) {
//        return (oldState.getBlock() != newState.getBlock());
//    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }
}
