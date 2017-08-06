package com.lavendergoons.dndcharacter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.multidex.MultiDexApplication;

import com.lavendergoons.dndcharacter.di.component.AppComponent;
import com.lavendergoons.dndcharacter.di.component.DaggerAppComponent;
import com.lavendergoons.dndcharacter.di.module.AppModule;
import com.squareup.leakcanary.LeakCanary;


public class DndApplication extends MultiDexApplication {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static DndApplication get(Activity activity) {
        return (DndApplication) activity.getApplication();
    }

    public static DndApplication get(Fragment fragment) {
        return (DndApplication) fragment.getActivity().getApplication();
    }

    public AppComponent getAppComponent() {
        return component;
    }
}
