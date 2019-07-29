package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class IceTile extends Tile {

    public IceTile(Cell cell) {
        super(cell);
        setTileColor(Color.CYAN);
    }

    public IceTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.CYAN);
    }

    @Override
    public String toString() {
        return String.format("i %d %d", getIndexX(), getIndexY());
    }
}
