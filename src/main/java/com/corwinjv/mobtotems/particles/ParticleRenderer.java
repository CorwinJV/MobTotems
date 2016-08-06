package com.corwinjv.mobtotems.particles;

import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by CorwinJV on 8/6/2016.
 */
public class ParticleRenderer
{
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WolfIdleParticle.RenderQueuedRenders(tessellator);
    }
}
