package com.sho.hire.hw.piserjohnmemory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.application.ConcentrationApplication;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGame;
import com.sho.hire.hw.piserjohnmemory.flickr.FlickrHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;
import com.sho.hire.hw.piserjohnmemory.util.GridViewImageAdapter;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ConcentrationGame.Host {

    public static final String THEME_KITTEN = "kitten";

    @Inject FlickrHelper flickrHelper;
    @Inject LogHelper logHelper;
    @Inject ConcentrationGame concentrationGame;


    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);

        //Dagger dependency injection
        ((ConcentrationApplication)getApplication()).getAppComponent().inject(this);

        startGame();
    }

    private void startGame() {
        concentrationGame.init(this, THEME_KITTEN);
    }

    @Override
    public void displayConcentrationCells(List<ConcentrationCell> concentrationCells) {
        logHelper.debug(Constants.LOGTAG, "[MainActivity] displaying concentration cells...");
        GridViewImageAdapter gridViewImageAdapter = new GridViewImageAdapter(this, concentrationCells, logHelper);
        gridView.setAdapter(gridViewImageAdapter);
        gridView.invalidateViews();

    }
}
