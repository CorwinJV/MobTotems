package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class CowLogic extends TotemLogic {
    @Override
    public List<ItemStack> getCost() {
        List<ItemStack> cost = new ArrayList<>();
        cost.add(new ItemStack(Blocks.TALL_GRASS, 4, null));
        return cost;
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }

    public static class CowTotemEntityJoinWorldEvent {
        @SubscribeEvent
        public void onEntityJoinWorldEvent(EntityJoinWorldEvent e) {
            if (!e.getWorld().isRemote) {
                List<TileEntity> loadedTileEntityList = e.getWorld().loadedTileEntityList;

                for (int i = loadedTileEntityList.size() - 1; i >= 0; i--) {
                    TileEntity tileEntity = loadedTileEntityList.get(i);
                    if (tileEntity instanceof OfferingBoxTileEntity) {
                        if (((OfferingBoxTileEntity) tileEntity).getChargeLevel() > 0) {
                            if (TotemHelper.hasTotemType((OfferingBoxTileEntity) tileEntity, TotemType.COW)) {
                                Modifiers mods = TotemHelper.getModifiers(e.getWorld(), (OfferingBoxTileEntity) tileEntity);
                                int radius = TotemHelper.DEFAULT_RADIUS + (int) (TotemHelper.RANGE_BOOST * mods.range);

                                if (!canSpawnMobHere(tileEntity.getPos(), e.getEntity(), radius)) {
                                    //FMLLog.log(Level.ERROR, "Stopped mob from spawning with radius: " + radius);
                                    e.setCanceled(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean canSpawnMobHere(BlockPos pos, Entity mob, int radius) {
            double dist = mob.getPosition().distanceSq(pos);

            return !(mob instanceof AnimalEntity && dist < radius);
        }
    }
}
