package com.corwinjv.di;

import com.corwinjv.di.modules.MobTotemsModule;
import com.corwinjv.mobtotems.MobTotems;
import dagger.Component;

@Component(modules = {
        MobTotemsModule.class
}, dependencies = {
        MinecraftComponent.class
})
public interface MobTotemsComponent extends MinecraftComponent {

    MobTotems mobTotems();
}
