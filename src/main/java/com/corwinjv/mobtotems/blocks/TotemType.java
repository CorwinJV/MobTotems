package com.corwinjv.mobtotems.blocks;

import net.minecraft.util.IStringSerializable;

/**
 * Created by CorwinJV on 1/18/2017.
 */
public enum TotemType implements IStringSerializable {
    NONE(0, "none"),
    CREEPER(1, "creeper"),
    ENDER(2, "ender"),
    BLAZE(3, "blaze"),
    SPIDER(4, "spider"),
    COW(5, "cow"),
    WOLF(6, "wolf"),
    OCELOT(7, "ocelot"),
    WITHER(8, "wither"),
    LLAMA(9, "llama"),
    RABBIT(10, "rabbit");

    private final int meta;
    private final String name;

    private TotemType(int meta, String name) {
        this.meta = meta;
        this.name = name;
    }

    public int getMeta() {
        return this.meta;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static TotemType fromMeta(int meta) {
        TotemType retType = TotemType.NONE;
        for (TotemType type : values()) {
            if (type.getMeta() == meta) {
                retType = type;
                break;
            }
        }
        return retType;
    }
}
