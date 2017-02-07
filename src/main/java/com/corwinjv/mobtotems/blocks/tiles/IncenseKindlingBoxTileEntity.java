package com.corwinjv.mobtotems.blocks.tiles;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.corwinjv.mobtotems.blocks.tiles.base.ModTileEntity;
import com.corwinjv.mobtotems.interfaces.IChargeable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by CorwinJV on 7/24/2016.
 */
public class IncenseKindlingBoxTileEntity extends ModTileEntity implements ITickable
{
    private static final String TIME_LIVED = "TIME_LIVED";

    private static final long UPDATE_TICKS = 20;
    private static final long PARTICLE_UPDATE_TICKS = 40;
    public static final double TMP_MANA_GAIN_DIST = 8;
    public static final int CHARGE_GAIN_PER_TICK = 2;
    private static final long TTL = 200;

    private long timeLived = 0;

    public IncenseKindlingBoxTileEntity()
    {
        super();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setLong(TIME_LIVED, timeLived);
        return ret;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        long time = 0;
        if(compound.hasKey(TIME_LIVED))
        {
            time = compound.getLong(TIME_LIVED);
        }
        timeLived = time;
    }

    @Override
    public void update()
    {
        if(!getWorld().isRemote)
        {
            performTTLUpdate();

            long worldTime = getWorld().getWorldTime();
            if(worldTime % UPDATE_TICKS == 0)
            {
                performChargeAura();
            }
        }
        else
        {
            long worldTime = getWorld().getWorldTime();
            if(worldTime % PARTICLE_UPDATE_TICKS == 0)
            {
                spawnParticleEffects();
            }
        }
    }

    private void performTTLUpdate()
    {
        timeLived++;
        if(timeLived > TTL)
        {
            getWorld().destroyBlock(pos, false);
        }
    }

    private void performChargeAura()
    {
        final BlockPos targetPos = getPos();
        Predicate<EntityPlayer> playerWithinRangePredicate = input -> input != null
                && input.getPosition().getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < TMP_MANA_GAIN_DIST;
        List<EntityPlayer> playersWithinRange = getWorld().getEntities(EntityPlayer.class, playerWithinRangePredicate::test);

        for(EntityPlayer player : playersWithinRange)
        {
            IBaublesItemHandler baublesItemHandler = BaublesApi.getBaublesHandler(player);
            for(int i = 0; i < baublesItemHandler.getSlots(); i++)
            {
                final ItemStack baubleStack = baublesItemHandler.getStackInSlot(i);
                if(baubleStack != ItemStack.EMPTY
                        && baubleStack.getItem() instanceof IChargeable)
                {
                    ((IChargeable) baubleStack.getItem()).incrementChargeLevel(baubleStack, CHARGE_GAIN_PER_TICK);
                }
            }
        }
    }

    private void spawnParticleEffects()
    {
        double startX = pos.getX() - TMP_MANA_GAIN_DIST;
        double startY = pos.getY() - TMP_MANA_GAIN_DIST;
        double startZ = pos.getZ() - TMP_MANA_GAIN_DIST;

        for(double x = startX; x < pos.getX() + TMP_MANA_GAIN_DIST; x++)
        {
            for(double y = startY; y < pos.getY() + TMP_MANA_GAIN_DIST; y++)
            {
                for(double z = startZ; z < pos.getZ() + TMP_MANA_GAIN_DIST; z++)
                {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    Block block = getWorld().getBlockState(blockPos).getBlock();
                    Block blockAbove = getWorld().getBlockState(new BlockPos(x, y+1, z)).getBlock();

                    if(pos.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < TMP_MANA_GAIN_DIST
                            && block.isBlockSolid(getWorld(), blockPos, EnumFacing.UP)
                            && blockAbove instanceof BlockAir)
                    {
                        Random rand = getWorld().rand;
                        float width = 0.75f;
                        float height = 0.75f;

                        double motionX = rand.nextGaussian() * 0.02D;
                        double motionY = rand.nextGaussian() * 0.02D;
                        double motionZ = rand.nextGaussian() * 0.02D;
                        world.spawnParticle(
                                EnumParticleTypes.CLOUD,
                                x + rand.nextFloat() * width * 2.0F - width,
                                y + 1.0D + rand.nextFloat() * height,
                                z + rand.nextFloat() * width * 2.0F - width,
                                motionX,
                                motionY,
                                motionZ);

                        width = 1.0f;
                        height = 1.0f;

                        motionX = rand.nextGaussian() * 0.02D;
                        motionY = rand.nextGaussian() * 0.02D;
                        motionZ = rand.nextGaussian() * 0.02D;
                        world.spawnParticle(
                                EnumParticleTypes.VILLAGER_HAPPY,
                                x + rand.nextFloat() * width * 2.0F - width,
                                y + 0.5D + rand.nextFloat() * height,
                                z + rand.nextFloat() * width * 2.0F - width,
                                motionX,
                                motionY,
                                motionZ);
                    }
                }
            }
        }
    }
}
