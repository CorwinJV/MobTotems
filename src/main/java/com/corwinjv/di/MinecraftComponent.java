package com.corwinjv.di;


import com.corwinjv.di.modules.MinecraftModule;
import dagger.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;

@Component(modules = {
        MinecraftModule.class
})
public interface MinecraftComponent {
    Minecraft minecraft();
    TextureManager textureManager();
    RenderItem renderItem();
    ItemModelMesher itemModelMesher();
}
