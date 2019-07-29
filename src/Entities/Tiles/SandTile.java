package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class SandTile extends Tile {

    public SandTile(Cell cell) {
        super(cell);
        init();
    }

    public SandTile(Cell cell, int offset) {
        super(cell, offset);
        init();
    }

    private void init() {
        setTileColor(Color.ORANGE);
        setTileCost(1);

    }


    @Override
    public String toString() {
        return String.format("s %d %d", getIndexX(), getIndexY());
    }

}
