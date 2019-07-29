package Entities.Tiles;

import Entities.Cell;
import Entities.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Tile extends Entity {

    private int tileCost;
    private Color tileColor;
    private int fillOffset = 10;

    public Tile(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public Tile(Cell cell) {
        super(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight(),
                cell.getIndexX(), cell.getIndexY());
    }

    public Tile(Cell cell, int fillOffset) {
        this(cell);
        this.fillOffset = fillOffset;
    }

    public int getTileCost() {
        return tileCost;
    }

    public void setTileCost(int tileCost) {
        this.tileCost = tileCost;
    }

    public Color getTileColor() {
        return tileColor;
    }

    public void setTileColor(Color tileColor) {
        this.tileColor = tileColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getTileColor());
        gc.fillRect(getX() + fillOffset, getY() + fillOffset
                , getWidth() - 2 * fillOffset, getHeight() - 2 * fillOffset);
    }

}
