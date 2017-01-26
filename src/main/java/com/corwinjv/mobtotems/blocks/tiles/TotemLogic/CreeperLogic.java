package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class CreeperLogic extends TotemLogic {
    @Override
    public List<ItemStack> getCost() {
        {
            List<ItemStack> cost = new ArrayList<>();
            cost.add(new ItemStack(Items.GUNPOWDER, 4, 0));
            return cost;
        }
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }

    // This block limits what entities can spawn near it, we subscribe to the EntityJoinWorldEvent in order to stop entity spawning
    // based on proximity to TileEntities of our block's type
    public static class CreeperTotemEntityJoinWorldEvent
    {
        private static final int DEFAULT_RADIUS = 32;
        private static final int RANGE_BOOST = 16;

        @SubscribeEvent
        public void onEntityJoinWorldEvent(EntityJoinWorldEvent e)
        {
            if(!e.getWorld().isRemote)
            {
                List<TileEntity> loadedTileEntityList = e.getWorld().loadedTileEntityList;

                for (TileEntity tileEntity : loadedTileEntityList)
                {
                    if(tileEntity instanceof OfferingBoxTileEntity)
                    {
                        if(((OfferingBoxTileEntity)tileEntity).getChargeLevel() > 0)
                        {
                            if(TotemHelper.hasTotemType(e.getWorld(), (OfferingBoxTileEntity)tileEntity, TotemType.CREEPER))
                            {
                                Modifiers mods = TotemHelper.getModifiers(e.getWorld(), (OfferingBoxTileEntity)tileEntity);
                                int radius = DEFAULT_RADIUS + (int)(RANGE_BOOST * mods.range);
                                if(!canSpawnMobHere(tileEntity.getPos(), e.getEntity(), radius))
                                {
                                    FMLLog.log(Level.ERROR, "Stopped mob from spawning with radius: " + radius);
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
            double dist = mob.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
            return !(mob instanceof IMob && dist < radius);
        }
    }
}
