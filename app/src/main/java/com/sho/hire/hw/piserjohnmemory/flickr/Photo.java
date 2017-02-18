package com.sho.hire.hw.piserjohnmemory.flickr;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Model of a Photo provided by flickr api
 *
 * "photo": [
 * {
 *  "id": "32133131524",
 *  "owner": "147484878@N02",
 *  "secret": "664af524f5",
 *  "server": "2523",
 *  "farm": 3,
 *  "title": "Wow wee he so fresh",
 *  "ispublic": 1,
 *  "isfriend": 0,
 *  "isfamily": 0
 * },
 */

public class Photo {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private String title;
    private int farm;
    private int ispublic;
    private int isfriend;
    private int isfamily;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", server='" + server + '\'' +
                ", title='" + title + '\'' +
                ", farm=" + farm +
                '}';
    }
}
