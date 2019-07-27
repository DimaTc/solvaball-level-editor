package Entities.Logic;

import Entities.Ball;
import Entities.Cell;
import Entities.CellGrid;
import Entities.Entity;
import Entities.Handlers.ActionHandler;
import Entities.Tiles.Tile;

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
        System.out.println("Select Ball");
        selectedBall = ball;
        for (Entity entity : entities)
            if (entity instanceof Ball && !entity.equals(ball))
                ((Ball) entity).setSelected(false); //reset all balls
        for (Cell cell : mainGrid.getCells())
            cell.setSuggested(false);           //Reset before
        ArrayList<Cell> cells = mainGrid.getAdjacentCells(ball.getIndexX(), ball.getIndexY(), 2);
        for (Cell cell : cells) {
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
}
