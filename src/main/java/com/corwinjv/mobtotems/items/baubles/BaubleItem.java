package com.corwinjv.mobtotems.items.baubles;

import com.corwinjv.mobtotems.interfaces.IChargeable;
import com.corwinjv.mobtotems.items.ModItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by CorwinJV on 1/31/2016.
 */
// TODO: Expecting an update to Baubles
//@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class BaubleItem extends ModItem implements /*IBauble,*/ IChargeable {
    protected static final String CHARGE_LEVEL = "CHARGE_LEVEL";

    public BaubleItem(Item.Properties props) {
        super(props);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        initNbtData(stack);
    }

    protected void initNbtData(ItemStack stack) {
        stack.getOrCreateTag().putInt(CHARGE_LEVEL, 0);
    }

    @SuppressWarnings("all")
    @Override
    public int getChargeLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt(CHARGE_LEVEL);
    }

    @Override
    public void setChargeLevel(ItemStack stack, int chargeLevel) {
        stack.getOrCreateTag().putInt(CHARGE_LEVEL, chargeLevel);
    }

    @Override
    public void decrementChargeLevel(ItemStack stack, int amount) {
        int chargeLevel = getChargeLevel(stack);
        chargeLevel -= amount;
        if (chargeLevel < 0) {
            chargeLevel = 0;
        }

        setChargeLevel(stack, chargeLevel);
    }

    @Override
    public void incrementChargeLevel(ItemStack stack, int amount) {
        int chargeLevel = getChargeLevel(stack);
        chargeLevel += amount;
        if (chargeLevel > getMaxChargeLevel()) {
            chargeLevel = getMaxChargeLevel();
        }
        setChargeLevel(stack, chargeLevel);
    }

    @Override
    public int getMaxChargeLevel() {
        return 16;
    }

    public void onBaubleActivated(ItemStack stack, PlayerEntity player) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    // TODO: Expecting an update to Baubles
//    @Override
//    public BaubleType getBaubleType(ItemStack itemstack) {
//        return BaubleType.TRINKET;
//    }
//
//    @Override
//    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
//        return true;
//    }
//
//    @Override
//    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
//        return true;
//    }
//
//    @Override
//    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
//        return true;
//    }
}
