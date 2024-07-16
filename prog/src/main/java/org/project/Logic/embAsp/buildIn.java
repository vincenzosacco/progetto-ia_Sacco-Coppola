package org.project.Logic.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import java.awt.Point;
import java.util.Objects;

@Id("buildIn") //buildIn(X,Y,H)
public class buildIn {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int height;

    public buildIn() {
    }
    public buildIn(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public Point getCoord() {
        return new Point(x, y);
    }

    /**
     * Check if the buildIn is invalid.(x=-1, y=-1, height=-1)
     * @return true if the buildIn is invalid, false otherwise.
     */
    public boolean isInvalid(){
        return x == -1 && y == -1 && height == -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        buildIn buildIn = (buildIn) o;
        return x == buildIn.x && y == buildIn.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return " buildIn(" +
                x +
                "," + y +
                "," + height +
                ") ";
    }
}