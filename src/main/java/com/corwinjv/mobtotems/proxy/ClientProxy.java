package com.corwinjv.mobtotems.proxy;

import com.corwinjv.mobtotems.blocks.ModBlocks;

/**
 * Created by Corwin on 1/23/2016.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenders()
    {
        ModBlocks.registerRenders();
    }
}
