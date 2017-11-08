package jdraw.figures;

import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureHandle;
import jdraw.framework.FigureListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Line implements Figure {

    private Line2D line;

    /**
     * List of all FigureListeners registered on this rect
     */
    private ArrayList<FigureListener> figureListeners = new ArrayList<>();

    public Line(int x1, int y1, int x2, int y2) {
        line = new Line2D.Float(x1, y1, x2, y2);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(
            (int) line.getX1(),
            (int) line.getY1(),
            (int) line.getX2(),
            (int) line.getY2()
        );
    }

    @Override
    public void move(int dx, int dy) {
        line = new Line2D.Float(
            (float) line.getX1() + dx,
            (float) line.getY1() + dy,
            (float) line.getX2() + dx,
            (float) line.getY2() + dy
        );

        notifyAllListeners();
    }

    @Override
    public boolean contains(int x, int y) {
        // if AC is horizontal
        if ((int) line.getX1() == x) {
            return line.getX2() == x;
        }
        // if AC is vertical.
        if ((int) line.getY1() == y) {
            return line.getY2() == y;
        }

        // match the gradients
        return ((int) line.getX1() - x)*((int) line.getY1() - y) == (x - (int) line.getX2())*(y - (int) line.getY2());
    }

    @Override
    public void setBounds(Point origin, Point corner) {
        line = new Line2D.Float(
            origin.x,
            origin.y,
            corner.x,
            corner.y
        );

        notifyAllListeners();
    }

    @Override
    public Rectangle getBounds() {
        return line.getBounds();
    }

    /**
     * Returns a list of handles for this Line.
     * @return all handles that are attached to the targeted figure.
     * @see Figure#getHandles()
     */
    @Override
    public List<FigureHandle> getHandles() {
        return null;
    }

    @Override
    public void addFigureListener(FigureListener listener) {
        figureListeners.add(listener);
    }

    @Override
    public void removeFigureListener(FigureListener listener) {
        figureListeners.remove(listener);
    }

    @Override
    public Figure clone() {
        return null;
    }

    /**
     * Notifies all FigureListeners
     */
    private void notifyAllListeners() {
        // Copy listeners to avoid concurrent modification
        ArrayList<FigureListener> listenersCopy = new ArrayList<>(figureListeners);

        listenersCopy.forEach(figureListener -> figureListener.figureChanged(new FigureEvent(this)));
    }
}
