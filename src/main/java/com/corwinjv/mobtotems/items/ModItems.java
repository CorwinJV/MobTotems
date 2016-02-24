package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.WolfTotemBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
    public static final String TOTEM_STENCIL = "totem_stencil";

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

        BaseItem totem_stencil = new TotemStencil();
        totem_stencil.setUnlocalizedName(TOTEM_STENCIL);
        mItems.put(TOTEM_STENCIL, totem_stencil);
        ModelBakery.registerItemVariants(totem_stencil,
                new ResourceLocation(Reference.RESOURCE_PREFIX + TotemStencil.NULL_STENCIL),
                new ResourceLocation(Reference.RESOURCE_PREFIX + TotemStencil.CREEPER_STENCIL),
                new ResourceLocation(Reference.RESOURCE_PREFIX + TotemStencil.RABBIT_STENCIL),
                new ResourceLocation(Reference.RESOURCE_PREFIX + TotemStencil.SLIME_STENCIL),
                new ResourceLocation(Reference.RESOURCE_PREFIX + TotemStencil.WOLF_STENCIL));
    }

    public static void registerItems()
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
                registerRender(item, 0, key);
                if(TOTEM_STENCIL.equals(key))
                {
                    registerRender(item, TotemStencil.CREEPER_STENCIL_META, TotemStencil.CREEPER_STENCIL);
                    registerRender(item, TotemStencil.RABBIT_STENCIL_META, TotemStencil.RABBIT_STENCIL);
                    registerRender(item, TotemStencil.SLIME_STENCIL_META, TotemStencil.SLIME_STENCIL);
                    registerRender(item, TotemStencil.WOLF_STENCIL_META,TotemStencil.WOLF_STENCIL);
                }
            }
        }
    }

    private static void registerRender(BaseItem item, int meta, String key)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        meta,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
        //FMLLog.log(Level.WARN, "Registering render for key: " + Reference.RESOURCE_PREFIX + key + " with meta: ");
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

        item = mItems.get(TOTEMIC_FOCUS);
        GameRegistry.addRecipe(new ItemStack(item),
                "GXF",
                " X ",
                " X ",
                'X', Items.stick,
                'G', Items.gunpowder,
                'F', Items.feather);

        // TODO: Add crafting recipes for TotemStencil variants
    }
}
