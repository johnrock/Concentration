package com.sho.hire.hw.piserjohnmemory.flickr;

import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCellReceiver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author John Piser johnpiser@yahoo.com
 */
public class FlickrConcentrationCellProviderTest {


    public static final int PAGE = 0;
    public static final int BATCH_SIZE = 80;
    public static final String[] TAGS = new String[]{"test"};
    @Mock FlickrHelper flickrHelper;
    @Mock ConcentrationCellReceiver concentrationCellReceiver;

    FlickrConcentrationCellProvider flickrConcentrationCellProvider;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        flickrConcentrationCellProvider = new FlickrConcentrationCellProvider(flickrHelper);
    }

    @Test
    public void shouldGetConcentrationCellsFromFlicr(){

        flickrConcentrationCellProvider.getConcentrationCells(concentrationCellReceiver, PAGE, BATCH_SIZE, TAGS);
        verify(flickrHelper).getPhotosByTags(flickrConcentrationCellProvider, PAGE, BATCH_SIZE,TAGS);
    }

    @Test
    public void shouldLoadFlickrPhotos(){
        flickrConcentrationCellProvider.concentrationCellReceiver = concentrationCellReceiver;

        flickrConcentrationCellProvider.loadFlickrPhotos(listOfFlickrPhotos());
        verify(concentrationCellReceiver).loadConcentrationCellQueue(any(Queue.class));
    }

    private List<FlickrPhoto> listOfFlickrPhotos() {
        List<FlickrPhoto> results = new ArrayList<>();
        for(int i=0; i< BATCH_SIZE; i++){
            FlickrPhoto flickrPhoto = new FlickrPhoto();
            flickrPhoto.setId(String.valueOf(i));
        }
        return results;
    }
}