package com.sho.hire.hw.piserjohnmemory.flickr;

import com.sho.hire.hw.piserjohnmemory.helpers.HttpHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * @author John Piser johnpiser@yahoo.com
 */
public class FlickrHelperTest {

    public static final int PAGE = 0;
    public static final int BATCH_SIZE = 80;
    public static final String[] TAGS = new String[]{"test"};

    @Mock HttpHelper httpHelper;
    @Mock LogHelper logHelper;
    @Mock FlickrHelper.FlickrPhotoReceiver flickrPhotoReceiver;

    FlickrHelper flickrHelper;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        flickrHelper = new FlickrHelper(httpHelper, logHelper);
    }

    @Test
    public void shouldGetPhotosByTags(){
        flickrHelper.getPhotosByTags(flickrPhotoReceiver,PAGE, BATCH_SIZE, TAGS);

        verify(httpHelper).get(any(HttpHelper.ResponseListener.class), anyString());
    }

    @Test
    public void shouldHandleResponseCompleteOnUiThread(){
        flickrHelper.flickrPhotoReceiver = flickrPhotoReceiver;
        flickrHelper.flickrPhotos = new ArrayList<>();

        StringBuilder builder = new StringBuilder("https://test/").append(FlickrMethod.SEARCH.getValue());
        flickrHelper.handleResponseCompleteOnUiThread(builder.toString(), "");

        verify(flickrPhotoReceiver).loadFlickrPhotos(any(List.class));
    }

    //TODO: write additional tests
}