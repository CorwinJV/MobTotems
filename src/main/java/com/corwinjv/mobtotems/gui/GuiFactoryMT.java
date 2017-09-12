package com.corwinjv.mobtotems.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class GuiFactoryMT implements IModGuiFactory {
    /**
     * Called when instantiated to initialize with the active minecraft instance.
     *
     * @param minecraftInstance the instance
     */
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    /**
     * Return an initialized {@link GuiScreen}. This screen will be displayed
     * when the "config" button is pressed in the mod list. It will
     * have a single argument constructor - the "parent" screen, the same as all
     * Minecraft GUIs. The expected behaviour is that this screen will replace the
     * "mod list" screen completely, and will return to the mod list screen through
     * the parent link, once the appropriate action is taken from the config screen.
     *
     * This config GUI is anticipated to provide configuration to the mod in a friendly
     * visual way. It should not be abused to set internals such as IDs (they're gonna
     * keep disappearing anyway), but rather, interesting behaviours. This config GUI
     * is never run when a server game is running, and should be used to configure
     * desired behaviours that affect server state. Costs, mod game modes, stuff like that
     * can be changed here.
     *
     * @param parentScreen The screen to which must be returned when closing the
     * returned screen.
     * @return A class that will be instantiated on clicks on the config button
     *  or null if no GUI is desired.
     */
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGuiMT(parentScreen);
    }

    /**
     * Return a list of the "runtime" categories this mod wishes to populate with
     * GUI elements.
     * <p>
     * Runtime categories are created on demand and organized in a 'lite' tree format.
     * The parent represents the parent node in the tree. There is one special parent
     * 'Help' that will always list first, and is generally meant to provide Help type
     * content for mods. The remaining parents will sort alphabetically, though
     * this may change if there is a lot of alphabetic abuse. "AAA" is probably never a valid
     * category parent.
     * <p>
     * Runtime configuration itself falls into two flavours: in-game help, which is
     * generally non interactive except for the text it wishes to show, and client-only
     * affecting behaviours. This would include things like toggling minimaps, or cheat modes
     * or anything NOT affecting the behaviour of the server. Please don't abuse this to
     * change the state of the server in any way, this is intended to behave identically
     * when the server is local or remote.
     *
     * @return the set of options this mod wishes to have available, or empty if none
     */
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
