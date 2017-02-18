package com.sho.hire.hw.piserjohnmemory.helpers;

import android.util.Log;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class LogHelper {

    private final boolean DEBUG_MODE;

    public LogHelper(boolean debugMode) {
        this.DEBUG_MODE = debugMode;
    }

    public void log(int loglevel, String logtag, String message) {
        if(DEBUG_MODE){
            switch (loglevel){
                case Log.DEBUG:
                    Log.d(logtag, message);
                    break;
                case Log.ERROR:
                    Log.e(logtag, message);
                    break;
                case Log.INFO:
                    Log.i(logtag, message);
                    break;
                case Log.VERBOSE:
                    Log.v(logtag,message);
            }
        }
    }

    public void debug(String logtag, String message){
        log(Log.DEBUG, logtag, message);
    }
    public void error(String logtag, String message){
        log(Log.ERROR, logtag, message);
    }
    public void info(String logtag, String message){
        log(Log.INFO, logtag, message);
    }
    public void verbose(String logtag, String message){
        log(Log.VERBOSE, logtag, message);
    }
    public boolean debugMode() {
        return DEBUG_MODE;
    }

}
