package com.excilys.formation.battleships.android.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import battleships.formation.excilys.com.battleships.R;

public class PlayerNameActivity extends AppCompatActivity {
    EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);
        mNameEditText = (EditText) findViewById(R.id.text_name);

    }

    public void onClickButton(View v) {
        String name = mNameEditText.getText().toString();
        if (!name.isEmpty()) {
            Toast.makeText(PlayerNameActivity.this, name, Toast.LENGTH_LONG).show();
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);
            preferences.edit().putString("PlayerName", name).apply();
            if(preferences.getString("Playername", name) != null) {
                BattleShipsApplication.getGame().init(name);
            }
        }
        else Toast.makeText(PlayerNameActivity.this, "Entrez votre nom", Toast.LENGTH_LONG).show();
    }

}
