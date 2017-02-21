package com.sho.hire.hw.piserjohnmemory.concentration;

import java.util.List;
import java.util.Queue;

/**
 * @author John Piser johnpiser@yahoo.com
 */
public interface ConcentrationCellReceiver {
    void loadConcentrationCellQueue(Queue<ConcentrationCell> concentrationCellQueue);
    void loadGameCellsOnUiThread(List<ConcentrationCell> gameCells);
}
