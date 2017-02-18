package com.sho.hire.hw.piserjohnmemory.dagger;

import android.app.Application;

import com.sho.hire.hw.piserjohnmemory.BuildConfig;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Dagger2 Module for dependency injectopm
 *
 */

@Module
public class AppModule {

    Application application;
    LogHelper logHelper;

    public AppModule(Application application) {
        this.application = application;

        logHelper = new LogHelper(BuildConfig.DEBUG);

    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    LogHelper providesLogHelper(){
        return logHelper;
    }
}
