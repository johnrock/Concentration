package com.sho.hire.hw.piserjohnmemory.concentration;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Generic interface that decouples ConcentrationGame from any particular photo source.
 *
 */

public interface ConcentrationCellProvider {

    void getConcentrationCells(ConcentrationCellReceiver concentrationCellReceiver, int page, int batchSize, String[] tags);
}
