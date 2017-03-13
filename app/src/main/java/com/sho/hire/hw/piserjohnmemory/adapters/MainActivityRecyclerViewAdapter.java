package com.sho.hire.hw.piserjohnmemory.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;

import java.util.List;

/**
 * @author John Piser johnpiser@yahoo.com
 */

public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.ImageHolder> {

    public interface ConcentrationGameTapper{
        void tapCell(int position);
        List<ConcentrationCell> getGameCells();
    }

    private List<ConcentrationCell> concentrationCells;
    private ConcentrationGameTapper concentrationGameTapper;

    public static class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private ConcentrationGameTapper tapper;
        private int position;

        public ImageHolder(View itemView, ConcentrationGameTapper tapper) {
            super(itemView);
            this.tapper = tapper;
            imageView = (ImageView) itemView.findViewById(R.id.gridImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int showingNonMatchedCount = 0;
            for (ConcentrationCell concentrationCell : tapper.getGameCells()) {
                if(!concentrationCell.isMatched() && (concentrationCell.isShowing())){
                    showingNonMatchedCount++;
                }
            }
            if(showingNonMatchedCount < 2){
                //Cell tapping is bypassed if there are already two non matched cells showing
                //concentrationGame.tapCell(position);
                tapper.tapCell(position);

            }

        }

        public void bindImage(ConcentrationCell concentrationCell, int position) {
            this.position = position;

            /*int useableWidthInDp = deviceHelper.useableWidthInDp(context);
            int cellWidth = (useableWidthInDp / 4) - 15;

            imageView.setLayoutParams(new RecyclerView.LayoutParams(
                    Math.round(deviceHelper.convertDpToPx(context,cellWidth)),
                    Math.round(deviceHelper.convertDpToPx(context,cellWidth))));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);*/

            if(concentrationCell.isShowing()){
                imageView.setImageBitmap(concentrationCell.getImageBitmap());
            }
            else{
                imageView.setImageResource(concentrationCell.getDefaultResourceId());
            }
        }
    }

    public MainActivityRecyclerViewAdapter(List<ConcentrationCell> concentrationCells, ConcentrationGameTapper concentrationGameTapper) {
        this.concentrationCells = concentrationCells;
        this.concentrationGameTapper = concentrationGameTapper;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_concentration_item, parent, false);
        return new ImageHolder(view, concentrationGameTapper);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

        ConcentrationCell concentrationCell = concentrationCells.get(position);
        holder.bindImage(concentrationCell, position);
    }

    @Override
    public int getItemCount() {
        return concentrationCells.size();
    }
}
