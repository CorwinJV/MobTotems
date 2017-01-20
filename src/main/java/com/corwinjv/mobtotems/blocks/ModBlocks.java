package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.items.TotemWoodItemBlock;
import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.SacredLightTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;

/**
 * Created by CorwinJV on 9/1/14.
 */

@Mod.EventBusSubscriber
public class ModBlocks
{
    public static final String TOTEM_WOOD_NAME = "totem_wood";
    public static final String SACRED_LIGHT_NAME = "sacred_light";
    public static final String INCENSE_KINDLING_BOX_NAME = "incense_kindling_box";

    public static final Block TOTEM_WOOD = new TotemWoodBlock().setUnlocalizedName(TOTEM_WOOD_NAME);
    public static final Block SACRED_LIGHT = new SacredLightBlock().setUnlocalizedName(SACRED_LIGHT_NAME);
    public static final Block INCENSE_KINDLING_BOX = new IncenseKindlingBox().setUnlocalizedName(INCENSE_KINDLING_BOX_NAME);

    public static void init()
    {
        GameRegistry.registerTileEntity(TotemTileEntity.class, TOTEM_WOOD_NAME);
        GameRegistry.registerTileEntity(SacredLightTileEntity.class, SACRED_LIGHT_NAME);
        GameRegistry.registerTileEntity(IncenseKindlingBoxTileEntity.class, INCENSE_KINDLING_BOX_NAME);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e)
    {
        GameRegistry.register(TOTEM_WOOD.setRegistryName(new ResourceLocation(Reference.MOD_ID,  TOTEM_WOOD_NAME)));
        GameRegistry.register(new TotemWoodItemBlock(TOTEM_WOOD), TOTEM_WOOD.getRegistryName());

        GameRegistry.register(SACRED_LIGHT.setRegistryName(new ResourceLocation(Reference.MOD_ID, SACRED_LIGHT_NAME)));
        GameRegistry.register(new ItemBlock(SACRED_LIGHT), SACRED_LIGHT.getRegistryName());

        GameRegistry.register(INCENSE_KINDLING_BOX.setRegistryName(new ResourceLocation(Reference.MOD_ID, INCENSE_KINDLING_BOX_NAME)));
        GameRegistry.register(new ItemBlock(INCENSE_KINDLING_BOX), INCENSE_KINDLING_BOX.getRegistryName());
    }


    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent e)
    {
        for(int i = 0; i < TotemType.values().length; i++)
        {
            Item item = Item.getItemFromBlock(TOTEM_WOOD);
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Reference.RESOURCE_PREFIX + TOTEM_WOOD_NAME, "totem_type=" + TotemType.fromMeta(i).getName()) ;
            ModelLoader.setCustomModelResourceLocation(item, i, modelResourceLocation);
            //FMLLog.log(Level.INFO, i + " modelResourceLocation: " + modelResourceLocation.toString());
        }

        registerRender(SACRED_LIGHT, SACRED_LIGHT_NAME);
        registerRender(INCENSE_KINDLING_BOX, INCENSE_KINDLING_BOX_NAME);
    }

    private static void registerRender(Block block, String key) {
        Item item = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    public static void registerRecipes() {
        Block item = SACRED_LIGHT;
        GameRegistry.addRecipe(new ItemStack(item, 1),
                "GRG",
                "XOX",
                "GTG",
                'G', Items.GUNPOWDER,
                'R', Items.BLAZE_ROD,
                'X', Items.ROTTEN_FLESH,
                'O', Blocks.TORCH,
                'T', TOTEM_WOOD_NAME);

        item = INCENSE_KINDLING_BOX;
        GameRegistry.addRecipe(new ItemStack(item, 4),
                "WWW",
                "WIW",
                "WFW",
                'W', Blocks.PLANKS,
                'I', Blocks.TALLGRASS,
                'F', Items.FLINT);
    }
}
