package com.corwinjv.mobtotems.proxy;

import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.items.ModItems;

/**
 * Created by CorwinJV on 1/23/2016.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenders()
    {
        ModBlocks.registerRenders();
        ModItems.registerRenders();
    }
}
