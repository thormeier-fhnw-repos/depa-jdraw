package jdraw.figures.handles.cardinalDirection;

import jdraw.figures.handles.AbstractHandle;
import jdraw.framework.DrawView;
import jdraw.framework.Figure;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class WestHandle extends AbstractCardinalDirectionHandle {
    public WestHandle(Figure owner) {
        super(owner);
    }

    @Override
    public Point getLocation() {
        Rectangle bounds = getOwner().getBounds();

        return new Point(bounds.x, bounds.y + bounds.height / 2);
    }

    @Override
    public void startInteraction(int x, int y, MouseEvent e, DrawView v) {
        Rectangle bounds = getOwner().getBounds();

        setCorner(new Point(
            bounds.x + bounds.width,
            bounds.y + bounds.height
        ));
    }

    @Override
    public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
        if (null == getCorner()) {
            return;
        }

        getOwner().setBounds(
            new Point(
                x,
                getCorner().y - getOwner().getBounds().height
            ),
            getCorner()
        );
    }
}
