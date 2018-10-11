package com.excilys.formation.battleships.android.ui;

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
        }
    }

}
