package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.corwinjv.mobtotems.TotemHelper.DEFAULT_RADIUS;
import static com.corwinjv.mobtotems.TotemHelper.RANGE_BOOST;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class SpiderLogic extends TotemLogic {
    public static final int POISON_DURATION = 300;
    public static final int POTION_AMPLIFIER = 1;

    @Override
    public List<ItemStack> getCost() {
        List<ItemStack> cost = new ArrayList<>();
        cost.add(new ItemStack(Items.STRING, 2));
        return cost;
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }

    @Override
    public void performEffect(World world, BlockPos pos, Modifiers modifiers) {
        int radius = DEFAULT_RADIUS + (int) (RANGE_BOOST * modifiers.range);
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        List<EntityLivingBase> teList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

        for (EntityLivingBase entity : teList) {
            if (entity instanceof IMob) {
                if (entity.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ()) <= radius) {
                    PotionEffect potionEffect = new PotionEffect(MobEffects.POISON, POISON_DURATION, POTION_AMPLIFIER);
                    if (entity.isPotionApplicable(potionEffect)) {
                        entity.attackEntityFrom(DamageSource.MAGIC, LlamaLogic.DAMAGE_MODIFIER * modifiers.damage);
                        entity.addPotionEffect(potionEffect);
                    }
                }
            }
        }
    }
}
