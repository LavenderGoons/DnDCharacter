package com.lavendergoons.dndcharacter.di.module;


import android.content.Context;

import com.lavendergoons.dndcharacter.di.scope.DataScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @DataScope
    public Context context() {
        return context;
    }
}
