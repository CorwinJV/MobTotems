package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
public class BlazeLogic extends TotemLogic {
    private static final int FIRE_DURATION = 1;

    @Override
    public List<ItemStack> getCost() {
        List<ItemStack> cost = new ArrayList<>();
        cost.add(new ItemStack(Items.COAL, 2, 0));
        return cost;
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }

    @Override
    public void performEffect(World world, BlockPos pos, Modifiers modifiers) {
        int radius = DEFAULT_RADIUS + (int)(RANGE_BOOST * modifiers.range);
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        List<Entity> teList = world.getEntitiesWithinAABB(Entity.class, aabb);

        for(Entity entity : teList) {
            if(entity instanceof IMob) {
                if(entity.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ()) <= radius) {
                    entity.setFire(FIRE_DURATION + (int)(FIRE_DURATION * modifiers.damage));
                }
            }
        }
    }
}
