package com.excilys.formation.battleships.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
            Toast.makeText(PlayerNameActivity.this, name, Toast.LENGTH_LONG).show();
            preferences.edit().putString("PlayerName", name).apply();
            Log.d("PlayerName", preferences.getString("PlayeName", name));
            BattleShipsApplication.getGame().init(name);

        }
        else Toast.makeText(PlayerNameActivity.this, "Entrez votre nom", Toast.LENGTH_LONG).show();
    }

}
