package com.corwinjv.mobtotems.proxy;

import com.corwinjv.mobtotems.KeyBindings;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.entities.ModEntities;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        ModEntities.registerRenders();
    }

    @Override
    public void registerKeys()
    {
        MinecraftForge.EVENT_BUS.register(new KeyBindings());
    }
}
