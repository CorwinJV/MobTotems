package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.SacredLightTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CorwinJV on 9/1/14.
 */

public class ModBlocks
{
    public static final String TOTEM_WOOD = "totem_wood";
    public static final String SACRED_LIGHT = "sacred_light";
    public static final String INCENSE_KINDLING_BOX = "incense_kindling_box";

    private static Map<String,ModBlock> mBlocks = Collections.emptyMap();
    private static Map<String,Class<? extends TileEntity>> mTileEntityClasses = Collections.emptyMap();

    public static void init()
    {
        mBlocks = new HashMap<String, ModBlock>();
        mTileEntityClasses = new HashMap<String, Class<? extends TileEntity>>();

        ModBlock totem_wood = new TotemWoodBlock();
        totem_wood.setUnlocalizedName(TOTEM_WOOD);
        mBlocks.put(TOTEM_WOOD, totem_wood);
        mTileEntityClasses.put(TOTEM_WOOD, TotemTileEntity.class);

        ModBlock sacred_light = new SacredLightBlock();
        sacred_light.setUnlocalizedName(SACRED_LIGHT);
        mTileEntityClasses.put(SACRED_LIGHT, SacredLightTileEntity.class);
        mBlocks.put(SACRED_LIGHT, sacred_light);

        ModBlock incense_kindling_box = new IncenseKindlingBox();
        incense_kindling_box.setUnlocalizedName(INCENSE_KINDLING_BOX);
        mTileEntityClasses.put(INCENSE_KINDLING_BOX, IncenseKindlingBoxTileEntity.class);
        mBlocks.put(INCENSE_KINDLING_BOX, incense_kindling_box);
    }

    public static ModBlock getBlock(String key)
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
            ModBlock block = mBlocks.get(key);
            if(block != null)
            {
                GameRegistry.register(block.setRegistryName(new ResourceLocation(Reference.MOD_ID, key)));
                GameRegistry.register(new ItemBlock(block), block.getRegistryName());

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
            ModBlock block = mBlocks.get(key);
            if(block != null)
            {
                registerRender(block, key);
            }
        }
    }

    private static void registerRender(ModBlock block, String key)
    {
        Item item = Item.getItemFromBlock(block);

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        0,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    public static void registerRecipes()
    {
        ModBlock item = mBlocks.get(SACRED_LIGHT);
        GameRegistry.addRecipe(new ItemStack(item, 1),
                "GRG",
                "XOX",
                "GTG",
                'G', Items.GUNPOWDER,
                'R', Items.BLAZE_ROD,
                'X', Items.ROTTEN_FLESH,
                'O', Blocks.TORCH,
                'T', mBlocks.get(TOTEM_WOOD));

        item = mBlocks.get(INCENSE_KINDLING_BOX);
        GameRegistry.addRecipe(new ItemStack(item, 4),
                "PPP",
                "PIP",
                "PFP",
                'P', Blocks.PLANKS,
                'I', Blocks.TALLGRASS,
                'F', Items.FLINT);
    }
}
