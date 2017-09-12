package com.corwinjv.di;


import com.corwinjv.di.modules.MinecraftModule;
import dagger.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

@Component(modules = {
        MinecraftModule.class
})
public interface MinecraftComponent {
    @Nullable
    MinecraftServer minecraftServer();

    @Nullable
    Minecraft minecraft();

    TextureManager textureManager();

    RenderItem renderItem();

    ItemModelMesher itemModelMesher();
}
