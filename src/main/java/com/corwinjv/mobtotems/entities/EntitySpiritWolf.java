package com.corwinjv.mobtotems.entities;

import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
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

    public void tame(EntityPlayer player)
    {
        setTamed(true);
        getNavigator().clearPathEntity();
        setAttackTarget(null);
        getAISit().setSitting(false);
        setHealth(20.0F);
        setOwnerId(player.getUniqueID());
        world.setEntityState(this, (byte)7);
        setInitialized(true);
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
        Minecraft minecraft = MobTotems.component().minecraft();

        long worldTime = getEntityWorld().getTotalWorldTime();
        if(worldTime % 8 == 0)
        {
            double initialYSpeed = 0.05D;

            Vec3d forwardVec = getForward();
            Vec3d speedVec = new Vec3d(0, -forwardVec.yCoord * 0.10 + initialYSpeed, 0);
            float yPos = (float)this.getEntityBoundingBox().minY;

            for (int j = 0; j < 2; ++j)
            {
                float xPos = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                float zPos = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                Particle particle = particleFactory.createParticle(ModParticles.WOLF_IDLE_SMOKE, getEntityWorld(), this.posX + (double)xPos, (double)(yPos + 0.1F), this.posZ + (double)zPos, speedVec.xCoord, speedVec.yCoord, speedVec.zCoord);
                minecraft.effectRenderer.addEffect(particle);
            }
        }
    }
}
