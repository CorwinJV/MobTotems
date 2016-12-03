package com.corwinjv.mobtotems.entities;

import com.corwinjv.mobtotems.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.Random;

/**
 * Created by CorwinJV on 2/14/2016.
 */
public class EntitySpiritWolf extends EntityWolf
{
    private boolean initialized = false;

    public EntitySpiritWolf(World worldIn)
    {
        super(worldIn);
    }

    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if(initialized && getOwner() == null)
        {
            setDead();
        }

        if(getEntityWorld().isRemote)
        {
            spawnParticles();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initEntityAI()
    {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityMob.class, false));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles()
    {
        IParticleFactory particleFactory = new ModParticles.Factory();

        long worldTime = getEntityWorld().getTotalWorldTime();
        if(worldTime % 8 == 0)
        {
            double initialYSpeed = 0.05D;

            Vec3d lookVec = getLook(1.0f);
            Vec3d posVec = new Vec3d(posX - (lookVec.xCoord * 0.25), posY -(lookVec.yCoord * 0.25), posZ - (lookVec.zCoord * 0.25));
            Vec3d speedVec = new Vec3d(-lookVec.xCoord * 0.10, -lookVec.yCoord * 0.10 + initialYSpeed, -lookVec.zCoord * 0.10);

            Particle particle = particleFactory.getEntityFX(ModParticles.WOLF_IDLE_SMOKE, getEntityWorld(), posVec.xCoord, posVec.yCoord, posVec.zCoord, speedVec.xCoord, speedVec.yCoord, speedVec.zCoord);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }
}
