package com.corwinjv.mobtotems.blocks.items;

import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.TotemWoodBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 1/18/2017.
 */
public class TotemWoodItemBlock extends ItemBlock{
    public TotemWoodItemBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    // create a unique unlocalised name for each colour, so that we can give each one a unique name
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        TotemType totemType = TotemType.fromMeta(stack.getMetadata());
        return super.getUnlocalizedName() + "." + totemType.toString();
    }
}
