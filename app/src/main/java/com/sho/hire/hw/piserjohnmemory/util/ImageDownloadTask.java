package com.sho.hire.hw.piserjohnmemory.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import java.io.InputStream;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private LogHelper logHelper;

    public ImageDownloadTask(ImageView imageView, LogHelper logHelper) {
        this.imageView = imageView;
        this.logHelper = logHelper;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            logHelper.error(Constants.LOGTAG, "!Error downloading image:" + e.getMessage());
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
