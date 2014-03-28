package waldonsm.paint.tools;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.drawables.Drawable;
import waldonsm.paint.tools.drawables.FinalPolygonDrawable;
import waldonsm.paint.tools.drawables.InProgressPolygonDrawable;

/**
 * A DrawingTool for drawing polygons.  Implements the DrawingTool methods for the drawing of polygons.
 * @author Shawn Waldon
 *
 */
public class PolygonTool extends DrawingTool {
	
	private static final String TOOL_NAME = "POLYGONDRAWINGTOOL";
	private static final String FILENAME = "images/polygon.png";
	

	private static final int FILL_IN_OTHER_COLOR = 1;
	private static final int OUTLINE_ONLY = 0;
	
	private final static List<File> listOfFiles = Collections.unmodifiableList(
												  Arrays.asList(new File("images/outlineOnly.png"),
																new File("images/fillOtherColor.png"),
																new File("images/fillThisColor.png"))
												  ); 

	/**
	 * Returns the filename for the image file of the PolygonTool
	 */
	public String getButtonImageFileName() {
		return FILENAME;
	}

	/**
	 * Returns the tool name for the PolygonTool
	 */
	public String getToolName() {
		return TOOL_NAME;
	}

	/**
	 * Implements the mouseDragged method for drawing polygons.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mouseDragged method.
	 */
	public void mouseDragged(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("PaintModel is in incorrect mode");
		}
		
		int clickCode = pm.getClickCode();
		
		if (clickCode == PaintModel.DRAG_A_LINE || clickCode == PaintModel.FINAL_CLICK) {
			// if they are working on a polygon, draw the current state of the polygon
			mouseMovedOnPolygonDrawing(p,pm);
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal mouse state for PolygonTool");
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
	 * Implements the mouseMoved method for drawing polygons.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mouseMoved method.
	 */
	public void mouseMoved(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("PaintModel is in incorrect mode");
		}
		
		int clickCode = pm.getClickCode();
		
		if (pm.getClickCode() == PaintModel.CLICK_WAITING) {
			// if they are working on a polygon, draw the current state of the polygon
			mouseMovedOnPolygonDrawing(p,pm);
		} else if (clickCode != PaintModel.IGNORE_ALL && clickCode != PaintModel.NO_CLICKS) {
			throw new IllegalStateException("Illegal mouse state for PolygonTool" + clickCode);
		}
	}
	
	/**
	 * The actual worker method for the mouseMoved and mouseDragged methods on the PolygonTool
	 * @param p the point the mouse is at
	 * @param pm the PaintModel being changed
	 */
	private void mouseMovedOnPolygonDrawing(Point p, PaintModel pm) {

		List<Point> ps = pm.getPointsList();
		// get a copy of the list of points and add the current mouse location
		ps.add(p);
		// get what color to draw the partial polygon
		Color color = (pm.isRightClick()) ? pm.getSecondaryColor() : pm.getMainColor();
		// set the current Drawable to an updated InProgressPolygonDrawable
		pm.setCurrentDrawable(new InProgressPolygonDrawable(ps,color));
	}
	
	/**
	 * Implements the mousePressed method for drawing polygons.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mousePressed method.
	 */
	public void mousePressed(Point p, boolean isRightClick, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("PaintModel is in incorrect mode");
		}
		int clickCode = pm.getClickCode();
		// If this is the first click
		if (clickCode == PaintModel.NO_CLICKS) {
			// for a polygon store the point in the list of points for the polygon, set the click code to DRAG_A_LINE
			// and store whether or not it was a right click
			pm.addToPointsList(p);
			pm.setClickCode(PaintModel.DRAG_A_LINE);
			pm.setIsRightClick(isRightClick);

		} else if (clickCode == PaintModel.DRAG_A_LINE) {
			// if the user is doing something and clicks the other mouse button, cancel the drawing
			pm.setCurrentDrawable(Drawable.NOTHING);
			pm.clearPointsList();
			pm.setClickCode(PaintModel.IGNORE_ALL);
		} else if (clickCode == PaintModel.CLICK_WAITING) {
			// if waiting on another mouse click for a polygon or some other multi-click gesture, if the click is opposite the original as far as right click, treat this as the final click
			// add this point to the list of points
			if (pm.isRightClick() != isRightClick) {
				pm.setClickCode(PaintModel.FINAL_CLICK);
			} else {
				pm.setClickCode(PaintModel.DRAG_A_LINE);
				// otherwise, they are dragging another line for the polygon and the new line should be displayed
				mouseDragged(p, pm);
			}
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal mouse mode for PolygonTool");
		}
		// finally increment the click count on the model.
		pm.incrementClickCount();
	}

	/**
	 * Implements the mouseReleased method for drawing rectangles.
	 * <P>
	 * For implementation details see comments within the method, for specifications see the 
	 * DrawingTool class's mouseReleased method.
	 */
	public void mouseReleased(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("PaintModel is in incorrect mode");
		}

		// first decrement the click count
		pm.decrementClickCount();
		// get the click code
		int clickCode = pm.getClickCode();
		
		if (clickCode == PaintModel.DRAG_A_LINE) {
			// if the user is dragging a line on a polygon, add the point as a final polygon point and set the click code to CLICK_WAITING
			pm.addToPointsList(p);
			pm.setClickCode(PaintModel.CLICK_WAITING);
		} else if (clickCode == PaintModel.FINAL_CLICK) {
			// if this is the final click of a polygon, add the point to the points list
			pm.addToPointsList(p);
			// set up the fill color and border color
			Color fillColor, borderColor;
			// get the fill mode
			int fillMode = pm.getFillMode();
			// initialize the colors
			if (pm.isRightClick()) {
				borderColor = pm.getSecondaryColor();
				if (fillMode == FILL_IN_OTHER_COLOR) {
					fillColor = pm.getMainColor();
				} else {
					fillColor = pm.getSecondaryColor();
				}
			} else {
				borderColor = pm.getMainColor();
				if (fillMode == FILL_IN_OTHER_COLOR) {
					fillColor = pm.getSecondaryColor();
				} else {
					fillColor = pm.getMainColor();
				}
			}
			// create the FinalPolygonDrawable
			Drawable newDrawing = new FinalPolygonDrawable(pm.getPointsList(), borderColor, fillColor, fillMode != OUTLINE_ONLY);
			
			// set this as a final drawable on the drawing
			pm.finalizeDrawing(newDrawing);
			// set the click code to ignore all (done on the model in the finalizeDrawing method)
			clickCode = PaintModel.IGNORE_ALL;
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal mouse state in PolygonDrawable");
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

}
