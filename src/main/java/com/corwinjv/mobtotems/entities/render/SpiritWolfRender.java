package com.corwinjv.mobtotems.entities.render;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

/**
 * Created by CorwinJV on 8/2/2016.
 */
public class SpiritWolfRender extends RenderWolf
{
    private static final ResourceLocation SPIRIT_WOLF_TEXTURES = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/entity/entity_spirit_wolf.png");

    public SpiritWolfRender(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
        for(int i = layerRenderers.size() - 1; i >= 0; i--)
        {
            if(this.layerRenderers.get(i) instanceof LayerWolfCollar)
            {
                this.layerRenderers.remove(i);
            }
        }
    }

    protected ResourceLocation getEntityTexture(EntityWolf entity)
    {
        return SPIRIT_WOLF_TEXTURES;
    }
}
