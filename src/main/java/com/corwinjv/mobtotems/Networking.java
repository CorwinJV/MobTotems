package com.corwinjv.mobtotems;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * Created by Corwin on 1/22/2016.
 */
public class Networking
{
    public static SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
}
