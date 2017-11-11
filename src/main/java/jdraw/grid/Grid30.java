package jdraw.grid;

import jdraw.framework.PointConstrainer;

import java.awt.Point;

public class Grid30 implements PointConstrainer {

    private int stepSize = 30;

    @Override
    public Point constrainPoint(Point p) {
        return new Point(
            p.x - (p.x % stepSize),
            p.y - (p.y % stepSize)
        );
    }

    @Override
    public int getStepX(boolean right) {
        return stepSize;
    }

    @Override
    public int getStepY(boolean down) {
        return stepSize;
    }

    @Override
    public void activate() {
        System.out.println("Activated grid: Grid30");
    }

    @Override
    public void deactivate() {
        System.out.println("Deactivated grid: Grid30");
    }

    @Override
    public void mouseDown() {

    }

    @Override
    public void mouseUp() {

    }
}
