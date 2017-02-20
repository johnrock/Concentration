package com.sho.hire.hw.piserjohnmemory.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;

import java.util.List;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class GridViewImageAdapter extends BaseAdapter{

    private Context context;
    private List<ConcentrationCell> concentrationCells;
    private LogHelper logHelper;

    public GridViewImageAdapter(Context context, List<ConcentrationCell> concentrationCells, LogHelper logHelper) {
        this.context = context;
        this.concentrationCells = concentrationCells;
        this.logHelper = logHelper;
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
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        new ImageDownloadTask(imageView, logHelper).execute(((ConcentrationCell)getItem(position)).getUrl());

        return imageView;

    }
}
