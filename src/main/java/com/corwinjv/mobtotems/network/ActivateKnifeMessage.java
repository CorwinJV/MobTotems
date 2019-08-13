package com.corwinjv.mobtotems.network;

import com.corwinjv.mobtotems.items.CarvingKnife;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created by CorwinJV on 1/19/2017.
 */
public class ActivateKnifeMessage extends Message<ActivateKnifeMessage> {
    private EnumHand hand = EnumHand.MAIN_HAND;

    @Override
    protected void handleServer(ActivateKnifeMessage message, PlayerEntityMP player) {
        ItemStack stack = player.getHeldItem(message.hand);
        if (stack.getItem() instanceof CarvingKnife) {
            ((CarvingKnife) stack.getItem()).onKnifeActivated(player, message.hand);
        }
    }

    public ActivateKnifeMessage setHand(EnumHand hand) {
        this.hand = hand;
        return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        switch (hand) {
            case MAIN_HAND: {
                buf.writeInt(0);
                break;
            }
            case OFF_HAND: {
                buf.writeInt(1);
                break;
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int handInt = buf.readInt();
        switch (handInt) {
            case 0: {
                hand = EnumHand.MAIN_HAND;
                break;
            }
            case 1: {
                hand = EnumHand.OFF_HAND;
                break;
            }
        }
    }
}
