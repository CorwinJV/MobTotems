package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.Modifiers;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.TotemLogic;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockInventoryTileEntity;
import com.corwinjv.mobtotems.gui.OfferingBoxContainer;
import com.corwinjv.mobtotems.interfaces.IChargeableTileEntity;
import com.corwinjv.mobtotems.interfaces.IMultiblock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public class OfferingBoxTileEntity extends ModMultiblockInventoryTileEntity<TotemType> implements IChargeableTileEntity
{
    public static final int MAX_CHARGE = 500;

    private static int INVENTORY_SIZE = 9;
    private static int FUELED_INCR_AMOUNT = MAX_CHARGE;
    private static int TICK_DECR_AMOUNT = 1;

    private static final String CHARGE_LEVEL = "CHARGE_LEVEL";
    private static final String SLAVE_TYPES_COPY = "SLAVE_TYPES_COPY";

    private static final int MAX_TOTEM_HEIGHT = 3;

    private int chargeLevel = 0;
    private List<TotemType> slaveTypesCopy = new ArrayList<>();

    public OfferingBoxTileEntity() {
        super();
    }

    @Override
    public void update() {
        long worldTime = world.getTotalWorldTime();

        if(!world.isRemote
                && getIsMaster()
                && worldTime % 20 == 0) {

            if(getChargeLevel() + FUELED_INCR_AMOUNT <= MAX_CHARGE)
            {
                List<ItemStack> cost = new ArrayList<>();

                // Get cost for current multiblock
                for(BlockPos slavePos : getSlaves()) {
                    TileEntity totemTe = world.getTileEntity(slavePos);

                    if(totemTe instanceof TotemTileEntity) {
                        List<ItemStack> totemCost = TotemHelper.getCostForTotemType((((TotemTileEntity)totemTe).getType()));
                        for(ItemStack stack : totemCost) {
                            cost.add(stack);
                        }
                    }
                }

                cost = condenseCostStacks(cost);
                List<ItemStack> costCopy = new ArrayList<>(cost);

                // Confirm that the cost can be met
                for(int i = 0; i < getSizeInventory(); i++) {
                    for(int j = costCopy.size()-1; j >= 0; j--) {
                        ItemStack costStack = new ItemStack(costCopy.get(j).getItem(), costCopy.get(j).getCount(), costCopy.get(j).getMetadata());
                        if(costStack.getItem() == getStackInSlot(i).getItem()) {
                            if(getStackInSlot(i).getCount() >= costStack.getCount()) {
                                costCopy.remove(j);
                            } else if(getStackInSlot(i).getCount() < costStack.getCount()) {
                                costStack.setCount(costStack.getCount() - getStackInSlot(i).getCount());
                                costCopy.set(j, costStack);
                                if(costStack.getCount() == 0) {
                                    costCopy.remove(j);
                                }
                            }
                        }
                    }
                }

                // If costCopy an empty list, add charge
                if(costCopy.size() == 0)
                {
                    // Remove the cost from the inventory
                    costCopy = new ArrayList<>(cost);

                    for(int i = getSizeInventory() - 1; i >= 0; i--) {
                        for(int j = costCopy.size() - 1; j >= 0; j--) {
                            ItemStack costStack = costCopy.get(j);
                            if(costStack.getItem() == getStackInSlot(i).getItem()) {
                                if(getStackInSlot(i).getCount() <= costStack.getCount()) {
                                    costStack.setCount(costStack.getCount() - getStackInSlot(i).getCount());
                                    if(costStack.getCount() == 0) {
                                        costCopy.remove(costStack);
                                    }
                                    removeStackFromSlot(i);
                                } else if(getStackInSlot(i).getCount() > costStack.getCount()) {
                                    decrStackSize(i, costStack.getCount());
                                    costCopy.remove(j);
                                }
                            }
                        }
                    }

                    // Fill up charge
                    incrementChargeLevel(FUELED_INCR_AMOUNT);
                }
            }

            // Decrement charge level
            decrementChargeLevel(TICK_DECR_AMOUNT);
            if(getChargeLevel() > 0)
            {
                performChargeEffect();
            }
        }
    }

    private List<ItemStack> condenseCostStacks(List<ItemStack> costStacks)
    {
        List<ItemStack> ret = new ArrayList<>();
        for(ItemStack stack : costStacks)
        {
            boolean containsItem = false;
            for(ItemStack retStack : ret) {
                if(stack.getItem().equals(retStack.getItem())) {
                    retStack.setCount(retStack.getCount() + stack.getCount());
                    containsItem = true;
                }
            }
            if(!containsItem) {
                ret.add(stack);
            }
        }
        return ret;
    }

    private void performChargeEffect()
    {
        List<TotemType> slaveTypes = getSlaveTypes();
        List<TotemType> effects = new ArrayList<>();
        List<TotemType> modifiers = new ArrayList<>();

        TotemHelper.sortEffectsAndModifiers(slaveTypes, effects, modifiers);

        for(TotemType type : effects) {
            Modifiers mods = new Modifiers();
            for(TotemType modifierType : modifiers) {
                TotemLogic logic = TotemHelper.getTotemLogic(modifierType);
                if(logic != null) {
                    mods = logic.adjustModifiers(mods);
                }
            }

            TotemLogic logic = TotemHelper.getTotemLogic(type);
            if(logic != null)
            {
                logic.performEffect(pos, mods);
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger(CHARGE_LEVEL, chargeLevel);

        int[] slaveTypesArr = new int[slaveTypesCopy.size()];
        for(int i = 0; i < slaveTypesCopy.size(); i++) {
            slaveTypesArr[i] = slaveTypesCopy.get(i).ordinal();
        }

        nbt.setIntArray(SLAVE_TYPES_COPY, slaveTypesArr);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        chargeLevel = nbt.getInteger(CHARGE_LEVEL);

        int[] slaveTypesArr = nbt.getIntArray(SLAVE_TYPES_COPY);
        slaveTypesCopy.clear();
        for(int i = 0; i < slaveTypesArr.length; i++) {
            slaveTypesCopy.add(TotemType.fromMeta(slaveTypesArr[i]));
        }
    }

    // Charge
    @Override
    public int getMaxChargeLevel() {
        return MAX_CHARGE;
    }

    @Override
    public int getChargeLevel() {
        return chargeLevel;
    }

    @Override
    public void setChargeLevel(int chargeLevel) {
        this.chargeLevel = chargeLevel;
        markDirty();
    }

    public void decrementChargeLevel(int amount)
    {
        chargeLevel -= amount;
        if(chargeLevel < 0)
            chargeLevel = 0;
        markDirty();
    }

    @Override
    public void incrementChargeLevel(int amount)
    {
        chargeLevel += amount;
        if(chargeLevel > MAX_CHARGE)
            chargeLevel = MAX_CHARGE;
        markDirty();
    }

    // Multiblock
    @Override
    public void verifyMultiblock() {

        // Check adjacent blocks for a totem wood
        TotemTileEntity firstSlave = null;
        for(double x = getPos().getX() - 1; x <= getPos().getX() + 1; x++) {
            if(firstSlave != null) {
                break;
            }
            for (double z = getPos().getZ() - 1; z <= getPos().getZ() + 1; z++) {
                BlockPos posToCheck = new BlockPos(x, getPos().getY(), z);
                TileEntity te = world.getTileEntity(posToCheck);

                if(te != null &&
                        te instanceof TotemTileEntity) {
                    firstSlave = (TotemTileEntity)te;
                    break;
                }
            }
        }

        // Check above recursively...
        List<BlockPos> tmpSlaves = new ArrayList<>();

        if(firstSlave != null) {
            tmpSlaves = checkForSlaveAboveRecursively(firstSlave.getPos(), MAX_TOTEM_HEIGHT);
        }

        // Validation
        // only want to markDirty() once
        this.isMaster = false;
        if(tmpSlaves.size() >= 2) {
            this.setSlaves(tmpSlaves);
            this.isMaster = true;
        }
        else {
            this.setSlaves(new ArrayList<>());
        }
        this.setIsMaster(this.isMaster);

        // If we've changed status, reset the charge pool
        if(!slaveTypesCopy.equals(getSlaveTypes()) || !getIsMaster()) {
            this.setChargeLevel(0);
            slaveTypesCopy.clear();
            for(TotemType type : getSlaveTypes()) {
                slaveTypesCopy.add(type);
            }
        }
    }

    private List<BlockPos> checkForSlaveAboveRecursively(BlockPos startPos, int height)
    {
        List<BlockPos> totemTileList = new ArrayList<>();

        if(height > 0) {
            TileEntity totemTileEntity = world.getTileEntity(startPos);
            if(totemTileEntity instanceof TotemTileEntity) {
                totemTileList.add(startPos);
            }

            List<BlockPos> tmpList = checkForSlaveAboveRecursively(startPos.add(0, 1, 0), height-1);
            for(BlockPos te : tmpList) {
                totemTileList.add(te);
            }
        }

        return totemTileList;
    }

    @Override
    public List<BlockPos> getSlaves()
    {
        return slaves;
    }

    @Override
    public List<TotemType> getSlaveTypes() {
        List<TotemType> slaveTypes = new ArrayList<>();

        for(BlockPos slavePos : slaves) {
            TileEntity te = world.getTileEntity(slavePos);

            if(te instanceof TotemTileEntity) {
                slaveTypes.add(((TotemTileEntity)te).getType());
            }
        }
        return slaveTypes;
    }

    public List<TotemType> getSlaveTypesCopy()
    {
        return slaveTypesCopy;
    }

    public void setSlaveTypesCopy(List<TotemType> slaveTypesCopy)
    {
        this.slaveTypesCopy = slaveTypesCopy;
        markDirty();
    }

    @Override
    public TotemType getType() {
        return TotemType.NONE;
    }

    @Override
    public void setType(TotemType  type) {
    }

    @Override
    public void setMaster(IMultiblock<TotemType> master) {
    }

    @Override
    public IMultiblock<TotemType> getMaster() {
        return null;
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

    @Override
    public int getField(int id)
    {
        switch(id)
        {
            default:
            case 0:
            {
                return this.chargeLevel;
            }
        }
    }

    @Override
    public void setField(int id, int value) {
        switch(id)
        {
            default:
            case 0:
            {
                setChargeLevel(value);
            }
        }
    }

    public int getFieldCount()
    {
        return 1;
    };
}
