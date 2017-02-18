package com.sho.hire.hw.piserjohnmemory.flickr;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public enum FlickrMethod {
    RECENT ("flickr.photos.getRecent"),
    SIZES  ("flickr.photos.getSizes");

    private String value;

    FlickrMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
