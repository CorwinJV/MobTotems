package com.corwinjv.mobtotems;

import com.corwinjv.mobtotems.network.ActivateBaubleMessage;
import com.corwinjv.mobtotems.network.Network;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by CorwinJV on 1/31/2016.
 */
public class KeyBindings
{

    public static final int ACTIVATE_BAUBLE_KEY = 0;

    public static final KeyBinding[] keys = new KeyBinding[1];

    public static boolean ACTIVATE_BAUBLE_KEY_PRESSED = false;

    public KeyBindings()
    {
        keys[ACTIVATE_BAUBLE_KEY] = new KeyBinding("key.mobtotems.activateBaubleDescription", Keyboard.KEY_H, "key.mobtotems.category");
        ClientRegistry.registerKeyBinding(keys[ACTIVATE_BAUBLE_KEY]);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if(!FMLClientHandler.instance().isGUIOpen(GuiChat.class))
        {
            if(keys[ACTIVATE_BAUBLE_KEY].isKeyDown() && !ACTIVATE_BAUBLE_KEY_PRESSED)
            {
                Network.sendToServer(new ActivateBaubleMessage());
                ACTIVATE_BAUBLE_KEY_PRESSED = true;
            }
            else if(!keys[ACTIVATE_BAUBLE_KEY].isKeyDown() && ACTIVATE_BAUBLE_KEY_PRESSED)
            {
                ACTIVATE_BAUBLE_KEY_PRESSED = false;
            }
        }
    }
}
