/*
 * Copyright (c) 2017 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package jdraw.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import jdraw.framework.DrawCommandHandler;
import jdraw.framework.DrawModel;
import jdraw.framework.DrawModelEvent;
import jdraw.framework.DrawModelListener;
import jdraw.framework.Figure;
import jdraw.framework.FigureEvent;
import jdraw.framework.FigureListener;

/**
 * Provide a standard behavior for the drawing model. This class initially does not implement the methods
 * in a proper way.
 * It is part of the course assignments to do so.
 * @author Pascal Thormeier
 */
public class StdDrawModel implements DrawModel {

    /**
     * List of figures
     */
    private LinkedList<Figure> figures = new LinkedList<>();

    /**
     * ArrayList of all DrawModelListeners
     */
    private ArrayList<DrawModelListener> listeners = new ArrayList<>();

    /**
     * Listener to register on figures
     */
    private FigureListener figureListener = e -> notifyAllListeners(e.getFigure(), DrawModelEvent.Type.DRAWING_CHANGED);

    @Override
    public void addFigure(Figure f) {
        if (figures.contains(f)) {
            return;
        }

        figures.add(f);
        f.addFigureListener(figureListener);

        notifyAllListeners(f, DrawModelEvent.Type.FIGURE_ADDED);
    }

    @Override
    public Iterable<Figure> getFigures() {
        return figures;
    }

    @Override
    public void removeFigure(Figure f) {
        if (!figures.contains(f)) {
            return;
        }

        f.removeFigureListener(figureListener);
        figures.remove(f);

        notifyAllListeners(f, DrawModelEvent.Type.FIGURE_REMOVED);
    }

    @Override
    public void addModelChangeListener(DrawModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeModelChangeListener(DrawModelListener listener) {
        listeners.remove(listener);
    }

    /** The draw command handler. Initialized here with a dummy implementation. */
    // TODO initialize with your implementation of the undo/redo-assignment.
    private DrawCommandHandler handler = new EmptyDrawCommandHandler();

    /**
     * Retrieve the draw command handler in use.
     * @return the draw command handler.
     */
    @Override
    public DrawCommandHandler getDrawCommandHandler() {
        return handler;
    }

    @Override
    public void setFigureIndex(Figure f, int index) {
        if (!figures.contains(f)) {
            throw new IllegalArgumentException("Figure not contained in model!");
        }

        if (index > figures.size() - 1) {
            throw new IndexOutOfBoundsException("index " + index + " exceeds maximum index " + (figures.size() - 1));
        }

        figures.remove(f);
        figures.add(index, f);
        f.addFigureListener(figureListener);

        notifyAllListeners(f, DrawModelEvent.Type.DRAWING_CHANGED);
    }

    @Override
    public void removeAllFigures() {
        figures.forEach(f -> f.removeFigureListener(figureListener));
        figures.clear();

        notifyAllListeners(null, DrawModelEvent.Type.DRAWING_CLEARED);
    }

    /**
     * Notifies all listeners
     * @param f
     * @param type
     */
    private void notifyAllListeners(Figure f, DrawModelEvent.Type type) {
        // Copy listeners to avoid concurrent modification
        ArrayList<DrawModelListener> listenersCopy = new ArrayList<>(listeners);

        listenersCopy.forEach(
            drawModelListener -> drawModelListener.modelChanged(
                new DrawModelEvent(this, f, type)
            )
        );
    }
}
