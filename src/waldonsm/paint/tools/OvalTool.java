package waldonsm.paint.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.drawables.CircleDrawable;
import waldonsm.paint.tools.drawables.Drawable;

/**
 * This class is the DrawingTool for oval drawables. implements the methods of the DrawingTool for making ovals.
 * 
 * @author Shawn Waldon
 *
 */
public class OvalTool extends DrawingTool {
	
	private final static String TOOL_NAME = "OVALDRAWINGTOOL";
	private final static String FILENAME = "images/circle.png";
	
	private static final int FILL_IN_OTHER_COLOR = 1;
	private static final int OUTLINE_ONLY = 0;
	
	private final static List<File> listOfFiles = Collections.unmodifiableList(
												  Arrays.asList(new File("images/outlineOnly.png"),
																new File("images/fillOtherColor.png"),
																new File("images/fillThisColor.png"))
												  ); 

	/**
	 * Returns the tool name for the OvalTool class
	 */
	public String getToolName() {
		return TOOL_NAME;
	}
	
	/**
	 * Returns the image filename for the oval button image
	 */
	public String getButtonImageFileName() {
		return FILENAME;
	}

	/**
	 * Implements the mouseDragged method for drawing circles.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mouseDragged method.
	 */
	public void mouseDragged(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// get the click code
		int clickCode = pm.getClickCode();
		// if the click code is ONE_CLICK
		if (clickCode == PaintModel.ONE_CLICK) {
			// get the original point of the click
			Point mouseClickedAt = pm.getInitialPoint();
			// get the x and y at the top left of the box with corners at mouseClickedAt and mouseCurrentLocation
			int x = (mouseClickedAt.x < p.x) ? mouseClickedAt.x : p.x;
			int y = (mouseClickedAt.y < p.y) ? mouseClickedAt.y : p.y;
			// get the width of the box with corners at mouseClickedAt and mouseCurrentLocation
			int width = Math.abs(mouseClickedAt.x-p.x);
			int height = Math.abs(mouseClickedAt.y-p.y);
			// set up the fill color and outline color of the shape
			Color fillColor;
			Color outlineColor;
			// get the fill mode
			int fillMode = pm.getFillMode();
			// initialize the colors
			if (pm.isRightClick()) {
				outlineColor = pm.getSecondaryColor();
				if (fillMode == FILL_IN_OTHER_COLOR) {
					fillColor = pm.getMainColor();
				} else {
					fillColor = pm.getSecondaryColor();
				}
			} else {
				outlineColor = pm.getMainColor();
				if (fillMode == FILL_IN_OTHER_COLOR) {
					fillColor = pm.getSecondaryColor();
				} else {
					fillColor = pm.getMainColor();
				}
			}
			// if the mode is CIRCLE, set the current drawable to a CircleDrawable
			pm.setCurrentDrawable(new CircleDrawable(x,y,width,height,(fillMode != OUTLINE_ONLY),outlineColor,fillColor));
				
		}
	}
	
	/**
	 * Does nothing, since there is no need for it in this tool
	 */
	public void mouseClicked(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// does nothing for this tool.
	}

	/**
	 * Does nothing, since when the mouse is not pressed this DrawingTool has no behavior
	 */
	public void mouseMoved(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// this method does nothing -- nothing to do in this mode
	}

	/**
	 * Implements the mousePressed method for drawing circles.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mousePressed method.
	 */
	public void mousePressed(Point p, boolean isRightClick, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		int clickCode = pm.getClickCode();
		// If this is the first click
		if (clickCode == PaintModel.NO_CLICKS) {
				// for a circle store the initial point, set the click code to ONE_CLICK and store whether or not it was a right click
			pm.setInitialPoint(p);
			pm.setClickCode(PaintModel.ONE_CLICK);
			pm.setIsRightClick(isRightClick);
		} else if (clickCode == PaintModel.ONE_CLICK) {
			// if the user is doing something and clicks the other mouse button, cancel the drawing
			pm.setCurrentDrawable(Drawable.NOTHING);
			pm.setClickCode(PaintModel.IGNORE_ALL);
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal click code for Oval tool");
		}
		// finally increment the click count on the model.
		pm.incrementClickCount();
		
	}

	/**
	 * Implements the mouseReleased method for drawing circles.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mouseReleased method.
	 */
	public void mouseReleased(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// first decrement the click count
		pm.decrementClickCount();
		// get the click code
		int clickCode = pm.getClickCode();
		// if the click code is ONE_CLICK
		if (clickCode == PaintModel.ONE_CLICK) {
			// act as if the mouse was dragged to this point, creating the needed drawable and setting it to the current drawing of the model
			mouseDragged(p, pm);
			// get the current drawing
			Drawable newDrawing = pm.getCurrentDrawing();
				// if this is a RectangleDrawable or a CircleDrawable, tell it that it is a final drawable (increments the drawable name counter)
		
			((CircleDrawable)newDrawing).thisIsFinalDrawable();
			
			// set this drawable as a final drawable on the pm
			pm.finalizeDrawing(newDrawing);
			// set the click code to ignore all (done on the pm in the finalizeDrawing method)
			clickCode = PaintModel.IGNORE_ALL;
		} else if ( clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal click code for Oval tool");
		}
		
		// if the click code is IGNORE_ALL and there are no clicks in progress, then set the click code to NO_CLICKS
		if (clickCode == PaintModel.IGNORE_ALL && pm.getClickCount() == 0) {
			pm.setClickCode(PaintModel.NO_CLICKS);
		}
	}
	
	/**
	 * Returns the list of fill mode image files
	 */
	@Override
	public List<File> getFillModeFiles() {
		return listOfFiles;
	}
	
	/**
	 * A main method for testing the width and height of the images... renamed
	 */
	public static void testMain() {
		try {
			BufferedImage im = ImageIO.read(new File(FILENAME));
			System.out.println(im.getWidth());
			System.out.println(im.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
