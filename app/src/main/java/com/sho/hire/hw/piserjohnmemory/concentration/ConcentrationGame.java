package com.sho.hire.hw.piserjohnmemory.concentration;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

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
        void onTappedCell(int tapCount);
    }
    public static final int MISMATCH_TIMEOUT = 1000;
    public static  final int DEFAULT_GRID_ICON = R.drawable.cat_icon;
    public static final int BATCH_SIZE = 80;
    public static final int GRID_SIZE = 16;

    //Package level access to facilitate ease of testing
    LogHelper logHelper;
    ConcentrationCellProvider concentrationCellProvider;
    ConcentrationDownloadHelper concentrationDownloadHelper;
    Queue<ConcentrationCell> concentrationCellQueue; //Inventory of cells pre loaded from a ConcentrationCellProvider

    private String[] theme;
    private List<ConcentrationCell> gameCells;          //Cells in the current game
    private int currentPage;
    private Host host;
    private int tapCount;
    private int attemptsValue;

    @Inject
    public ConcentrationGame(LogHelper logHelper, ConcentrationCellProvider concentrationCellProvider, ConcentrationDownloadHelper concentrationDownloadHelper) {
        this.logHelper = logHelper;
        this.concentrationCellProvider = concentrationCellProvider;
        this.concentrationDownloadHelper = concentrationDownloadHelper;
        currentPage = 1;
    }
    public List<ConcentrationCell> getGameCells() {
        return gameCells;
    }

    public int getAttemptsValue() {
        return attemptsValue;
    }

    public void init(Host host, String... theme) {
        if(host == null){
            logHelper.error(Constants.LOGTAG, "!Aborting game dur to null host");
            return;
        }
        this.host = host;
        this.theme = theme;
        this.tapCount = 0;
        this.attemptsValue = 0;

        if(concentrationCellQueue != null && concentrationCellQueue.size() >= GRID_SIZE/2){
            //download the next set of images
            concentrationDownloadHelper.downloadImages(this, fetchNextCells());
        }
        else{
            //Only make the call to get more cells if the current batch has been extinguished
            concentrationCellProvider.getConcentrationCells(this, currentPage++, BATCH_SIZE, theme);
        }
    }


    /**
     * Callback to Receive a new batch of ConcentrationCells from the ConcentrationCellProvider
     * @param concentrationCellQueue
     */
    @Override
    public void loadConcentrationCellQueue(Queue<ConcentrationCell> concentrationCellQueue) {
        this.concentrationCellQueue = concentrationCellQueue;
        //Once the next batch of cells has loaded: dowload the next set of images
        concentrationDownloadHelper.downloadImages(this, fetchNextCells());
    }

    /**
     * Callback to load a new game on the host after images have been downloaded
     */
    @Override
    public void loadGameCellsOnUiThread(List<ConcentrationCell> gameCells) {
        this.gameCells = gameCells;
        prepareGameCells(gameCells);
        host.displayConcentrationCells();
    }

    public void tapCell(int position) {
        ConcentrationCell concentrationCell = gameCells.get(position);
        if(concentrationCell.isShowing()){
            return;
        }
        tapCount++;

        concentrationCell.setShowing(true);
        ConcentrationCell duplicate = concentrationCell.getDuplicate();
        if(duplicate.isShowing()){
            concentrationCell.setMatched(true);
            duplicate.setMatched(true);
        }

        attemptsValue = tapCount == 2 ? ++attemptsValue : attemptsValue;

        host.onTappedCell(tapCount);

        tapCount = tapCount == 2 ? 0 : tapCount;
    }


    /**
     * Final method before returning the game cells to the view:
     * - duplicate each cell, add to the collection, and shuffle
     *
     * protected access for testability
     */
    protected void prepareGameCells(List<ConcentrationCell> gameCells) {
        if(gameCells != null){
            List<ConcentrationCell> duplicates = new ArrayList<>();
            for (ConcentrationCell gameCell : gameCells) {
                duplicates.add(gameCell.duplicate());
            }
            gameCells.addAll(duplicates);
            Collections.shuffle(gameCells);
        }
    }

    //protected access for testability
    protected List<ConcentrationCell> fetchNextCells() {
        List<ConcentrationCell> results = new ArrayList<>();

        if(concentrationCellQueue != null && concentrationCellQueue.size() >= GRID_SIZE/2){
            for(int i =0; i<GRID_SIZE/2; i++){
                results.add(concentrationCellQueue.poll());
            }
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

    public boolean isSolved() {
        if(gameCells == null || gameCells.isEmpty()){
            return false;
        }

        int solvedCount = 0;
        for (ConcentrationCell gameCell : gameCells) {
            if(gameCell.isMatched()){
                solvedCount++;
            }
        }
        return solvedCount == gameCells.size();
    }

    public int getTapCount() {
        return tapCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String[] getTheme() {
        return theme;
    }

    public Host getHost() {
        return host;
    }
}
