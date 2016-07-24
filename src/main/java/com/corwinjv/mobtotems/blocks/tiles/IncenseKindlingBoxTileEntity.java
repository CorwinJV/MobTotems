package com.corwinjv.mobtotems.blocks.tiles;

import baubles.api.BaublesApi;
import com.corwinjv.mobtotems.interfaces.ICharged;
import com.corwinjv.mobtotems.items.baubles.BaubleItem;
import com.google.common.base.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public class IncenseKindlingBoxTileEntity extends ModTileEntity
{
    private static final String CREATION_TIME = "CREATION_TIME";

    private static final long UPDATE_TICKS = 20;
    public static final double TMP_MANA_GAIN_DIST = 8;
    public static final int CHARGE_GAIN_PER_TICK = 2;
    private static final long TTL = 200;


    private long creationTime = 0;

    public IncenseKindlingBoxTileEntity()
    {
        super();
    }

    public void setCreationTime(long time)
    {
        creationTime = time;
        markForUpdate();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setLong(CREATION_TIME, creationTime);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        long time = 0;
        if(compound.hasKey(CREATION_TIME))
        {
            time = compound.getLong(CREATION_TIME);
        }
        creationTime = time;
    }

    @Override
    public void update()
    {
        if(!getWorld().isRemote)
        {
            performTTLUpdate();

            long worldTime = getWorld().getWorldInfo().getWorldTime();
            if(worldTime % UPDATE_TICKS == 0)
            {
                performChargeAura();
            }
        }
        else
        {
            spawnParticleEffects();
        }
    }

    private void performTTLUpdate()
    {
        if((getWorld().getTotalWorldTime() - creationTime) > TTL)
        {
            getWorld().destroyBlock(pos, false);
        }
    }

    private void performChargeAura()
    {
        final BlockPos targetPos = getPos();
        Predicate<EntityPlayer> playerWithinRangePredicate = new Predicate<EntityPlayer>()
        {
            @Override
            public boolean apply(@Nullable EntityPlayer input)
            {
                return input != null && input.getPosition().getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < TMP_MANA_GAIN_DIST;
            }
        };
        List<EntityPlayer> playersWithinRange = getWorld().getEntities(EntityPlayer.class, playerWithinRangePredicate);

        for(EntityPlayer player : playersWithinRange)
        {
            IInventory inventory = BaublesApi.getBaubles(player);
            for(int i = 0; i < inventory.getSizeInventory(); i++)
            {
                final ItemStack baubleStack = inventory.getStackInSlot(i);
                if(baubleStack != null
                        && baubleStack.getItem() instanceof ICharged)
                {
                    ((ICharged) baubleStack.getItem()).incrementChargeLevel(baubleStack, CHARGE_GAIN_PER_TICK);
                }
            }
        }
    }

    private void spawnParticleEffects()
    {

    }
}
