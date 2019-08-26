package com.corwinjv.mobtotems.network;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Created by CorwinJV on 2/1/2016.
 */
public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int DISCRIMINATOR_ID = 0;

    public static void init() {
        HANDLER.registerMessage(getDiscriminatorId(), ActivateBaublePacket.class, ActivateBaublePacket::encode, ActivateBaublePacket::decode, ActivateBaublePacket.Handler::handle);
        HANDLER.registerMessage(getDiscriminatorId(), ActivateKnifePacket.class, ActivateKnifePacket::encode, ActivateKnifePacket::decode, ActivateKnifePacket.Handler::handle);
        HANDLER.registerMessage(getDiscriminatorId(), SetKnifeMetaMessage.class, SetKnifeMetaMessage::encode, SetKnifeMetaMessage::decode, SetKnifeMetaMessage.Handler::handle);

        HANDLER.registerMessage(getDiscriminatorId(), OpenKnifeGuiMessage.class, OpenKnifeGuiMessage::encode, OpenKnifeGuiMessage::decode, OpenKnifeGuiMessage.Handler::handle);
    }

    private static int getDiscriminatorId() {
        return DISCRIMINATOR_ID++;
    }

    public static void sendToServer(Object message) {
        HANDLER.sendToServer(message);
    }

    public static void sendToAll(Object message) {
        HANDLER.sendToServer(message);
    }

    public static void sendTo(Object message, ServerPlayerEntity player) {
        if(!(player instanceof FakePlayer)) {
            HANDLER.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
