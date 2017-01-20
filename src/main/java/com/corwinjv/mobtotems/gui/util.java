package com.corwinjv.mobtotems.gui;

import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.BaubleItem;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 1/19/2017.
 */
public class util {
    public static ResourceLocation getGuiResourceLocation(@Nonnull BaubleItem baubleItem)
    {
        String resourceName = baubleItem.getRegistryName().toString().substring(Reference.RESOURCE_PREFIX.length());
        return new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/" + resourceName + ".png");
    }

    public static ResourceLocation getGuiResourceLocation(@Nonnull String guiFileName)
    {
        return new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/" + guiFileName);
    }
}
