package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.SacredLightBlock;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.Modifiers;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.TotemLogic;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockInventoryTileEntity;
import com.corwinjv.mobtotems.gui.OfferingBoxContainer;
import com.corwinjv.mobtotems.interfaces.IChargeableTileEntity;
import com.corwinjv.mobtotems.interfaces.IMultiblock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public class OfferingBoxTileEntity extends ModMultiblockInventoryTileEntity<TotemType> implements IChargeableTileEntity, ITickableTileEntity {
    public static final int MAX_CHARGE = 1000;

    private static int INVENTORY_SIZE = 9;
    private static int FUELED_INCR_AMOUNT = MAX_CHARGE;
    private static int TICK_DECR_AMOUNT = 1;

    private static final String CHARGE_LEVEL = "CHARGE_LEVEL";
    private static final String SLAVE_TYPES_COPY = "SLAVE_TYPES_COPY";

    private int chargeLevel = 0;
    private List<TotemType> slaveTypesCopy = new ArrayList<>();

    public OfferingBoxTileEntity() {
        super();
    }

    @Override
    public void tick() {
        long worldTime = this.getTotalWorldTime();

        if (!world.isRemote
                && getIsMaster()
                && worldTime % 20 == 0) {

            if (getChargeLevel() + FUELED_INCR_AMOUNT <= MAX_CHARGE) {
                List<ItemStack> cost = new ArrayList<>();

                // Get cost for current multiblock
                for (BlockPos slavePos : getSlaves()) {
                    TileEntity totemTe = world.getTileEntity(slavePos);

                    if (totemTe instanceof TotemTileEntity) {
                        List<ItemStack> totemCost = TotemHelper.getCostForTotemType((((TotemTileEntity) totemTe).getType()));
                        for (ItemStack stack : totemCost) {
                            cost.add(stack);
                        }
                    }
                }

                cost = condenseCostStacks(cost);
                List<ItemStack> costCopy = new ArrayList<>(cost);

                // Confirm that the cost can be met
                for (int i = 0; i < getSizeInventory(); i++) {
                    for (int j = costCopy.size() - 1; j >= 0; j--) {
                        ItemStack costStack = new ItemStack(costCopy.get(j).getItem(), costCopy.get(j).getCount(), costCopy.get(j).getMetadata());
                        if (costStack.getItem() == getStackInSlot(i).getItem()) {
                            if (getStackInSlot(i).getCount() >= costStack.getCount()) {
                                costCopy.remove(j);
                            } else if (getStackInSlot(i).getCount() < costStack.getCount()) {
                                costStack.setCount(costStack.getCount() - getStackInSlot(i).getCount());
                                costCopy.set(j, costStack);
                                if (costStack.getCount() == 0) {
                                    costCopy.remove(j);
                                }
                            }
                        }
                    }
                }

                // Adjust cost for sacred light
                boolean hasSacredLight = false;
                List<BlockPos> slaves = getSlaves();
                if (slaves.size() > 0) {
                    BlockPos lastSlave = getSlaves().get(getSlaves().size() - 1);
                    Block blockToCheck = world.getBlockState(new BlockPos(lastSlave.getX(), lastSlave.getY() + 1, lastSlave.getZ())).getBlock();
                    if (blockToCheck instanceof SacredLightBlock) {
                        hasSacredLight = true;
                    }
                }

                // If costCopy an empty list, add charge
                if (costCopy.size() == 0) {
                    // Remove the cost from the inventory
                    costCopy = new ArrayList<>(cost);

                    for (int i = getSizeInventory() - 1; i >= 0; i--) {
                        for (int j = costCopy.size() - 1; j >= 0; j--) {
                            ItemStack costStack = costCopy.get(j);
                            if (costStack.getItem() == getStackInSlot(i).getItem()) {
                                if (getStackInSlot(i).getCount() <= costStack.getCount()) {
                                    costStack.setCount(costStack.getCount() - getStackInSlot(i).getCount());
                                    if (costStack.getCount() == 0) {
                                        costCopy.remove(costStack);
                                    }
                                    removeStackFromSlot(i);
                                } else if (getStackInSlot(i).getCount() > costStack.getCount()) {
                                    decrStackSize(i, costStack.getCount());
                                    costCopy.remove(j);
                                }
                            }
                        }
                    }

                    // Don't give charge for using TotemType.NONE
                    int blankTotemCount = 0;
                    for (TotemType type : getSlaveTypes()) {
                        if (type.equals(TotemType.NONE)) {
                            blankTotemCount++;
                        }
                    }
                    if (blankTotemCount < getSlaveTypes().size()) {
                        // Fill up charge
                        incrementChargeLevel(FUELED_INCR_AMOUNT);
                    }
                }
                if (hasSacredLight) {
                    incrementChargeLevel(FUELED_INCR_AMOUNT);
                }
            }

            // Decrement charge level
            decrementChargeLevel(TICK_DECR_AMOUNT);
        }

        if (!world.isRemote
                && getIsMaster()
                && getChargeLevel() > 0) {
            performChargeEffect(worldTime);
        }
    }

    private List<ItemStack> condenseCostStacks(List<ItemStack> costStacks) {
        List<ItemStack> ret = new ArrayList<>();
        for (ItemStack stack : costStacks) {
            boolean containsItem = false;
            for (ItemStack retStack : ret) {
                if (stack.getItem().equals(retStack.getItem())) {
                    retStack.setCount(retStack.getCount() + stack.getCount());
                    containsItem = true;
                }
            }
            if (!containsItem) {
                ret.add(stack);
            }
        }
        return ret;
    }

    private void performChargeEffect(long tick) {
        List<TotemType> slaveTypes = getSlaveTypes();
        List<TotemType> effects = new ArrayList<>();
        List<TotemType> modifiers = new ArrayList<>();

        TotemHelper.sortEffectsAndModifiers(slaveTypes, effects, modifiers);

        for (TotemType type : effects) {
            Modifiers mods = new Modifiers();
            for (TotemType modifierType : modifiers) {
                TotemLogic logic = TotemHelper.getTotemLogic(modifierType);
                if (logic != null) {
                    mods = logic.adjustModifiers(mods);
                }
            }

            int baseEffectTick = 40;
            int baseSpeedEffect = -10;
            if (tick % (baseEffectTick + (baseSpeedEffect * mods.speed)) == 0) {
                TotemLogic logic = TotemHelper.getTotemLogic(type);
                if (logic != null) {
                    logic.performEffect(world, pos, mods);
                }
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setInteger(CHARGE_LEVEL, chargeLevel);

        int[] slaveTypesArr = new int[slaveTypesCopy.size()];
        for (int i = 0; i < slaveTypesCopy.size(); i++) {
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
        for (int i = 0; i < slaveTypesArr.length; i++) {
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

    public void decrementChargeLevel(int amount) {
        chargeLevel -= amount;
        if (chargeLevel < 0)
            chargeLevel = 0;
        markDirty();
    }

    @Override
    public void incrementChargeLevel(int amount) {
        chargeLevel += amount;
        if (chargeLevel > MAX_CHARGE)
            chargeLevel = MAX_CHARGE;
        markDirty();
    }

    // Multiblock
    @Override
    public void verifyMultiblock() {

        // Check adjacent blocks for a totem wood
        TotemTileEntity firstSlave = null;

        for (Direction direction : Direction.HORIZONTALS) {
            Vec3i dirVec = direction.getDirectionVec();
            BlockPos posToCheck = new BlockPos(getPos().getX() + dirVec.getX(), getPos().getY() + dirVec.getY(), getPos().getZ() + dirVec.getZ());
            TileEntity te = world.getTileEntity(posToCheck);

            if (te != null &&
                    te instanceof TotemTileEntity) {
                firstSlave = (TotemTileEntity) te;
                break;
            }
        }

        // Check above recursively...
        List<BlockPos> tmpSlaves = new ArrayList<>();

        if (firstSlave != null) {
            tmpSlaves = TotemHelper.checkForSlaveAboveRecursively(world, firstSlave.getPos(), TotemHelper.MAX_TOTEM_HEIGHT);
        }

        // Validation
        // only want to markDirty() once
        this.isMaster = false;
        if (areSlavesValid(tmpSlaves)) {
            this.setSlaves(tmpSlaves);
            this.isMaster = true;
        } else {
            this.setSlaves(new ArrayList<>());
        }
        this.setIsMaster(this.isMaster);

        // Iterate over new slaves and set master to this
        // invalidated by invalidateSlaves()
        for (BlockPos slavePos : getSlaves()) {
            TileEntity te = world.getTileEntity(slavePos);
            if (te != null
                    && te instanceof TotemTileEntity) {
                ((TotemTileEntity) te).setMaster(this);
            }
        }

        // If we've changed status, reset the charge pool
        if (!slaveTypesCopy.equals(getSlaveTypes()) || !getIsMaster()) {
            this.setChargeLevel(0);
            slaveTypesCopy.clear();
            for (TotemType type : getSlaveTypes()) {
                slaveTypesCopy.add(type);
            }
        }
    }

    private boolean areSlavesValid(List<BlockPos> tmpSlaves) {
        boolean slavesAreValid = true;
        if (tmpSlaves.size() < 2) {
            return false;
        }
        for (BlockPos slavePos : tmpSlaves) {
            TileEntity te = world.getTileEntity(slavePos);
            if (te != null
                    && te instanceof TotemTileEntity) {
                TileEntity master = (TileEntity) ((TotemTileEntity) te).getMaster();
                if (master != null
                        && master.getPos() != this.getPos()) {
                    slavesAreValid = false;
                    break;
                }
            }
        }
        return slavesAreValid;
    }

    @Override
    public void invalidateSlaves() {
        super.invalidateSlaves();
        this.setChargeLevel(0);
    }

    @Override
    public List<BlockPos> getSlaves() {
        return slaves;
    }

    @Override
    public List<TotemType> getSlaveTypes() {
        List<TotemType> slaveTypes = new ArrayList<>();

        for (BlockPos slavePos : slaves) {
            TileEntity te = world.getTileEntity(slavePos);

            if (te instanceof TotemTileEntity) {
                slaveTypes.add(((TotemTileEntity) te).getType());
            }
        }
        return slaveTypes;
    }

    @Override
    public TotemType getMultiblockType() {
        return TotemType.NONE;
    }

    public List<TotemType> getSlaveTypesCopy() {
        return slaveTypesCopy;
    }

    public void setSlaveTypesCopy(List<TotemType> slaveTypesCopy) {
        this.slaveTypesCopy = slaveTypesCopy;
        markDirty();
    }

    @Override
    public void setMultiblockType(TotemType type) {
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
    public Container createContainer(@Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerIn) {
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

    @Override
    public void markDirty() {

    }

    @Nonnull
    @Override
    public String getName() {
        return "container." + ModBlocks.OFFERING_BOX_NAME;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int i, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    protected int getUsableDistance() {
        return 3;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            default:
            case 0: {
                return this.chargeLevel;
            }
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            default:
            case 0: {
                setChargeLevel(value);
            }
        }
    }

    public int getFieldCount() {
        return 1;
    }

    // Refactored....
    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    ;
}
