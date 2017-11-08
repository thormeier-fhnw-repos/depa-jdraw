package jdraw.figures;

import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureListener;

import java.util.ArrayList;

abstract class AbstractFigure implements Figure
{
    /**
     * List of all FigureListeners registered on this rect
     */
    private ArrayList<FigureListener> figureListeners = new ArrayList<>();

    @Override
    public void addFigureListener(FigureListener listener) {
        figureListeners.add(listener);
    }

    @Override
    public void removeFigureListener(FigureListener listener) {
        figureListeners.remove(listener);
    }

    /**
     * Notifies all FigureListeners
     */
    protected void notifyAllListeners() {
        // Copy listeners to avoid concurrent modification
        ArrayList<FigureListener> listenersCopy = new ArrayList<>(figureListeners);

        listenersCopy.forEach(figureListener -> figureListener.figureChanged(new FigureEvent(this)));
    }

    @Override
    public Figure clone() {
        return null;
    }
}
