package com.corwinjv.mobtotems.gui;

import com.corwinjv.mobtotems.blocks.OfferingBox;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CorwinJV on 1/22/2017.
 */
public class OfferingBoxContainer extends Container {

    public static int width = 3;
    public static int height = 3;

    public static int PLAYER_WIDTH = 9;
    public static int PLAYER_HEIGHT = 4;

    IInventory inventory = null;
    BlockPos offeringBoxPos = null;

    private int chargeLevel = 0;

    public OfferingBoxContainer(InventoryPlayer playerInventory, IInventory inventory)
    {
        this.inventory = inventory;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                addSlotToContainer(new Slot(inventory, x + (y * width), 62 + (x * 18), 17 + (y * 18)));
            }
        }

        for(int x = 0; x < PLAYER_WIDTH; x++)
        {
            for(int y = 1; y < PLAYER_HEIGHT; y++)
            {
                    addSlotToContainer(new Slot(playerInventory, x + (y * PLAYER_WIDTH),8 + (x * 18), 12 + (((PLAYER_HEIGHT - 1) + y) * 18)));
            }
        }
        for(int x = 0; x < PLAYER_WIDTH; x++)
        {
            addSlotToContainer(new Slot(playerInventory, x,8 + (x * 18), 142));
        }

        if(inventory instanceof OfferingBoxTileEntity)
        {
            offeringBoxPos = ((OfferingBoxTileEntity) inventory).getPos();
            chargeLevel = (((OfferingBoxTileEntity) inventory).getChargeLevel());
        }
    }


    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.inventory);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = this.listeners.get(i);

            TileEntity te = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getTileEntity(offeringBoxPos);
            if (te instanceof OfferingBoxTileEntity) {
                if (this.chargeLevel != inventory.getField(0)) {
                    icontainerlistener.sendProgressBarUpdate(this, 0, inventory.getField(0));
                    this.chargeLevel = inventory.getField(0);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.inventory.setField(id, data);
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack prevStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            prevStack = stack.copy();

            if (index < 9)
            {
                if (!this.mergeItemStack(stack, 9, 45, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (!this.mergeItemStack(stack, 0, 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if(stack.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if(stack.getCount() == prevStack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stack);
        }
        return prevStack;
    }
}
