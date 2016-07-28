package com.corwinjv.mobtotems.network;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by CorwinJV on 7/27/2016.
 */
public class SyncEquippedBauble extends Message<SyncEquippedBauble>
{
    int slot;
    int playerId;
    ItemStack stack;

    public SyncEquippedBauble()
    {

    }

    public SyncEquippedBauble(int slot, ItemStack stack, EntityPlayer player)
    {
        this.slot = slot;
        this.playerId = player.getEntityId();
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(slot);
        buffer.writeInt(playerId);
        ByteBufUtils.writeItemStack(buffer, stack);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        slot = buffer.readInt();
        playerId = buffer.readInt();
        stack = ByteBufUtils.readItemStack(buffer);
    }

    @Override
    protected void handleClient(SyncEquippedBauble message, EntityPlayerSP player)
    {
        Entity e = player.getEntityWorld().getEntityByID(message.playerId);
        if(e != null && e instanceof EntityPlayer)
        {
            IInventory baubleInventory = BaublesApi.getBaubles((EntityPlayer)e);
            baubleInventory.setInventorySlotContents(message.slot, message.stack);
        }
    }
}
