package jdraw.figures.handles.cardinalDirection;

import jdraw.figures.handles.AbstractHandle;
import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import java.awt.Point;
import java.awt.event.MouseEvent;

abstract public class AbstractCardinalDirectionHandle extends AbstractHandle {
    public AbstractCardinalDirectionHandle(Figure owner) {
        super(owner);
    }
}
