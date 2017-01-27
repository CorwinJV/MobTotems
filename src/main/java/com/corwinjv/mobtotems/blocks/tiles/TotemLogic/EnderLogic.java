package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class EnderLogic extends TotemLogic {
    @Override
    public List<ItemStack> getCost() {
        List<ItemStack> cost = new ArrayList<>();
        cost.add(new ItemStack(Blocks.GRASS, 1, 0));
        return cost;
    }

    @Nonnull
    @Override
    public EffectType getEffectType() {
        return EffectType.EFFECT;
    }

    public static class EnderTotemEnderTeleportEvent
    {
        private static final int DEFAULT_RADIUS = 32;
        private static final int RANGE_BOOST = 16;

        @SubscribeEvent
        public void onEnderTeleportEvent(EnderTeleportEvent e)
        {
            World world = e.getEntityLiving().getEntityWorld();
            if(!world.isRemote)
            {
                List<TileEntity> loadedTileEntityList = new ArrayList<>(world.loadedTileEntityList);

                for (TileEntity tileEntity : loadedTileEntityList)
                {
                    if(tileEntity instanceof OfferingBoxTileEntity)
                    {
                        if(((OfferingBoxTileEntity)tileEntity).getChargeLevel() > 0)
                        {
                            if(TotemHelper.hasTotemType(world, (OfferingBoxTileEntity)tileEntity, TotemType.ENDER))
                            {
                                Modifiers mods = TotemHelper.getModifiers(world, (OfferingBoxTileEntity)tileEntity);
                                int radius = DEFAULT_RADIUS + (int)(RANGE_BOOST * mods.range);
                                if(!isMobAffected(tileEntity.getPos(), e.getEntity(), radius))
                                {
                                    BlockPos validPos = getValidTeleportPosAround(world, tileEntity.getPos());
                                    if(validPos != null) {
                                        //FMLLog.log(Level.ERROR, "Redirected endermen with radius: " + radius + " to target: " + validPos);
                                        e.setTargetX(validPos.getX());
                                        e.setTargetY(validPos.getY());
                                        e.setTargetZ(validPos.getZ());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean isMobAffected(BlockPos pos, Entity mob, int radius) {
            double dist = mob.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
            return !(mob instanceof EntityEnderman && dist < radius);
        }

        private BlockPos getValidTeleportPosAround(World world, BlockPos pos) {
            BlockPos validPos = null;
            List<BlockPos> validPositions = new ArrayList<>();
            for(int x = -2; x <= 2; x++) {
                for(int z = -2; z <= 2; z++) {
                    BlockPos posToCheck = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    if(world.getBlockState(posToCheck).getBlock().equals(Blocks.AIR)) {
                        validPositions.add(posToCheck);
                    }
                }
            }
            int idx = (int)(Math.random() * (validPositions.size() - 1));
            validPos = validPositions.get(idx);

            return validPos;
        }
    }
}
