package com.corwinjv.mobtotems;
/**
 * Created by corwinjv on 8/30/14.
 */

import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.entities.ModEntities;
import com.corwinjv.mobtotems.gui.BaublesChargeGui;
import com.corwinjv.mobtotems.items.ModItems;
import com.corwinjv.mobtotems.network.Network;
import com.corwinjv.mobtotems.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.corwinjv.mobtotems.config.ConfigurationHandler;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class MobTotems
{
    @Mod.Instance
    public static MobTotems instance;

    @SidedProxy(clientSide = Reference.CLIENT_SIDE_PROXY_CLASS, serverSide = Reference.SERVER_SIDE_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Network & Messages
        Network.init();

        // Config
        ConfigurationHandler.Init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());

        // Blocks and Items
        ModBlocks.init();
        ModBlocks.registerBlocks();
        ModBlocks.registerRecipes();

        ModItems.init();
        ModItems.registerItems();
        ModItems.registerRecipes();

        // Entities
        ModEntities.init();
        ModEntities.registerEntities(instance);
        ModEntities.registerEntityRenders();

        // Keybinds
        proxy.registerKeys();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenders();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.registerGui();
    }
}
