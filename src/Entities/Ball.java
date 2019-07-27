package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class Ball extends Entity {

    private int moves = 0;
    private boolean selected = false;
    private int textOffset = 8;
    private int drawOffset;
    public Ball(double x, double y, double width, double height, int indexX, int indexY, int drawOffset) {
        super(x, y, width, height, indexX, indexY);
        this.drawOffset = drawOffset;
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                getActionHandler().onEntityRightClick(event, this);
            else {
                this.selected = !selected;
                getActionHandler().ballSelectionChanged(this);
            }
        });
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void moveTo(Cell cell, int moves) {
        this.moves += moves;
        this.setX(cell.getX());
        this.setY(cell.getY());
        this.setIndexX(cell.getIndexX());
        this.setIndexY(cell.getIndexY());
    }

    @Override
    public void draw(GraphicsContext gc) {
        Paint tmpFill = gc.getFill();
        Paint tmpStroke = gc.getStroke();

        gc.setStroke(Color.BLACK);
        if (selected)
            gc.setFill(Color.LIGHTGREEN);
        else
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);

        gc.fillOval(getX() + drawOffset, getY() + drawOffset,
                getWidth() - 2 * drawOffset, getHeight() - 2 * drawOffset);
        gc.strokeOval(getX() + drawOffset, getY() + drawOffset,
                getWidth() - 2 * drawOffset, getHeight() - 2 * drawOffset);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(20));
        gc.fillText(String.valueOf(moves), getX() + getWidth() / 2 - textOffset
                , getY() + getHeight() / 2 + textOffset, 15);

        gc.setFill(tmpFill);
        gc.setStroke(tmpStroke);
    }
}
