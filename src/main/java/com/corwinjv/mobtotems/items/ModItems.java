package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.WolfTotemBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

/**
 * Created by CorwinJV on 9/1/14.
 */

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModItems {
    public static final Map<String, Item> ITEMS = new HashMap<String, Item>();

    public static final String WOLF_TOTEM_BAUBLE_NAME = "wolf_totem_bauble";
    public static final String TOTEMIC_FOCUS_NAME = "totemic_focus";
    public static final String CARVING_KNIFE_NAME = "carving_knife";

    // We're going to use the TotemicFocus for the CreativeTab:
    public static final Item TOTEMIC_FOCUS = new TotemicFocus().setUnlocalizedName(TOTEMIC_FOCUS_NAME).setRegistryName(Reference.MOD_ID, TOTEMIC_FOCUS_NAME);
    public static final Item WOLF_TOTEM_BAUBLE = new WolfTotemBauble().setUnlocalizedName(WOLF_TOTEM_BAUBLE_NAME).setRegistryName(Reference.MOD_ID, WOLF_TOTEM_BAUBLE_NAME);
    ;
    public static final Item CARVING_KNIFE = new CarvingKnife().setUnlocalizedName(CARVING_KNIFE_NAME).setRegistryName(Reference.MOD_ID, CARVING_KNIFE_NAME);
    ;

    public static void init() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        final IForgeRegistry<Item> registry = e.getRegistry();
        final Item[] items = {
                TOTEMIC_FOCUS,
                WOLF_TOTEM_BAUBLE,
                CARVING_KNIFE
        };

        for (Item item : items) {
            registry.register(item);
        }

        ITEMS.put(TOTEMIC_FOCUS_NAME, TOTEMIC_FOCUS);
        ITEMS.put(WOLF_TOTEM_BAUBLE_NAME, WOLF_TOTEM_BAUBLE);
        ITEMS.put(CARVING_KNIFE_NAME, CARVING_KNIFE);
    }

    public static void registerRenders() {
        for (String key : ITEMS.keySet()) {
            Item item = ITEMS.get(key);
            if (item != null) {
                registerRender(item, 0, key);
            }
        }
    }

    private static void registerRender(Item item, int meta, String key) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,
                meta,
                new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }
}
