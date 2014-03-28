package waldonsm.paint.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import waldonsm.paint.gui.listeners.ModelChangeListener;
import waldonsm.paint.tools.drawables.Drawable;

/**
 * This interface gives the API for the model backing this program.  
 * @author Shawn Waldon
 *
 */
public interface PaintModel {
	

	public static final int NO_CLICKS = -37;
	public static final int ONE_CLICK = -36;
	public static final int IGNORE_ALL = -35;
	public static final int DRAG_A_LINE = -34;
	public static final int CLICK_WAITING = -33;
	public static final int FINAL_CLICK = -32;
	
	
	
	/**
	 * The code to represent that a model was changed when passed to a ModelChangeListener
	 */
	public static final int CODE_MODEL_CHANGED = 1;
	/**
	 * The code to represent that a model was saved when passed to a ModelChangeListener
	 */
	public static final int CODE_MODEL_SAVED = 0;
	/**
	 * The code to represent that a model has either been resized to a ModelChangeListener
	 */
	public static final int CODE_MODEL_SIZE_CHANGED = -1;
	/**
	 * The code to represent that a model's scale factor has been changed to a ModelChangeListener
	 */
	public static final int CODE_MODEL_RESCALED = -2;
	
	/**
	 * This constant hold the string representation of the format used in the program's custom save format
	 */
	public static final String INTERNAL_FORMAT_OF_ZIPFILE = "png";
	
	/**
	 * This enum represents the possible save file formats used by this program
	 * @author Shawn Waldon
	 *
	 */
	public static enum Format {
		GIF, JPG, PNG, ZDLIF;
		
		/**
		 * Returns the file extension in all lower case
		 */
		public String toString() {
			return name().toLowerCase();
		}
	}

	/**
	 * Returns the main backing image from this PaintModel, to be drawn onscreen
	 * @return the main backing image from this PaintModel
	 */
	BufferedImage getMainImage();

	/**
	 * Returns a Drawable representation of what the current mouse activity will draw when finished
	 * @return
	 */
	Drawable getCurrentDrawing();
	
	/**
	 * Returns the size of the PaintModel
	 * @return
	 */
	Dimension getSize();
	
	/**
	 * Forces an internal redraw of the backing image from the list of Drawables stored
	 */
	void redrawImage();
	
	/**
	 * Returns the main (left click) color of the PaintModel
	 * @return the main color of the PaintModel (most often associated with the left click)
	 */
	Color getMainColor();
	
	/**
	 * Sets the main (left click) color of the PaintModel
	 * @param c the new color to use for the main color (most often associated with the left click
	 */
	void setMainColor(Color c);
	
	/**
	 * Returns the secondary (right click) color of the PaintModel
	 * @return the main color of the PaintModel (most often associated with the right click)
	 */
	Color getSecondaryColor();
	
	/**
	 * Sets the secondary (right click) color of the PaintModel
	 * @param c the new color to use for the secondary color (most often associated with the right click
	 */
	void setSecondaryColor(Color c);
	
	/**
	 * returns true if the model has changed since the last save
	 * @return true if the model has changed since the last save
	 */
	boolean hasChanged();
	
	/**
	 * Saves the model to the specified file, with the given format
	 * @param f the file to save the model to
	 * @param format the format to save the image to
	 * @throws IOException if the save fails for some reason
	 */
	void saveModelToFile(File f, Format format) throws IOException;
	
	/**
	 * Returns the shape mode of the model
	 * @return the shape mode of the model
	 */
	int getShapeMode();
	
	/**
	 * Sets the shape mode of the model
	 * @param shapeMode the new shape mode to use, should be one of the constants RECTANGLE, CIRCLE, or POLYGON
	 */
	void setShapeMode(int shapeMode);
	
	/**
	 * Returns the fill mode of the model
	 * @return the fill mode of the model
	 */
	int getFillMode();
	
	/**
	 * Sets the fill mode of the model
	 * @param fillMode the new fill mode to use, should be one of the constants OUTLINE_ONLY, FILL_IN_OTHER_COLOR, or FILL_IN_SAME_COLOR
	 */
	void setFillMode(int fillMode);
	
	/**
	 * returns the model's current click code
	 * @return the current click code of the model, one of NO_CLICKS, ONE_CLICK, IGNORE_ALL, CLICK_WAITING, DRAG_A_LINE, or FINAL_CLICK
	 */
	int getClickCode();
	
	/**
	 * sets the model's click code
	 * @param code the new click code, should be one of NO_CLICKS, ONE_CLICK, IGNORE_ALL, CLICK_WAITING, DRAG_A_LINE, or FINAL_CLICK
	 */
	void setClickCode(int code);
	
	/**
	 * Sets the PaintModel's current Drawable to a new Drawable
	 * @param d the new Drawable object to set as the current Drawable
	 */
	void setCurrentDrawable(Drawable d);
	
	/**
	 * Increments the PaintModel's internal click counter
	 */
	void incrementClickCount();
	
	/**
	 * Decrements the PaintModel's internal click counter
	 */
	void decrementClickCount();
	
	/**
	 * Returns the value of the PaintModel's internal click counter <P>
	 * This value is initially zero, but can be adjusted via the incrementClickCount() and decrementClickCount() methods
	 * @return the value of the PaintModel's internal click counter
	 */
	int getClickCount();
	
	/**
	 * Adds the passed in Drawable to the PaintModel's final Drawables List. <P>
	 * This method also has the side effects of </P>
	 * <ul>
	 * <li> removing all points from the internal points List </li>
	 * <li> setting the initial point to null </li>
	 * <li> setting the click code of the model to IGNORE_ALL </li>
	 * <li> resetting the current Drawable to Drawable.NOTHING </li>
	 * </ul>
	 * @param newFinalDrawing the Drawable to add to the PaintModel's final Drawables List
	 */
	void finalizeDrawing(Drawable newFinalDrawing);
	
	/**
	 * Adds this Point to the internal list of points stored by this PaintModel
	 * @param p the Point to add to the list
	 */
	void addToPointsList(Point p);
	
	/**
	 * Returns a mutable copy of the internal points list <P>
	 * Due to the copy used, neither changes to the returned List or the Points it contains will in any way affect the PaintModel
	 * @return a List of Points copied from the internal stored points List
	 */
	List<Point> getPointsList();
	
	/**
	 *  
	 * @return a copy of the initial point (set by the setInitialPoint method, or null if the finalizeDrawable method has been called since the initial point has been set
	 */
	Point getInitialPoint();
	
	/**
	 * Sets the initial point to a copy of the passed in Point
	 * @param p
	 */
	void setInitialPoint(Point p);
	
	/**
	 * sets whether or not the current action should be treated as a right click
	 * @param isRightClick true if the current action should be treated as a right click
	 */
	void setIsRightClick(boolean isRightClick);
	
	/**
	 * Returns true if the current click should be treated as a right click <P> 
	 * NOTE: This method really returns the value input by the last call to setIsRightClick, but assuming the setIsRightClick method was called appropriately, the statement on the return value is correct
	 * @return true if the current click should be treated as a right click (or if there is no current click, returns whether the last click was treated as a right click) 
	 */
	boolean isRightClick();
	
	/**
	 * Returns the last file that this model was saved to. <P>
	 * If the model has not been saved, then this method returns null.
	 * @return the last file that this model was saved to/loaded from or null if this is an unsaved model.
	 */
	File getSaveFile();
	
	/**
	 * Changes the size of the internal image of this PaintModel to a new Size.
	 * @param w the new width of the model
	 * @param h the new height of the model
	 */
	void setSize(int w, int h);
	
	/**
	 * Returns the display name of the model (the name of the save file or "Untitled#" where # is a count of untitled images
	 * @return the display name of the model
	 */
	String getName();
	
	/**
	 * Adds the listener to the list of ModelChangeListeners notified when the model changes
	 * @param l the listener to be added
	 */
	void addModelChangeListener(ModelChangeListener l);
	
	/**
	 * Removes the specified listener from the list of ModelChangeListeners notified when the model changes if it is in the list. <P>
	 * If the specified listener is not in the list, then this method does nothing.
	 * @param l the listener to be removed.
	 */
	void removeModelChangeListener(ModelChangeListener l);
	
	/**
	 * Removes all elements from the internal list of points clicked.  If a drawable that fills the internal list is canceled,
	 * then this method must be called as part of the canceling process to avoid interference with the next drawable.
	 */
	void clearPointsList();
	
	/**
	 * Sets the scale factor to a new value
	 * @param scaleFactor the new scale factor, should be a decimal between 0 and 5
	 */
	public void setScaleFactor(int scaleFactor);

	/**
	 * Returns the scale factor for this panel
	 * @return
	 */
	public int getScaleFactor();
	
	/**
	 * Returns a Dimension representing the effective size of the model plus the scale factor
	 * @return a Dimension representing the effective size of the model plus the scale factor
	 */
	public Dimension getEffectiveSize();
	
	/**
	 * Undoes the last change made to the model
	 */
	public void undoLastAction();
	
	/**
	 * Redoes the last action that was undone (if nothing has been done since then)
	 */
	public void redoLastAction();
	
	/**
	 * Returns the height of this model's image
	 * @return the height of this model's image
	 */
	public int getHeight();
	
	/**
	 * Returns the width of this model's image
	 * @return the width of this model's image
	 */
	public int getWidth();
}
