package com.corwinjv.di.modules;

import dagger.Module;
import dagger.Provides;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;

@Module
public class MinecraftModule {

    private Minecraft minecraft;

    public MinecraftModule(Minecraft minecraft) {
        this.minecraft = minecraft;
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
