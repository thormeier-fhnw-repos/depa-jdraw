package jdraw.grid;

import jdraw.framework.DrawModelEvent;
import jdraw.framework.DrawModelListener;
import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import jdraw.framework.PointConstrainer;

import java.awt.Point;
import java.util.ArrayList;

public class SnapGrid implements PointConstrainer {

    /**
     * DrawContext to get all figures that are currently in the DrawModel
     */
    private DrawView view;

    /**
     * The currently adding figure
     */
    private Figure currentFigure = null;

    /**
     * Radius around a handle in which the point should snap
     */
    private int snapRadius = 15;

    public SnapGrid(DrawView view) {
        this.view = view;

        view.getModel().addModelChangeListener(e -> currentFigure = e.getFigure());
    }

    @Override
    public Point constrainPoint(Point p) {
        Point snappedPoint = getSnappedPoint(p);

        if (null == snappedPoint) {
            return p;
        }

        return snappedPoint;
    }

    @Override
    public int getStepX(boolean right) {
        return 0;
    }

    @Override
    public int getStepY(boolean down) {
        return 0;
    }

    @Override
    public void activate() {
        System.out.println("Activated grid: Snap");
    }

    @Override
    public void deactivate() {
        System.out.println("Deactivated grid: Snap");
    }

    @Override
    public void mouseDown() {

    }

    @Override
    public void mouseUp() {

    }

    /**
     * Find a point that a given Point P should snap to
     * @param p
     * @return The point of a snapped handle
     */
    private Point getSnappedPoint(Point p) {
        ArrayList<Point> handlePoints = new ArrayList<>();

        view.getModel().getFigures().forEach(figure -> {
            if (!view.getSelection().contains(figure) && figure != currentFigure) {
                figure.getHandles().forEach(figureHandle -> {
                    handlePoints.add(figureHandle.getLocation());
                });
            }
        });

        for (Point handlePoint : handlePoints) {
            if (shouldSnap(handlePoint, p)) {
                return handlePoint;
            }
        }

        return null;
    }

    /**
     * Checks if a given Point p is within a given radius around a handlePoint
     * @param handlePoint
     * @param p
     * @return
     */
    private boolean shouldSnap(Point handlePoint, Point p) {
        return handlePoint.distance(p) <= snapRadius;
    }
}
