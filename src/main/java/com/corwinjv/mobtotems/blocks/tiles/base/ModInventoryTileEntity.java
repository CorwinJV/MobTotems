package com.corwinjv.mobtotems.blocks.tiles.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public abstract class ModInventoryTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private static final String SLOT_TAG = "Slot";
    private static final String INVENTORY_TAG = "inventory";

    protected ItemStack[] stacks = new ItemStack[getSizeInventory()];

    public ModInventoryTileEntity(TileEntityType<?> type) {
        super(type);
        for (int i = 0; i < getSizeInventory(); i++) {
            if (stacks[i] == null) {
                stacks[i] = ItemStack.EMPTY;
            }
        }
    }

    // Nbt syncing & inventory nbt
    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        CompoundNBT tagCompound = super.write(nbt);

        ListNBT list = new ListNBT();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            CompoundNBT stackTag = new CompoundNBT();
            stackTag.putByte(SLOT_TAG, (byte) i);
            this.getStackInSlot(i).write(stackTag);
            list.add(stackTag);
        }
        tagCompound.put(INVENTORY_TAG, list);

        return tagCompound;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        ListNBT list = nbt.getList(INVENTORY_TAG, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); ++i) {
            CompoundNBT stackTag = list.getCompound(i);
            int slot = stackTag.getByte(SLOT_TAG) & 255;
            // TODO: Figure out how to save/restore inventory now that ItemStack doesn't take CompoundNBT
            //this.setInventorySlotContents(slot, new ItemStack(stackTag));
        }
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

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull BlockState oldState, @Nonnull BlockState newState) {
//        return (oldState.getBlock() != newState.getBlock());
//    }

    // IInventory & Gui stuff
    protected abstract int getUsableDistance();

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull Direction side) {

        int[] ret = new int[getSizeInventory()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = i;
        }
        return ret;
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        return (getStackInSlot(index) == ItemStack.EMPTY);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        return (getStackInSlot(index) != ItemStack.EMPTY);
    }

    @Override
    public boolean isEmpty() {
        boolean ret = true;
        for (ItemStack stack : stacks) {
            if (stack != ItemStack.EMPTY) {
                ret = false;
            }
        }
        return ret;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= 0 && index < getSizeInventory()) {
            return stacks[index];
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != ItemStack.EMPTY) {
            ItemStack stack = ItemStack.EMPTY;

            if (this.getStackInSlot(index).getCount() <= count) {
                stack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, ItemStack.EMPTY);
                this.markDirty();
                return stack;
            } else {
                stack = this.getStackInSlot(index).split(count);

                if (this.getStackInSlot(index).getCount() <= 0) {
                    this.setInventorySlotContents(index, ItemStack.EMPTY);
                } else {
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return stack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        this.setInventorySlotContents(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        if (index >= 0 && index < getSizeInventory()) {
            if (stack != ItemStack.EMPTY && stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }
            if (stack != ItemStack.EMPTY && stack.getCount() < 0) {
                stack = ItemStack.EMPTY;
            }

            stacks[index] = stack;
            markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return player.getPosition().withinDistance(getPos(), getUsableDistance());
    }

    @Override
    public void openInventory(@Nonnull PlayerEntity player) {
    }

    @Override
    public void closeInventory(@Nonnull PlayerEntity player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return true;
    }

    // TODO: I don't think these are needed, remove.
//    @Override
//    public int getField(int id) {
//        return 0;
//    }
//
//    @Override
//    public void setField(int id, int value) {
//    }
//
//    @Override
//    public int getFieldCount() {
//        return 0;
//    }

    @Override
    public void clear() {
        for (int i = 0; i < getSizeInventory(); i++) {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
