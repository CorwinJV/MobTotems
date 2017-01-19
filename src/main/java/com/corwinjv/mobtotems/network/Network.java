package com.corwinjv.mobtotems.network;

import com.corwinjv.mobtotems.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by CorwinJV on 2/1/2016.
 */
public class Network {
    private static SimpleNetworkWrapper instance = null;
    private static int DISCRIMINATOR_ID = 0;

    public static void init()
    {
        instance = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

        instance.registerMessage(ActivateBaubleMessage.class, ActivateBaubleMessage.class, getDiscriminatorId(), Side.SERVER);
        instance.registerMessage(ActivateKnifeMessage.class, ActivateKnifeMessage.class, getDiscriminatorId(), Side.SERVER);
    }

    private static int getDiscriminatorId()
    {
        return DISCRIMINATOR_ID++;
    }

    public static void sendToServer(Message message)
    {
        instance.sendToServer(message);
    }

    public static void sendToAll(Message message)
    {
        instance.sendToAll(message);
    }
}
