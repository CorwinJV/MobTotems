package com.corwinjv.mobtotems.interfaces;

/**
 * Created by CorwinJV on 1/23/2017.
 */
public interface IChargeableTileEntity {
    int getChargeLevel();

    void setChargeLevel(int chargeLevel);

    void decrementChargeLevel(int amount);

    void incrementChargeLevel(int amount);

    int getMaxChargeLevel();
}
