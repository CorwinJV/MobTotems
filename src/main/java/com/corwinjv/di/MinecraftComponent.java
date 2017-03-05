package com.corwinjv.di;


import com.corwinjv.di.modules.MinecraftModule;
import dagger.Component;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;

@Component(modules = {
        MinecraftModule.class
})
public interface MinecraftComponent {

    RenderItem renderItem();
    ItemModelMesher itemModelMesher();
}
