package com.corwinjv.mobtotems.entities.render;

import com.corwinjv.mobtotems.entities.EntitySpiritWolf;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Created by CorwinJV on 8/2/2016.
 */
public class SpiritWolfRenderFactory implements IRenderFactory<EntitySpiritWolf>
{
    @Override
    public Render<? super EntitySpiritWolf> createRenderFor(RenderManager manager) {
        return new SpiritWolfRender(manager, new ModelWolf(), 0.5F);
    }
}

