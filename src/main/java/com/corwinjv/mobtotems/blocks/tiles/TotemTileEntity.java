package com.corwinjv.mobtotems.blocks.tiles;

import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.base.ModMultiblockTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by CorwinJV on 2/18/2016.
 */
public class TotemTileEntity extends ModMultiblockTileEntity<TotemType> {
    private static final String TOTEM_TYPE = "totem_type";
    private TotemType type = TotemType.NONE;

    public TotemTileEntity() {
        super(ModBlocks.TOTEM_TILE_ENTITY);
        this.setMultiblockType(TotemType.NONE);
    }

    public TotemTileEntity(TotemType type) {
        super(ModBlocks.TOTEM_TILE_ENTITY);
        this.setMultiblockType(type);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        tagCompound = super.write(tagCompound);
        tagCompound.putInt(TOTEM_TYPE, type.getMeta());
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        type = TotemType.fromMeta(tagCompound.getInt(TOTEM_TYPE));
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull BlockState oldState, @Nonnull BlockState newState) {
//        if (newState.getProperties().containsKey(TotemWoodBlock.TOTEM_TYPE)) {
//            return !newState.getValue(TotemWoodBlock.TOTEM_TYPE).equals(oldState.getValue(TotemWoodBlock.TOTEM_TYPE));
//        }
//        return (oldState.getBlock() != newState.getBlock());
//    }

    @Override
    public boolean getIsMaster() {
        return false;
    }

    @Override
    public void verifyMultiblock() {
        return;
    }

    @Override
    public void invalidateSlaves() {
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
    public TotemType getMultiblockType() {
        return type;
    }

    @Override
    public void setMultiblockType(TotemType type) {
        this.type = type;
        markDirty();
    }
}
