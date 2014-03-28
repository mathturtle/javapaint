package waldonsm.paint.tools;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.Collections;
import java.util.List;

import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.drawables.Drawable;
import waldonsm.paint.tools.drawables.PencilDrawable;

/**
 * This DrawingTool is the tool for small touch up or detailed work, able to set single pixels and draw scribbles.
 * <P> It is approximately equivalent to the Pencil tool from MS Paint and that is where its name comes from.
 * @author Shawn Waldon
 *
 */
public class PencilTool extends DrawingTool {
	
	private static final String TOOL_IMAGE_FILE_NAME = "images/pencil.png";
	private static final String TOOL_NAME = "PENCILTOOL";

	/**
	 * Returns the image filename for this tool
	 */
	@Override
	public String getButtonImageFileName() {
		return TOOL_IMAGE_FILE_NAME;
	}

	/**
	 * Returns an empty list, since no image files are needed
	 */
	@Override
	public List<File> getFillModeFiles() {
		return Collections.emptyList();
	}

	/**
	 * Returns the tool name for this tool
	 */
	@Override
	public String getToolName() {
		return TOOL_NAME;
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseClicked(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
	}

	/**
	 * Implements the behavior of the pencil tool for a mouse dragged event
	 */
	@Override
	public void mouseDragged(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		
		// get the click code
		int clickCode = pm.getClickCode();
		
		if (clickCode == PaintModel.DRAG_A_LINE) {
			// if there is a drawing in process
			PencilDrawable d = (PencilDrawable) pm.getCurrentDrawing();
			// get the current drawing (and it should be a PencilDrawable), cast it and add the new Point
			d.addPoint(p);
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			// otherwise, unless a drawing was canceled, there was an error, so throw an exception
			throw new IllegalStateException("Illegal click code, mouse state inconsistent: " + clickCode);
		}
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseMoved(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
	}

	/**
	 * Implements the behavior of the pencil tool for a mouse pressed event
	 */
	@Override
	public void mousePressed(Point p, boolean isRightClick, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// get the click code
		int clickCode = pm.getClickCode();
		
		// if this is the first click
		if (clickCode == PaintModel.NO_CLICKS) {
			
			// store whether the click was a right click
			pm.setIsRightClick(isRightClick);
			
			// set the click code to DRAG_A_LINE
			pm.setClickCode(PaintModel.DRAG_A_LINE);
			
			// get the color
			Color color;
			if (isRightClick) {
				color = pm.getSecondaryColor();
			} else {
				color = pm.getMainColor();
			}
			
			pm.setCurrentDrawable(new PencilDrawable(p,color));
			
		} else if (clickCode == PaintModel.DRAG_A_LINE) {
			// cancel drawing
			pm.clearPointsList();
			pm.setClickCode(PaintModel.IGNORE_ALL);
			pm.setCurrentDrawable(Drawable.NOTHING);
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal click code (mouse in inconsistent state): " + clickCode);
		}
		pm.incrementClickCount();
	}

	/**
	 * Implements the behavior of the Pencil tool for a mouse released event
	 */
	@Override
	public void mouseReleased(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		pm.decrementClickCount();
		
		// get the click code
		int clickCode = pm.getClickCode();
		if (clickCode == PaintModel.DRAG_A_LINE) {
			// make sure the current point is added to the Drawable
			mouseDragged(p,pm);
			// add the Drawable to the list of final Drawables
			pm.finalizeDrawing(pm.getCurrentDrawing());
			
			// set the local variable clickCode to IGNORE_ALL
			clickCode = PaintModel.IGNORE_ALL;
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			throw new IllegalStateException("Illegal click code (mouse in inconsistent state): " + clickCode);
		}
		
		// if the click code is ignore all and there are no clicks, then reset the click code to NO_CLICKS
		if (clickCode == PaintModel.IGNORE_ALL && pm.getClickCount() == 0) {
			pm.setClickCode(PaintModel.NO_CLICKS);
		}
	}

}
