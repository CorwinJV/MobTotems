package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.WolfTotemBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
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
    public static final String TOTEMIC_FOCUS = "totemic_focus";

    private static Map<String,BaseItem> mItems = Collections.emptyMap();

    public static void init()
    {
        mItems = new HashMap<String,BaseItem>();

        BaseItem wolf_totem_bauble = new WolfTotemBauble();
        wolf_totem_bauble.setUnlocalizedName(WOLF_TOTEM_BAUBLE);
        mItems.put(WOLF_TOTEM_BAUBLE, wolf_totem_bauble);

        BaseItem totemic_focus = new TotemicFocus();
        totemic_focus.setUnlocalizedName(TOTEMIC_FOCUS);
        mItems.put(TOTEMIC_FOCUS, totemic_focus);
    }

    public static void registerItems()
    {
        for (String key : mItems.keySet())
        {
            BaseItem item = mItems.get(key);
            if(item != null)
            {
                GameRegistry.register(mItems.get(key), new ResourceLocation(Reference.MOD_ID, key));
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
                registerRender(item, 0, key);
            }
        }
    }

    private static void registerRender(BaseItem item, int meta, String key)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        meta,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    public static void registerRecipes()
    {
        Item item = mItems.get(WOLF_TOTEM_BAUBLE);
        GameRegistry.addRecipe(new ItemStack(item),
                "GFG",
                " G ",
                " F ",
                'G', Items.GUNPOWDER,
                'F', Blocks.OAK_FENCE);

        item = mItems.get(TOTEMIC_FOCUS);
        GameRegistry.addRecipe(new ItemStack(item),
                "GXF",
                " X ",
                " X ",
                'X', Items.STICK,
                'G', Items.GUNPOWDER,
                'F', Items.FEATHER);
    }
}
