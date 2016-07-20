package com.corwinjv.mobtotems.entities;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;

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

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if(initialized && getOwner() == null)
        {
            setDead();
        }
    }

}
