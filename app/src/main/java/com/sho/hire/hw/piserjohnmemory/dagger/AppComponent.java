package com.sho.hire.hw.piserjohnmemory.dagger;

import com.sho.hire.hw.piserjohnmemory.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Application component for Dagger2 dependency injection
 */

@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

}
