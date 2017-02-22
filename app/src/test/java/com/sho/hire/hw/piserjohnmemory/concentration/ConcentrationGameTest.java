package com.sho.hire.hw.piserjohnmemory.concentration;

import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGame.GRID_SIZE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author John Piser johnpiser@yahoo.com
 */
public class ConcentrationGameTest {

    public static final String TAGS = "kitten";

    @Mock LogHelper logHelper;
    @Mock ConcentrationCellProvider concentrationCellProvider;
    @Mock ConcentrationGame.Host host;
    @Mock ConcentrationDownloadHelper concentrationDownloadHelper;

    ConcentrationGame concentrationGame;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        concentrationGame = new ConcentrationGame(logHelper, concentrationCellProvider, concentrationDownloadHelper);

    }

    @Test
    public void shouldInitStateDuringInit(){
        concentrationGame.init(host,  TAGS);

        assertEquals(0, concentrationGame.getTapCount());
        assertEquals(0, concentrationGame.getAttemptsValue());
        assertEquals(host, concentrationGame.getHost());
        String[] theme = concentrationGame.getTheme();
        assertEquals(TAGS, theme[0]);
    }

    @Test
    public void shouldGetConcentrationCellsDuringInitIfNeeded(){
        concentrationGame.init(host,  TAGS);

        verifyZeroInteractions(concentrationDownloadHelper);
        verify(concentrationCellProvider).getConcentrationCells(concentrationGame, 1, ConcentrationGame.BATCH_SIZE, new String[]{TAGS});
    }

    @Test
    public void shouldDownloadImagesAndNotGetConcentrationCellsDuringInitIfInventoryExists(){
        concentrationGame.concentrationCellQueue  = newQueueWith(GRID_SIZE);
        concentrationGame.init(host,  TAGS);

        verifyZeroInteractions(concentrationCellProvider);
        verify(concentrationDownloadHelper).downloadImages(any(ConcentrationCellReceiver.class), ArgumentMatchers.<ConcentrationCell>anyList());
    }

    @Test
    public void shouldDownloadImagesAfterLoadingConcentrationCellQueue(){
        concentrationGame.loadConcentrationCellQueue(newQueueWith(20));

        verify(concentrationDownloadHelper).downloadImages(any(ConcentrationCellReceiver.class), ArgumentMatchers.<ConcentrationCell>anyList());
    }

    @Test
    public void shouldLoadGameCellsOnHostAfterPreparation(){
        concentrationGame.init(host,  TAGS);
        concentrationGame.loadGameCellsOnUiThread(gameCellsWith(8));

        assertGameCellsHaveBeenPrepared(concentrationGame.getGameCells());

        verify(host).displayConcentrationCells();
    }

    @Test
    public void shouldPrepareGameCells(){
        concentrationGame.init(host,  TAGS);
        List<ConcentrationCell> gameCells = gameCellsWith(8);
        concentrationGame.prepareGameCells(gameCells);

        assertGameCellsHaveBeenPrepared(gameCells);
    }

    @Test
    public void shouldFetchNextCellsAndReduceIncventory(){
        concentrationGame.init(host,  TAGS);
        concentrationGame.concentrationCellQueue  = newQueueWith(ConcentrationGame.BATCH_SIZE);

        List<ConcentrationCell> nextCells = concentrationGame.fetchNextCells();
        assertEquals(GRID_SIZE/2, nextCells.size());

        assertEquals(ConcentrationGame.BATCH_SIZE - GRID_SIZE/2, concentrationGame.concentrationCellQueue.size());
    }

    @Test
    public void shouldReturnFalseIfGameCellsDoNotContainMisMatch(){
        concentrationGame.init(host,  TAGS);
        concentrationGame.loadGameCellsOnUiThread(gameCellsWith(8));

        boolean result = concentrationGame.containsMisMatchedCells();
        assertEquals(false, result);
    }

    @Test
    public void shouldReturnTrueIfGameCellsContainMisMatch(){
        concentrationGame.init(host,  TAGS);
        concentrationGame.loadGameCellsOnUiThread(gameCellsWith(8));
        List<ConcentrationCell> gameCells = concentrationGame.getGameCells();
        createMisMatchedCellAndMatchedCell(gameCells);
        boolean result = concentrationGame.containsMisMatchedCells();
        assertEquals(true, result);
    }

    @Test
    public void shouldResetMisMatchedCells(){
        concentrationGame.init(host,  TAGS);
        concentrationGame.loadGameCellsOnUiThread(gameCellsWith(8));
        List<ConcentrationCell> gameCells = concentrationGame.getGameCells();
        createMisMatchedCellAndMatchedCell(gameCells);

        concentrationGame.resetMisMatchedCells();

        for (ConcentrationCell cell : concentrationGame.getGameCells()) {
            if(!cell.isMatched()){
                assertFalse(cell.isShowing());
            }
            else{
                assertTrue(cell.isShowing());
            }
        }
    }

    @Test
    public void shouldIndicatePuzzleIsSolvedWhenAllCellsAreMatched(){
        concentrationGame.init(host,  TAGS);
        concentrationGame.loadGameCellsOnUiThread(gameCellsWith(8));
        List<ConcentrationCell> gameCells = concentrationGame.getGameCells();

        boolean solved = concentrationGame.isSolved();
        assertFalse(solved);

        markAllCellsMatched(gameCells);

        solved = concentrationGame.isSolved();
        assertTrue(solved);
    }

    private void markAllCellsMatched(List<ConcentrationCell> gameCells) {
        for (ConcentrationCell gameCell : gameCells) {
            gameCell.setMatched(true);
        }
    }

    private void createMisMatchedCellAndMatchedCell(List<ConcentrationCell> gameCells) {
        for (ConcentrationCell gameCell : gameCells) {
            if(gameCell.getId().equals("2")){
                gameCell.setShowing(true);
                gameCell.getDuplicate().setShowing(false);
            }
            // add a matched cell
            if(gameCell.getId().equals("5")){
                gameCell.setShowing(true);
                gameCell.setMatched(true);
                gameCell.getDuplicate().setShowing(true);
                gameCell.getDuplicate().setMatched(true);
            }

        }
    }

    private void assertGameCellsHaveBeenPrepared(List<ConcentrationCell> gameCells) {
        assertDuplicatesWereAdded(gameCells.size());
        assertGameCellsWereShuffled(gameCells);
    }

    private void assertDuplicatesWereAdded(int size) {
        assertEquals(GRID_SIZE, size);
    }

    private void assertGameCellsWereShuffled(List<ConcentrationCell> gameCells) {
        boolean shuffled = false;
        for(int i=0; i<gameCells.size(); i++){
            ConcentrationCell cell = gameCells.get(i);

            int halfGridSize = GRID_SIZE / 2;
            if(i< halfGridSize){
                if(Integer.parseInt(cell.getId()) != i){
                    shuffled = true;
                }
            }
            else{
                if(Integer.parseInt(cell.getId()) != i - halfGridSize){
                    shuffled = true;
                }
            }

        }
        assertEquals(true, shuffled);
    }

    //Dummy Data generators for testing

    private Queue<ConcentrationCell> newQueueWith(int size) {
        Queue<ConcentrationCell> queue = new ArrayDeque<>();
        for (int i=0; i< size; i++){
            ConcentrationCell cell = new ConcentrationCell();
            cell.setId(String.valueOf(i));
            queue.add(cell);
        }
        return queue;
    }

    private List<ConcentrationCell> gameCellsWith(int size){
        Queue<ConcentrationCell> concentrationCellQueue = newQueueWith(size);
        List<ConcentrationCell> concentrationCells = new ArrayList<>();
        while(!concentrationCellQueue.isEmpty()){
            concentrationCells.add(concentrationCellQueue.poll());
        }
        return concentrationCells;
    }



}