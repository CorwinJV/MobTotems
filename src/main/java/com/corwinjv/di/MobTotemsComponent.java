package com.corwinjv.di;

import com.corwinjv.di.modules.MinecraftModule;
import com.corwinjv.mobtotems.MobTotems;
import dagger.Component;

@Component( modules = {
        MinecraftModule.class
}, dependencies = {
    MinecraftComponent.class
})
public interface MobTotemsComponent {

    void inject(MobTotems mobTotems);


    MobTotems mobTotems();
}
