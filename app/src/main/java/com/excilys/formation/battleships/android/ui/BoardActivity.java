package com.excilys.formation.battleships.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import battleships.Board;
import battleships.Hit;
import battleships.Player;
import battleships.ShipException;
import battleships.formation.excilys.com.battleships.R;
import battleships.ship.AbstractShip;


public class BoardActivity extends AppCompatActivity implements BoardGridFragment.BoardGridFragmentListener{
    private static final String TAG = BoardActivity.class.getSimpleName();

    private static class Default {
        private static final int TURN_DELAY = 500; // ms
    }

    /* ***
     * Widgets
     */
    /** contains BoardFragments to display ships & hits grids */
    private CustomViewPager mViewPager;
    private TextView mInstructionTextView;

    /* ***
     * Attributes
     */
    private BoardController mBoardController;
    private Board mOpponentBoard;
    private Player mOpponent;
    private boolean mDone = false;
    private boolean mPlayerTurn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup layout
        setContentView(R.layout.activity_game_session);

        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.board_viewpager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(BoardController.HITS_FRAGMENT);

        mInstructionTextView = (TextView) findViewById(R.id.instruction_textview);

        // Init the Board Controller (to create BoardGridFragments)
        mBoardController = BattleShipsApplication.getBoard();
        mOpponentBoard = BattleShipsApplication.getOpponentBoard();
        mOpponent = BattleShipsApplication.getPlayers()[1];
//        Button backToScore = (Button)findViewById(R.id.btn_score);
//        backToScore.setVisibility(View.INVISIBLE);
//        if(mDone){
//            backToScore.setVisibility(View.VISIBLE);
//        }
    }

    // TODO  call me maybe
    private void doPlayerTurn(int x, int y) {
        new AsyncTask<Integer, String, Boolean>() {
            private String DISPLAY_TEXT = "0", DISPLAY_HIT = "1";

            @Override
            protected Boolean doInBackground(Integer... params) {
                Boolean hitAgain = null;
                //do {
                int[] coordinate = {params[0], params[1]};
                int x = params[0];
                int y = params[1];
                boolean hitflag = false; //true si on a déjà tiré sur la case
                sleep(Default.TURN_DELAY);
                publishProgress("...");
                //                    sleep(Default.TURN_DELAY);
                mPlayerTurn = false;
                Hit hit = null;
                Log.d("HitState", "doInBackground: myhit" + mBoardController.getHit(x, y));


                //pb : les hits de l'adversaire sont stockés dans notre grille aussi
                if(mBoardController.getHit(x,y) == null){
                    hit = mOpponentBoard.sendHit(x, y);
                    hitAgain = hit != Hit.MISS;
                }
                else {
                    if (!mBoardController.getHit(x, y)) {
                        hit = Hit.ALREADY_MISSED;
                        hitAgain = true;
                    } else {
                        hit = Hit.ALREADY_STRUCK;
                        hitAgain = true;
                    }
                }

                publishProgress(DISPLAY_TEXT, makeHitMessage(false, coordinate, hit));
                publishProgress(DISPLAY_HIT, String.valueOf(hitAgain), String.valueOf(x), String.valueOf(y), hit.toString());
                mDone = updateScore();
                sleep(Default.TURN_DELAY);

                //} while (hitAgain && !mDone);
                Log.d("HitState", "doInBackground: hitAgain = " + hitAgain + " hit = " + hit.toString());
                Log.d("HitState", "doInBackground: AIhit = " + mOpponentBoard.getHit(x,y));
                return hitAgain;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (values[0].equals(DISPLAY_TEXT)) {
                    showMessage(values[1]);
                } else if (values[0].equals(DISPLAY_HIT)) {
                    if(!values[4].equals(Hit.ALREADY_MISSED.toString()) && !values[4].equals(Hit.ALREADY_STRUCK.toString())){
                        mBoardController.setHit(Boolean.parseBoolean(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));
                    }

                }
            }

            @Override
            protected void onPostExecute(Boolean hitAgain) {
                if (hitAgain)

                {
                    mPlayerTurn = true;
                    mDone = updateScore();
                    if (mDone) {
                        gotoScoreActivity();
                    }
                } else {
                    // TODO sleep a while...
                    mViewPager.setCurrentItem(BoardController.SHIPS_FRAGMENT);
                    sleep(Default.TURN_DELAY);
                    mViewPager.setEnableSwipe(false);
                    doOpponentTurn();
                }

            }


        }.execute(x, y);

    }

    private void doOpponentTurn() {
        new AsyncTask<Void, String, Boolean>() {
            private String DISPLAY_TEXT = "0", DISPLAY_HIT = "1";

            @Override
            protected Boolean doInBackground(Void... params) {
                Hit hit;
                boolean hitAgain2;
                do {
                    sleep(Default.TURN_DELAY);
                    publishProgress("...");
                    sleep(Default.TURN_DELAY);

                    int[] coordinate = new int[2];

                    hit = mOpponent.sendHit(coordinate);
                    Log.d("AIhit", "doInBackground: "+ mOpponentBoard.getHit(coordinate[0], coordinate[1]) + "playerhit = " + mBoardController.getHit(coordinate[0], coordinate[1]));
                    hitAgain2 = hit != Hit.MISS;

                    publishProgress(DISPLAY_TEXT, makeHitMessage(true, coordinate, hit));
                    publishProgress(DISPLAY_HIT, String.valueOf(hitAgain2), String.valueOf(coordinate[0]), String.valueOf(coordinate[1]), hit.toString());

                    mDone = updateScore();
                    sleep(Default.TURN_DELAY);
                } while(hitAgain2 && !mDone);
                return mDone;
            }

            @Override
            protected void onPostExecute(Boolean done) {
                if (!done) {
                    sleep(Default.TURN_DELAY);
                    mViewPager.setEnableSwipe(true);
                    mViewPager.setCurrentItem(BoardController.HITS_FRAGMENT);
                    mPlayerTurn = true;
                } else {
                    gotoScoreActivity();
                }
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (values[0].equals(DISPLAY_TEXT)) {
                    showMessage(values[1]);
                } else if (values[0].equals(DISPLAY_HIT)) {

                    mBoardController.displayHitInShipBoard(Boolean.parseBoolean(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));

                }
            }


        }.execute();

    }

    private void gotoScoreActivity() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra(ScoreActivity.Extra.WIN, mOpponent.lose);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a BoardGridFragment
            switch (position) {
                case BoardController.SHIPS_FRAGMENT:
                case BoardController.HITS_FRAGMENT:
                    return mBoardController.getFragments()[position];

                default:
                    throw new IllegalStateException("BoardController doesn't support fragment position : " + position);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case BoardController.SHIPS_FRAGMENT:
                case BoardController.HITS_FRAGMENT:
                    return mBoardController.getFragments()[position].getName();
            }

            return null;
        }
    }

    private boolean updateScore() {
        for (Player player : BattleShipsApplication.getPlayers()) {
            int destroyed = 0;
            for (AbstractShip ship : player.getShips())
                if (ship.isSunk()) {
                    destroyed++;
                }

            player.destroyedCount = destroyed;
            player.lose = destroyed == player.getShips().length;
            if (player.lose) {
                return true;
            }
        }
        return false;
    }

    private String makeHitMessage(boolean incoming, int[] coords, Hit hit) {
        String msg;
        switch (hit) {
            case MISS:
                msg = hit.toString();
                break;
            case STRIKE:
                msg = hit.toString();
                break;
            case ALREADY_MISSED:
                msg = hit.toString();
                break;
            case ALREADY_STRUCK:
                msg = hit.toString();
                break;
            default:
                msg = String.format(getString(R.string.board_ship_sunk_format), hit.toString());
        }
        msg = String.format(getString(R.string.board_ship_hit_format), incoming ? "IA" : BattleShipsApplication.getPlayers()[0].getName(),
                ((char) ('A' + coords[0])),
                (coords[1] + 1), msg);
        return msg;
    }

    private void showMessage(String msg) {
        mInstructionTextView.setText(msg);
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTileClick(int id, int x, int y) {
        if(mPlayerTurn && id == BoardController.HITS_FRAGMENT){
            doPlayerTurn(x,y);

        }
    }

    @Override
    public void onBackPressed(){

    }

    public void onClickRestart(View v){
        String name = BattleShipsApplication.getPlayers()[0].getName();
        BattleShipsApplication.getGame().init(name);
    }

}
