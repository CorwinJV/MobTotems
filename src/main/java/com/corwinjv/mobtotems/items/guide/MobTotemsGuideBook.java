package com.corwinjv.mobtotems.items.guide;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.TextHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
import amerifrance.guideapi.page.PageTextImage;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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

@GuideBook
public class MobTotemsGuideBook implements IGuideBook {

    public static Book myGuide;

    @Nonnull
    @Override
    public Book buildBook() {
        // Create the map of entries. A LinkedHashMap is used to retain the order of entries.
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();

        // Creation of our first entry.
        List<IPage> pages = new ArrayList<IPage>();
        pages.add(new PageText(getLocalizedGuideText("totemic_focus_page_1")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.getItem(ModItems.TOTEMIC_FOCUS), "GXF",
                " X ",
                " X ",
                'X', Items.STICK,
                'G', Items.GUNPOWDER,
                'F', Items.FEATHER)));
        entries.put(new ResourceLocation(Reference.MOD_ID, "totemic_focus"), new EntryItemStack(pages, getLocalizedGuideText("totemic_focus_entry_name"), new ItemStack(ModItems.getItem(ModItems.TOTEMIC_FOCUS))));

        pages = new ArrayList<>();
        pages.add(new PageText(getLocalizedGuideText("sacred_light_page_1")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(ModBlocks.getBlock(ModBlocks.SACRED_LIGHT),
                "GRG",
                "XOX",
                "GTG",
                'G', Items.GUNPOWDER,
                'R', Items.BLAZE_ROD,
                'X', Items.ROTTEN_FLESH,
                'O', Blocks.TORCH,
                'T', ModBlocks.getBlock(ModBlocks.TOTEM_WOOD))));
        pages.add(new PageTextImage(getLocalizedGuideText("sacred_light_example"), getGuideResourceLocation("finished_totem_example.png"), false));
        entries.put(new ResourceLocation(Reference.MOD_ID, "sacred_light"), new EntryItemStack(pages, getLocalizedGuideText("sacred_light_entry_name"), new ItemStack(ModBlocks.getBlock(ModBlocks.SACRED_LIGHT))));

        // Setup the list of categories and add our entries to it.
        List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
        categories.add(new CategoryItemStack(entries, getLocalizedGuideText("totems_category_name"), new ItemStack(ModBlocks.getBlock(ModBlocks.SACRED_LIGHT))));


        entries = new LinkedHashMap<>();

        pages = new ArrayList<IPage>();
        pages.add(new PageText(getLocalizedGuideText("wolf_bauble_page_1")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.getItem(ModItems.WOLF_TOTEM_BAUBLE),
                "SSS",
                "CPC",
                " C ",
                'C', Blocks.CLAY,
                'P', Items.BLAZE_POWDER,
                'S', Items.STRING)));
        pages.add(new PageText(getLocalizedGuideText("wolf_bauble_page_2")));
        pages.add(new PageIRecipe(new ShapedOreRecipe(ModBlocks.getBlock(ModBlocks.INCENSE_KINDLING_BOX), "WWW",
                "WIW",
                "WFW",
                'W', Blocks.PLANKS,
                'I', Blocks.TALLGRASS,
                'F', Items.FLINT)));
        pages.add(new PageText(getLocalizedGuideText("wolf_bauble_page_3")));
        entries.put(new ResourceLocation(Reference.MOD_ID, "wolf_totem_bauble"), new EntryItemStack(pages, getLocalizedGuideText("wolf_bauble_entry_name"), new ItemStack(ModItems.getItem(ModItems.WOLF_TOTEM_BAUBLE))));
        categories.add(new CategoryItemStack(entries, getLocalizedGuideText("baubles_category_name"), new ItemStack(ModItems.getItem(ModItems.WOLF_TOTEM_BAUBLE))));

        // Setup the book's base information
        myGuide = new Book();
        myGuide.setTitle("mobtotems.GuideBook.title");
        myGuide.setDisplayName(getLocalizedGuideText("name"));
        myGuide.setWelcomeMessage(getLocalizedGuideText("welcome"));
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
        GameRegistry.addShapelessRecipe(bookStack, Items.BOOK, Items.BONE);
    }

    public static ResourceLocation getGuideResourceLocation(String fileName)
    {
        return new ResourceLocation(Reference.MOD_ID, "textures/guide/" + fileName);
    }

    public static String getLocalizedGuideText(String text)
    {
        return TextHelper.localizeEffect(Reference.MOD_ID + ".text.guide." + text);
    }

    public static String getUnlocalizedGuideText(String text)
    {
        return Reference.MOD_ID + ".text.guide." + text;
    }
}
