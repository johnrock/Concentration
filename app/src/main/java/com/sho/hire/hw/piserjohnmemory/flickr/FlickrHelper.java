package com.sho.hire.hw.piserjohnmemory.flickr;

import com.sho.hire.hw.piserjohnmemory.helpers.HttpHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Helper class responsible for all Interactions with Flickr api
 */
@Singleton
public class FlickrHelper implements HttpHelper.ResponseListener {

    public interface FlickrPhotoReceiver {
        void loadFlickrPhotos(List<FlickrPhoto> flickrFlickrPhotoList);
    }

    public static final String BASE_URL = "https://api.flickr.com/services/rest/?";
    public static final String API_KEY = "5423dbab63f23a62ca4a986e7cbb35e2";
    public static final String JSON = "json";

    HttpHelper httpHelper;
    LogHelper logHelper;
    FlickrPhotoReceiver flickrPhotoReceiver;
    List<FlickrPhoto> flickrPhotos;

    @Inject
    public FlickrHelper(HttpHelper httpHelper, LogHelper logHelper) {
        this.httpHelper = httpHelper;
        this.logHelper = logHelper;
    }


    public void getPhotosByTags(FlickrPhotoReceiver flickrPhotoReceiver, int page, int maxPerPage, String... tags){
        this.flickrPhotoReceiver = flickrPhotoReceiver;
        flickrPhotos = new ArrayList<>();

        logHelper.debug(Constants.LOGTAG, "Requesting new images from Flickr using page " + page);
        httpHelper.get(this, makeEndpoint(FlickrMethod.SEARCH, page, maxPerPage, tags));
    }



    /**
     * Method to handle all Flickr responses based on the FlickrMethod
     * This callback will be called from a background thread
     * @param url
     * @param response
     */
    @Override
    public void handleResponseInBackground(String url, String response) {
        if(url != null && response != null){
            if(url.contains(FlickrMethod.SEARCH.getValue())){

                logHelper.debug(Constants.LOGTAG, "Inside FlickrHelper handleResponseInBackground with: " + response);
                JSONObject jsonObject = makeJsonObject(response);

                if(jsonObject != null){
                    try {
                        JSONObject photos = (JSONObject) jsonObject.get("photos");
                        if(photos != null){
                            JSONArray photoArray = (JSONArray) photos.getJSONArray("photo");
                            if(photoArray != null){
                                for(int i=0; i<photoArray.length(); i++){
                                    try {
                                        FlickrPhoto photo = new FlickrPhoto();
                                        JSONObject photoJSONObject = photoArray.getJSONObject(i);
                                        photo.setId(photoJSONObject.getString("id"));
                                        photo.setOwner(photoJSONObject.getString("owner"));
                                        photo.setSecret(photoJSONObject.getString("secret"));
                                        photo.setServer(photoJSONObject.getString("server"));
                                        photo.setFarm(photoJSONObject.getInt("farm"));
                                        photo.setTitle(photoJSONObject.getString("title"));
                                        flickrPhotos.add(photo);
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
                logHelper.debug(Constants.LOGTAG, "Inside FlickrHelper handleResponseInBackground with flickrPhotos: " + flickrPhotos);
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
    public void handleResponseCompleteOnUiThread(String url, String response) {

        if(url.contains(FlickrMethod.SEARCH.getValue())){
            if(flickrPhotoReceiver != null){
                flickrPhotoReceiver.loadFlickrPhotos(flickrPhotos);
            }
        }

    }

    private String makeEndpoint(FlickrMethod flickrMethod, int page, int maxPerPage, String... tags) {
        StringBuilder stringBuilder = new StringBuilder(BASE_URL);
        stringBuilder.append("method=").append(flickrMethod.getValue());
        stringBuilder.append("&api_key=").append(API_KEY);
        stringBuilder.append("&format=").append(JSON);
        stringBuilder.append("&page=").append(String.valueOf(page));
        stringBuilder.append("&per_page=").append(String.valueOf(maxPerPage));


        String separator = "&tags=";
        for (String tag : tags) {
            stringBuilder.append(separator).append(tag);
            separator=",";
        }
        return stringBuilder.toString();
    }

    private JSONObject makeJsonObject(String response) {
        //prefix to the json: jsonFlickrApi(........)

        if(response != null && response.length() > 0){
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
