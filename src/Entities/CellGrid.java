package Entities;

import Entities.Tiles.Tile;
import Entities.Tiles.TileFactory;
import Entities.Tiles.TileType;
import Handlers.ActionHandler;
import Logic.GameLogic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class CellGrid extends Entity {

    private int size;
    private ArrayList<Cell> cells;
    private TileType selectedType = TileType.END_TILE;
    private GameLogic gameLogic = GameLogic.getInstance();
    public CellGrid(double x, double y, double width, double height, int size) {
        super(x, y, width, height);
        this.size = size;
        setOnMouseClicked(this::fireEventInCell);
        cells = new ArrayList<>();
        initGrid();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private void initGrid() {
        double cellWidth = getWidth() / size;
        double cellHeight = getHeight() / size;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell(getX() + i * cellWidth, getY() + j * cellHeight, cellWidth, cellHeight, i, j);
                cells.add(cell);
            }
    }

    private void fireEventInCell(MouseEvent e) {
        if (cells != null)
            for (Cell cell : cells) {
                //hardcoded -26 because there is an offset because the menu and title bar and it just an level editor
                if (cell.contains(e.getX() + getX(), e.getY() + getY() - 26)) {
                    if (gameLogic.isEditMode()) {
                        Tile tile = TileFactory.getTile(selectedType, cell);
                        tile.setOnActionListener(getActionHandler());
                        if (e.getButton() == MouseButton.PRIMARY)
                            cell.toggleEntity(tile);
                    }
                    cell.fireEvent(e);
                    return;
                }
            }
    }

    @Override
    public void draw(GraphicsContext gc) {
        Paint tmp = gc.getFill();
        gc.setFill(Color.BROWN);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        for (Cell cell : cells)
            cell.draw(gc);
        gc.setFill(tmp);
    }

    @Override
    public void setOnActionListener(ActionHandler handler) {
        super.setOnActionListener(handler);
        for (Cell cell : cells)
            cell.setOnActionListener(handler);
    }

    public TileType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(TileType selectedType) {
        this.selectedType = selectedType;
    }

    public Cell getCell(int indexX, int indexY) {
        for (Cell cell : cells)
            if (cell.getIndexY() == indexY && cell.getIndexX() == indexX)
                return cell;
        return null;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public ArrayList<Cell> getAdjacentCells(double x, double y, int distance) {
        ArrayList<Cell> adjacentCells = new ArrayList<>();
        double startX = x - distance < 0 ? 0 : x - distance;
        double endX = x + distance > size ? size : x + distance;
        double startY = y - distance < 0 ? 0 : y - distance;
        double endY = y + distance > size ? size : y + distance;
        for (Cell cell : cells)
            if (cell.getIndexX() >= startX && cell.getIndexX() <= endX &&
                    cell.getIndexY() >= startY && cell.getIndexY() <= endY)
                if ((cell.getIndexY() == y || cell.getIndexX() == x) && !(cell.getIndexX() == x && cell.getIndexY() == y)) {
                    adjacentCells.add(cell);
                }
        return adjacentCells;


    }


}
