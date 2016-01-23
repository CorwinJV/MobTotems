package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vanhc011 on 9/1/14.
 */

public class ModBlocks
{
    public static final String CREEPER_TOTEM = "creeper_totem";

    public static Map<String,BaseBlock> mBlocks = Collections.emptyMap();


    public static void init()
    {
        mBlocks = new HashMap<String,BaseBlock>();

        BaseBlock creeper_totem = new CreeperTotem();
        creeper_totem.setUnlocalizedName(CREEPER_TOTEM);
        mBlocks.put(CREEPER_TOTEM, creeper_totem);
    }

    public static void registerBlocks()
    {
        for (String key : mBlocks.keySet())
        {
            BaseBlock block = mBlocks.get(key);
            if(block != null)
            {
                GameRegistry.registerBlock(block, key);
            }
        }
    }

    public static void registerRenders()
    {
        for (String key : mBlocks.keySet())
        {
            BaseBlock block = mBlocks.get(key);
            if(block != null)
            {
                registerRender(block, key);
            }
        }
    }

    private static void registerRender(BaseBlock block, String key)
    {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        0,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));

    }
}