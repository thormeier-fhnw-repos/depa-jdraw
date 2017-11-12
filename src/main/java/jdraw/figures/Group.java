package jdraw.figures;

import jdraw.figures.handles.cardinalDirection.EastHandle;
import jdraw.figures.handles.cardinalDirection.NorthEastHandle;
import jdraw.figures.handles.cardinalDirection.NorthHandle;
import jdraw.figures.handles.cardinalDirection.NorthWestHandle;
import jdraw.figures.handles.cardinalDirection.SouthEastHandle;
import jdraw.figures.handles.cardinalDirection.SouthHandle;
import jdraw.figures.handles.cardinalDirection.SouthWestHandle;
import jdraw.figures.handles.cardinalDirection.WestHandle;
import jdraw.framework.Figure;
import jdraw.framework.FigureGroup;
import jdraw.framework.FigureHandle;
import jdraw.framework.FigureListener;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Group extends AbstractFigure implements FigureGroup {

    /**
     * All figures this group consists of
     */
    LinkedList<Figure> figureParts = new LinkedList<>();

    public Group(List<Figure> parts) {
        figureParts = (LinkedList<Figure>) parts;
    }

    @Override
    public void draw(Graphics g) {
        figureParts.forEach(figure -> figure.draw(g));
    }

    @Override
    public void move(int dx, int dy) {
        figureParts.forEach(figure -> figure.move(dx, dy));
    }

    @Override
    public boolean contains(int x, int y) {
        return getBounds().contains(new Point(x, y));
    }

    @Override
    public void setBounds(Point origin, Point corner) {

    }

    @Override
    public Rectangle getBounds() {
        Rectangle bounds = new Rectangle(figureParts.getFirst().getBounds());
        figureParts.forEach(figure -> bounds.add(figure.getBounds()));

        return bounds;
    }

    /**
     * Returns a list of 8 handles for this "Rectangle".
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
        List<Figure> clonedParts = new LinkedList<>();

        getFigureParts().forEach(figure -> clonedParts.add(figure.clone()));

        return new Group(clonedParts);
    }

    @Override
    public Iterable<Figure> getFigureParts() {
        return figureParts;
    }
}
