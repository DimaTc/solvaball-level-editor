package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class EndTile extends Tile {

    public EndTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.RED);
    }

}
