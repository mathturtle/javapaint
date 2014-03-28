package waldonsm.paint.tools;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import waldonsm.paint.model.PaintModel;

/**
 * Implements the MouseListener methods for a particular type of Drawable (or drawing mode).
 * <P>
 * The classes that implement this interface must have a parameterless constructor.  This will be the constructor 
 * that will be used to get all of the DrawingTools for the PaintListeners to use.  All of the methods in this class
 * should must check to see if the PaintModel is in the correct mode and throw an IllegalStateException if it is not.  If a
 * clickCode that is not supported by the DrawingTool is detected, the methods should also throw an IllegalStateException
 *  (the getShapeMode method will return the hashCode of the String returned by the getToolName method of the DrawingTool).
 * @author Shawn Waldon
 *
 */
public abstract class DrawingTool {
	
	private int offset;
	
	/**
	 * Handles the mousePressed events on the given PaintModel.
	 * <P>
	 * This method may throw any exception that it needs to,
	 * however these must be for fatal errors and will be displayed to the user.
	 * <P>
	 * If the PaintModel is not in the correct mode (shapeMode), throw an IllegalStateException.  If a
	 * clickCode that is not supported by the DrawingTool or is invalid in the current method is detected, 
	 * the methods should also throw an IllegalStateException
	 * @param p the Point that the mouse was pressed at
	 * @param isRightClick true if the MouseEvent had a right click, false otherwise
	 * @param pm the PaintModel to work on
	 * @throws Throwable if there are any fatal errors in the method
	 */
	public abstract void mousePressed(Point p, boolean isRightClick, PaintModel pm) throws Throwable;
	
	/**
	 * Handles the mouseClicked events on the given PaintModel.
	 * <P>
	 * This method may throw any exception that it needs to,
	 * however these must be for fatal errors and will be displayed to the user.
	 * <P>
	 * If the PaintModel is not in the correct mode (shapeMode), throw an IllegalStateException.  If a
	 * clickCode that is not supported by the DrawingTool or is invalid in the current method is detected, 
	 * the methods should also throw an IllegalStateException
	 * @param p the Point that the mouse was pressed at
	 * @param pm the PaintModel to work on
	 * @throws Throwable if there are any fatal errors in the method
	 */
	public abstract void mouseClicked(Point p, PaintModel pm) throws Throwable;
	
	/**
	 * Handles the mouseReleased events on the given PaintModel.
	 * <P>
	 * This method may throw any exception that it needs to,
	 * however these must be for fatal errors and will be displayed to the user.
	 * <P>
	 * If the PaintModel is not in the correct mode (shapeMode), throw an IllegalStateException.  If a
	 * clickCode that is not supported by the DrawingTool or is invalid in the current method is detected, 
	 * the methods should also throw an IllegalStateException
	 * @param p the Point that the mouse was released at
	 * @param pm the PaintModel to work on
	 * @throws Throwable if there are any fatal errors in the method
	 */
	public abstract void mouseReleased(Point p, PaintModel pm) throws Throwable;
	
	/**
	 * Handles the mouseDragged events on the given PaintModel.
	 * <P>
	 * This method may throw any exception that it needs to,
	 * however these must be for fatal errors and will be displayed to the user.
	 * <P>
	 * If the PaintModel is not in the correct mode (shapeMode), throw an IllegalStateException.  If a
	 * clickCode that is not supported by the DrawingTool or is invalid in the current method is detected, 
	 * the methods should also throw an IllegalStateException
	 * @param p the Point that the mouse was dragged to (where it is currently)
	 * @param pm the PaintModel to work on
	 * @throws Throwable if there are any fatal errors in the method
	 */
	public abstract void mouseDragged(Point p, PaintModel pm) throws Throwable;
	
	/**
	 * Handles the mouseDragged events on the given PaintModel.
	 * <P>
	 * This method may throw any exception that it needs to,
	 * however these must be for fatal errors and will be displayed to the user.
	 * <P>
	 * If the PaintModel is not in the correct mode (shapeMode), throw an IllegalStateException.  If a
	 * clickCode that is not supported by the DrawingTool or is invalid in the current method is detected, 
	 * the methods should also throw an IllegalStateException
	 * @param p the Point that the mouse was moved to (where it is currently)
	 * @param pm the PaintModel to work on
	 * @throws Throwable if there are any fatal errors in the method
	 */
	public abstract void mouseMoved(Point p, PaintModel pm) throws Throwable;
	
	/**
	 * Returns a unique identifier that will be used to identify this DrawingTool.
	 * <P>The hashCode of this String will be used as the ShapeMode of the PaintModels.
	 * @return a unique identifier that will be used to identify this DrawingTool
	 */
	public abstract String getToolName();
	
	/**
	 * Returns the filename of the image file that will be used as the image on the button for this DrawingTool.
	 * <P>
	 * This image should be 24 x 24 pixels in size.
	 * @return the filename of the image file that will be used as the image on the button for this DrawingTool
	 */
	public abstract String getButtonImageFileName();
	
	/**
	 * Returns the current offset of the DrawingTool's name's hashCode.  
	 * <P>
	 * This offset is used to avoid collisions in the HashMap of code (Integer) to DrawingTool.
	 * @return the offset of the DrawingTool's name's hashCode (used to avoid collisions)
	 */
	public final int getOffset() {
		return offset;
	}

	/**
	 * Sets the current offset of the DrawingTool's name's hashCode
	 * <P>
	 * This offset is used to avoid collisions in the HashMap of code (Integer) to DrawingTool.
	 * @param offset the new offset to use
	 */
	public final void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * Returns the list of valid fill mode image files for this DrawingTool
	 * <P>
	 * NOTE: This method may not return null. If there are no needed fill modes,
	 * then return Collections.emptyList().
	 * <P>
	 * It is also preferable if this method returns a reference to the same list
	 * upon invocation, as references may be held for caching.
	 * @return
	 */
	public abstract List<File> getFillModeFiles();
	

	/**
	 * Tests that the image is in the file returned by the getButtonImageFileName method 
	 * and that the image is of the correct size
	 * @return true if the image is good, false if it is the wrong size or there was an exception reading
	 */
	public static boolean testOutFileNames(DrawingTool tool) {
		try {
			BufferedImage im = ImageIO.read(new File(tool.getButtonImageFileName()));
			if ( im.getHeight() != 24 || im.getWidth() != 24)
				return false;
			for (File f : tool.getFillModeFiles()) {
				im = ImageIO.read(f);
				if ( im.getWidth() > 60)
					return false;
			}
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
}
