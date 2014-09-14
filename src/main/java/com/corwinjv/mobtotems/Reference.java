package com.corwinjv.mobtotems;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import net.minecraft.entity.EntityList;

import java.util.Iterator;

/**
 * Created by vanhc011 on 8/31/14.
 */
public class Reference {
    public static final String MOD_ID = "mobtotems";
    public static final String MOD_NAME = "Mob Totems";
    public static final String MOD_VERSION = "1.7.2-1.0";

    public static final String RESOURCE_PREFIX = MOD_ID + ":";

    public static final String CLIENT_PROXY = "com.corwinjv.mobtotems.proxy.ClientProxy";
    public static final String SERVER_PROXY = "com.corwinjv.mobtotems.proxy.ServerProxy";

    public static final String GUI_FACTORY_CLASS = "com.corwinjv.mobtotems.gui.GuiFactoryMT";

    public static void PrintEntityList()
    {
        Iterator entityNameItr = EntityList.stringToClassMapping.keySet().iterator();
        FMLLog.log(Reference.MOD_NAME, Level.INFO, "-Printing Registered Entity Classes-");
        while(entityNameItr.hasNext())
        {
            String key = (String)entityNameItr.next();
            FMLLog.log(Reference.MOD_NAME, Level.INFO, String.format("     %s", key));
        }
    }
}
