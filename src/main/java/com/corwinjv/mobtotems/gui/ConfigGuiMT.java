package com.corwinjv.mobtotems.gui;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.config.ConfigurationHandler;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class ConfigGuiMT extends GuiConfig
{
    public ConfigGuiMT(GuiScreen guiScreen)
    {
        super(guiScreen,
                new ConfigElement(ConfigurationHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Reference.MOD_ID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
    }
}
