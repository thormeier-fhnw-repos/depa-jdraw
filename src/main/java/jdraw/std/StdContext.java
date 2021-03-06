/*
 * Copyright (c) 2017 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved.
 */
package jdraw.std;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import jdraw.figures.EllipseTool;
import jdraw.figures.Group;
import jdraw.figures.LineTool;
import jdraw.figures.RectTool;
import jdraw.framework.DrawCommandHandler;
import jdraw.framework.DrawModel;
import jdraw.framework.DrawTool;
import jdraw.framework.DrawToolFactory;
import jdraw.framework.DrawView;
import jdraw.framework.Figure;
import jdraw.framework.FigureGroup;
import jdraw.grid.Grid30;
import jdraw.grid.SimpleGrid;
import jdraw.grid.SnapGrid;

/**
 * Standard implementation of interface DrawContext.
 * 
 * @see DrawView
 * @author Dominik Gruntz & Christoph Denzler
 * @version 2.6, 24.09.09
 */
public class StdContext extends AbstractContext {

    private List<Figure> clipboard;
    private int numberOfPastes = 0;
    private String saveFilePath = "./saveFiles";

    /**
     * Constructs a standard context with a default set of drawing tools.
     * @param view the view that is displaying the actual drawing.
     */
    public StdContext(DrawView view) {
        super(view, null);
    }

  /**
   * Constructs a standard context. The drawing tools available can be parameterized using <code>toolFactories</code>.
   * @param view the view that is displaying the actual drawing.
   * @param toolFactories a list of DrawToolFactories that are available to the user
   */
    public StdContext(DrawView view, List<DrawToolFactory> toolFactories) {
        super(view, toolFactories);
    }

    /**
     * Creates and initializes the "Edit" menu.
     *
     * @return the new "Edit" menu.
     */
    @Override
    protected JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        final JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        editMenu.add(undo);
        undo.addActionListener(e -> {
                final DrawCommandHandler h = getModel().getDrawCommandHandler();
                if (h.undoPossible()) {
                    h.undo();
                }
            }
        );

        final JMenuItem redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        editMenu.add(redo);
        redo.addActionListener(e -> {
                final DrawCommandHandler h = getModel().getDrawCommandHandler();
                if (h.redoPossible()) {
                    h.redo();
                }
            }
        );
        editMenu.addSeparator();

        JMenuItem sa = new JMenuItem("SelectAll");
        sa.setAccelerator(KeyStroke.getKeyStroke("control A"));
        editMenu.add(sa);
        sa.addActionListener( e -> {
                for (Figure f : getModel().getFigures()) {
                    getView().addToSelection(f);
                }
                getView().repaint();
            }
        );

        editMenu.addSeparator();
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cutItem.addActionListener(e -> {
            clipboard = new LinkedList<>();
            getView().getSelection().forEach(figure -> {
                getModel().removeFigure(figure);
                clipboard.add(figure.clone());
            });
            numberOfPastes = 0;
        });
        editMenu.add(cutItem);

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copyItem.addActionListener(e -> {
            clipboard = new LinkedList<>();
            clipboard.addAll(getView().getSelection());
            numberOfPastes = 0;
        });
        editMenu.add(copyItem);

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke("control V"));
        pasteItem.addActionListener(e -> {
            if (null != clipboard) {
                numberOfPastes++;

                getView().clearSelection();
                clipboard.forEach(figure -> {
                    Figure newFigure = figure.clone();
                    newFigure.move(5 * numberOfPastes, 5 * numberOfPastes);

                    getModel().addFigure(newFigure);
                    getView().addToSelection(newFigure);
                });
            }
        });
        editMenu.add(pasteItem);

        editMenu.addSeparator();
        JMenuItem clear = new JMenuItem("Clear");
        editMenu.add(clear);
        clear.addActionListener(e -> {
            getModel().removeAllFigures();
        });

        editMenu.addSeparator();
        JMenuItem group = new JMenuItem("Group");
        group.addActionListener(e -> {
            Group groupFigure = new Group(getView().getSelection());

            getModel().addFigure(groupFigure);
            getView().getSelection().forEach(figure -> getModel().removeFigure(figure));

            getView().addToSelection(groupFigure);
        });
        editMenu.add(group);

        JMenuItem ungroup = new JMenuItem("Ungroup");
        ungroup.addActionListener(e -> {
            LinkedList<Figure> selectionCopy = new LinkedList<>(getView().getSelection());

            selectionCopy.forEach(figure -> {
                if (figure instanceof FigureGroup) {
                    ((Group) figure).getFigureParts().forEach(figurePart -> {
                        getModel().addFigure(figurePart);
                        getView().addToSelection(figurePart);
                    });

                    getModel().removeFigure(figure);
                }
            });
        });
        ungroup.setEnabled(true);
        editMenu.add(ungroup);

        editMenu.addSeparator();

        JMenu orderMenu = new JMenu("Order...");
        JMenuItem frontItem = new JMenuItem("Bring To Front");
        frontItem.addActionListener(e -> bringToFront(getView().getModel(), getView().getSelection()));
        orderMenu.add(frontItem);
        JMenuItem backItem = new JMenuItem("Send To Back");
        backItem.addActionListener(e -> sendToBack(getView().getModel(), getView().getSelection()));
        orderMenu.add(backItem);
        editMenu.add(orderMenu);

        JMenu gridMenu = new JMenu("Grid...");

        JMenuItem noGridItem = new JMenuItem("No grid");
        noGridItem.addActionListener(e -> getView().setConstrainer(new SimpleGrid()));

        JMenuItem grid30Item = new JMenuItem("30");
        grid30Item.addActionListener(e -> getView().setConstrainer(new Grid30()));

        JMenuItem gridSnapItem = new JMenuItem("Snap");
        gridSnapItem.addActionListener(e -> getView().setConstrainer(new SnapGrid(getView())));

        gridMenu.add(noGridItem);
        gridMenu.add(grid30Item);
        gridMenu.add(gridSnapItem);

        editMenu.add(gridMenu);

        return editMenu;
    }

    /**
     * Creates and initializes items in the file menu.
     *
     * @return the new "File" menu.
     */
    @Override
    protected JMenu createFileMenu() {
      JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        fileMenu.add(open);
        open.setAccelerator(KeyStroke.getKeyStroke("control O"));
        open.addActionListener(e -> doOpen());

        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        fileMenu.add(save);
        save.addActionListener(e ->    doSave());

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        exit.addActionListener(e -> System.exit(0));

        return fileMenu;
    }

    @Override
    protected void doRegisterDrawTools() {
        DrawTool rectangleTool = new RectTool(this);
        DrawTool ellipseTool = new EllipseTool(this);
        DrawTool lineTool = new LineTool(this);

        addTool(rectangleTool);
        addTool(ellipseTool);
        addTool(lineTool);
    }

    /**
     * Changes the order of figures and moves the figures in the selection
     * to the front, i.e. moves them to the end of the list of figures.
     * @param model model in which the order has to be changed
     * @param selection selection which is moved to front
     */
    public void bringToFront(DrawModel model, List<Figure> selection) {
        // the figures in the selection are ordered according to the order in
        // the model
        List<Figure> orderedSelection = new LinkedList<>();
        int pos = 0;
        for (Figure f : model.getFigures()) {
            pos++;
            if (selection.contains(f)) {
                orderedSelection.add(0, f);
            }
        }
        for (Figure f : orderedSelection) {
            model.setFigureIndex(f, --pos);
        }
    }

    /**
     * Changes the order of figures and moves the figures in the selection
     * to the back, i.e. moves them to the front of the list of figures.
     * @param model model in which the order has to be changed
     * @param selection selection which is moved to the back
     */
    public void sendToBack(DrawModel model, List<Figure> selection) {
        // the figures in the selection are ordered according to the order in
        // the model
        List<Figure> orderedSelection = new LinkedList<>();
        for (Figure f : model.getFigures()) {
            if (selection.contains(f)) {
                orderedSelection.add(f);
            }
        }
        int pos = 0;
        for (Figure f : orderedSelection) {
            model.setFigureIndex(f, pos++);
        }
    }

    /**
     * Handles the saving of a drawing to a file.
     */
    private void doSave() {
        JFileChooser chooser = new JFileChooser(saveFilePath);
        chooser.setDialogTitle("Save Graphic");
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        FileFilter filter = new FileFilter() {
            @Override
            public String getDescription() {
                return "JDraw Graphic (*.draw)";
            }

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".draw");
            }
        };
        chooser.setFileFilter(filter);
        int res = chooser.showSaveDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {
            // save graphic
            File file = chooser.getSelectedFile();
            if (chooser.getFileFilter() == filter && !filter.accept(file)) {
                file = new File(chooser.getCurrentDirectory(), file.getName() + ".draw");
            }

            System.out.println("save current graphic to file " + file.getName());

            List<Figure> saveFigures = new LinkedList<>();
            getModel().getFigures().forEach(figure -> saveFigures.add(figure.clone()));

            try {
                FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(saveFigures);
                out.close();
                fileOut.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles the opening of a new drawing from a file.
     */
    private void doOpen() {
        JFileChooser chooser = new JFileChooser(saveFilePath);
        chooser.setDialogTitle("Open Graphic");
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public String getDescription() {
                return "JDraw Graphic (*.draw)";
            }

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".draw");
            }
        });
        int res = chooser.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {
            // read jdraw graphic
            System.out.println("read file "
                    + chooser.getSelectedFile().getName());

            try {
                FileInputStream fileIn = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                List<Figure> loadedFigures = (LinkedList<Figure>) in.readObject();
                in.close();
                fileIn.close();

                getModel().removeAllFigures();
                loadedFigures.forEach(figure -> getModel().addFigure(figure));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
