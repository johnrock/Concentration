package com.sho.hire.hw.piserjohnmemory.concentration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sho.hire.hw.piserjohnmemory.helpers.DeviceHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import java.util.List;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class ConcentrationGridViewImageAdapter extends BaseAdapter{

    private Context context;
    private List<ConcentrationCell> concentrationCells;
    private LogHelper logHelper;
    private DeviceHelper deviceHelper;

    public ConcentrationGridViewImageAdapter(Context context, List<ConcentrationCell> concentrationCells, LogHelper logHelper, DeviceHelper deviceHelper) {
        this.context = context;
        this.concentrationCells = concentrationCells;
        this.logHelper = logHelper;
        this.deviceHelper = deviceHelper;
    }

    @Override
    public int getCount() {
        return concentrationCells.size();
    }

    @Override
    public Object getItem(int position) {
        return concentrationCells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);

            int useableWidthInDp = deviceHelper.useableWidthInDp(context);
            int cellWidth = (useableWidthInDp / 4) - 15;

            imageView.setLayoutParams(new GridView.LayoutParams(
                    Math.round(deviceHelper.convertDpToPx(context,cellWidth)),
                    Math.round(deviceHelper.convertDpToPx(context,cellWidth))));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        ConcentrationCell concentrationCell = (ConcentrationCell) getItem(position);

        if(concentrationCell.isShowing()){
            imageView.setImageBitmap(concentrationCell.getImageBitmap());
        }
        else{
            imageView.setImageResource(concentrationCell.getDefaultResourceId());
        }

        return imageView;

    }

}
