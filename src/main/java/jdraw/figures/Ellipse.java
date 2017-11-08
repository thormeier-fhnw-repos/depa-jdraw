/*
 * Copyright (c) 2017 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.figures;

import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureHandle;
import jdraw.framework.FigureListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents ellipse2Ds in JDraw.
 * 
 * @author Christoph Denzler
 *
 */
public class Ellipse implements Figure {
    /**
     * Use the java.awt.Ellipse2D in order to save/reuse code.
     */
    private Ellipse2D ellipse2D;

    /**
     * List of all FigureListeners registered on this rect
     */
    private ArrayList<FigureListener> figureListeners = new ArrayList<>();

    /**
     * Create a new ellipse2D of the given dimension.
     * @param x the x-coordinate of the center of the ellipse
     * @param y the y-coordinate of the center of the ellipse
     * @param w the ellipse's width
     * @param h the ellipse's height
     */
    public Ellipse(int x, int y, int w, int h) {
        ellipse2D = new Ellipse2D.Float(x, y, w, h);
    }

    /**
     * Draw the ellipse2D to the given graphics context.
     * @param g the graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) ellipse2D.getX(), (int) ellipse2D.getY(),(int)  ellipse2D.getWidth(), (int) ellipse2D.getHeight());
        g.setColor(Color.BLACK);
        g.drawOval((int) ellipse2D.getX(), (int) ellipse2D.getY(),(int)  ellipse2D.getWidth(), (int) ellipse2D.getHeight());
    }

    @Override
    public void setBounds(Point origin, Point corner) {
        ellipse2D.setFrameFromDiagonal(origin, corner);
        notifyAllListeners();
    }

    @Override
    public void move(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return;
        }

        ellipse2D = new Ellipse2D.Float(
            (float) ellipse2D.getX() + dx,
            (float) ellipse2D.getY() + dy,
            (float) ellipse2D.getWidth(),
            (float) ellipse2D.getHeight()
        );

        notifyAllListeners();
    }

    @Override
    public boolean contains(int x, int y) {
        return ellipse2D.contains(x, y);
    }

    @Override
    public Rectangle getBounds() {
        return ellipse2D.getBounds();
    }

    /**
     * Returns a list of handles for this Ellipse2D.
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
