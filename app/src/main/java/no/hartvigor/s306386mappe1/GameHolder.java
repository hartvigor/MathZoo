package no.hartvigor.s306386mappe1;

import java.io.Serializable;
import java.util.ArrayList;

public class GameHolder implements Serializable {
    private ArrayList<GameItem> Items;

    public GameHolder(ArrayList<GameItem> items) {
        Items = items;
    }

    public ArrayList<GameItem> getItems() {
        return Items;
    }
}
