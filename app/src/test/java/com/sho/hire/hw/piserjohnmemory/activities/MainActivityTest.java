package com.sho.hire.hw.piserjohnmemory.activities;

import com.sho.hire.hw.piserjohnmemory.BuildConfig;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author John Piser johnpiser@yahoo.com
 */
@Config(constants = BuildConfig.class, sdk=21)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity mainActivity;


    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000004;
    public static final int GONE = 0x00000008;


    @Mock ConcentrationGame concentrationGameMock;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mainActivity = Robolectric.buildActivity(MainActivity.class).attach().create().start().resume().get();
        mainActivity.concentrationGame = concentrationGameMock;
    }


    @Test
    public void shouldCallInitOnStartOfGame(){

        mainActivity.startGame(null);

        verify(concentrationGameMock).init(any(ConcentrationGame.Host.class), anyString());
    }

    @Test
    public void shouldShowInternetConnectionMessageIfCellsDidNotLoad(){
        when(concentrationGameMock.getGameCells()).thenReturn(Collections.emptyList());

        mainActivity.displayConcentrationCells();

        assertEquals(VISIBLE, mainActivity.connectivityMessageTextView.getVisibility());
    }

    @Test
    public void shouldNOTShowInternetConnectionMessageIfCellsLoaded(){
        when(concentrationGameMock.getGameCells()).thenReturn(listOfGameCells(16));

        mainActivity.displayConcentrationCells();

        assertEquals(GONE, mainActivity.connectivityMessageTextView.getVisibility());
    }

    @Test
    public void shouldShowProgressBarAndHideGameRelatedViewsWhileLoading(){
        mainActivity.toggleLoading(true, false);

        assertEquals(VISIBLE, mainActivity.progressBar.getVisibility());

        assertEquals(GONE, mainActivity.buttonNewGame.getVisibility());
        assertEquals(INVISIBLE, mainActivity.gridView.getVisibility());
        assertEquals(INVISIBLE, mainActivity.attemptsValueTextView.getVisibility());
        assertEquals(INVISIBLE, mainActivity.attemptsLabelTextView.getVisibility());
        assertEquals(INVISIBLE, mainActivity.gamesolvedTextView.getVisibility());
        assertEquals(GONE, mainActivity.connectivityMessageTextView.getVisibility());
    }

    @Test
    public void shouldHideProgressBarAndShowGameViewsWhenLoaded(){

        mainActivity.toggleLoading(false, false);

        assertEquals(GONE, mainActivity.progressBar.getVisibility());
        assertEquals(VISIBLE, mainActivity.buttonNewGame.getVisibility());
        assertEquals(VISIBLE, mainActivity.gridView.getVisibility());

        assertEquals(INVISIBLE, mainActivity.attemptsValueTextView.getVisibility());
        assertEquals(INVISIBLE, mainActivity.attemptsLabelTextView.getVisibility());
        assertEquals(INVISIBLE, mainActivity.gamesolvedTextView.getVisibility());
    }

    @Test
    public void shouldShowGameSolvedWhenGameIsSolved(){

        when(concentrationGameMock.isSolved()).thenReturn(true);

        mainActivity.toggleLoading(false, false);

        assertEquals(VISIBLE, mainActivity.gamesolvedTextView.getVisibility());
        assertEquals(VISIBLE, mainActivity.attemptsValueTextView.getVisibility());
        assertEquals(VISIBLE, mainActivity.attemptsLabelTextView.getVisibility());

    }

    private List<ConcentrationCell> listOfGameCells(int size) {
        List<ConcentrationCell> results = new ArrayList<>();
        for (int i=0; i< size; i++){
            ConcentrationCell cell = new ConcentrationCell();
            cell.setId(String.valueOf(i));
            results.add(cell);
        }
        return results;
    }

}