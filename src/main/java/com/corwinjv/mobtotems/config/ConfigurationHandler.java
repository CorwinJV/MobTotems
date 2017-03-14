package com.corwinjv.mobtotems.config;

import com.corwinjv.mobtotems.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Created by CorwinJV on 8/31/14.
 */
public class ConfigurationHandler {
    public static Configuration configuration;

    /**
     * Config Properties
     **/
    public static boolean hardSacredLightRecipe = true;

    public static void Init(File aConfigFile) {
        if (configuration == null) {
            configuration = new Configuration(aConfigFile);
            loadConfiguration();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (Reference.MOD_ID.equalsIgnoreCase(event.getModID())) {
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        hardSacredLightRecipe = configuration.getBoolean("HardSacredLightRecipe", Configuration.CATEGORY_GENERAL, true, "Hard Sacred Light recipe");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
