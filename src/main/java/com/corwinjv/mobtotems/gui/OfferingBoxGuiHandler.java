package com.corwinjv.mobtotems.gui;

import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 1/22/2017.
 */
public class OfferingBoxGuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        OfferingBoxContainer ret = null;
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity instanceof OfferingBoxTileEntity) {
            ret = new OfferingBoxContainer(player.inventory, (IInventory) tileEntity);
        }
        return ret;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        OfferingBoxGuiContainer ret = null;
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity instanceof OfferingBoxTileEntity) {
            ret = new OfferingBoxGuiContainer(player.inventory, (IInventory) tileEntity);
        }
        return ret;
    }
}
