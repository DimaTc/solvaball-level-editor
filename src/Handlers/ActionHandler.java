package Handlers;

import Entities.Ball;
import Entities.Cell;
import Entities.Entity;
import Entities.Tiles.Tile;
import Entities.Tiles.TileType;
import javafx.scene.input.MouseEvent;


public interface ActionHandler {

    void onTileSelected(TileType tileType);

    void onTileRightClick(MouseEvent event, Tile tile);

    void onPlaySelected();

    void onEditSelected();

    void ballSelectionChanged(Ball ball);

    void onRedraw();

    void onEntityRightClick(MouseEvent event, Entity entity);

    void onSuggestedCellClicked(Cell cell);
}
