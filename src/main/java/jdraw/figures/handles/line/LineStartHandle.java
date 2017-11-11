package jdraw.figures.handles.line;

import jdraw.figures.Line;
import jdraw.figures.handles.AbstractHandle;
import jdraw.framework.DrawView;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class LineStartHandle extends AbstractHandle {
    public LineStartHandle(Line owner) {
        super(owner);
    }

    @Override
    public Point getLocation() {
        Line owner = (Line) getOwner();

        return owner.getP1();
    }

    @Override
    public void startInteraction(int x, int y, MouseEvent e, DrawView v) {
        Line owner = (Line) getOwner();

        setCorner(owner.getP2());
    }

    @Override
    public void dragInteraction(int x, int y, MouseEvent e, DrawView v) {
        if (null == getCorner()) {
            return;
        }

        getOwner().setBounds(
            new Point(x, y),
            getCorner()
        );
    }
}
