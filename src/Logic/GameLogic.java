package Logic;

import Entities.Ball;
import Entities.Cell;
import Entities.CellGrid;
import Entities.Entity;
import Entities.Tiles.*;
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
                ball.getIndexY(), 1), ball);
        for (Cell cell : suggestedCells) {
            cell.setSuggested(true);
        }
        handler.onRedraw();
    }

    public void deselectBall() {
        if (selectedBall == null)
            return;
        selectedBall.setSelected(false);
        selectedBall = null;
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

    public void addBall(int indexX, int indexY, int moves) {
        Cell cell = mainGrid.getCell(indexX, indexY);
        cell.setOccupied(true);
        Ball ball = new Ball(cell, moves);
        ball.setOnActionListener(getHandler());
        entities.add(ball);

    }
//
//    public void addEntity(EntityType type, int indexX, int indexY) {
//        if (type == EntityType.BALL) {
//            Cell cell = mainGrid.getCell(indexX, indexY);
//            Ball ball = new Ball(cell);
//            entities.add(ball);
//        }
//
//    }

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
                if (!(tmpCell.getTile() instanceof IceTile)) {
                    Ball ball = new Ball(tmpCell.getX(), tmpCell.getY(),
                            tmpCell.getWidth(), tmpCell.getHeight(), ballX, ballY, 10);
                    ball.setOnActionListener(handler);
                    tmpCell.setOccupied(true);
                    entities.add(ball);
                }
            }
        }
        if (selectedBall == null)
            return;
        Tile tile = cell.getTile();
        if (tile != null) {
//            if (tile instanceof Tile)
            moves += ((Tile) tile).getTileCost();
        }
        Cell tmpCell = mainGrid.getCell(startX, startY);
        tmpCell.setOccupied(false);
        cell.setOccupied(true);
        selectedBall.moveTo(cell, moves);
        deselectBall();
        handler.onRedraw();
    }

    private ArrayList<Cell> suggestionLogicFilter(ArrayList<Cell> cells, Ball ball) {
        ArrayList<Cell> suggested = new ArrayList<>();
        for (Cell cell : cells) {

            int cellX = cell.getIndexX();
            int cellY = cell.getIndexY();
            int dx = cell.getIndexX() - ball.getIndexX();
            int dy = cell.getIndexY() - ball.getIndexY();
            if (!verifyNewCell(mainGrid.getCell(ball.getIndexX(), ball.getIndexY()), cell) && !(cell.getTile() instanceof IceTile))
                continue;
            if (cell.getTile() instanceof IceTile) {
                if (cell.getLineDistance(ball) > 1) //irrelevant with the new design
                    continue;
                else {
                    for (int i = 0; i < mainGrid.getSize(); i++) {
                        cellX += dx;
                        cellY += dy;
                        if (cellX >= mainGrid.getSize() || cellY >= mainGrid.getSize())
                            break;
                        if (cellX < 0 || cellY < 0)
                            break;
                        Cell newCell = mainGrid.getCell(cellX, cellY);
                        if (verifyNewCell(cell, newCell))
                            suggested.add(newCell);
                        if (newCell == null || !(newCell.getTile() instanceof IceTile))
                            break;

                    }
                    continue;
                }
            } else if (!cell.isOccupied()) {
                Cell newCell = mainGrid.getCell(cellX + dx, cellY + dy);
                if (verifyNewCell(cell, newCell))
                    suggested.add(newCell);
            } else continue;
            suggested.add(cell);
        }
        return suggested;
    }

    private boolean verifyNewCell(Cell oldCell, Cell newCell) {
        if (newCell == null)
            return false;
        if (newCell.getTile() instanceof IceTile)
            return false;
        if (newCell.isOccupied())
            return false;
        if (newCell.getTile() instanceof WallTile)
            if (((WallTile) newCell.getTile()).isBlocking(oldCell))
                return false;
        if (oldCell.getTile() instanceof WallTile)
            return !((WallTile) oldCell.getTile()).isBlocking(newCell);
        return true;
    }

    public void addTile(TileType type, int indexX, int indexY, int[] args) {
        Cell cell = mainGrid.getCell(indexX, indexY);
        Tile tile = null;
        switch (type) {
            case END_TILE:
                if (args != null && args.length > 0)
                    tile = new EndTile(cell, args[0] == 1);
                else
                    tile = new EndTile(cell);
                break;
            case ICE_TILE:
                tile = new IceTile(cell);
                break;
            case SAND_TILE:
                tile = new SandTile(cell);
                break;
            case WALL_TILE:
                if (args != null && args.length > 0)
                    tile = new WallTile(cell, args);
                break;
            case BATTERY_TILE:
                tile = new BatteryTile(cell);
                break;
        }
        if (tile != null) {
            cell.setTile(tile);
            tile.setOnActionListener(handler);
        }
    }

    public Ball getSelectedBall() {
        return selectedBall;
    }

    public void reset() {
        deselectBall();
        getMainGrid().reset();
        entities.clear();
        handler.onRedraw();
    }
}
