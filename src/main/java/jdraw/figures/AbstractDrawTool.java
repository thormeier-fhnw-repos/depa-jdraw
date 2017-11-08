package jdraw.figures;

import jdraw.framework.DrawContext;
import jdraw.framework.DrawTool;
import jdraw.framework.DrawView;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.Point;

abstract public class AbstractDrawTool implements DrawTool
{
    /**
     * the image resource path.
     */
    private static final String IMAGES = "/images/";

    /**
     * The context we use for drawing.
     */
    protected DrawContext context;

    /**
     * The context's view. This variable can be used as a shortcut, i.e.
     * instead of calling context.getView().
     */
    protected DrawView view;

    /**
     * Temporary variable.
     * During rectangle creation this variable refers to the point the
     * mouse was first pressed.
     */
    protected Point anchor = null;

    /**
     * Create a new rectangle tool for the given context.
     * @param context a context to use this tool in.
     */
    public AbstractDrawTool(DrawContext context) {
        this.context = context;
        this.view = context.getView();
    }

    /**
     * Deactivates the current mode by resetting the cursor
     * and clearing the status bar.
     * @see DrawTool#deactivate()
     */
    @Override
    public void deactivate() {
        this.context.showStatusText("");
    }

    /**
     * Activates the Rectangle Mode. There will be a
     * specific menu added to the menu bar that provides settings for
     * Rectangle attributes
     */
    @Override
    public void activate() {
        this.context.showStatusText( getName() + " Mode");
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(getClass().getResource(IMAGES + getIconName()));
    }


    @Override
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    /**
     * Returns the name
     * @return Name of this tool
     */
    @Override
    abstract public String getName();

    abstract public String getIconName();
}
