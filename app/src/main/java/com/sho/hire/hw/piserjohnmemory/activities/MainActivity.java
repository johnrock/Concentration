package com.sho.hire.hw.piserjohnmemory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.application.ConcentrationApplication;
import com.sho.hire.hw.piserjohnmemory.flickr.Photo;
import com.sho.hire.hw.piserjohnmemory.helpers.FlickrHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements FlickrHelper.FlickrPhotoReceiver{

    public static final String THEME_KITTEN = "kitten";

    @Inject FlickrHelper flickrHelper;
    @Inject LogHelper logHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ConcentrationApplication)getApplication()).getAppComponent().inject(this);


        flickrHelper.fetchImages(this, THEME_KITTEN);

    }

    @Override
    public void loadFlickrPhotoList(List<Photo> flickrPhotoList) {
        logHelper.debug(Constants.LOGTAG, "Inside MainActivity: loadFlickrPhotoList with:" + flickrPhotoList);
    }
}
