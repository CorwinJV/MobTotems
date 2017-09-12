package com.corwinjv.mobtotems.creativetab;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class CreativeTabMT {
    public static final CreativeTabs MT_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.TOTEMIC_FOCUS);
        }
    };
}
