package com.corwinjv.mobtotems.items.baubles;

import com.corwinjv.mobtotems.entities.EntitySpiritWolf;
import com.corwinjv.mobtotems.entities.ModEntities;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;


/**
 * Created by CorwinJV on 1/31/2016.
 */
public class WolfTotemBauble extends BaubleItem {
    private static final String WOLF_ID = "WOLF_ID";
    private static final String WOLF_TOTEM_COMPOUND = "WOLF_TOTEM_COMPOUND";
    private static final int SPAWN_DISTANCE = 3;
    private static final long UPDATE_TICKS = 20;

    private static final int CHARGE_COST_PER_TICK = 1;

    public WolfTotemBauble(Item.Properties props) {
        super(props);
    }

    // TODO: Expecting an update to Baubles
//    @Override
//    public BaubleType getBaubleType(ItemStack itemStack) {
//        return BaubleType.AMULET;
//    }

    @Override
    public int getMaxChargeLevel() {
        return 32;
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
        super.onCreated(stack, world, player);
    }

    @Override
    protected void initNbtData(ItemStack stack) {
        super.initNbtData(stack);
        CompoundNBT tagCompound = stack.getTag();

        if (tagCompound == null) {
            tagCompound = new CompoundNBT();
        }

        CompoundNBT wolfTotemCompound = tagCompound.getCompound(WOLF_TOTEM_COMPOUND);
        wolfTotemCompound.putString(WOLF_ID, "");
        tagCompound.put(WOLF_TOTEM_COMPOUND, wolfTotemCompound);

        stack.setTag(tagCompound);
    }

    private boolean hasValidNbt(ItemStack stack) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound == null) {
//            FMLLog.log(Level.WARN, "WolfTotemBauble onBaubleActivated() no tagCompound");
            return false;
        }
        return true;
    }

    private void setWolfId(@Nonnull ItemStack stack, @Nonnull String wolfId) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound == null) {
            tagCompound = new CompoundNBT();
        }
        CompoundNBT wolfTotemCompound = tagCompound.getCompound(WOLF_TOTEM_COMPOUND);
        wolfTotemCompound.putString(WOLF_ID, wolfId);
    }

    @Nullable
    private EntitySpiritWolf getWolf(@Nonnull ItemStack stack, @Nonnull ServerWorld world) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null) {
            CompoundNBT wolfTotemCompound = tagCompound.getCompound(WOLF_TOTEM_COMPOUND);
            String wolfId = wolfTotemCompound.getString(WOLF_ID);
            if (!StringUtils.isNullOrEmpty(wolfId)) {
                final UUID wolfUUID = UUID.fromString(wolfId);
                Predicate<Entity> spiritWolfForUUID = input -> (input != null && input.getUniqueID().equals(wolfUUID));

                List<Entity> entities = world.getEntities(ModEntities.SPIRIT_WOLF, spiritWolfForUUID);
                if (entities.size() > 0
                        && entities.get(0) instanceof EntitySpiritWolf) {
                    return (EntitySpiritWolf)entities.get(0);
                }
            }
        }
        return null;
    }

    @Override
    public void onBaubleActivated(ItemStack stack, PlayerEntity player) {
        if (!player.world.isRemote) {
            if (!hasValidNbt(stack)) {
                return;
            }

            CompoundNBT wolfTotemCompound = stack.getOrCreateChildTag(WOLF_TOTEM_COMPOUND);


            EntitySpiritWolf spiritWolf = getWolf(stack, player.getServer().getWorld(player.world.dimension.getType()));
            if (spiritWolf == null
                    && stack.getOrCreateTag().getInt(CHARGE_LEVEL) > 0) {
                //FMLLog.log(Level.INFO, "spiritWolf is null and charge is greater than zero");
                Vec3i facingVec = player.getHorizontalFacing().getDirectionVec();
                double posX = player.posX + (facingVec.getX() * SPAWN_DISTANCE);
                double posY = player.posY;
                double posZ = player.posZ + (facingVec.getZ() * SPAWN_DISTANCE);

                // Check if spawn location is occupied by a solid block
                BlockPos blockPos = new BlockPos((int) posX, (int) posY, (int) posZ);
                if (!BlockUtils.isAreaSolid(player, blockPos)) {
                    spiritWolf = spawnSpiritWolf(player.world, posX, posY, posZ);
                    spiritWolf.tame(player);

                    wolfTotemCompound.putString(WOLF_ID, spiritWolf.getUniqueID().toString());
                }
            } else if (spiritWolf != null) {
                //FMLLog.log(Level.INFO, "spiritWolf ain't dead. Killing and saving now.");
                spiritWolf.setDead();
                wolfTotemCompound.putString(WOLF_ID, "");
            }
            stack.setTagInfo(WOLF_TOTEM_COMPOUND, wolfTotemCompound);
        }
    }

    // Spirit Wolf Spawning
    private EntitySpiritWolf spawnSpiritWolf(World worldIn, double x, double y, double z) {
          LivingEntity livingEntity = new EntitySpiritWolf(ModEntities.SPIRIT_WOLF, worldIn);

// TODO: Fix spawning code
//        livingEntity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
//        livingEntity.rotationYawHead = livingEntity.rotationYaw;
//        livingEntity.renderYawOffset = livingEntity.rotationYaw;
//        //livingEntity.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(livingEntity)), (ILivingEntityData) null);
//        worldIn.spawnEntity(livingEntity);
//        livingEntity.playLivingSound();

        return (EntitySpiritWolf) livingEntity;
    }

// TODO: Move onWornTick code to a generic we can call from here based on cfg
//    /**
//     * This method is called once per tick if the bauble is being worn by a player
//     */
//    public void onWornTick(ItemStack stack, EntityLivingBase player) {
//        super.onWornTick(stack, player);
//        World world = player.getEntityWorld();
//        if (!world.isRemote) {
//            long worldTime = world.getWorldInfo().getWorldTime();
//            EntitySpiritWolf spiritWolf = getWolf(stack, world);
//
//            if (worldTime % UPDATE_TICKS == 0) {
//                if (spiritWolf != null) {
//                    decrementChargeLevel(stack, CHARGE_COST_PER_TICK);
//                }
//            }
//
//            // Should wolf still be summoned? This should happen every tick
//            if (spiritWolf != null
//                    && getChargeLevel(stack) == 0) {
//                spiritWolf.setDead();
//                setWolfId(stack, "");
//            }
//        }
//    }
//
//    /**
//     * This method is called when the bauble is unequipped by a player
//     */
//    public void onUnequipped(ItemStack stack, LivingEntity player) {
//        if (!player.getEntityWorld().isRemote) {
//            EntitySpiritWolf spiritWolf = getWolf(stack, player.getEntityWorld());
//            if (spiritWolf != null) {
//                spiritWolf.setDead();
//            }
//            setWolfId(stack, "");
//        }
//    }
}
