package Entities.Tiles;

import Entities.Ball;
import Entities.Cell;
import javafx.scene.paint.Color;

public class EndTile extends Tile {

    private Ball bounded;

    public EndTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.RED);
    }

    public Ball getBounded() {
        return bounded;
    }

    public void setBounded(Ball bounded) {
        this.bounded = bounded;
    }
}
