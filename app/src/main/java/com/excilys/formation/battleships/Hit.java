package com.excilys.formation.battleships;


import java.util.NoSuchElementException;

public enum Hit {
    MISS(-1, "manqué"),
    STRIKE(-2, "touché"),
    ALREADY_STRIKE(-3, "déjà touché"),
    ALREADY_MISSED(-4, "déjà manqué"),
    DESTROYER(2, "Frégate"),
    SUBMARINE(3, "Sous-marin"),
    BATTLESHIP(4, "Croiseur"),
    CARRIER(5, "Porte-avion")
    ;
    private int value;
    private String label;
    Hit(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Hit fromInt(int value) {
        for (Hit hit : Hit.values()) {
            if (hit.value == value) {
                return hit;
            }
        }
        throw new NoSuchElementException("no enum for value " + value);
    }

    public String toString() {
        return this.label;
    }
};