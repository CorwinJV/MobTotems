package com.corwinjv.mobtotems.entities;

import com.corwinjv.mobtotems.entities.render.SpiritWolfRenderFactory;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CorwinJV on 2/15/2016.
 */
public class ModEntities
{
    private static int ENTITY_ID = 0;

    public static final String SPIRIT_WOLF = "spirit_wolf";

    private static Map<String,Class<? extends Entity>> mEntities = Collections.emptyMap();

    public static void init()
    {
        mEntities = new HashMap<String,Class<? extends Entity>>();
        mEntities.put(SPIRIT_WOLF, EntitySpiritWolf.class);
    }

    public static void registerEntities(Object modObject)
    {
        for (String key : mEntities.keySet())
        {
            Class<? extends Entity> entityClass = mEntities.get(key);
            if(entityClass != null)
            {
                EntityRegistry.registerModEntity(entityClass, key, ENTITY_ID++, modObject, 80, 3, false);
                EntityRegistry.registerEgg(entityClass, 1, 2);
            }
        }
    }

    public static void registerEntityRenders()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiritWolf.class, new SpiritWolfRenderFactory());
    }
}
