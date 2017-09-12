package com.corwinjv.mobtotems.network;

import com.corwinjv.mobtotems.items.CarvingKnife;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created by CorwinJV on 1/19/2017.
 */
public class SetKnifeMetaMessage extends Message<SetKnifeMetaMessage> {
    private int meta;
    private EnumHand hand = EnumHand.MAIN_HAND;

    public SetKnifeMetaMessage() {
    }

    public SetKnifeMetaMessage(int meta, EnumHand hand) {
        this.meta = meta;
        this.hand = hand;
    }

    @Override
    protected void handleServer(SetKnifeMetaMessage message, EntityPlayerMP player) {
        ItemStack stack = player.getHeldItem(message.hand);
        if (stack.getItem() instanceof CarvingKnife) {
            ((CarvingKnife) stack.getItem()).setSelectedCarving(player.getHeldItem(message.hand), message.meta);
        }
    }

    public SetKnifeMetaMessage setHand(EnumHand hand) {
        this.hand = hand;
        return this;
    }

    public SetKnifeMetaMessage setMeta(int meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(meta);
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
        meta = buf.readInt();
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
