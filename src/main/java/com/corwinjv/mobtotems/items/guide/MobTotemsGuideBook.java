package com.corwinjv.mobtotems.items.guide;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
import amerifrance.guideapi.page.PageTextImage;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.config.ConfigurationHandler;
import com.corwinjv.mobtotems.gui.util;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.corwinjv.mobtotems.blocks.ModBlocks.TOTEM_WOOD;
import static com.corwinjv.mobtotems.blocks.ModBlocks.TOTEM_WOOD_NAME;

@GuideBook
public class MobTotemsGuideBook implements IGuideBook {

    public static Book myGuide;

    @Nonnull
    @Override
    public Book buildBook() {
        // Create the map of entries. A LinkedHashMap is used to retain the order of entries.
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        List<CategoryAbstract> categories = new ArrayList<>();

        // =====================================
        // Totems Category

        // Totemic Focus Entry
        List<IPage> pages = new ArrayList<IPage>();
        pages.add(new PageText(util.getLocalizedGuideText("totemic_focus_page_1")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(null, ModItems.ITEMS.get(ModItems.TOTEMIC_FOCUS_NAME), "GXF",
                " X ",
                " X ",
                'X', Items.STICK,
                'G', Items.GUNPOWDER,
                'F', Items.FEATHER)));
        entries.put(new ResourceLocation(Reference.MOD_ID, ModItems.TOTEMIC_FOCUS_NAME), new EntryItemStack(pages, util.getLocalizedGuideText("totemic_focus_entry_name"), new ItemStack(ModItems.ITEMS.get(ModItems.TOTEMIC_FOCUS_NAME))));

        // Carving Knife Entry
        pages = new ArrayList<>();
        pages.add(new PageText(util.getLocalizedGuideText("carving_knife_page_1")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(null, ModItems.ITEMS.get(ModItems.CARVING_KNIFE_NAME),
                "   ",
                " F ",
                "S  ",
                'F', Items.FLINT,
                'S', Items.STICK)));
        entries.put(new ResourceLocation(Reference.MOD_ID, ModItems.CARVING_KNIFE_NAME), new EntryItemStack(pages, util.getLocalizedGuideText("carving_knife_entry_name"), new ItemStack(ModItems.ITEMS.get(ModItems.CARVING_KNIFE_NAME))));

        // Offering Box Entry
        pages = new ArrayList<>();
        pages.add(new PageText(util.getLocalizedGuideText("offering_box_page_1")));
        pages.add(new PageText(util.getLocalizedGuideText("offering_box_page_2")));
        pages.add(new PageText(util.getLocalizedGuideText("offering_box_page_3")));
        pages.add(new PageTextImage(util.getLocalizedGuideText("offering_box_page_4"), util.getGuideResourceLocation("finished_totem_example_3.png"), false));
        pages.add(new PageTextImage(util.getLocalizedGuideText("offering_box_page_5"), util.getGuideResourceLocation("finished_totem_example_4.png"), false));
        entries.put(new ResourceLocation(Reference.MOD_ID, ModBlocks.OFFERING_BOX_NAME), new EntryItemStack(pages, util.getLocalizedGuideText("offering_box_entry_name"), new ItemStack(ModBlocks.OFFERING_BOX)));

        // Sacred Light Entry
        pages = new ArrayList<>();
        pages.add(new PageText(util.getLocalizedGuideText("sacred_light_page_1")));
//        if (ConfigurationHandler.hardSacredLightRecipe) {
            pages.add(new PageIRecipe(new ShapedOreRecipe(null, ModBlocks.SACRED_LIGHT,
                    "GRG",
                    "XNX",
                    "GTG",
                    'G', Items.GUNPOWDER,
                    'R', Items.BLAZE_ROD,
                    'X', Items.ROTTEN_FLESH,
                    'N', Items.NETHER_STAR,
                    'T', TOTEM_WOOD).setRegistryName(ModBlocks.SACRED_LIGHT_NAME)));
//        } else {
//            pages.add(new PageIRecipe(new ShapedOreRecipe(null, ModBlocks.SACRED_LIGHT,
//                    "GRG",
//                    "XOX",
//                    "GTG",
//                    'G', Items.GUNPOWDER,
//                    'R', Items.BLAZE_ROD,
//                    'X', Items.ROTTEN_FLESH,
//                    'O', Blocks.TORCH,
//                    'T', TOTEM_WOOD)));
//        }

        pages.add(new PageTextImage(util.getLocalizedGuideText("sacred_light_example"), util.getGuideResourceLocation("finished_totem_example.png"), false));
        entries.put(new ResourceLocation(Reference.MOD_ID, "sacred_light"), new EntryItemStack(pages, util.getLocalizedGuideText("sacred_light_entry_name"), new ItemStack(ModBlocks.SACRED_LIGHT)));

        categories.add(new CategoryItemStack(entries, util.getLocalizedGuideText("totems_category_name"), new ItemStack(ModItems.ITEMS.get(ModItems.TOTEMIC_FOCUS_NAME), 1, 0)));

        // =====================================
        // Totem Types Category
        entries = new LinkedHashMap<>();
        for (int i = 1; i < TotemType.values().length; i++) {
            pages = new ArrayList<>();
            pages.add(new PageText(util.getLocalizedGuideText("totem_types_page_" + i)));
            // TODO: add cost
            pages.add(new PageOfferingBoxCost(TotemType.fromMeta(i)));

            entries.put(new ResourceLocation(Reference.MOD_ID, TOTEM_WOOD_NAME + "_totem_type=" + TotemType.fromMeta(i).getName()),
                    new EntryItemStack(pages,
                            util.getLocalizedGuideText("totem_type_page_name_" + i),
                            new ItemStack(ModBlocks.TOTEM_WOOD, 1, i)));
        }

        categories.add(new CategoryItemStack(entries, util.getLocalizedGuideText("totem_types_category_name"), new ItemStack(ModBlocks.TOTEM_WOOD, 1, TotemType.CREEPER.getMeta())));

        // =====================================
        // Baubles category
        entries = new LinkedHashMap<>();

        pages = new ArrayList<IPage>();
        pages.add(new PageText(util.getLocalizedGuideText("wolf_bauble_page_1")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(null, ModItems.ITEMS.get(ModItems.WOLF_TOTEM_BAUBLE_NAME),
                "SSS",
                "CPC",
                " C ",
                'C', Blocks.CLAY,
                'P', Items.BLAZE_POWDER,
                'S', Items.STRING)));
        pages.add(new PageText(util.getLocalizedGuideText("wolf_bauble_page_2")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(null, ModBlocks.INCENSE_KINDLING_BOX, "WWW",
                "WIW",
                "WFW",
                'W', Blocks.PLANKS,
                'I', Blocks.TALLGRASS,
                'F', Items.FLINT)));
        pages.add(new PageText(util.getLocalizedGuideText("wolf_bauble_page_3")));
        entries.put(new ResourceLocation(Reference.MOD_ID, "wolf_totem_bauble"), new EntryItemStack(pages, util.getLocalizedGuideText("wolf_bauble_entry_name"), new ItemStack(ModItems.ITEMS.get(ModItems.WOLF_TOTEM_BAUBLE_NAME))));
        categories.add(new CategoryItemStack(entries, util.getLocalizedGuideText("baubles_category_name"), new ItemStack(ModItems.ITEMS.get(ModItems.WOLF_TOTEM_BAUBLE_NAME))));

        // Setup the book's base information
        myGuide = new Book();
        myGuide.setTitle("mobtotems.GuideBook.title");
        myGuide.setDisplayName(util.getLocalizedGuideText("name"));
        myGuide.setWelcomeMessage(util.getLocalizedGuideText("welcome"));
        myGuide.setAuthor("CorwinJV");
        myGuide.setColor(Color.DARK_GRAY);
        myGuide.setCategoryList(categories);
        myGuide.setRegistryName(new ResourceLocation(Reference.MOD_ID, "mobtotems_guide"));
        return myGuide;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleModel(ItemStack bookStack) {
        // Use the default GuideAPI model
        GuideAPI.setModel(myGuide);
    }

    @Override
    public void handlePost(ItemStack bookStack) {
        // Register a recipe so player's can obtain the book
        GameRegistry.addShapelessRecipe(bookStack.getItem().getRegistryName(), null, bookStack, Ingredient.fromItem(Items.BOOK), Ingredient.fromItem(Items.BONE));
    }

}
