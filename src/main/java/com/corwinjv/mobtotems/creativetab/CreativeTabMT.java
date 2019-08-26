package com.corwinjv.mobtotems.creativetab;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class CreativeTabMT extends ItemGroup {
    public static final CreativeTabMT INSTANCE = new CreativeTabMT();
    NonNullList<ItemStack> list;

    public CreativeTabMT() {
        super(Reference.MOD_ID);
    }
    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.TOTEMIC_FOCUS);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        this.list = items;

        add(ModItems.CARVING_KNIFE);
        add(ModItems.TOTEMIC_FOCUS);
        add(ModItems.WOLF_TOTEM_BAUBLE);
        add(ModBlocks.INCENSE_KINDLING_BOX);
        add(ModBlocks.OFFERING_BOX);
        add(ModBlocks.SACRED_LIGHT);
        add(ModBlocks.TOTEM_WOOD);

        // TODO: Add flattened totem wood variants
    }

    private void add(IItemProvider item) {
        item.asItem().fillItemGroup(this, list);
    }
}
