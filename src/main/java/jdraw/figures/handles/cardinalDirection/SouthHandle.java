package jdraw.figures.handles.cardinalDirection;

import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class SouthHandle extends AbstractCardinalDirectionHandle {
    public SouthHandle(Figure owner) {
        super(owner);
    }

    @Override
    public Point getLocation() {
        Rectangle bounds = getOwner().getBounds();

        return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height);
    }

    @Override
    public void startInteraction(int x, int y, MouseEvent e, DrawView v) {
        Rectangle bounds = getOwner().getBounds();

        setCorner(new Point(
                bounds.x,
                bounds.y
        ));
    }

    @Override
    public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
        if (null == getCorner()) {
            return;
        }

        getOwner().setBounds(
            getCorner(),
            new Point(
                getCorner().x + getOwner().getBounds().width,
                y
            )
        );
    }
}
