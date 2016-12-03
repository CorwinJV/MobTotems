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
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by CorwinJV on 8/3/2016.
 */
public class WolfIdleParticle extends Particle
{
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/particles/spirit_wolf_smoke.png");

    private static Queue<WolfIdleParticle> queuedRenders = new LinkedList<WolfIdleParticle>();

    private float partialTicks;
    private float rotationX;
    private float rotationZ;
    private float rotationYZ;
    private float rotationXZ;
    private float rotationXY;

    private static final int numFrames = 2;

    public WolfIdleParticle(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        super(worldIn, posXIn, posYIn, posZIn);
        this.particleGravity = 0.06F;
        this.particleMaxAge = (int)(8.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
    }

    public WolfIdleParticle(World worldIn, double posXIn, double posYIn, double posZIn, double speedXIn, double speedYIn, double speedZIn)
    {
        this(worldIn, posXIn, posYIn, posZIn);
        this.motionX = speedXIn;
        this.motionY = speedYIn;
        this.motionZ = speedZIn;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        float cappedParticleAge = (particleAge > particleMaxAge) ? particleMaxAge : particleAge;
        float particleTime = cappedParticleAge / particleMaxAge;
        int curFrame = (int)Math.floor(particleTime * numFrames);

        this.setParticleTextureIndex(curFrame);
    }

    @Override
    public void setParticleTextureIndex(int particleTextureIndex)
    {
        particleTextureIndexX = particleTextureIndex;
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
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
        float numFrames = 2;
        float width = 16;
        //int height = 16;

        float minU = (width * (float)particleTextureIndexX) / (numFrames * width);
        float maxU = minU + (width / (numFrames * width));
        float minV = 0.0f;
        float maxV = 1.0f;
        float scale = 0.1f * this.particleScale;

        //FMLLog.log(Level.ERROR, "renderQueued() - indexX: " + particleTextureIndexX + "minU: " + minU + "maxU: " + maxU);

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
