package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.blocks.items.TotemWoodItemBlock;
import com.corwinjv.mobtotems.blocks.tiles.IncenseKindlingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.SacredLightTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import com.corwinjv.mobtotems.items.ModItems;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;

import static com.corwinjv.mobtotems.Reference.MOD_ID;

/**
 * Created by CorwinJV on 9/1/14.
 */
@Mod.EventBusSubscriber(modid = MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MOD_ID)
public class ModBlocks {
    public static final HashSet<BlockItem> ITEM_BLOCKS = new HashSet<>();

    public static final String TOTEM_WOOD_NAME = "totem_wood";
    public static final String SACRED_LIGHT_NAME = "sacred_light";
    public static final String INCENSE_KINDLING_BOX_NAME = "incense_kindling_box";
    public static final String OFFERING_BOX_NAME = "offering_box";

    @ObjectHolder(TOTEM_WOOD_NAME) public static final Block TOTEM_WOOD = null;
    @ObjectHolder(SACRED_LIGHT_NAME) public static final Block SACRED_LIGHT = null;
    @ObjectHolder(INCENSE_KINDLING_BOX_NAME) public static final Block INCENSE_KINDLING_BOX = null;
    @ObjectHolder(OFFERING_BOX_NAME) public static final Block OFFERING_BOX = null;

    public static void init() {
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> r, IForgeRegistryEntry<V> event, ResourceLocation name) {
        r.register(event.setRegistryName(name));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        final IForgeRegistry<Block> r = e.getRegistry();

        // Totem Wood & Variants
        Block.Properties builder = Block.Properties.create(Material.WOOD);
        register(r, new TotemWoodBlock(builder), new ResourceLocation(MOD_ID, TOTEM_WOOD_NAME));
        // todo: flatten totem wood into many blocks
//        register(r, new CreeperTotemBlock(builder), new ResourceLocation(Reference.MOD_ID, CREEPER_TOTEM_NAME));
//        register(r, new OcelotTotemBlock(builder), new ResourceLocation(Reference.MOD_ID, OCELOT_TOTEM_NAME));

        // Sacred Light
        builder = Block.Properties.create(Material.WOOD);
        register(r, new SacredLightBlock(builder), new ResourceLocation(MOD_ID, SACRED_LIGHT_NAME));

        // Incense Kindling Box
        builder = Block.Properties.create(Material.WOOD);
        register(r, new IncenseKindlingBox(builder), new ResourceLocation(MOD_ID, INCENSE_KINDLING_BOX_NAME));

        // Offering Box
        builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(1, 1);
        register(r, new OfferingBox(builder), new ResourceLocation(MOD_ID, OFFERING_BOX_NAME));
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> e) {
        final IForgeRegistry<Item> registry = e.getRegistry();

        final BlockItem[] items = {
                new TotemWoodItemBlock(TOTEM_WOOD, ModItems.defaultItemProperties()),
                new BlockItem(SACRED_LIGHT, ModItems.defaultItemProperties()),
                new BlockItem(INCENSE_KINDLING_BOX, ModItems.defaultItemProperties()),
                new BlockItem(OFFERING_BOX, ModItems.defaultItemProperties()),
        };

        for(final BlockItem item : items)
        {
            final Block block = item.getBlock();
            final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
            registry.register(item.setRegistryName(registryName));
            ITEM_BLOCKS.add(item);
        }
    }

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
        register(r, TileEntityType.Builder.create(TotemTileEntity::new, TOTEM_WOOD).build(null), new ResourceLocation(MOD_ID, TOTEM_WOOD_NAME));
        register(r, TileEntityType.Builder.create(SacredLightTileEntity::new, SACRED_LIGHT).build(null), new ResourceLocation(MOD_ID, SACRED_LIGHT_NAME));
        register(r, TileEntityType.Builder.create(IncenseKindlingBoxTileEntity::new, INCENSE_KINDLING_BOX).build(null), new ResourceLocation(MOD_ID, INCENSE_KINDLING_BOX_NAME));
        register(r, TileEntityType.Builder.create(OfferingBoxTileEntity::new, OFFERING_BOX).build(null), new ResourceLocation(MOD_ID, OFFERING_BOX_NAME));
    }

    // TODO: Flatten Totem Blocks
//    @SubscribeEvent
//    public static void registerRenders(ModelRegistryEvent e) {
//        // TODO: ModelLoader.SetCustomModelResourceLocation() got removed because metadata is gone now
//        // it is "no longer needed due to the flattening" - https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
//        for (int i = 0; i < TotemType.values().length; i++) {
//            Item item = Item.getItemFromBlock(TOTEM_WOOD);
//            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Reference.RESOURCE_PREFIX + TOTEM_WOOD_NAME, "totem_type=" + TotemType.fromMeta(i).getName());
//            ModelLoader.setCustomModelResourceLocation(item, i, modelResourceLocation);
//        }
//
//        registerRender(SACRED_LIGHT, SACRED_LIGHT_NAME);
//        registerRender(INCENSE_KINDLING_BOX, INCENSE_KINDLING_BOX_NAME);
//        registerRender(OFFERING_BOX, OFFERING_BOX_NAME);
//    }
//
//    // TODO: Flatten Totem Blocks
//    private static void registerRender(Block block, String key) {
//        Item item = Item.getItemFromBlock(block);
//        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
//    }
}
