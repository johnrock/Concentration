package com.sho.hire.hw.piserjohnmemory.helpers;

import com.sho.hire.hw.piserjohnmemory.flickr.FlickrMethod;
import com.sho.hire.hw.piserjohnmemory.flickr.Photo;
import com.sho.hire.hw.piserjohnmemory.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Helper class responsible for all Interactions with FlickrMethod api
 */
@Singleton
public class FlickrHelper implements  HttpHelper.ResponseListener{

    public static final int MAX_PAGES_FOR_ENDPOINT = 10;

    public interface FlickrPhotoReceiver{
        void loadFlickrPhotoList(List<Photo> flickrPhotoList);
    }

    public static final String BASE_URL = "https://api.flickr.com/services/rest/?";
    public static final String API_KEY = "5423dbab63f23a62ca4a986e7cbb35e2";
    public static final String JSON = "json";
    public static final int UNIQUE_IMAGES_PER_GAME = 4;

    HttpHelper httpHelper;
    LogHelper logHelper;

    int currentPage;
    List<Photo> photoList;
    FlickrPhotoReceiver flickrPhotoReceiver;

    @Inject
    public FlickrHelper(HttpHelper httpHelper, LogHelper logHelper) {
        this.httpHelper = httpHelper;
        this.logHelper = logHelper;
        currentPage = 1;
    }

    private void initPhotoList() {
        if(photoList == null){
            photoList = new ArrayList<>();
        }
        else{
            photoList.clear();
        }
    }


    public void fetchImages(FlickrPhotoReceiver flickrPhotoReceiver, String...tags){
        this.flickrPhotoReceiver = flickrPhotoReceiver;
        if(flickrPhotoReceiver != null){
            if(photoList != null && photoList.size() >= UNIQUE_IMAGES_PER_GAME){
                logHelper.debug(Constants.LOGTAG, "We have enough images to load a new game so no need to request new ones");
                flickrPhotoReceiver.loadFlickrPhotoList(popNextPhotos());
            }
            else{
                getRecent(tags);
            }
        }
    }


    private List<Photo> popNextPhotos() {
        List<Photo> results = new ArrayList<>();
        Iterator<Photo> iterator = photoList.iterator();
        for(int i = 0; i< UNIQUE_IMAGES_PER_GAME; i++){
            if(iterator.hasNext()){
                results.add(iterator.next());
                iterator.remove();
            }
        }
        return results;
    }


    private void getRecent(String... tags){
        logHelper.debug(Constants.LOGTAG, "Requesting new images from Flickr using the next page...");
        initPhotoList();
        httpHelper.get(this, makeEndpoint(FlickrMethod.RECENT, getCurrentPage(),  tags));

    }



    private String makeEndpoint(FlickrMethod flickrMethod, int page, String...tags) {
        StringBuilder stringBuilder = new StringBuilder(BASE_URL);
        stringBuilder.append("method=").append(flickrMethod.getValue());
        stringBuilder.append("&api_key=").append(API_KEY);
        stringBuilder.append("&format=").append(JSON);
        stringBuilder.append("&page=").append(String.valueOf(page));


        String separator = "&tags=";
        for (String tag : tags) {
            stringBuilder.append(separator).append(tag);
            separator=",";
        }

        return stringBuilder.toString();
    }

    private int getCurrentPage() {
        if(currentPage == MAX_PAGES_FOR_ENDPOINT){
            currentPage = 0;
        }
        return currentPage++;
    }


    /**
     * Method to handle all Flickr responses based on the FlickrMethod
     *
     * This callback will be called from a background thread
     * @param url
     * @param response
     */
    @Override
    public void handleResponse(String url, String response) {
        if(url != null && response != null){
            if(url.contains(FlickrMethod.RECENT.getValue())){


                logHelper.debug(Constants.LOGTAG, "Inside FlickrHelper handleResponse with: " + response);
                JSONObject jsonObject = makeJsonObject(response);

                if(jsonObject != null){
                    try {
                        JSONObject photos = (JSONObject) jsonObject.get("photos");
                        if(photos != null){
                            JSONArray photoArray = (JSONArray) photos.getJSONArray("photo");
                            if(photoArray != null){
                                for(int i=0; i<photoArray.length(); i++){
                                    try {
                                        Photo photo = new Photo();
                                        JSONObject photoJSONObject = photoArray.getJSONObject(i);
                                        photo.setId(photoJSONObject.getString("id"));
                                        photo.setOwner(photoJSONObject.getString("owner"));
                                        photo.setSecret(photoJSONObject.getString("secret"));
                                        photo.setServer(photoJSONObject.getString("server"));
                                        photo.setFarm(photoJSONObject.getInt("farm"));
                                        photo.setTitle(photoJSONObject.getString("title"));
                                        photoList.add(photo);
                                    }
                                    catch (JSONException e){
                                        logHelper.error(Constants.LOGTAG, "!Error when parsing photoArray: " + photoArray);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        logHelper.error(Constants.LOGTAG, "!Error when parsing jsonObject: " + jsonObject);
                    }
                }
                logHelper.debug(Constants.LOGTAG, "Inside FlickrHelper handleResponse with photoList: " + photoList);
            }
        }
    }

    /**
     * Method to hamdle all UI updates after a background request completes based on FlickrMethod
     *
     * This will be called on the UI thread
     * @param url
     * @param response
     */
    @Override
    public void responseComplete(String url, String response) {

        if(url.contains(FlickrMethod.RECENT.getValue())){

            if(flickrPhotoReceiver != null){
                flickrPhotoReceiver.loadFlickrPhotoList(popNextPhotos());
            }
        }

    }

    private JSONObject makeJsonObject(String response) {
        //prefix to the json: jsonFlickrApi(........)

        if(response != null){
            String pureJson = response.substring(14, response.length() - 1);
            try {
                return new JSONObject(pureJson);
            } catch (JSONException e) {
                logHelper.error(Constants.LOGTAG, "!Error when parsing json: " + pureJson);
            }
        }
        return null;
    }
}
