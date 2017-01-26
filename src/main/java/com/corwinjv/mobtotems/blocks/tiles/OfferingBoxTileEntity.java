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
import com.corwinjv.mobtotems.network.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public class OfferingBoxTileEntity extends ModMultiblockInventoryTileEntity<TotemType> implements IChargeableTileEntity
{
    private static int INVENTORY_SIZE = 9;
    private static int FUELED_INCR_AMOUNT = 1000;
    private static int TICK_DECR_AMOUNT = 1;

    private static final int MAX_CHARGE = 1000;
    private static final String CHARGE_LEVEL = "CHARGE_LEVEL";

    private static final int MAX_TOTEM_HEIGHT = 3;

    private int chargeLevel = 0;

    public OfferingBoxTileEntity() {
        super();
    }

    @Override
    public void update() {
        long worldTime = world.getTotalWorldTime();

        if(!world.isRemote
                && getIsMaster()
                && worldTime % 20 == 0) {

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

            // TODO: Condense cost stacks? If two totems both cost 3 grass, they should turn into 1 stack of 6 grass

            List<ItemStack> costCopy = new ArrayList<>(cost);
            // Look for items that match the cost in inventory
            for(int i = 0; i < getSizeInventory(); i++) {
                for(ItemStack stack : cost) {
                    if(stack.getItem() == getStackInSlot(i).getItem()) {
                        // remove anything that matches in the inventory from costCopy

                        //
                    }
                }
            }

            // If costCopy an empty list, add charge
            if(costCopy.size() == 0)
            {
                incrementChargeLevel(FUELED_INCR_AMOUNT);
            }

            // Check charge level
            if(getChargeLevel() >= TICK_DECR_AMOUNT)
            {
                decrementChargeLevel(TICK_DECR_AMOUNT);
                //FMLLog.log(Level.ERROR, "OfferingBoxTE Decrementing charge, charge at: " + getChargeLevel());

                // Perform charge effect
                performChargeEffect();
            }
        }
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
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        chargeLevel = nbt.getInteger(CHARGE_LEVEL);
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

        if(firstSlave == null) {
            this.setSlaves(new ArrayList<BlockPos>());
            this.setIsMaster(false);
            return;
        }

        // Check above recursively...
        List<BlockPos> tmpSlaves = checkForSlaveAboveRecursively(firstSlave.getPos(), MAX_TOTEM_HEIGHT);

        // TODO: Validation
        this.setSlaves(tmpSlaves);

        // Only want to markDirty() once
        this.isMaster = false;
        if(this.slaves.size() > 0) {
            this.isMaster = true;
        }
        this.setIsMaster(this.isMaster);
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
