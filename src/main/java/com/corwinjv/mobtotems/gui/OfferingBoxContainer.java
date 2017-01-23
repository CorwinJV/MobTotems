package com.corwinjv.mobtotems.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by CorwinJV on 1/22/2017.
 */
public class OfferingBoxContainer extends Container {

    public static int width = 3;
    public static int height = 3;

    public static int PLAYER_WIDTH = 9;
    public static int PLAYER_HEIGHT = 4;

    public OfferingBoxContainer(InventoryPlayer playerInventory, IInventory iinventory)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                addSlotToContainer(new Slot(iinventory, x + (y * width), 62 + (x * 18), 17 + (y * 18)));
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
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
