package com.sho.hire.hw.piserjohnmemory.concentration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;

import java.io.InputStream;
import java.util.List;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Task to download bitmaps for the current batch of concentration cells
 */

public class ConcentrationImageDownloadTask extends AsyncTask<String, Void, String> {
    private ConcentrationCellReceiver concentrationCellReceiver;
    private LogHelper logHelper;
    private List<ConcentrationCell> concentrationCells;

    public ConcentrationImageDownloadTask(ConcentrationCellReceiver concentrationCellReceiver, LogHelper logHelper, List<ConcentrationCell> concentrationCells) {
        this.concentrationCellReceiver = concentrationCellReceiver;
        this.logHelper = logHelper;
        this.concentrationCells = concentrationCells;
    }

    protected String doInBackground(String... params) {

        logHelper.debug(Constants.LOGTAG, "[ConcentrationImageDownloadTask] Downloading bitmap images in background thread ....");

        int count = 1;
        if(concentrationCells != null && !concentrationCells.isEmpty()){
            for (ConcentrationCell concentrationCell : concentrationCells) {
                Bitmap bitmap = null;
                logHelper.debug(Constants.LOGTAG, "...loading bitmap: " + count++);
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
        this.concentrationCellReceiver.loadGameCellsOnUiThread(concentrationCells);
    }
}
