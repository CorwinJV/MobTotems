package com.corwinjv.mobtotems;
/**
 * Created by corwinjv on 8/30/14.
 */

import com.corwinjv.di.MobTotemsComponent;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.CowLogic;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.CreeperLogic;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.EnderLogic;
import com.corwinjv.mobtotems.config.ConfigurationHandler;
import com.corwinjv.mobtotems.entities.ModEntities;
import com.corwinjv.mobtotems.gui.OfferingBoxGuiHandler;
import com.corwinjv.mobtotems.items.ModItems;
import com.corwinjv.mobtotems.network.Network;
import com.corwinjv.mobtotems.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS,
        dependencies = "before:guideapi")
public class MobTotems {
    @Mod.Instance
    public static MobTotems instance;

    @SidedProxy(clientSide = Reference.CLIENT_SIDE_PROXY_CLASS, serverSide = Reference.SERVER_SIDE_PROXY_CLASS)
    public static CommonProxy proxy;

    private static MobTotemsComponent mobTotemsComponent;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
//        //Dagger 2 implementation
//        mobTotemsComponent = proxy.initializeDagger(instance);

        // Network & Messages
        Network.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new OfferingBoxGuiHandler());

        // Config
        ConfigurationHandler.Init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());

        // Helper stuff
        TotemHelper.init();
        MinecraftForge.EVENT_BUS.register(new CreeperLogic.CreeperTotemEntityJoinWorldEvent());
        MinecraftForge.EVENT_BUS.register(new CowLogic.CowTotemEntityJoinWorldEvent());
        MinecraftForge.EVENT_BUS.register(new EnderLogic.EnderTotemEnderTeleportEvent());

        // Blocks
        ModBlocks.init();

        // Items
        ModItems.init();

        // Entities
        ModEntities.init();
        ModEntities.registerEntities(instance);
        proxy.registerEntityRenders();

        // Keybinds
        proxy.registerKeys();
    }

    public static MobTotemsComponent component() {
        return mobTotemsComponent;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRenders();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.registerGui();
        proxy.registerParticleRenderer();
    }
}
