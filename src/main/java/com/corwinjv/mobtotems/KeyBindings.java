package com.corwinjv.mobtotems;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by CorwinJV on 1/31/2016.
 */
public class KeyBindings
{

    public static final int ACTIVATE_BAUBLE_KEY = 0;

    public static final KeyBinding[] keys = new KeyBinding[1];

    public static void init()
    {
        keys[0] = new KeyBinding("key.mobtotems.activateBaubleDescription", Keyboard.KEY_H, "key.mobtotems.category");
        ClientRegistry.registerKeyBinding(keys[ACTIVATE_BAUBLE_KEY]);
    }
}
