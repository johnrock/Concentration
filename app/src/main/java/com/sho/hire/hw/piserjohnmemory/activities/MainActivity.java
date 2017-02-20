package com.sho.hire.hw.piserjohnmemory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.application.ConcentrationApplication;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGame;
import com.sho.hire.hw.piserjohnmemory.flickr.FlickrHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity  {

    public static final String THEME_KITTEN = "kitten";

    @Inject FlickrHelper flickrHelper;
    @Inject LogHelper logHelper;
    @Inject ConcentrationGame concentrationGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dagger dependency injection
        ((ConcentrationApplication)getApplication()).getAppComponent().inject(this);

        startGame();
    }

    private void startGame() {
        concentrationGame.init(THEME_KITTEN);
    }

}
