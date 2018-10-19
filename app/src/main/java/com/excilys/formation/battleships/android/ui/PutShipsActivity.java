package com.excilys.formation.battleships.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import battleships.formation.excilys.com.battleships.R;
import battleships.ship.AbstractShip;

import static java.lang.Thread.sleep;

public class PutShipsActivity extends AppCompatActivity implements BoardGridFragment.BoardGridFragmentListener {
    private static final String TAG = PutShipsActivity.class.getSimpleName();

    /* ***
     * Widgets
     */
    private RadioGroup mOrientationRadioGroup;
    private RadioButton mNorthRadio;
    private RadioButton mSouthRadio;
    private RadioButton mEastRadio;
    private RadioButton mWestRadio;
    private TextView mShipName;

    /* ***
     * Attributes
     */
    private BoardController mBoard;
    private int mCurrentShip;
    private AbstractShip[] mShips;
    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the layout
        setContentView(R.layout.activity_put_ships);

        // Init the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOrientationRadioGroup = (RadioGroup) findViewById(R.id.putship_radios_orientation);
        mOrientationRadioGroup.setOnCheckedChangeListener(new ShipOrientationChangeListener());

        mNorthRadio = (RadioButton) findViewById(R.id.radio_north);
        mSouthRadio = (RadioButton) findViewById(R.id.radio_south);
        mEastRadio = (RadioButton) findViewById(R.id.radio_east);
        mWestRadio = (RadioButton) findViewById(R.id.radio_west);
        mShipName = (TextView) findViewById(R.id.ship_name);

        // init board controller to create BoardGridFragments
        int playerId = 0;
        mCurrentShip = 0;
        mBoard = BattleShipsApplication.getBoard();
        mShips = BattleShipsApplication.getPlayers()[playerId].getShips();

        mFragment = mBoard.getFragments()[BoardController.SHIPS_FRAGMENT];
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.putships_fragment_container,
                            mFragment)
                    .commit();
        }

        updateRadioButton();
        updateNextShipNameToDisplay();
    }

    @Override
    public void onTileClick(int boardId, int x, int y) {
        String msg;
        msg = String.format(Locale.US, "put ship : (%d, %d)", x, y);
        Log.d(TAG, msg);
        try {
            mBoard.putShip(mShips[mCurrentShip], x, y);
            mCurrentShip++;
            updateNextShipNameToDisplay();
        } catch (Exception e) {
            Toast.makeText(this, R.string.put_ship_error, Toast.LENGTH_LONG).show();
        }

//        if (mCurrentShip >= mShips.length) {
//            new AsyncTask<Void, Void, Boolean>(){
//                @Override
//                protected Boolean doInBackground(Void... params){
//                    try {
//                        Thread.sleep(500);
//                        gotoBoardActivity();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    return false;
//                }
//
////                @Override
////                protected void onPostExecute(Boolean... result) {
////                    gotoBoardActivity();
////                }
//            }.execute();
//        } else {
        if(mCurrentShip < mShips.length) {
            updateRadioButton();
        }
//        }
    }

    private void gotoBoardActivity() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(mFragment)
                .commit();

        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateRadioButton() {

        switch (mShips[mCurrentShip].getOrientation()) {
            case NORTH:
                mNorthRadio.setChecked(true);
                break;
            case SOUTH:
                mSouthRadio.setChecked(true);
                break;
            case EAST:
                mEastRadio.setChecked(true);
                break;
            case WEST:
                mWestRadio.setChecked(true);
                break;
        }
    }

    private class ShipOrientationChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_east:
                    mShips[mCurrentShip].setOrientation(AbstractShip.Orientation.EAST);
                    break;
                case R.id.radio_north:
                    mShips[mCurrentShip].setOrientation(AbstractShip.Orientation.NORTH);
                    break;
                case R.id.radio_south:
                    mShips[mCurrentShip].setOrientation(AbstractShip.Orientation.SOUTH);
                    break;
                case R.id.radio_west:
                    mShips[mCurrentShip].setOrientation(AbstractShip.Orientation.WEST);
                    break;
            }
        }
    }

    private void updateNextShipNameToDisplay() {
        if (mCurrentShip < mShips.length) {
            mShipName.setText(mShips[mCurrentShip].getName());
        }
    }

    @Override
    public void onBackPressed(){

    }

    public void onClickButton(View v){
        if(mCurrentShip == mShips.length) {
            gotoBoardActivity();
        }
        else
        {
            Toast.makeText(PutShipsActivity.this, "Vous n'avez pas placé tous les navires", Toast.LENGTH_LONG).show();
        }
    }
}
