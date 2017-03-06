package com.corwinjv.di.modules;

import dagger.Module;
import dagger.Provides;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;

@Module
public class MinecraftModule {

    private Minecraft minecraft;

    public MinecraftModule(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Provides
    Minecraft provideMinecraft() {
        return minecraft;
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
