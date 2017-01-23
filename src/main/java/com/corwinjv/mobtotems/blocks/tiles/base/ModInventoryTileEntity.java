package com.corwinjv.mobtotems.blocks.tiles.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public abstract class ModInventoryTileEntity extends TileEntityLockable implements ISidedInventory, ITickable {
    private static final String SLOT_TAG = "Slot";
    private static final String INVENTORY_TAG = "inventory";

    protected ItemStack[] stacks = new ItemStack[getSizeInventory()];

    public ModInventoryTileEntity()
    {
        super();
        for(int i = 0; i < getSizeInventory(); i++) {
            if(stacks[i] == null) {
                stacks[i] = ItemStack.EMPTY;
            }
        }
    }

    // Nbt syncing & inventory nbt
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tagCompound = super.writeToNBT(nbt);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stackTag.setByte(SLOT_TAG, (byte) i);
            this.getStackInSlot(i).writeToNBT(stackTag);
            list.appendTag(stackTag);
        }
        tagCompound.setTag(INVENTORY_TAG, list);

        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        NBTTagList list = nbt.getTagList(INVENTORY_TAG, 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte(SLOT_TAG) & 255;
            this.setInventorySlotContents(slot, new ItemStack(stackTag));
        }
    }

    @Nonnull
    @Override
    public final NBTTagCompound getUpdateTag()
    {
        return writeToNBT(super.getUpdateTag());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet)
    {
        super.onDataPacket(networkManager, packet);
        readFromNBT(packet.getNbtCompound());

        final IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

    // IInventory & Gui stuff
    protected abstract int getUsableDistance();

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {

        int[] ret = new int[getSizeInventory()];
        for(int i = 0; i < ret.length; i++)
        {
            ret[i] = i;
        }
        return ret;
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        return (getStackInSlot(index) == ItemStack.EMPTY);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        return (getStackInSlot(index) != ItemStack.EMPTY);
    }

    @Override
    public boolean isEmpty() {
        boolean ret = true;
        for(ItemStack stack : stacks)
        {
            if(stack != ItemStack.EMPTY)
            {
                ret = false;
            }
        }
        return ret;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        if(index >= 0 && index < getSizeInventory())
        {
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
                stack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).getCount() <= 0)
                {
                    this.setInventorySlotContents(index, ItemStack.EMPTY);
                }
                else
                {
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
        if(index >= 0 && index < getSizeInventory())
        {
            if(stack != ItemStack.EMPTY && stack.getCount() > this.getInventoryStackLimit())
            {
                stack.setCount(this.getInventoryStackLimit());
            }
            if(stack != ItemStack.EMPTY && stack.getCount() < 0)
            {
                stack = ItemStack.EMPTY;
            }

            stacks[index] = stack;
            markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player)
    {
        return (player.getPosition().getDistance(getPos().getX(), getPos().getY(), getPos().getZ()) < getUsableDistance());
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < getSizeInventory(); i++)
        {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
