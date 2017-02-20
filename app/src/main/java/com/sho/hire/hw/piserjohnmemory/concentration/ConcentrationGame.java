package com.sho.hire.hw.piserjohnmemory.concentration;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;
import com.sho.hire.hw.piserjohnmemory.util.ImageDownloadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * This class is responsible for handling all logic and state
 * required for playing a game of concentration
 */

public class ConcentrationGame implements ConcentrationCellReceiver{

    public interface Host{
        void displayConcentrationCells(List<ConcentrationCell> concentrationCells);

    }

    private static final int BATCH_SIZE = 80;
    private static final int GRID_SIZE = 16;
    public static  final int DEFAULT_GRID_ICON = R.drawable.cat_icon;

    LogHelper logHelper;
    ConcentrationCellProvider concentrationCellProvider;
    private String[] theme;
    private List<ConcentrationCell> concentrationCells;
    private int currentPage;
    private Host host;


    @Inject
    public ConcentrationGame(LogHelper logHelper, ConcentrationCellProvider concentrationCellProvider) {
        this.logHelper = logHelper;
        this.concentrationCellProvider = concentrationCellProvider;
    }


    public void init(Host host, String... theme) {
        if(host == null){
            logHelper.error(Constants.LOGTAG, "!Aborting game dur to null host");
            return;
        }
        this.host = host;
        this.theme = theme;
        currentPage = 0;
        concentrationCellProvider.getConcentrationCells(this, currentPage++, BATCH_SIZE, theme);

    }

    @Override
    public void loadConcentrationCells(List<ConcentrationCell> concentrationCells) {
        this.concentrationCells = concentrationCells;
        logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] Loading concentration cells: " + concentrationCells);

        loadBitmaps(); //must be called before fetchNextCells
    }

    @Override
    public void displayConcentrationCells() {

        logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] Displaying concentration cells on host");
        host.displayConcentrationCells(fetchNextCells());

    }

    /**
     * Download the images. Must be called each time a new batch of images is returned
     */
    private void loadBitmaps() {
        if(concentrationCells != null){
            new ImageDownloadTask(this, logHelper, concentrationCells).execute();
        }
    }

    private List<ConcentrationCell> fetchNextCells() {
        List<ConcentrationCell> results = new ArrayList<>();

        if(concentrationCells != null && concentrationCells.size() >= GRID_SIZE/2){
            Iterator<ConcentrationCell> iter = concentrationCells.iterator();
            for(int i =0; i<GRID_SIZE/2; i++){
                ConcentrationCell concentrationCell = iter.next();
                results.add(concentrationCell);
                results.add(concentrationCell.duplicate());
                iter.remove();
            }

            Collections.shuffle(results);

            logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] fetching concentration cells: " + results);
            return results;
        }

        return null;
    }
}
