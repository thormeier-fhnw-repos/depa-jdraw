package jdraw.grid;

import jdraw.framework.PointConstrainer;

import java.awt.Point;

public class SimpleGrid implements PointConstrainer {
    @Override
    public Point constrainPoint(Point p) {
        return p;
    }

    @Override
    public int getStepX(boolean right) {
        return 1;
    }

    @Override
    public int getStepY(boolean down) {
        return 1;
    }

    @Override
    public void activate() {
        System.out.println("Activated grid: Simple");
    }

    @Override
    public void deactivate() {
        System.out.println("Deactivated grid: Simple");
    }

    @Override
    public void mouseDown() {

    }

    @Override
    public void mouseUp() {

    }
}
