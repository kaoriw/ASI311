package com.excilys.formation.battleships.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import battleships.formation.excilys.com.battleships.R;

public class ScoreActivity extends AppCompatActivity {


    public static class Extra {
        public static String WIN = "EXTRA_WIN";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        boolean win = getIntent().getExtras().getBoolean(Extra.WIN);
        TextView winLabel = (TextView) findViewById(R.id.score_win_label);
        TextView loseLabel = (TextView) findViewById(R.id.score_lose_label);

        int winVisible = View.VISIBLE, loseVisible = View.VISIBLE;
        if (win) {
            loseVisible = View.GONE;
        } else {
            winVisible = View.GONE;
        }

        winLabel.setVisibility(winVisible);
        loseLabel.setVisibility(loseVisible);

    }

    @Override
    public void onBackPressed(){

    }

}
