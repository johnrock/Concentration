package com.sho.hire.hw.piserjohnmemory.concentration;

import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import java.util.List;

import javax.inject.Inject;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Helper class to handle downloads to keep AsyncTasks decoupled from the ConcentrationGame
 */

public class ConcentrationDownloadHelper {

    private LogHelper logHelper;

    @Inject
    public ConcentrationDownloadHelper(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

    public void downloadImages(ConcentrationCellReceiver concentrationCellReceiver , List<ConcentrationCell> concentrationCells){
        if(concentrationCells != null && !concentrationCells.isEmpty()){

            new ConcentrationImageDownloadTask(concentrationCellReceiver, logHelper, concentrationCells).execute();
        }
    }
}
