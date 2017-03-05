package com.corwinjv.mobtotems.proxy;

import com.corwinjv.mobtotems.KeyBindings;
import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.blocks.ModBlocks;
import com.corwinjv.mobtotems.entities.ModEntities;
import com.corwinjv.mobtotems.gui.BaublesChargeGui;
import com.corwinjv.mobtotems.items.ModItems;
import com.corwinjv.mobtotems.particles.ParticleRenderer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by CorwinJV on 1/23/2016.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenders()
    {
        ModItems.registerRenders();
    }

    @Override
    public void registerEntityRenders()
    {
        ModEntities.registerEntityRenders();
    }

    @Override
    public void registerKeys()
    {
        MinecraftForge.EVENT_BUS.register(new KeyBindings());
    }

    @Override
    public void registerGui()
    {
        MinecraftForge.EVENT_BUS.register(new BaublesChargeGui(MobTotems.component().minecraft()));
    }

    @Override
    public void registerParticleRenderer()
    {
        MinecraftForge.EVENT_BUS.register(new ParticleRenderer());
    }
}
