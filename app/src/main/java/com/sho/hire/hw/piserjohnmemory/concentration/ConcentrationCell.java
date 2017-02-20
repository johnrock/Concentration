package com.sho.hire.hw.piserjohnmemory.concentration;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Pojo  representing one cell of a concentration grid
 */
public class ConcentrationCell {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ConcentrationCell{" +
                "url='" + url + '\'' +
                '}';
    }
}
