package com.corwinjv.di.modules;

import com.corwinjv.mobtotems.MobTotems;
import dagger.Module;
import dagger.Provides;

@Module
public class MobTotemsModule {

    private MobTotems mobTotems;

    public MobTotemsModule(MobTotems mobTotems) {
        this.mobTotems = mobTotems;
    }

    @Provides
    public MobTotems provideMobTotems() {
        return mobTotems;
    }
}
