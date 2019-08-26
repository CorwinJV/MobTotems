package com.corwinjv.mobtotems.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by CorwinJV on 2/1/2016.
 */
public class ActivateBaublePacket {

    public ActivateBaublePacket() {
    }

    public static void encode(ActivateBaublePacket packet, PacketBuffer buffer) {
    }

    public static ActivateBaublePacket decode(PacketBuffer buffer) {
        return new ActivateBaublePacket();
    }

    public static class Handler {
        public static void handle(final ActivateBaublePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
//        final IBaublesItemHandler baublesItemHandler = BaublesApi.getBaublesHandler(player);
//
//        if (baublesItemHandler == null) {
//            return;
//        }
//
//        for (int i = 0; i < baublesItemHandler.getSlots(); i++) {
//            final ItemStack baubleStack = baublesItemHandler.getStackInSlot(i);
//            if (baubleStack != ItemStack.EMPTY
//                    && baubleStack.getItem() instanceof BaubleItem) {
//                ((BaubleItem) baubleStack.getItem()).onBaubleActivated(baubleStack, player);
//            }
//        }
            });
        }
    }
}
