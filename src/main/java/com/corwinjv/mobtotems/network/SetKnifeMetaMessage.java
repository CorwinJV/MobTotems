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
public class SetKnifeMetaMessage  {
    public int meta;
    public Hand hand = Hand.MAIN_HAND;

    public SetKnifeMetaMessage(int meta, Hand hand) {
        this.meta = meta;
        this.hand = hand;
    }

    public SetKnifeMetaMessage setHand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public SetKnifeMetaMessage setMeta(int meta) {
        this.meta = meta;
        return this;
    }

    public static void encode(SetKnifeMetaMessage packet, PacketBuffer buffer) {
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

    public static SetKnifeMetaMessage decode(PacketBuffer buffer) {

        int meta = buffer.readInt();
        int handInt = buffer.readInt();
        Hand hand = Hand.MAIN_HAND;
        switch (handInt) {
            case 0: {
                hand = Hand.MAIN_HAND;
                break;
            }
            case 1: {
                hand = Hand.OFF_HAND;
                break;
            }
        }
        return new SetKnifeMetaMessage(meta, hand);
    }

    public static class Handler {
        public static void handle(final SetKnifeMetaMessage packet, Supplier<NetworkEvent.Context> context) {
            ItemStack stack = context.get().getSender().getHeldItem(packet.hand);
            if (stack.getItem() instanceof CarvingKnife) {
                ((CarvingKnife) stack.getItem()).setSelectedCarving(stack, packet.meta);
            }
        }
    }
}
