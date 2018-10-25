package com.excilys.formation.battleships.android.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import battleships.formation.excilys.com.battleships.R;
import battleships.ship.BattleShip;

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
        ImageView winImage = (ImageView) findViewById(R.id.score_win_image);
        ImageView loseImage = (ImageView) findViewById(R.id.score_lose_image);

        int winVisible = View.VISIBLE, loseVisible = View.VISIBLE;
        if (win) {
            loseVisible = View.GONE;
        } else {
            winVisible = View.GONE;
        }

        winLabel.setVisibility(winVisible);
        winImage.setVisibility(winVisible);
        loseLabel.setVisibility(loseVisible);
        loseImage.setVisibility(loseVisible);
    }


    @Override
    public void onBackPressed(){

    }

    public void openDialogToRestart() {

        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);

    // TODO Changer l'activity
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScoreActivity.this);

    //TODO Ajouter un message à la dialog et l'empêcher qu'elle soit fermée sans cliquer sur un bouton.
    //TODO Utiliser pour cela .setCancelable(boolean) sur le builder.
        alertDialogBuilder.setMessage("Voulez-vous recommencer le jeu ?");
        alertDialogBuilder.setCancelable(false);
        DialogInterface.OnClickListener onClickDialogListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:

                        String name = preferences.getString("PlayerName", "");
                        BattleShipsApplication.getGame().init(name);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.cancel();
                        break;
                }
            }
        };

        alertDialogBuilder.setPositiveButton("Oui", onClickDialogListener);

        alertDialogBuilder.setNegativeButton("Non", onClickDialogListener);


        // Créer l' Alert dialog en utilisant la méthode .create() sur le builder.
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Afficher l'Alert dialog en appelant la méthode .show() sur celle-ci.
        alertDialog.show();
    }

    public void onClickRestart(View v){
        openDialogToRestart();
    }


}
