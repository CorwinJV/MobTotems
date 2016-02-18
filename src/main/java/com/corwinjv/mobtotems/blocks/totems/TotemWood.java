package com.corwinjv.mobtotems.blocks.totems;

import com.corwinjv.mobtotems.blocks.BaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Created by CorwinJV on 2/17/2016.
 */
public class TotemWood extends BaseBlock
{
    public TotemWood()
    {
        super(Material.wood);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        FMLLog.log(Level.INFO, "TotemWood added to world!");
    }

}
