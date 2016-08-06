package com.corwinjv.mobtotems.particles;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by CorwinJV on 8/3/2016.
 */
public class WolfIdleParticle extends Particle
{
    public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/items/totemic_focus.png");

    static Queue<WolfIdleParticle> queuedRenders = new LinkedList<WolfIdleParticle>();

    private Entity entity = null;
    private float partialTicks;
    private float rotationX;
    private float rotationZ;
    private float rotationYZ;
    private float rotationXZ;
    private float rotationXY;

    protected WolfIdleParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);

        this.motionY = 0.025f;
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        this.entity = entityIn;
        this.partialTicks = partialTicks;
        this.rotationX = rotationX;
        this.rotationZ = rotationZ;
        this.rotationYZ = rotationYZ;
        this.rotationXZ = rotationXZ;
        this.rotationXY = rotationXY;

        queuedRenders.add(this);
    }

    public int getFXLayer()
    {
        return 1;
    }


    static void RenderQueuedRenders(Tessellator tessellator)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        Minecraft.getMinecraft().renderEngine.bindTexture(PARTICLE_TEXTURES);

        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        for(WolfIdleParticle particle : queuedRenders)
        {
            particle.renderQueued(tessellator);
        }
        tessellator.draw();

        queuedRenders.clear();
    }

    public void renderQueued(Tessellator tessellator)
    {
        float minU = 0.0f;
        float maxU = 1.0f;
        float minV = 0.0f;
        float maxV = 1.0f;
        float scale = 0.1f * this.particleScale;

        Vec3d[] avec3d = new Vec3d[]
                {
                    new Vec3d((double)(-rotationX * scale - rotationXY * scale), (double)(-rotationZ * scale), (double)(-rotationYZ * scale - rotationXZ * scale)),
                    new Vec3d((double)(-rotationX * scale + rotationXY * scale), (double)(rotationZ * scale), (double)(-rotationYZ * scale + rotationXZ * scale)),
                    new Vec3d((double)(rotationX * scale + rotationXY * scale), (double)(rotationZ * scale), (double)(rotationYZ * scale + rotationXZ * scale)),
                    new Vec3d((double)(rotationX * scale - rotationXY * scale), (double)(-rotationZ * scale), (double)(rotationYZ * scale - rotationXZ * scale))
                };

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);

        VertexBuffer buffer = tessellator.getBuffer();
        buffer.pos((double)f5 + avec3d[0].xCoord, (double)f6 + avec3d[0].yCoord, (double)f7 + avec3d[0].zCoord).tex(maxU, maxV).color(1.0f, 1.0f, 1.0f, 0.5f).endVertex();
        buffer.pos((double)f5 + avec3d[1].xCoord, (double)f6 + avec3d[1].yCoord, (double)f7 + avec3d[1].zCoord).tex(maxU, minV).color(1.0f, 1.0f, 1.0f, 0.5f).endVertex();
        buffer.pos((double)f5 + avec3d[2].xCoord, (double)f6 + avec3d[2].yCoord, (double)f7 + avec3d[2].zCoord).tex(minU, minV).color(1.0f, 1.0f, 1.0f, 0.5f).endVertex();
        buffer.pos((double)f5 + avec3d[3].xCoord, (double)f6 + avec3d[3].yCoord, (double)f7 + avec3d[3].zCoord).tex(minU, maxV).color(1.0f, 1.0f, 1.0f, 0.5f).endVertex();
    }
}
