package com.corwinjv.mobtotems.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Created by CorwinJV on 2/14/2016.
 */
public class EntitySpiritWolf extends WolfEntity {
    private boolean initialized = false;

    public EntitySpiritWolf(EntityType<? extends WolfEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void tame(PlayerEntity player) {
        setTamed(true);
        getNavigator().clearPath();
        setAttackTarget(null);
        getAISit().setSitting(false);
        setHealth(20.0F);
        setOwnerId(player.getUniqueID());
        world.setEntityState(this, (byte) 7);
        setInitialized(true);
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if (initialized && getOwner() == null) {
             setDead();
        }

        if (getEntityWorld().isRemote) {
//            spawnParticles();
        }
    }

    public void setDead() {
        onKillCommand();
    }

    // TODO Port
//    @SuppressWarnings("unchecked")
//    @Override
//    protected void initEntityAI() {
//        this.aiSit = new EntityAISit(this);
//        this.tasks.addTask(1, new EntityAISwimming(this));
//        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
//        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
//        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
//        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
//        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
//        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
//        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
//        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityMob.class, false));
//    }
//
//    @Override
//    protected void applyEntityAttributes() {
//        super.applyEntityAttributes();
//        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
//    }
//
//    @SideOnly(Side.CLIENT)
//    private void spawnParticles() {
//        IParticleFactory particleFactory = new ModParticles.Factory();
//
//        long worldTime = getEntityWorld().getTotalWorldTime();
//        if (worldTime % 8 == 0) {
//            double initialYSpeed = 0.05D;
//
//            Vec3d forwardVec = getForward();
//            Vec3d speedVec = new Vec3d(0, -forwardVec.y * 0.10 + initialYSpeed, 0);
//            float yPos = (float) this.getEntityBoundingBox().minY;
//
//            for (int j = 0; j < 2; ++j) {
//                float xPos = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
//                float zPos = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
//                Particle particle = particleFactory.createParticle(ModParticles.WOLF_IDLE_SMOKE, getEntityWorld(), this.posX + (double) xPos, (double) (yPos + 0.1F), this.posZ + (double) zPos, speedVec.x, speedVec.y, speedVec.z);
//                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
//            }
//        }
//    }
}
