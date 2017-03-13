package com.corwinjv.mobtotems.proxy;

import com.corwinjv.di.DaggerMinecraftComponent;
import com.corwinjv.di.DaggerMobTotemsComponent;
import com.corwinjv.di.MinecraftComponent;
import com.corwinjv.di.MobTotemsComponent;
import com.corwinjv.di.modules.MinecraftModule;
import com.corwinjv.di.modules.MobTotemsModule;
import com.corwinjv.mobtotems.MobTotems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Created by CorwinJV on 1/23/2016.
 */
public class CommonProxy
{
    public void registerRenders() {
    }

    public void registerEntityRenders() {
    }

    public void registerKeys() {
    }

    public void registerGui() {
    }

    public void registerParticleRenderer() {
    }

    public MobTotemsComponent initializeDagger() {
        MinecraftComponent minecraftComponent = initializeMinecraftComponent();
        return DaggerMobTotemsComponent.builder()
                .mobTotemsModule(new MobTotemsModule(MobTotems.instance))
                .minecraftComponent(minecraftComponent)
                .build();
    }

    public MinecraftComponent initializeMinecraftComponent() {
        return DaggerMinecraftComponent.builder()
                .minecraftModule(new MinecraftModule(null, FMLCommonHandler.instance().getMinecraftServerInstance()))
                .build();
    }
}
