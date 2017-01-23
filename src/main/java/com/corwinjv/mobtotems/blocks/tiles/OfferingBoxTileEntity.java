package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockInventoryTileEntity;
import com.corwinjv.mobtotems.gui.OfferingBoxContainer;
import com.corwinjv.mobtotems.interfaces.IChargeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public class OfferingBoxTileEntity extends ModMultiblockInventoryTileEntity implements IChargeable
{
    public static int INVENTORY_SIZE = 9;

    private static final String CHARGE_LEVEL = "CHARGE_LEVEL";

    public OfferingBoxTileEntity() {
        super();
    }

    @Override
    public void update() {
        // Get cost for current multiblock

        // Look for items that match the cost in inventory

        // Remove items that match the cost in inventory
        // Add charge

        // Check charge level

        // Perform charge effect
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

    // Charge fun
    @Override
    public int getChargeLevel(ItemStack stack) {
        return 0;
    }

    @Override
    public void setChargeLevel(ItemStack stack, int chargeLevel) {

    }

    @Override
    public void decrementChargeLevel(ItemStack stack, int amount) {

    }

    @Override
    public void incrementChargeLevel(ItemStack stack, int amount) {

    }

    @Override
    public int getMaxChargeLevel() {
        return 0;
    }
}
