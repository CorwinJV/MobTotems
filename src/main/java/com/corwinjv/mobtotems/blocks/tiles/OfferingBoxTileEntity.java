package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockInventoryTileEntity;
import com.corwinjv.mobtotems.gui.OfferingBoxContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public class OfferingBoxTileEntity extends ModMultiblockInventoryTileEntity
{
    public static int INVENTORY_SIZE = 9;

    private static final int MAX_CHARGE = 100;
    private static final String CHARGE_LEVEL = "CHARGE_LEVEL";

    private int chargeLevel = 0;

    public OfferingBoxTileEntity() {
        super();
    }

    @Override
    public void update() {
        // Verify multiblock status
        if(this.verifyMultiblock())
        {
            // Get cost for current multiblock

            // Look for items that match the cost in inventory

            // Remove items that match the cost in inventory
            // Add charge

            // Check charge level

            // Perform charge effect
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger(CHARGE_LEVEL, chargeLevel);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        chargeLevel = nbt.getInteger(CHARGE_LEVEL);
    }


    // Charge
    private int incrementChargeLevel(int amount)
    {
        chargeLevel += amount;
        if(chargeLevel > MAX_CHARGE)
            chargeLevel = MAX_CHARGE;
        return chargeLevel;
    }

    private int decrementChargeLevel(int amount)
    {
        chargeLevel -= amount;
        if(chargeLevel < 0)
            chargeLevel = 0;
        return chargeLevel;
    }

    // Multiblock
    @Override
    public boolean verifyMultiblock() {
        return false;
    }

    // Inventory
    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new OfferingBoxContainer(playerInventory, this);
    }

    @Nonnull
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
        return 9;
    }

    @Nonnull
    @Override
    public String getName() {
        return "container." + ModBlocks.OFFERING_BOX_NAME;
    }

    @Override
    protected int getUsableDistance() {
        return 3;
    }
}
