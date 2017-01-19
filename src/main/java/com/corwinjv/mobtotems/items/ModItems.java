package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.WolfTotemBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    public static final String CARVING_KNIFE = "carving_knife";
    //public static final String RESIN_BALL = "resin_ball";

    // We're going to use the TotemicFocus for the CreativeTab:
    public static Item TOTEMIC_FOCUS_ITEM = new TotemicFocus().setUnlocalizedName(TOTEMIC_FOCUS);
    private static Map<String,Item> mItems = Collections.emptyMap();

    public static void init()
    {
        mItems = new HashMap<String,Item>();

        Item item = new WolfTotemBauble().setUnlocalizedName(WOLF_TOTEM_BAUBLE);
        mItems.put(WOLF_TOTEM_BAUBLE, item);

        mItems.put(TOTEMIC_FOCUS, TOTEMIC_FOCUS_ITEM);

        item = new CarvingKnife().setUnlocalizedName(CARVING_KNIFE);
        mItems.put(CARVING_KNIFE, item);
    }

    public static Item getItem(String key)
    {
        if(mItems.containsKey(key))
        {
            return mItems.get(key);
        }
        return null;
    }


    public static void registerItems()
    {
        for (String key : mItems.keySet())
        {
            Item item = mItems.get(key);
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
            Item item = mItems.get(key);
            if(item != null)
            {
                registerRender(item, 0, key);
            }
        }
    }

    private static void registerRender(Item item, int meta, String key)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        meta,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    public static void registerRecipes()
    {
        Item item = mItems.get(TOTEMIC_FOCUS);
        GameRegistry.addRecipe(new ItemStack(item),
                "GXF",
                " X ",
                " X ",
                'X', Items.STICK,
                'G', Items.GUNPOWDER,
                'F', Items.FEATHER);

        // Wolf totem bauble recipe

        item = mItems.get(WOLF_TOTEM_BAUBLE);
        GameRegistry.addRecipe(new ItemStack(item),
                "SSS",
                "CPC",
                " C ",
                'C', Blocks.CLAY,
                'P', Items.BLAZE_POWDER,
                'S', Items.STRING);
    }
}
