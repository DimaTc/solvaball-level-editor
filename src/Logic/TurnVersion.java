package Logic;

import Entities.Ball;
import Entities.Cell;

public class TurnVersion {
    private Cell oldCell;
    private Cell newCell;
    private int originMoves;
    private Ball ball;
    private Ball ballToDelete;
    private Cell cellToBeFree;

    public TurnVersion(Cell oldCell, Cell newCell, Ball ball) {
        this.oldCell = oldCell;
        this.newCell = newCell;
        this.ball = ball;
        originMoves = ball.getMoves();
    }

    public Ball getBallToDelete() {
        return ballToDelete;
    }

    public void setBallToDelete(Ball ballToDelete) {
        this.ballToDelete = ballToDelete;
    }

    public Cell getCellToBeFree() {
        return cellToBeFree;
    }

    public void setCellToBeFree(Cell cellToBeFree) {
        this.cellToBeFree = cellToBeFree;
    }

    public Cell getOldCell() {
        return oldCell;
    }

    public void setOldCell(Cell oldCell) {
        this.oldCell = oldCell;
    }

    public Cell getNewCell() {
        return newCell;
    }

    public void setNewCell(Cell newCell) {
        this.newCell = newCell;
    }

    public int getOriginMoves() {
        return originMoves;
    }

    public void setOriginMoves(int originMoves) {
        this.originMoves = originMoves;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }
}
