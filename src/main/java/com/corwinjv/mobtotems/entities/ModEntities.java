package com.corwinjv.mobtotems.entities;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.entities.render.SpiritWolfRenderFactory;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Created by CorwinJV on 2/15/2016.
 */
// TODO: Port
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModEntities {
    public static final String SPIRIT_WOLF_NAME = "spirit_wolf";

    @ObjectHolder(SPIRIT_WOLF_NAME)
    public static EntityType<EntitySpiritWolf> SPIRIT_WOLF;

    public static void init() {
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {

        event.getRegistry().register(EntityType.Builder.create(EntitySpiritWolf::new, EntityClassification.CREATURE)
                .size(0, 0)
                .setUpdateInterval(10)
                .build("")
                .setRegistryName(SPIRIT_WOLF_NAME));
    }

    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiritWolf.class, new SpiritWolfRenderFactory());
    }
}
