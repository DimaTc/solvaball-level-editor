package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class IceTile extends Tile {
    public IceTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.CYAN);
    }

}
