package com.corwinjv.mobtotems.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.corwinjv.mobtotems.items.baubles.BaubleItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 2/1/2016.
 */
public class ActivateBaubleMessage extends Message<ActivateBaubleMessage> {
    @Override
    protected void handleServer(ActivateBaubleMessage message, PlayerEntityMP player) {
        final IBaublesItemHandler baublesItemHandler = BaublesApi.getBaublesHandler(player);

        if (baublesItemHandler == null) {
            return;
        }

        for (int i = 0; i < baublesItemHandler.getSlots(); i++) {
            final ItemStack baubleStack = baublesItemHandler.getStackInSlot(i);
            if (baubleStack != ItemStack.EMPTY
                    && baubleStack.getItem() instanceof BaubleItem) {
                ((BaubleItem) baubleStack.getItem()).onBaubleActivated(baubleStack, player);
            }
        }
    }
}
