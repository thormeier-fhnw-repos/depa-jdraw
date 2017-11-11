package jdraw.figures.handles;

import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import jdraw.framework.FigureHandle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public abstract class AbstractHandle implements FigureHandle
{
    /**
     * Width of the handle in pixels
     */
    private static int handleSize = 6;

    /**
     * Owner figure of this handle
     */
    private Figure owner;

    /**
     * Corner that should be fixed
     */
    private Point corner;

    /**
     * The opposing point of this handle
     */
    protected Point opposingPoint;

    public AbstractHandle(Figure owner) {
        this.owner = owner;
    }

    @Override
    public Figure getOwner() {
        return owner;
    }

    @Override
    public void draw(Graphics g) {
        Point loc = getTopLeftPoint();
        g.setColor(Color.WHITE); g.fillRect(loc.x, loc.y, handleSize, handleSize);
        g.setColor(Color.BLACK); g.drawRect(loc.x, loc.y, handleSize, handleSize);
    }

    @Override
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
    }

    @Override
    public boolean contains(int x, int y) {
        return new Rectangle(
            (int) getTopLeftPoint().getX(),
            (int) getTopLeftPoint().getY(),
            handleSize,
            handleSize
        ).contains(x, y);
    }

    /**
     * Returns the top left point of this handle
     * @return coords as Point
     */
    private Point getTopLeftPoint() {
        Point loc = getLocation();

        return new Point(loc.x - handleSize / 2, loc.y - handleSize / 2);
    }

    /**
     * Returns the bottom right point of this handle
     * @return coords as Point
     */
    private Point getBottomRightPoint() {
        Point loc = getLocation();

        return new Point(loc.x + handleSize / 2, loc.y + handleSize / 2);
    }

    /**
     * Sets the corner that should be fixed
     * @param corner The corner to be fixed
     */
    protected void setCorner(Point corner) {
        this.corner = corner;
    }

    /**
     * Get fixed corner
     * @return the corner
     */
    protected Point getCorner() {
        return corner;
    }

    @Override
    public void stopInteraction(int x, int y, MouseEvent e, DrawView v) {
        corner = null;
    }
}
