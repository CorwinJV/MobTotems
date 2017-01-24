package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.TotemWoodBlock;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockTileEntity;
import com.corwinjv.mobtotems.interfaces.IMultiblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by CorwinJV on 2/18/2016.
 */
public class TotemTileEntity extends ModMultiblockTileEntity<TotemType>
{
    private static final String TOTEM_TYPE = "totem_type";
    private TotemType type = TotemType.NONE;

    public TotemTileEntity() {
        super();
        this.setType(TotemType.NONE);
    }

    public TotemTileEntity(TotemType type) {
        super();
        this.setType(type);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        tagCompound = super.writeToNBT(tagCompound);
        tagCompound.setInteger(TOTEM_TYPE, type.getMeta());
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        type = TotemType.fromMeta(tagCompound.getInteger(TOTEM_TYPE));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        if(newState.getProperties().containsKey(TotemWoodBlock.TOTEM_TYPE)) {
            return !newState.getValue(TotemWoodBlock.TOTEM_TYPE).equals(oldState.getValue(TotemWoodBlock.TOTEM_TYPE));
        }
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public boolean getIsMaster() {
        return false;
    }

    @Override
    public boolean verifyMultiblock() {
        return false;
    }

    @Override
    public List<BlockPos> getSlaves() {
        return null;
    }

    @Override
    public List<TotemType> getSlaveTypes() {
        return null;
    }

    @Override
    public TotemType getType() {
        return type;
    }

    @Override
    public void setType(TotemType type) {
        this.type = type;
        markDirty();
    }

    public List<ItemStack> getCost()
    {
        List<ItemStack> cost = new ArrayList<ItemStack>();
        switch(type)
        {
            default:
            {
                cost.add(new ItemStack(Items.APPLE, 1, 0));
            }
        }
        return cost;
    }
}
