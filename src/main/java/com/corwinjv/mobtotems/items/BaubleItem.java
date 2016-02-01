package com.corwinjv.mobtotems.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Optional;
import org.apache.logging.log4j.Level;

/**
 * Created by CorwinJV on 1/31/2016.
 */
@Optional.Interface(modid = "Baubles", iface = "baubles.api.IBauble")
public class BaubleItem extends BaseItem implements IBauble
{
    public BaubleItem()
    {
        super();
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return null;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
//        FMLLog.log(Level.INFO, "canEquip() - itemStack: " + itemstack.getItem() + "player: " + player);
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
//        FMLLog.log(Level.INFO, "canUnequip() - itemStack: " + itemstack.getItem() + "player: " + player);
        return true;
    }
}
