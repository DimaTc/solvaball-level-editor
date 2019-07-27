package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class BatteryTile extends Tile {

    public BatteryTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.YELLOW);
        setTileCost(-1);
    }

}
