package Entities;

import Logic.GameLogic;
import Entities.Tiles.WallTile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class Cell extends Entity {

    private Entity selectedEntity = null;
    private boolean suggested;
    private GameLogic gameLogic = GameLogic.getInstance();

    Cell(double x, double y, double width, double height, int indexX, int indexY) {
        super(x, y, width, height, indexX, indexY);
        setOnMouseClicked(event -> {
            if (suggested) {
                getActionHandler().onSuggestedCellClicked(this);
            } else if (selectedEntity != null && gameLogic.isEditMode())
                selectedEntity.fireEvent(event);
        });
    }

    public boolean isSuggested() {
        return suggested;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public void toggleEntity(Entity selectedEntity) {
        if (this.selectedEntity != null && selectedEntity != null)
            if (selectedEntity.getClass().getName().equals(this.selectedEntity.getClass().getName())) {
                if (selectedEntity instanceof WallTile && this.selectedEntity instanceof WallTile) {
                    ((WallTile) this.selectedEntity).addDirection();
                    return;
                }
                this.selectedEntity = null;
                return;
            }


        this.selectedEntity = selectedEntity;
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
        if (selectedEntity != null)
            selectedEntity.draw(gc);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(tmpFill);

    }

    @Override
    public String toString() {
        return String.format("Entity: %s | point:(%d,%d)", selectedEntity, getIndexX(), getIndexY());
    }
}
