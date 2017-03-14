package com.corwinjv.mobtotems.interfaces;

import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by CorwinJV on 1/21/2017.
 */
public interface IMultiblock<T> {
    String IS_MASTER = "isMaster";
    String SLAVES = "slaves";
    String MASTER_POS = "masterPos";

    boolean getIsMaster();

    void setIsMaster(boolean isMaster);

    void verifyMultiblock();

    void setSlaves(List<BlockPos> slaves);

    void invalidateSlaves();

    List<BlockPos> getSlaves();

    List<T> getSlaveTypes();

    T getType();

    void setType(T type);

    void setMaster(IMultiblock<T> master);

    IMultiblock<T> getMaster();
}
