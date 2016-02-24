package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.blocks.TotemWood;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * Created by CorwinJV on 2/24/2016.
 */
public class TotemStencil extends BaseItem
{
    public static final String NULL_STENCIL = "totem_stencil";
    public static final String CREEPER_STENCIL = "totem_stencil_creeper";
    public static final String RABBIT_STENCIL = "totem_stencil_rabbit";
    public static final String SLIME_STENCIL = "totem_stencil_slime";
    public static final String WOLF_STENCIL = "totem_stencil_wolf";

    public static final int NULL_STENCIL_META = 0;
    public static final int CREEPER_STENCIL_META = 1;
    public static final int RABBIT_STENCIL_META = 2;
    public static final int SLIME_STENCIL_META = 3;
    public static final int WOLF_STENCIL_META = 4;

    public TotemStencil()
    {
        super();
        this.init();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String metaName = null;
        switch(stack.getMetadata())
        {
            case CREEPER_STENCIL_META:
            {
                metaName = CREEPER_STENCIL;
                break;
            }
            case RABBIT_STENCIL_META:
            {
                metaName = RABBIT_STENCIL;
                break;
            }
            case SLIME_STENCIL_META:
            {
                metaName = SLIME_STENCIL;
                break;
            }
            case WOLF_STENCIL_META:
            {
                metaName = WOLF_STENCIL;
                break;
            }
            default:
            {
                metaName = NULL_STENCIL;
            }
        }

        //FMLLog.log(Level.WARN, "TotemStencil getUnlocalizedName() - " + super.getUnlocalizedName() + "." + metaName);
        return super.getUnlocalizedName() + "." + metaName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List subItems)
    {
        subItems.add(new ItemStack(item, 1, NULL_STENCIL_META));
        subItems.add(new ItemStack(item, 1, CREEPER_STENCIL_META));
        subItems.add(new ItemStack(item, 1, RABBIT_STENCIL_META));
        subItems.add(new ItemStack(item, 1, SLIME_STENCIL_META));
        subItems.add(new ItemStack(item, 1, WOLF_STENCIL_META));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote)
        {
            Block targetBlock = BlockUtils.getBlock(world, pos);
            if (targetBlock != null
                    && targetBlock instanceof TotemWood)
            {
                // TODO: Do something with TotemWood or TotemWoodTileEntity
            }
        }
        return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
    }
}
