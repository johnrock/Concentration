package com.sho.hire.hw.piserjohnmemory.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Piser johnpiser@yahoo.com
 */

@Singleton
public class DeviceHelper {

    @Inject
    public DeviceHelper() {
    }

    public int useableWidthInDp(Context context){
        if (context == null) return -1;
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.screenWidthDp;
    }

    public int useableWidthInPixels(Context context){
        if (context == null) return -1;
        int widthInDp = useableWidthInDp(context);
        return (int) convertDpToPx(context, (float) widthInDp);

    }

    public float convertDpToPx(Context context, float dp) {
        if (context == null) return -1;
        Resources resource = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }

    public float convertPxToDp(Context context, float px) {
        if (context == null) return -1;
        Resources resource = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, resource.getDisplayMetrics());
    }
}
