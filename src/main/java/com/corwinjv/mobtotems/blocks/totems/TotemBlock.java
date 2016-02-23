package com.corwinjv.mobtotems.blocks.totems;

import com.corwinjv.mobtotems.blocks.BaseBlock;
import net.minecraft.block.material.Material;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemBlock extends BaseBlock
{
    public TotemBlock(Material aMaterial) {
        super(aMaterial);
        this.init();
    }

    public TotemBlock()
    {
        super(Material.wood);
        this.init();
    }
}
