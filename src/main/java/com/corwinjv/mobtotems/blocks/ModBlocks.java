package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by vanhc011 on 9/1/14.
 */

//TODO: Figure out why this crashes now... Wtf?
//@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks
{
    public static final BlockMT creeperTotem = new BlockCreeperTotem();
    public static final String CREEPER_TOTEM = "creeperTotem";


    public static void init()
    {
        GameRegistry.registerBlock(creeperTotem, ModBlocks.CREEPER_TOTEM);
    }
}