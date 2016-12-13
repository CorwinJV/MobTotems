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
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
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
public class ExampleBook implements IGuideBook {

    public static Book myGuide;

    @Nonnull
    @Override
    public Book buildBook() {
        // Create the map of entries. A LinkedHashMap is used to retain the order of entries.
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();

        // Creation of our first entry.
        List<IPage> pages1 = new ArrayList<IPage>();
        pages1.add(new PageText("This is a page in my guide!"));
        entries.put(new ResourceLocation("example", "entry_one"), new EntryItemStack(pages1, "Entry One", new ItemStack(Blocks.BEACON)));

        // Creation of our second entry.
        List<IPage> pages2 = new ArrayList<IPage>();
        pages2.add(new PageIRecipe(new ShapedOreRecipe(Items.DIAMOND_SWORD, "D", "D", "S", 'D', Items.DIAMOND, 'S', Items.STICK)));
        pages2.add(new PageFurnaceRecipe("oreGold"));
        entries.put(new ResourceLocation("example", "entry_two"), new EntryItemStack(pages2, "Entry Two", new ItemStack(Items.DIAMOND_SWORD)));

        // Setup the list of categories and add our entries to it.
        List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
        categories.add(new CategoryItemStack(entries, "My Category", new ItemStack(Blocks.COMMAND_BLOCK)));

        // Setup the book's base information
        myGuide = new Book();
        myGuide.setTitle("My Guide");
        myGuide.setDisplayName("My Guide");
        myGuide.setAuthor("ExampleDude");
        myGuide.setColor(Color.CYAN);
        myGuide.setCategoryList(categories);
        myGuide.setRegistryName(new ResourceLocation("mymod", "first_guide"));
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
        GameRegistry.addShapelessRecipe(bookStack, Items.BOOK, Items.PAPER);
    }
}
