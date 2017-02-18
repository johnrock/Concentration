package com.sho.hire.hw.piserjohnmemory.application;

import android.app.Application;

import com.sho.hire.hw.piserjohnmemory.dagger.AppComponent;
import com.sho.hire.hw.piserjohnmemory.dagger.AppModule;
import com.sho.hire.hw.piserjohnmemory.dagger.DaggerAppComponent;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class ConcentrationApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Dependency Injection
        appComponent = DaggerAppComponent.builder()
                       .appModule(new AppModule(this))
                       .build();

                           


    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
