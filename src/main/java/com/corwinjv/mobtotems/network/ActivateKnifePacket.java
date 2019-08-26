package com.corwinjv.mobtotems.network;

import com.corwinjv.mobtotems.items.CarvingKnife;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by CorwinJV on 1/19/2017.
 */
public class ActivateKnifePacket {
    private Hand hand = Hand.MAIN_HAND;

    public ActivateKnifePacket setHand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public static void encode(ActivateKnifePacket message, PacketBuffer buffer) {
        switch (message.hand) {
            case MAIN_HAND: {
                buffer.writeInt(0);
                break;
            }
            case OFF_HAND: {
                buffer.writeInt(1);
                break;
            }
        }
    }

    public static ActivateKnifePacket decode(PacketBuffer buffer) {
        ActivateKnifePacket message = new ActivateKnifePacket();

        int handInt = buffer.readInt();
        switch (handInt) {
            case 0: {
                message.hand = Hand.MAIN_HAND;
                break;
            }
            case 1: {
                message.hand = Hand.OFF_HAND;
                break;
            }
        }

        return message;
    }

    public static class Handler {
        public static void handle(final ActivateKnifePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                ItemStack stack = context.get().getSender().getHeldItem(packet.hand);

                if (stack.getItem() instanceof CarvingKnife) {
                    ((CarvingKnife) stack.getItem()).onKnifeActivated(context.get().getSender(), packet.hand);
                }
            });
        }
    }
}
