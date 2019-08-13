package com.corwinjv.mobtotems.entities.render;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by CorwinJV on 8/2/2016.
 */
public class SpiritWolfRender extends WolfRenderer {
    private static final ResourceLocation SPIRIT_WOLF_TEXTURES = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/entity/entity_spirit_wolf.png");

    public SpiritWolfRender(EntityRendererManager renderManagerIn, Model modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
        for (int i = layerRenderers.size() - 1; i >= 0; i--) {
            if (this.layerRenderers.get(i) instanceof WolfCollarLayer) {
                this.layerRenderers.remove(i);
            }
        }
    }

    protected ResourceLocation getEntityTexture(WolfEntity entity) {
        return SPIRIT_WOLF_TEXTURES;
    }
}
