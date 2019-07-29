package Entities;

import Entities.Tiles.Tile;
import Entities.Tiles.WallTile;
import Logic.GameLogic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class Cell extends Entity {

    private Entity selectedEntity = null;
    private Tile tile = null;
    private boolean suggested;
    private GameLogic gameLogic = GameLogic.getInstance();
    private boolean occupied;

    Cell(double x, double y, double width, double height, int indexX, int indexY) {
        super(x, y, width, height, indexX, indexY);
        setOnMouseClicked(event -> {
            if (suggested) {
                getActionHandler().onSuggestedCellClicked(this);
            } else if (tile != null && gameLogic.isEditMode())
                tile.fireEvent(event);
        });
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isSuggested() {
        return suggested;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public void toggleEntity(Tile tile) {
        if (this.tile != null && tile != null)
            if (tile.getClass().getName().equals(this.tile.getClass().getName())) {
                if (tile instanceof WallTile && this.tile instanceof WallTile) {
                    ((WallTile) this.tile).addDirection();
                    return;
                }
                this.tile = null;
                return;
            }


        this.tile = tile;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Paint tmpFill = gc.getFill();
        if (suggested)
            gc.setFill(Color.BLUE);
        else
            gc.setFill(Color.LIGHTSLATEGREY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        if (tile != null)
            tile.draw(gc);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(tmpFill);

    }

    @Override
    public String toString() {
        return String.format("Entity: %s | point:(%d,%d)", tile, getIndexX(), getIndexY());
    }
}
