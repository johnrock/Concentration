package com.sho.hire.hw.piserjohnmemory.concentration;

import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;

import java.util.List;

import javax.inject.Inject;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * This class is responsible for handling all logic and state
 * required for playing a game of concentration
 */

public class ConcentrationGame implements ConcentrationCellReceiver{

    private static final int BATCH_SIZE = 80;

    LogHelper logHelper;
    ConcentrationCellProvider concentrationCellProvider;
    private String[] theme;
    private List<ConcentrationCell> concentrationCells;
    private int currentPage;


    @Inject
    public ConcentrationGame(LogHelper logHelper, ConcentrationCellProvider concentrationCellProvider) {
        this.logHelper = logHelper;
        this.concentrationCellProvider = concentrationCellProvider;
    }


    public void init(String... theme) {
        this.theme = theme;
        currentPage = 0;
        concentrationCellProvider.getConcentrationCells(this, currentPage++, BATCH_SIZE, theme);

    }

    @Override
    public void loadConcentrationCells(List<ConcentrationCell> concentrationCells) {
        this.concentrationCells = concentrationCells;
        logHelper.debug(Constants.LOGTAG, "[ConcentrationGame] Loading concentration cells: " + concentrationCells);

    }
}
