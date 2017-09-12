package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.items.TotemWoodItemBlock;
import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.SacredLightTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import com.corwinjv.mobtotems.config.ConfigurationHandler;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import scala.tools.nsc.backend.icode.TypeKinds;

import java.util.HashSet;

/**
 * Created by CorwinJV on 9/1/14.
 */

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModBlocks {
    public static final HashSet<ItemBlock> ITEM_BLOCKS = new HashSet<>();

    public static final String TOTEM_WOOD_NAME = "totem_wood";
    public static final String SACRED_LIGHT_NAME = "sacred_light";
    public static final String INCENSE_KINDLING_BOX_NAME = "incense_kindling_box";
    public static final String OFFERING_BOX_NAME = "offering_box";

    public static final Block TOTEM_WOOD = new TotemWoodBlock().setUnlocalizedName(TOTEM_WOOD_NAME).setRegistryName(Reference.MOD_ID, TOTEM_WOOD_NAME);
    public static final Block SACRED_LIGHT = new SacredLightBlock().setUnlocalizedName(SACRED_LIGHT_NAME).setRegistryName(Reference.MOD_ID, SACRED_LIGHT_NAME);
    public static final Block INCENSE_KINDLING_BOX = new IncenseKindlingBox().setUnlocalizedName(INCENSE_KINDLING_BOX_NAME).setRegistryName(Reference.MOD_ID, INCENSE_KINDLING_BOX_NAME);
    public static final Block OFFERING_BOX = new OfferingBox().setUnlocalizedName(OFFERING_BOX_NAME).setRegistryName(Reference.MOD_ID, OFFERING_BOX_NAME);

    public static void init() {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        final IForgeRegistry<Block> registry = e.getRegistry();
        final Block[] blocks = {
                TOTEM_WOOD,
                SACRED_LIGHT,
                INCENSE_KINDLING_BOX,
                OFFERING_BOX
        };

        registry.registerAll(blocks);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> e) {
        final IForgeRegistry<Item> registry = e.getRegistry();

        final ItemBlock[] items = {
                new TotemWoodItemBlock(TOTEM_WOOD),
                new ItemBlock(SACRED_LIGHT),
                new ItemBlock(INCENSE_KINDLING_BOX),
                new ItemBlock(OFFERING_BOX),
        };

        for(final ItemBlock item : items)
        {
            final Block block = item.getBlock();
            final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
            registry.register(item.setRegistryName(registryName));
            ITEM_BLOCKS.add(item);
        }

        registerTileEntities();
    }

    private static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TotemTileEntity.class, Reference.RESOURCE_PREFIX + TOTEM_WOOD_NAME);
        GameRegistry.registerTileEntity(SacredLightTileEntity.class, Reference.RESOURCE_PREFIX + SACRED_LIGHT_NAME);
        GameRegistry.registerTileEntity(IncenseKindlingBoxTileEntity.class, Reference.RESOURCE_PREFIX + INCENSE_KINDLING_BOX_NAME);
        GameRegistry.registerTileEntity(OfferingBoxTileEntity.class, Reference.RESOURCE_PREFIX + OFFERING_BOX_NAME);
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent e) {
        for (int i = 0; i < TotemType.values().length; i++) {
            Item item = Item.getItemFromBlock(TOTEM_WOOD);
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Reference.RESOURCE_PREFIX + TOTEM_WOOD_NAME, "totem_type=" + TotemType.fromMeta(i).getName());
            ModelLoader.setCustomModelResourceLocation(item, i, modelResourceLocation);
        }

        registerRender(SACRED_LIGHT, SACRED_LIGHT_NAME);
        registerRender(INCENSE_KINDLING_BOX, INCENSE_KINDLING_BOX_NAME);
        registerRender(OFFERING_BOX, OFFERING_BOX_NAME);
    }

    private static void registerRender(Block block, String key) {
        Item item = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> e) {
        Block item = SACRED_LIGHT;
        if (ConfigurationHandler.hardSacredLightRecipe) {
            e.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(item, 1),
                    "GRG",
                    "XNX",
                    "GTG",
                    'G', Items.GUNPOWDER,
                    'R', Items.BLAZE_ROD,
                    'X', Items.ROTTEN_FLESH,
                    'N', Items.NETHER_STAR,
                    'T', TOTEM_WOOD).setRegistryName(item.getRegistryName()));
        } else {
            e.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(item, 1),
                    "GRG",
                    "XOX",
                    "GTG",
                    'G', Items.GUNPOWDER,
                    'R', Items.BLAZE_ROD,
                    'X', Items.ROTTEN_FLESH,
                    'O', Blocks.TORCH,
                    'T', TOTEM_WOOD).setRegistryName(item.getRegistryName()));
        }

        item = INCENSE_KINDLING_BOX;
        e.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(item, 4),
                "WWW",
                "WIW",
                "WFW",
                'W', Blocks.PLANKS,
                'I', Blocks.TALLGRASS,
                'F', Items.FLINT).setRegistryName(item.getRegistryName()));

        item = OFFERING_BOX;
        e.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(item, 1),
                "   ",
                "SIS",
                "SSS",
                'S', Blocks.STONE,
                'I', Blocks.TALLGRASS).setRegistryName(item.getRegistryName()));
    }
}
