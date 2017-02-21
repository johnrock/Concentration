package com.sho.hire.hw.piserjohnmemory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sho.hire.hw.piserjohnmemory.R;
import com.sho.hire.hw.piserjohnmemory.application.ConcentrationApplication;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationCell;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGame;
import com.sho.hire.hw.piserjohnmemory.flickr.FlickrHelper;
import com.sho.hire.hw.piserjohnmemory.helpers.LogHelper;
import com.sho.hire.hw.piserjohnmemory.util.Constants;
import com.sho.hire.hw.piserjohnmemory.concentration.ConcentrationGridViewImageAdapter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ConcentrationGame.Host {

    public static final String THEME_KITTEN = "kitten";
    public static final int MISMATCH_TIMEOUT = 3000;

    @Inject FlickrHelper flickrHelper;
    @Inject LogHelper logHelper;
    @Inject ConcentrationGame concentrationGame;

    GridView gridView;
    TextView attemptsValueTextView;
    TextView attemptsLabelTextView;
    TextView gamesolvedTextView;
    ProgressBar progressBar;
    Button buttonNewGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //In a production app Butterknife library would be used for all view binding.
        gridView = (GridView) findViewById(R.id.gridview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonNewGame = (Button) findViewById(R.id.buttonNewGame);
        attemptsValueTextView = (TextView) findViewById(R.id.attemptsValue);
        attemptsLabelTextView = (TextView) findViewById(R.id.attempts);
        gamesolvedTextView = (TextView) findViewById(R.id.gamesolved);

        //Dagger dependency injection
        ((ConcentrationApplication)getApplication()).getAppComponent().inject(this);

        initGridView();

        startGame(null);
    }

    private void initGridView() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int showingNonMatchedCount = 0;
                for (ConcentrationCell concentrationCell : concentrationGame.getGameCells()) {
                    if(!concentrationCell.isMatched() && (concentrationCell.isShowing())){
                        showingNonMatchedCount++;
                    }
                }
                if(showingNonMatchedCount < 2){
                    //Cell tapping is bypassed if there are already two non matched cells showing
                    concentrationGame.tapCell(position);
                }
            }
        });
    }

    public void startGame(View view) {

        toggleLoading(true);
        concentrationGame.init(this, THEME_KITTEN);
    }

    @Override
    public void displayConcentrationCells() {
        logHelper.debug(Constants.LOGTAG, "[MainActivity] displaying concentration cells...");
        ConcentrationGridViewImageAdapter gridViewImageAdapter = new ConcentrationGridViewImageAdapter(this, concentrationGame.getGameCells(), logHelper);
        gridView.setAdapter(gridViewImageAdapter);
        attemptsValueTextView.setText(String.valueOf(concentrationGame.getAttemptsValue()));
        toggleLoading(false);
    }

    @Override
    public void onTappedCell(int tapCount) {
        logHelper.debug(Constants.LOGTAG, "[MainActivity] Inside onTappedCell with tapCount: " + tapCount);
        displayConcentrationCells();

        if(tapCount ==2){
            pauseGameIfTappedCellsDoNotMatch();
        }
    }

    private void pauseGameIfTappedCellsDoNotMatch() {
        if(concentrationGame.containsMisMatchedCells()){

            gridView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    concentrationGame.resetMisMatchedCells();
                    displayConcentrationCells();
                }
            }, MISMATCH_TIMEOUT);

        }
    }

    private void toggleLoading(boolean loading) {
        if(loading){
            progressBar.setVisibility(View.VISIBLE);
            buttonNewGame.setVisibility(View.GONE);
            gridView.setVisibility(View.INVISIBLE);
            attemptsValueTextView.setVisibility(View.INVISIBLE);
            attemptsLabelTextView.setVisibility(View.INVISIBLE);
            gamesolvedTextView.setVisibility(View.INVISIBLE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            buttonNewGame.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
            attemptsValueTextView.setVisibility(View.VISIBLE);
            attemptsLabelTextView.setVisibility(View.VISIBLE);
        }
        if(concentrationGame.isSolved()){
            gamesolvedTextView.setVisibility(View.VISIBLE);
        }
    }
}
