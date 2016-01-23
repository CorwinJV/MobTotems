package com.corwinjv.mobtotems.creativetab;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class CreativeTabMT
{
    public static final CreativeTabs MT_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Items.bone;
        }
    };
}
