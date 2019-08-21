import Entities.Ball;
import Entities.Cell;
import Entities.CellGrid;
import Entities.Entity;
import Entities.Tiles.EndTile;
import Entities.Tiles.Tile;
import Entities.Tiles.TileType;
import Entities.Tiles.WallTile;
import Handlers.ActionHandler;
import Logic.GameLogic;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class CanvasPanel extends Canvas implements ActionHandler {

    private ArrayList<Entity> entities;
    private CellGrid cellGrid;
    private GraphicsContext gc;
    private ContextMenu wallMenu;
    private ContextMenu ballMenu;
    private GameLogic gameLogic = GameLogic.getInstance();
    private int gridSize = 5;

    public CanvasPanel(double width, double height) {
        entities = new ArrayList<>();
        initWallContextMenu();
        initBallContextMenu();
        setHeight(width);
        setWidth(height);
        gc = getGraphicsContext2D();

        widthProperty().addListener(e -> draw(gc));
        heightProperty().addListener(e -> draw(gc));

        initGrid();
        draw(gc);
        gameLogic.setEntities(entities);
        gameLogic.setMainGrid(cellGrid);
        gameLogic.setHandler(this);

        setEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            wallMenu.hide();
            ballMenu.hide();
            Ball selectedBall = gameLogic.getSelectedBall();
            for (Entity entity : entities) {
                if (entity.contains(event.getX(), event.getY()))
                    entity.fireEvent(event);
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                if (cellGrid.contains(event.getX(), event.getY()))
                    cellGrid.fireEvent(event);
            } else if (cellGrid.contains(event.getX(), event.getY())) {
                cellGrid.fireEvent(event);
            }
            if (selectedBall != null && selectedBall.equals(gameLogic.getSelectedBall()))
                gameLogic.deselectBall();
            draw(gc);

        });

    }

    private void initBallContextMenu() {
        ballMenu = new ContextMenu();
        CustomMenuItem textMovesItem = new CustomMenuItem();
        textMovesItem.setHideOnClick(false);
        CustomMenuItem confirmButtonItem = new CustomMenuItem();
        TextField movesField = new TextField();
        movesField.textProperty().addListener((e, o, n) -> {
            if (!n.matches("\\d*"))
                movesField.setText(n.replaceAll("[^\\d]", ""));
        });
        Button confirmButton = new Button("Save");
        textMovesItem.setContent(movesField);
        confirmButtonItem.setContent(confirmButton);
        ballMenu.getItems().addAll(textMovesItem, confirmButtonItem);

    }

    private void initWallContextMenu() {
        wallMenu = new ContextMenu();
        CustomMenuItem upMenuItem = new CustomMenuItem();
        CustomMenuItem rightMenuItem = new CustomMenuItem();
        CustomMenuItem bottomMenuItem = new CustomMenuItem();
        CustomMenuItem leftMenuItem = new CustomMenuItem();
        CheckBox leftButton = new CheckBox("Left Wall");
        CheckBox upButton = new CheckBox("Top Wall");
        CheckBox bottomButton = new CheckBox("Bottom Wall");
        CheckBox rightButton = new CheckBox("Right Wall");
        upMenuItem.setContent(upButton);
        rightMenuItem.setContent(rightButton);
        bottomMenuItem.setContent(bottomButton);
        leftMenuItem.setContent(leftButton);
        wallMenu.getItems().addAll(upMenuItem, rightMenuItem, bottomMenuItem, leftMenuItem);

    }

    private void initGrid() {
        DoubleProperty gridSizeProperty = new SimpleDoubleProperty();
        gridSizeProperty.bind(heightProperty().multiply(0.8));
        double x = getWidth() / 2 - 0.8 * getHeight() / 2;
        double y = getHeight() / 2 - 0.8 * getHeight() / 2;
        cellGrid = new CellGrid(x, y, 0.8 * getHeight(), 0.8 * getHeight(), gridSize);
        cellGrid.setOnActionListener(this);

    }

    private void draw(GraphicsContext gc) {
//        gc.clearRect(0, 0, getWidth(), getHeight());
        Paint tmpColor = gc.getFill();
        gc.setFill(Color.BURLYWOOD);
        gc.fillRect(0, 0, getWidth(), getHeight());
        cellGrid.draw(gc);
        for (Entity entity : entities)
            entity.draw(gc);
        gc.setFill(tmpColor);

    }

    @Override
    public void onTileSelected(TileType tileType) {
        cellGrid.setSelectedType(tileType);
    }

    @Override
    public void onEntityRightClick(MouseEvent event, Entity entity) {
        ballMenu.hide();
        if (entity instanceof Ball) {
            TextField textField = (TextField) ((CustomMenuItem) ballMenu.getItems().get(0)).getContent();
            textField.setText(String.valueOf(((Ball) entity).getMoves()));
            Button submit = (Button) ((CustomMenuItem) ballMenu.getItems().get(1)).getContent();
            submit.setOnAction(e -> {
                ((Ball) entity).setMoves(Integer.valueOf(textField.getText()));
                redraw();
            });
            ballMenu.show(this, event.getScreenX(), event.getScreenY());

        }

    }

    @Override
    public void onTileRightClick(MouseEvent event, Tile tile) {
        if (tile instanceof WallTile) {
            for (int i = 0; i < WallTile.Direction.values().length; i++) {
                //0 up  | 1 right | 2 down | 3 left
                CheckBox menuCheckBox = ((CheckBox) ((CustomMenuItem) wallMenu.getItems().get(i)).getContent());
                menuCheckBox.setSelected(false); //reset
                int finalI = i;
                menuCheckBox.setOnAction(event1 -> {
                    ((WallTile) tile).toggleDirection(WallTile.Direction.values()[finalI]);
                    draw(gc);
                });
                if (((WallTile) tile).getDirections().contains(WallTile.Direction.values()[i]))
                    menuCheckBox.setSelected(true);
            }
            wallMenu.show(this, event.getScreenX(), event.getScreenY());
        } else
            wallMenu.hide();
    }

    @Override
    public void onPlaySelected() {
        gameLogic.setEditMode(false);
        ArrayList<Cell> cells = cellGrid.getCells();
        for (Cell cell : cells) {
            if (cell.getTile() instanceof EndTile) {
                if (((EndTile) cell.getTile()).getBounded())
                    continue;
                Ball ball = new Ball(cell.getX(), cell.getY(),
                        cell.getWidth(), cell.getHeight(), cell.getIndexX(), cell.getIndexY(), 10);
                if (!entities.contains(ball)) {
                    ((EndTile) cell.getTile()).setBounded(true);
                    ball.setOnActionListener(this);
                    entities.add(ball);
                    cell.setOccupied(true);
//                    gameLogic.addEntity(ball);
                }
            }
        }
        draw(gc);
    }

    public void redraw() {
        draw(gc);
    }

    @Override
    public void onEditSelected() {
        gameLogic.setEditMode(true);
    }

    @Override
    public void ballSelectionChanged(Ball ball) {
        if (ball.isSelected())
            gameLogic.selectBall(ball);
        else
            gameLogic.deselectBall();
    }

    @Override
    public void onRedraw() {
        redraw();
    }

    @Override
    public void onSuggestedCellClicked(Cell cell) {
        gameLogic.moveToCell(cell);
    }

    @Override
    public void onGridSizeChanged(int newSize) {
        this.gridSize = newSize;
        cellGrid.setSize(newSize);
        gameLogic.reset();
    }
}
