package com.sho.hire.hw.piserjohnmemory.concentration;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;
import com.sho.hire.hw.piserjohnmemory.util.ConcentrationImageDownloadTask;

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

        void displayConcentrationCells();

        void onTappedCell(int position, int tapCount);
    }
    private static final int BATCH_SIZE = 80;
    private static final int GRID_SIZE = 16;

    public static  final int DEFAULT_GRID_ICON = R.drawable.cat_icon;
    LogHelper logHelper;
    ConcentrationCellProvider concentrationCellProvider;

    private String[] theme;
    private List<ConcentrationCell> concentrationCells; //Inventory of cells pre loaded from a ConcentrationCellProvider
    private List<ConcentrationCell> gameCells;          //Cells in the current game
    private int currentPage;
    private Host host;
    private int tapCount;
    @Inject
    public ConcentrationGame(LogHelper logHelper, ConcentrationCellProvider concentrationCellProvider) {
        this.logHelper = logHelper;
        this.concentrationCellProvider = concentrationCellProvider;
        currentPage = 1;
    }
    public List<ConcentrationCell> getGameCells() {
        return gameCells;
    }


    public void init(Host host, String... theme) {
        if(host == null){
            logHelper.error(Constants.LOGTAG, "!Aborting game dur to null host");
            return;
        }
        this.host = host;
        this.theme = theme;
        this.tapCount = 0;


        //Only make the call to get more cells if the current batch has been extinguished
        if(concentrationCells != null && concentrationCells.size() >= GRID_SIZE/2){
            loadBitmaps(fetchNextCells());
        }
        else{
            concentrationCellProvider.getConcentrationCells(this, currentPage++, BATCH_SIZE, theme);
        }
    }


    /**
     * Callback to Receive a new batch of ConcentrationCells from the ConcentrationCellProvider
     */
    @Override
    public void loadConcentrationCells(List<ConcentrationCell> concentrationCells) {
        this.concentrationCells = concentrationCells;
        logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] Loading concentration cells: " + concentrationCells);

        loadBitmaps(fetchNextCells()); //must be called before fetchNextCells
    }

    @Override
    public void loadGameCells(List<ConcentrationCell> gameCells) {
        this.gameCells = gameCells;

        prepareGameCells(gameCells);

        logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] Displaying game concentration cells on host");
        host.displayConcentrationCells();
    }

    /**
     * Called when a user taps on a cell
     * @param position of the game cell tapped
     */
    public void tapCell(int position) {
        tapCount++;
        ConcentrationCell concentrationCell = gameCells.get(position);
        if(concentrationCell.isMatched()){
            return;
        }
        concentrationCell.setShowing(true);
        ConcentrationCell duplicate = concentrationCell.getDuplicate();
        if(duplicate.isShowing()){
            concentrationCell.setMatched(true);
            duplicate.setMatched(true);
        }

        host.onTappedCell(position, tapCount);

        tapCount = tapCount == 2 ? 0 : tapCount;
    }


    /**
     * Final method before returning the game cells to the view:
     * - duplicate each cell, add to the collection, and shuffle
     */
    private void prepareGameCells(List<ConcentrationCell> gameCells) {
        List<ConcentrationCell> duplicates = new ArrayList<>();
        for (ConcentrationCell gameCell : gameCells) {
            duplicates.add(gameCell.duplicate());
        }
        gameCells.addAll(duplicates);
        Collections.shuffle(gameCells);
    }

    /**
     * Download the images. Must be called each time a new batch of images is returned
     * @param gameCells
     */
    private void loadBitmaps(List<ConcentrationCell> gameCells) {
        if(this.concentrationCells != null){
            new ConcentrationImageDownloadTask(this, logHelper,gameCells).execute();
        }
    }

    private List<ConcentrationCell> fetchNextCells() {
        List<ConcentrationCell> results = new ArrayList<>();

        if(concentrationCells != null && concentrationCells.size() >= GRID_SIZE/2){
            Iterator<ConcentrationCell> iter = concentrationCells.iterator();
            for(int i =0; i<GRID_SIZE/2; i++){
                ConcentrationCell concentrationCell = iter.next();
                results.add(concentrationCell);
                iter.remove();
            }

            logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] fetching concentration cells: " + results);
            return results;
        }

        return null;
    }

    public boolean containsMisMatchedCells() {
        for (ConcentrationCell gameCell : gameCells) {
            if(!gameCell.isMatched() && (gameCell.isShowing() && !gameCell.getDuplicate().isShowing())){
                return true;
            }
        }
        return false;
    }

    public void resetMisMatchedCells() {
        for (ConcentrationCell gameCell : gameCells) {
            if(!gameCell.isMatched() && (gameCell.isShowing() && !gameCell.getDuplicate().isShowing())){
                gameCell.setShowing(false);
            }
        }
    }
}
