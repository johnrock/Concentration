package com.sho.hire.hw.piserjohnmemory.concentration;

import android.graphics.Bitmap;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Pojo  representing one cell of a concentration grid
 */
public class ConcentrationCell {

    private String url;
    private String id;
    private boolean showing;
    private Bitmap imageBitmap;
    private int defaultResourceId = ConcentrationGame.DEFAULT_GRID_ICON;

    public ConcentrationCell duplicate() {
        //do not duplicate 'showing' field
        ConcentrationCell concentrationCell = new ConcentrationCell();
        concentrationCell.setUrl(this.url);
        concentrationCell.setId(this.id);
        concentrationCell.setDefaultResourceId(this.defaultResourceId);
        concentrationCell.setImageBitmap(this.imageBitmap);
        return concentrationCell;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setDefaultResourceId(int defaultResourceId) {
        this.defaultResourceId = defaultResourceId;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public int getDefaultResourceId() {
        return defaultResourceId;
    }
}
