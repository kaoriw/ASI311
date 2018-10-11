package com.excilys.formation.battleships.android.ui.ships;

import java.util.HashMap;
import java.util.Map;

import battleships.formation.excilys.com.battleships.R;
import battleships.ship.AbstractShip;
import battleships.ship.Submarine;

public class DrawableSubmarine extends Submarine implements DrawableShip {

    static final Map<Orientation, Integer> DRAWABLES = new HashMap<>();

    static {
        DRAWABLES.put(Orientation.NORTH, R.drawable.submarine_n);
        DRAWABLES.put(Orientation.EAST, R.drawable.submarine_e);
        DRAWABLES.put(Orientation.WEST, R.drawable.submarine_w);
        DRAWABLES.put(Orientation.SOUTH, R.drawable.submarine_s);
    }

    @Override
    public int getDrawable() {
        return DRAWABLES.get(this.getOrientation());
    }
}
