package com.corwinjv.di.modules;

import dagger.Module;
import dagger.Provides;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

@Module
public class MinecraftModule {

    private Minecraft minecraft;
    private MinecraftServer minecraftServer;

    public MinecraftModule(Minecraft minecraft, MinecraftServer minecraftServer) {
        this.minecraft = minecraft;
        this.minecraftServer = minecraftServer;
    }

    @Provides
    @Nullable
    Minecraft provideMinecraft() {
        return minecraft;
    }

    @Provides
    @Nullable
    MinecraftServer provideMinecraftServer() {
        return minecraftServer;
    }

    @Provides
    TextureManager provideTextureManager() {
        return minecraft.getTextureManager();
    }

    @Provides
    public RenderItem provideRenderItem() {
        return minecraft.getRenderItem();
    }

    @Provides
    public ItemModelMesher provideItemModelMesher() {
        return minecraft.getRenderItem().getItemModelMesher();
    }
}
