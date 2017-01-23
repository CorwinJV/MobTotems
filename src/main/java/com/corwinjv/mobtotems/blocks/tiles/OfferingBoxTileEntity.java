package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockInventoryTileEntity;
import com.corwinjv.mobtotems.gui.OfferingBoxContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public class OfferingBoxTileEntity extends ModMultiblockInventoryTileEntity
{
    public static int INVENTORY_SIZE = 9;

    public OfferingBoxTileEntity()
    {
        super();
        stacks = new ItemStack[getSizeInventory()];
        for(int i = 0; i < getSizeInventory(); i++)
        {
            if(stacks[i] == null)
            {
                stacks[i] = ItemStack.EMPTY;
            }
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new OfferingBoxContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return Reference.RESOURCE_PREFIX + ModBlocks.OFFERING_BOX_NAME;
    }

    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public String getName() {
        return "container." + ModBlocks.OFFERING_BOX_NAME;
    }

    @Override
    protected int getUsableDistance() {
        return 3;
    }
}
