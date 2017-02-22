package com.sho.hire.hw.piserjohnmemory.flickr;

import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCellProvider;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCellReceiver;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGame;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * A ConcetrationCellProvider powered by Flickr
 */

public class FlickrConcentrationCellProvider implements ConcentrationCellProvider, FlickrHelper.FlickrPhotoReceiver {

    private static final String UNDERSCORE = "_";
    private static final String LARGE_SQUARE = "q";
    private static final String JPG = ".jpg";
    private static final String PROTOCOL = "https://";
    private static final String FARM = "farm";
    private static final String DOMAIN = ".staticflickr.com/";
    private static final String SLASH = "/";

    //package access for testability
    FlickrHelper flickrHelper;
    ConcentrationCellReceiver concentrationCellReceiver;

    @Inject
    public FlickrConcentrationCellProvider(FlickrHelper flickrHelper) {
        this.flickrHelper = flickrHelper;
    }

    @Override
    public void getConcentrationCells(ConcentrationCellReceiver concentrationCellReceiver, int page, int batchSize, String[] tags) {
        this.concentrationCellReceiver = concentrationCellReceiver;
        flickrHelper.getPhotosByTags(this, page, batchSize, tags);
    }

    @Override
    public void loadFlickrPhotos(List<FlickrPhoto> flickrFlickrPhotoList) {
        concentrationCellReceiver.loadConcentrationCellQueue(convertPhotos(flickrFlickrPhotoList));
    }

    private Queue<ConcentrationCell> convertPhotos(List<FlickrPhoto> flickrFlickrPhotoList) {
        if(flickrFlickrPhotoList != null){

            Queue<ConcentrationCell> concentrationCellQueue = new ArrayDeque<>(ConcentrationGame.BATCH_SIZE);
            for (FlickrPhoto flickrPhoto : flickrFlickrPhotoList) {
                ConcentrationCell concentrationCell = new ConcentrationCell();
                concentrationCell.setUrl(composeImageUrl(flickrPhoto));
                concentrationCell.setId(flickrPhoto.getId());

                concentrationCellQueue.add(concentrationCell);
            }
            return concentrationCellQueue;
        }
        return null;
    }

    private String composeImageUrl(FlickrPhoto flickrPhoto) {
        //Flickr url syntax: https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
        if (flickrPhoto != null) {
            StringBuilder builder = new StringBuilder(PROTOCOL)
                    .append(FARM).append(flickrPhoto.getFarm())
                    .append(DOMAIN)
                    .append(flickrPhoto.getServer()).append(SLASH)
                    .append(flickrPhoto.getId())
                    .append(UNDERSCORE)
                    .append(flickrPhoto.getSecret())
                    .append(UNDERSCORE)
                    .append(LARGE_SQUARE)
                    .append(JPG);

            return builder.toString();
        }
        return null;
    }
}
