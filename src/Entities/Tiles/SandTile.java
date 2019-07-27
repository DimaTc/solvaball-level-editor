package Entities.Tiles;

import Entities.Cell;
import javafx.scene.paint.Color;

public class SandTile extends Tile {

    public SandTile(Cell cell, int offset) {
        super(cell, offset);
        setTileColor(Color.ORANGE);
        setTileCost(1);
        setOnMouseClicked(event -> System.out.println("Test?"));
    }

}
