package Entities.Tiles;

import Entities.Cell;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class WallTile extends Tile {
    private ArrayList<Direction> directions;
    private int wallWidth = 10;

    public WallTile(Cell cell, int i) {
        super(cell);
        directions = new ArrayList<>();
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                this.getActionHandler().onTileRightClick(event, this);
            }
        });
        //tmp
        directions.add(Direction.UP);
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public void addDirection() {
        int len = directions.size();
        if (len >= 4) {
            directions.clear();
            return;
        }
        for (int i = 0; i < Direction.values().length; i++) {
            Direction d = Direction.values()[i];
            if (!directions.contains(d)) {
                directions.add(d);
                return;
            }
        }
        //just in case
        directions.clear();
    }

    public void toggleDirection(Direction direction) {
        if (directions.contains(direction))
            directions.remove(direction);
        else directions.add(direction);
    }

    public int getWallWidth() {
        return wallWidth;
    }

    public void setWallWidth(int wallWidth) {
        this.wallWidth = wallWidth;
    }

    public void setWallDirection(Direction direction) {
        if (directions.contains(direction))
            directions.remove(direction);
        else directions.add(direction);
    }

    @Override
    public void draw(GraphicsContext gc) {
//        double tmpStrokeWidth = gc.getLineWidth();
        Paint tmpStrokeColor = gc.getFill();
        gc.setFill(Color.BROWN);
//        gc.setLineWidth(10);
        for (Direction direction : directions) {
            double x = getX(), y = getY(), width = getWidth(), height = getHeight();
            switch (direction) {
                case UP:
                    height = wallWidth;
                    break;
                case LEFT:
                    width = wallWidth;
                    break;
                case RIGHT:
                    x += getWidth() - wallWidth;
                    width = wallWidth;
                    break;
                case DOWN:
                    y += getHeight() - wallWidth;
                    height = wallWidth;
                    break;
            }


            gc.fillRect(x, y, width, height);
        }

//        gc.setLineWidth(tmpStrokeWidth);
        gc.setFill(tmpStrokeColor);
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
}
