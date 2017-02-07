package com.corwinjv.mobtotems;

import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import com.corwinjv.mobtotems.blocks.tiles.TotemLogic.*;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CorwinJV on 1/25/2017.
 */
public class TotemHelper {

    public static final int DEFAULT_RADIUS = 32;
    public static final int RANGE_BOOST = 16;
    public static final int MAX_TOTEM_HEIGHT = 3;

    private static Map<TotemType,Class<? extends TotemLogic>> totemMap = new HashMap<>();

    public static void init()
    {
        totemMap.put(TotemType.NONE, null);
        totemMap.put(TotemType.CREEPER, CreeperLogic.class);
        totemMap.put(TotemType.ENDER, EnderLogic.class);
        totemMap.put(TotemType.BLAZE, BlazeLogic.class);
        totemMap.put(TotemType.SPIDER, SpiderLogic.class);
        totemMap.put(TotemType.COW, CowLogic.class);
        totemMap.put(TotemType.WOLF, WolfLogic.class);
        totemMap.put(TotemType.OCELOT, OcelotLogic.class);
        totemMap.put(TotemType.WITHER, WitherLogic.class);
        totemMap.put(TotemType.LLAMA, LlamaLogic.class);
        totemMap.put(TotemType.RABBIT, RabbitLogic.class);
    }

    @Nullable
    public static TotemLogic getTotemLogic(TotemType totemType) {
        TotemLogic ret = null;
        if(totemMap.containsKey(totemType)) {
            try {
                if(totemMap.get(totemType) != null)
                {
                    ret = totemMap.get(totemType).newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Nonnull
    public static List<ItemStack> getCostForTotemType(TotemType totemType) {
        List<ItemStack> cost = new ArrayList<>();
        TotemLogic logic = getTotemLogic(totemType);
        if(logic != null) {
            cost = logic.getCost();
        }
        return cost;
    }

    public static void sortEffectsAndModifiers(List<TotemType> list, List<TotemType> effectsList, List<TotemType> modifiersList) {
        for(TotemType type : list) {
            TotemLogic logic = getTotemLogic(type);

            if(logic != null) {
                switch(logic.getEffectType()) {
                    case EFFECT: {
                        effectsList.add(type);
                        break;
                    }
                    case MODIFIER: {
                        modifiersList.add(type);
                        break;
                    }
                }
            }
        }
    }

    public static boolean hasTotemType(World world, OfferingBoxTileEntity tileEntity, TotemType totemType)
    {
        for(TotemType type : tileEntity.getSlaveTypes()) {
            if(type == totemType) {
                return true;
            }
        }
        return false;
    }

    public static Modifiers getModifiers(World world, OfferingBoxTileEntity tileEntity)
    {
        Modifiers mods = new Modifiers();
        for(TotemType type : tileEntity.getSlaveTypes()) {
            TotemLogic logic = getTotemLogic(type);
            if(logic != null) {
                mods = logic.adjustModifiers(mods);
            }
        }
        return mods;
    }

    public static List<BlockPos> checkForSlaveAboveRecursively(World world, BlockPos startPos, int height)
    {
        List<BlockPos> totemTileList = new ArrayList<>();

        if(height > 0) {
            TileEntity totemTileEntity = world.getTileEntity(startPos);
            if(totemTileEntity instanceof TotemTileEntity) {
                totemTileList.add(startPos);

                List<BlockPos> tmpList = checkForSlaveAboveRecursively(world, startPos.add(0, 1, 0), height-1);
                for(BlockPos te : tmpList) {
                    totemTileList.add(te);
                }
            }
        }
        return totemTileList;
    }
}
