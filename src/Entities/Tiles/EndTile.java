package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class EndTile extends Tile {

    //    private Ball bounded;
    private boolean bounded;

    public EndTile(Cell cell, boolean bounded) {
        this(cell);
        this.bounded = bounded;
    }

    public EndTile(Cell cell) {
        super(cell);
        setTileColor(Color.RED); //duplicate but quick fix
    }

    public EndTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.RED);
    }

    public boolean getBounded() {
        return bounded;
    }

    public void setBounded(boolean bounded) {
        this.bounded = bounded;
    }

    @Override
    public String toString() {
        return String.format("e %d %d", getIndexX(), getIndexY());

    }
}
