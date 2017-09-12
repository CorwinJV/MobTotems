package com.corwinjv.mobtotems.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 8/3/2016.
 */
public class ModParticles {
    public static final int WOLF_IDLE_SMOKE = 0;
    public static final int WOLF_ENTRANCE_SMOKE = 1;
    public static final int WOLF_EXIT_SMOKE = 2;

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Nullable
        public Particle createParticle(int particleID, @Nonnull World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            Particle ret = null;

            switch (particleID) {
                case WOLF_IDLE_SMOKE: {
                    ret = new WolfIdleParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
                    break;
                }
                default: {
                    FMLLog.log(Level.ERROR, "Unknown particleId: " + particleID);
                    break;
                }
            }

            return ret;
        }
    }
}
