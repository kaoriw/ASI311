package com.excilys.formation.battleships.android.ui;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import battleships.formation.excilys.com.battleships.R;

public class PlayerNameActivity extends AppCompatActivity {
    EditText mNameEditText;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);
        String savedName = preferences.getString("PlayerName", ""); //Voir si un nom est stocké dans Playername
        if(!savedName.equals("")) { //si le nom existe déjà
            BattleShipsApplication.getGame().init(savedName);  //commencer directement le jeu
        }
        else {
            setContentView(R.layout.activity_player_name);
            mNameEditText = (EditText) findViewById(R.id.text_name);
            String name = mNameEditText.getText().toString();
        }
    }

    public void onClickButton(View v) {
        String name = mNameEditText.getText().toString();
        if (!name.isEmpty()) {
            Snackbar.make(findViewById(R.id.main_name), name, Snackbar.LENGTH_SHORT);
            preferences.edit().putString("PlayerName", name).apply();
            BattleShipsApplication.getGame().init(name);

        }
        else {
            Snackbar.make(findViewById(R.id.main_name), "Entrez votre nom", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onBackPressed(){

    }

}
