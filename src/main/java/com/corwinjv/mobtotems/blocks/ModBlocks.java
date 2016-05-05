package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CorwinJV on 9/1/14.
 */

// Testing git integration
    
public class ModBlocks
{
    public static final String CREEPER_TOTEM = "creeper_totem";
    public static final String TOTEM_WOOD = "totem_wood";

    private static Map<String,BaseBlock> mBlocks = Collections.emptyMap();
    private static Map<String,Class<? extends TileEntity>> mTileEntityClasses = Collections.emptyMap();

    public static void init()
    {
        mBlocks = new HashMap<String,BaseBlock>();
        mTileEntityClasses = new HashMap<String, Class<? extends TileEntity>>();

        BaseBlock totem_wood = new TotemWood();
        totem_wood.setUnlocalizedName(TOTEM_WOOD);
        mBlocks.put(TOTEM_WOOD, totem_wood);
        mTileEntityClasses.put(TOTEM_WOOD, TotemTileEntity.class);
    }

    public static BaseBlock getBlock(String key)
    {
        if(mBlocks.containsKey(key))
        {
            return mBlocks.get(key);
        }
        return null;
    }

    public static void registerBlocks()
    {
        for (String key : mBlocks.keySet())
        {
            BaseBlock block = mBlocks.get(key);
            if(block != null)
            {
                GameRegistry.registerBlock(block, key);
                Class<? extends TileEntity> tileEntityClass = mTileEntityClasses.get(key);
                if(tileEntityClass != null)
                {
                    GameRegistry.registerTileEntity(tileEntityClass, key);
                }
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

    public static void registerRecipes()
    {
        for (String key : mBlocks.keySet())
        {
        }
    }
}
