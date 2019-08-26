package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.creativetab.CreativeTabMT;
import com.corwinjv.mobtotems.items.baubles.WolfTotemBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashMap;
import java.util.Map;

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
    @ObjectHolder(TOTEMIC_FOCUS_NAME)
    public static Item TOTEMIC_FOCUS;
    public static final Item WOLF_TOTEM_BAUBLE = new WolfTotemBauble().setRegistryName(Reference.MOD_ID, WOLF_TOTEM_BAUBLE_NAME);
    public static final Item CARVING_KNIFE = new CarvingKnife().setRegistryName(Reference.MOD_ID, CARVING_KNIFE_NAME);

    public static void init() {
    }

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties().group(CreativeTabMT.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        final IForgeRegistry<Item> registry = e.getRegistry();

        register(registry, new TotemicFocus(defaultItemProperties()), TOTEMIC_FOCUS_NAME);
        register(registry, new WolfTotemBauble(defaultItemProperties()), WOLF_TOTEM_BAUBLE_NAME);
        register(registry, new CarvingKnife(defaultItemProperties()), CARVING_KNIFE_NAME);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> registry, IForgeRegistryEntry<V> entry, ResourceLocation name) {
        registry.register(entry.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> registry, IForgeRegistryEntry<V> entry, String name) {
        register(registry, entry, new ResourceLocation(Reference.MOD_ID, name));
    }

    public static void registerRenders() {
        for (String key : ITEMS.keySet()) {
            Item item = ITEMS.get(key);
            if (item != null) {
                registerRender(item, key);
            }
        }
    }

    private static void registerRender(Item item, String key) {
        Minecraft.getInstance().getItemRenderer().getItemModelMesher().register(item,
                new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }
}
