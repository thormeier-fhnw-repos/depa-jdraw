/*
 * Copyright (c) 2017 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import jdraw.figures.handles.cardinalDirection.EastHandle;
import jdraw.figures.handles.cardinalDirection.NorthEastHandle;
import jdraw.figures.handles.cardinalDirection.NorthHandle;
import jdraw.figures.handles.cardinalDirection.NorthWestHandle;
import jdraw.figures.handles.cardinalDirection.SouthEastHandle;
import jdraw.figures.handles.cardinalDirection.SouthHandle;
import jdraw.figures.handles.cardinalDirection.SouthWestHandle;
import jdraw.figures.handles.cardinalDirection.WestHandle;
import jdraw.framework.Figure;
import jdraw.framework.FigureHandle;

/**
 * Represents rectangles in JDraw.
 * 
 * @author Christoph Denzler
 *
 */
public class Rect extends AbstractFigure {
    /**
     * Use the java.awt.Rectangle in order to save/reuse code.
     */
    private java.awt.Rectangle rectangle;

    /**
     * Create a new rectangle of the given dimension.
     * @param x the x-coordinate of the upper left corner of the rectangle
     * @param y the y-coordinate of the upper left corner of the rectangle
     * @param w the rectangle's width
     * @param h the rectangle's height
     */
    public Rect(int x, int y, int w, int h) {
        rectangle = new java.awt.Rectangle(x, y, w, h);
    }

    /**
     * Copy constructor
     * @param old
     */
    public Rect(Rect old) {
        Rectangle bounds = old.getBounds();

        rectangle = new Rectangle(
            bounds.x,
            bounds.y,
            bounds.width,
            bounds.height
        );
    }

    /**
     * Draw the rectangle to the given graphics context.
     * @param g the graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        g.setColor(Color.BLACK);
        g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public void setBounds(Point origin, Point corner) {
        rectangle.setFrameFromDiagonal(origin, corner);
        notifyAllListeners();
    }

    @Override
    public void move(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return;
        }

        rectangle.setLocation(rectangle.x + dx, rectangle.y + dy);
        notifyAllListeners();
    }

    @Override
    public boolean contains(int x, int y) {
        return rectangle.contains(x, y);
    }

    @Override
    public Rectangle getBounds() {
        return rectangle.getBounds();
    }

    /**
     * Returns a list of 8 handles for this Rectangle.
     * @return all handles that are attached to the targeted figure.
     * @see jdraw.framework.Figure#getHandles()
     */
    @Override
    public List<FigureHandle> getHandles() {
        List<FigureHandle> handles = new ArrayList<>();

        handles.add(new NorthWestHandle(this));
        handles.add(new NorthHandle(this));
        handles.add(new NorthEastHandle(this));
        handles.add(new EastHandle(this));
        handles.add(new SouthEastHandle(this));
        handles.add(new SouthHandle(this));
        handles.add(new SouthWestHandle(this));
        handles.add(new WestHandle(this));

        return handles;
    }

    @Override
    public Figure clone() {
        return new Rect(this);
    }
}
