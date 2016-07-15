package com.corwinjv.mobtotems.blocks.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by CorwinJV on 7/14/2016.
 */
public class SacredLightTileEntity extends ModTileEntity
{
    private static double BASE_SACRED_LIGHT_RADIUS = 32.0;
    private static double SACRED_LIGHT_RADIUS_MODIFIER = 16.0;

    public SacredLightTileEntity() { }

    public boolean canSpawnMobHere(Entity mob)
    {
        double dist = mob.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
        if(mob instanceof EntityMob
                && dist < getSacredLightRadius())
        {
            return false;
        }
        return true;
    }

    private double getSacredLightRadius()
    {
        double radius = BASE_SACRED_LIGHT_RADIUS;

        int totemTileCount = 0;
        for(int i = 1; i < 3; i++)
        {
            BlockPos checkPos = new BlockPos(pos.getX(), pos.getY() - i, pos.getZ());
            TileEntity checkTileEntity = this.getWorld().getTileEntity(checkPos);
            if(checkTileEntity != null
                    && checkTileEntity instanceof TotemTileEntity)
            {
                radius += SACRED_LIGHT_RADIUS_MODIFIER;
            }
        }

        return radius;
    }
}
