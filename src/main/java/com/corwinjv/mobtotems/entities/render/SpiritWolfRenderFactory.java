package com.corwinjv.mobtotems.entities.render;

import com.corwinjv.mobtotems.entities.EntitySpiritWolf;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Created by CorwinJV on 8/2/2016.
 */
public class SpiritWolfRenderFactory implements IRenderFactory<EntitySpiritWolf> {
    @Override
    public EntityRenderer<? super EntitySpiritWolf> createRenderFor(EntityRendererManager manager) {
        return new SpiritWolfRender(manager, new WolfModel<>(), 0.5F);
    }
}
