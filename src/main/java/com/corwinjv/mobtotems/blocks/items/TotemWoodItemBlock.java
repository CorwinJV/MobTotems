package com.corwinjv.mobtotems.blocks.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * Created by CorwinJV on 1/18/2017.
 */
public class TotemWoodItemBlock extends BlockItem {
    public TotemWoodItemBlock(Block block, Item.Properties properties) {
        super(block, properties);
        properties.maxDamage(0);
        //his.setHasSubtypes(true);
    }

    // TODO: metadata is gone... port
//    @Override
//    public int getMetadata(int metadata) {
//        return metadata;
//    }
//    // create a unique unlocalised name for each totem, so that we can give each one a unique name
//    @Override
//    public String getUnlocalizedName(ItemStack stack) {
//        TotemType totemType = TotemType.fromMeta(stack.getMetadata());
//        return super.getUnlocalizedName() + "." + totemType.getName();
//    }
}
