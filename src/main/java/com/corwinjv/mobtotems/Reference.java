package com.corwinjv.mobtotems;

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import net.minecraft.entity.EntityList;

import java.util.Iterator;

/**
 * Created by CorwinJV on 8/31/14.
 */
public class Reference {
    public static final String MOD_ID = "mobtotems";
    public static final String MOD_NAME = "Mob Totems";
    public static final String MOD_VERSION = "1.11-0.2.1";
    public static final String RESOURCE_PREFIX = MOD_ID + ":";

    public static final String GUI_FACTORY_CLASS = "com.corwinjv.mobtotems.gui.GuiFactoryMT";
    public static final String CLIENT_SIDE_PROXY_CLASS = "com.corwinjv.mobtotems.proxy.ClientProxy";
    public static final String SERVER_SIDE_PROXY_CLASS = "com.corwinjv.mobtotems.proxy.CommonProxy";

    public static void PrintEntityList()
    {
        Iterator entityNameItr = EntityList.getEntityNameList().iterator();
        FMLLog.log(Reference.MOD_NAME, Level.INFO, "-Printing Registered Entity Classes-");
        while(entityNameItr.hasNext())
        {
            String key = (String)entityNameItr.next();
            FMLLog.log(Reference.MOD_NAME, Level.INFO, String.format("     %s", key));
        }
    }
}
