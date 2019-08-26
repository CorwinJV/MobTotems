package com.corwinjv.mobtotems.network;

import com.corwinjv.mobtotems.items.CarvingKnife;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by CorwinJV on 1/20/2017.
 */
public class OpenKnifeGuiMessage {
    private Hand hand = Hand.MAIN_HAND;
    private int meta = 0;

    public OpenKnifeGuiMessage setHand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public OpenKnifeGuiMessage setMeta(int meta) {
        this.meta = meta;
        return this;
    }

    public static void encode(OpenKnifeGuiMessage packet, PacketBuffer buffer) {
        buffer.writeInt(packet.meta);
        switch (packet.hand) {
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

    public static OpenKnifeGuiMessage decode(PacketBuffer buffer) {

        OpenKnifeGuiMessage packet = new OpenKnifeGuiMessage();
        packet.meta = buffer.readInt();
        int handInt = buffer.readInt();
        switch (handInt) {
            case 0: {
                packet.hand = Hand.MAIN_HAND;
                break;
            }
            case 1: {
                packet.hand = Hand.OFF_HAND;
                break;
            }
        }
        return packet;
    }

    public static class Handler {
        public static void handle(OpenKnifeGuiMessage packet, Supplier<NetworkEvent.Context> context) {
            ItemStack stack = context.get().getSender().getHeldItem(packet.hand);
            if (stack.getItem() instanceof CarvingKnife) {
                ((CarvingKnife) stack.getItem()).openGui(context.get().getSender(), packet.meta);
            }

        }
    }
}
