package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.creativetab.CreativeTabMT;
import net.minecraft.item.Item;

/**
 * Created by CorwinJV on 1/31/2016.
 */
public class ModItem extends Item {
    public ModItem() {
        super();
        this.init();
    }

    public void init() {
        this.setCreativeTab(CreativeTabMT.MT_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Reference.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
