package com.sho.hire.hw.piserjohnmemory.concentration;

import android.support.annotation.NonNull;

import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayDeque;

import static junit.framework.Assert.assertEquals;
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

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInitStateDuringInit(){
        ConcentrationGame concentrationGame = new ConcentrationGame(logHelper, concentrationCellProvider, concentrationDownloadHelper);
        concentrationGame.init(host,  TAGS);

        assertEquals(0, concentrationGame.getTapCount());
        assertEquals(0, concentrationGame.getAttemptsValue());
        assertEquals(host, concentrationGame.getHost());
        String[] theme = concentrationGame.getTheme();
        assertEquals(TAGS, theme[0]);
    }

    @Test
    public void shouldGetConcentrationCellsDuringInitIfNeeded(){
        ConcentrationGame concentrationGame = new ConcentrationGame(logHelper, concentrationCellProvider, concentrationDownloadHelper);
        concentrationGame.init(host,  TAGS);

        verifyZeroInteractions(concentrationDownloadHelper);
        verify(concentrationCellProvider).getConcentrationCells(concentrationGame, 1, ConcentrationGame.BATCH_SIZE, new String[]{TAGS});
    }

    @Test
    public void shouldDownloadImagesAndNotGetConcentrationCellsDuringInitIfInventoryExists(){
        ConcentrationGame concentrationGame = new ConcentrationGame(logHelper, concentrationCellProvider, concentrationDownloadHelper);
        concentrationGame.concentrationCellQueue  = queueOfSize(ConcentrationGame.GRID_SIZE);

        concentrationGame.init(host,  TAGS);

        verifyZeroInteractions(concentrationCellProvider);
        verify(concentrationDownloadHelper).downloadImages(any(ConcentrationCellReceiver.class), ArgumentMatchers.<ConcentrationCell>anyList());
    }

    @NonNull
    private ArrayDeque<ConcentrationCell> queueOfSize(int size) {
        ArrayDeque<ConcentrationCell> cells = new ArrayDeque<>();
        for (int i = 0; i<size; i++){
            ConcentrationCell cell = new ConcentrationCell();
            cell.setId(String.valueOf(i));
            cells.add(cell);
        }
        return cells;
    }


}