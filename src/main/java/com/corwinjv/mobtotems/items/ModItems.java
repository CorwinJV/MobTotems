package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.WolfTotemBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CorwinJV on 9/1/14.
 */

// Testing git integration

public class ModItems
{
    public static final String WOLF_TOTEM_BAUBLE = "wolf_totem_bauble";

    private static Map<String,BaseItem> mItems = Collections.emptyMap();

    public static void init()
    {
        mItems = new HashMap<String,BaseItem>();

        BaseItem wolf_totem_bauble = new WolfTotemBauble();
        wolf_totem_bauble.setUnlocalizedName(WOLF_TOTEM_BAUBLE);
        mItems.put(WOLF_TOTEM_BAUBLE, wolf_totem_bauble);
    }

    public static void registerBlocks()
    {
        for (String key : mItems.keySet())
        {
            BaseItem item = mItems.get(key);
            if(item != null)
            {
                GameRegistry.registerItem(item, key);
            }
        }
    }

    public static void registerRenders()
    {
        for (String key : mItems.keySet())
        {
            BaseItem item = mItems.get(key);
            if(item != null)
            {
                registerRender(item, key);
            }
        }
    }

    private static void registerRender(BaseItem item, String key)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        0,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    public static void registerRecipes()
    {
        Item item = mItems.get(WOLF_TOTEM_BAUBLE);
        GameRegistry.addRecipe(new ItemStack(item),
                "GFG",
                " G ",
                " F ",
                'G', Items.gunpowder,
                'F', Blocks.oak_fence);

        FMLLog.log(Level.DEBUG, "CJV OreDict says: " + OreDictionary.getOreID("plankWood"));
    }
}
