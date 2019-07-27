package Entities;

import Handlers.ActionHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;

public abstract class Entity extends Region {


    private double x;
    private double y;
    private int indexX = 0;
    private int indexY = 0;
    private ActionHandler handler;

    public Entity(double x, double y, double width, double height) {
//        this.x = x;
//        this.y = y;
        setX(x);
        setY(y);
        super.setLayoutX(x);
        super.setLayoutY(y);
        super.setWidth(width);
        super.setHeight(height);

    }

    public Entity(double x, double y, double width, double height, int indexX, int indexY) {
        this(x, y, width, height);
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public abstract void draw(GraphicsContext gc);

    public ActionHandler getActionHandler() {
        return handler;
    }

    public void setOnActionListener(ActionHandler handler) {
        this.handler = handler;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    @Override
    public boolean contains(double localX, double localY) {
        if (localX >= getX() && localX <= getX() + getWidth())
            return localY >= getY() && localY <= getY() + getHeight();

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            Entity o = (Entity) obj;
            if (o.getX() == x && o.getY() == y)
                if (o.getWidth() == getWidth() && o.getHeight() == getHeight())
                    return o.indexX == indexX && o.indexY == indexY;
        }
        return false;

    }

    public int getLineDistance(Entity entity) {
        if (entity.getIndexY() == indexY)
            return Math.abs(entity.getIndexX() - indexX);
        else if (entity.getIndexX() == indexX)
            return Math.abs(entity.getIndexY() - indexY);
        return 0;
    }

}
