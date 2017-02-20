package com.sho.hire.hw.piserjohnmemory.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCellReceiver;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import java.io.InputStream;
import java.util.List;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class ImageDownloadTask extends AsyncTask<String, Void, String> {
    private ConcentrationCellReceiver concentrationCellReceiver;
    private LogHelper logHelper;
    private List<ConcentrationCell> concentrationCells;

    public ImageDownloadTask(ConcentrationCellReceiver concentrationCellReceiver, LogHelper logHelper, List<ConcentrationCell> concentrationCells) {
        this.concentrationCellReceiver = concentrationCellReceiver;
        this.logHelper = logHelper;
        this.concentrationCells = concentrationCells;
    }

    protected String doInBackground(String... params) {

        logHelper.debug(Constants.LOGTAG, "[ImageDownloadTask] Downloading bitmap images in background thread ....");

        if(concentrationCells != null){
            for (ConcentrationCell concentrationCell : concentrationCells) {
                Bitmap bitmap = null;

                try {
                    InputStream in = new java.net.URL(concentrationCell.getUrl()).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    logHelper.error(Constants.LOGTAG, "!Error downloading image:" + e.getMessage());
                }
                concentrationCell.setImageBitmap(bitmap);
            }
        }
        return "success";

    }

    protected void onPostExecute(String result) {
        this.concentrationCellReceiver.displayConcentrationCells();
    }
}
