package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.creativetab.CreativeTabMT;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by vanhc011 on 9/1/14.
 */
public class BlockMT extends Block
{
    public BlockMT(Material aMaterial) {
        super(aMaterial);
    }

    public BlockMT()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabMT.MT_TAB);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

}
