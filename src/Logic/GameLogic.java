package Logic;

import Entities.Ball;
import Entities.Cell;
import Entities.CellGrid;
import Entities.Entity;
import Entities.Tiles.IceTile;
import Entities.Tiles.Tile;
import Handlers.ActionHandler;

import java.util.ArrayList;

public class GameLogic {
    private static final Object lock = new Object();
    private static GameLogic instance = new GameLogic();


    private boolean editMode = true;
    private CellGrid mainGrid;
    private Ball selectedBall;
    private ArrayList<Entity> entities; //probably always will be balls
    private ActionHandler handler;

    private GameLogic() {
        entities = new ArrayList<>();
    }

    public static GameLogic getInstance() {
        if (instance == null)
            synchronized (lock) {
                if (instance == null)
                    instance = new GameLogic();
            }
        return instance;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void selectBall(Ball ball) {
        selectedBall = ball;
        for (Entity entity : entities)
            if (entity instanceof Ball && !entity.equals(ball))
                ((Ball) entity).setSelected(false); //reset all balls
        for (Cell cell : mainGrid.getCells())   //Should be filtered - will do it in the game
            cell.setSuggested(false);           //Reset before
        ArrayList<Cell> suggestedCells = suggestionLogicFilter(mainGrid.getAdjacentCells(ball.getIndexX(),
                ball.getIndexY(), 2), ball);
        for (Cell cell : suggestedCells) {
            cell.setSuggested(true);
        }
        handler.onRedraw();
    }

    public void deselectBall(Ball ball) {

        selectedBall = null;
        ball.setSelected(false);
        for (Cell cell : mainGrid.getCells())
            cell.setSuggested(false);
    }

    public ActionHandler getHandler() {
        return handler;
    }

    public void setHandler(ActionHandler handler) {
        this.handler = handler;
    }

    public CellGrid getMainGrid() {
        return mainGrid;
    }

    public void setMainGrid(CellGrid mainGrid) {
        this.mainGrid = mainGrid;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Ball ball) {
        entities.add(ball);
    }

    public void moveToCell(Cell cell) {
        int moves = 1;
        int startX = selectedBall.getIndexX();
        int startY = selectedBall.getIndexY();
        Entity checkSpace = null;
        if (Math.abs(cell.getIndexY() - startY) > 1 || Math.abs(cell.getIndexX() - startX) > 1) {
            //if we need a ball
            int ballX = cell.getIndexX();
            int ballY = cell.getIndexY();
            if (cell.getIndexX() == startX)
                ballY = Math.min(cell.getIndexY(), startY) + 1;
            else
                ballX = Math.min(cell.getIndexX(), startX) + 1;
            for (Entity entity : entities) {
                if (entity.getIndexX() == ballX && entity.getIndexY() == ballY) {
                    checkSpace = entity;
                    break;
                }
            }
            if (checkSpace == null) {
                Cell tmpCell = mainGrid.getCell(ballX, ballY);
                if (!(tmpCell.getSelectedEntity() instanceof IceTile)) {
                    Ball ball = new Ball(tmpCell.getX(), tmpCell.getY(),
                            tmpCell.getWidth(), tmpCell.getHeight(), ballX, ballY, 10);
                    ball.setOnActionListener(handler);
                    entities.add(ball);
                }
            }
        }
        if (selectedBall == null)
            return;
        Entity tile = cell.getSelectedEntity();
        if (tile != null) {
            if (tile instanceof Tile)
                moves += ((Tile) tile).getTileCost();
        }
        selectedBall.moveTo(cell, moves);
        deselectBall(selectedBall);
        handler.onRedraw();
    }

    private ArrayList<Cell> suggestionLogicFilter(ArrayList<Cell> cells, Ball ball) {
        ArrayList<Cell> suggested = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.getSelectedEntity() instanceof IceTile) {
                if (cell.getLineDistance(ball) > 1)
                    continue;
                else {
                    int cellX = cell.getIndexX();
                    int cellY = cell.getIndexY();
                    int dx = cell.getIndexX() - ball.getIndexX();
                    int dy = cell.getIndexY() - ball.getIndexY();
                    for (int i = 0; i < mainGrid.getSize(); i++) {
                        cellX += dx;
                        cellY += dy;
                        if (cellX >= mainGrid.getSize() || cellY >= mainGrid.getSize())
                            break;
                        if (cellX < 0 || cellY < 0)
                            break;
                        Cell newCell = mainGrid.getCell(cellX, cellY);
                        if (newCell == null)
                            break;
                        if (!(newCell.getSelectedEntity() instanceof IceTile)) {
                            suggested.add(newCell);
                            break;
                        }
                    }
                    continue;
                }
            }

            suggested.add(cell);
        }
        return suggested;
    }

}
