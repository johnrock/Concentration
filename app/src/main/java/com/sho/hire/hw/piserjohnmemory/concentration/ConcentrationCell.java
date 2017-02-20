package com.sho.hire.hw.piserjohnmemory.concentration;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Pojo  representing one cell of a concentration grid
 */
public class ConcentrationCell {

    private String url;
    private String id;

    public ConcentrationCell duplicate() {
        ConcentrationCell concentrationCell = new ConcentrationCell();
        concentrationCell.setUrl(this.url);
        concentrationCell.setId(this.id);
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

    
}
