package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class BatteryTile extends Tile {

    public BatteryTile(Cell cell) {
        super(cell);
        init();
    }

    public BatteryTile(Cell cell, int offset) {
        super(cell, offset);
        init();
    }

    private void init() {
        setTileColor(Color.YELLOW);
        setTileCost(-1);
    }

    @Override
    public String toString() {
        return String.format("b %d %d", getIndexX(), getIndexY());
    }
}
