package waldonsm.paint.tools;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.Collections;
import java.util.List;

import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.drawables.Drawable;
import waldonsm.paint.tools.drawables.LineDrawable;

public class LineTool extends DrawingTool {
	
	private static final String TOOL_NAME = "LINETOOL";
	private static final String IMAGE_FILE_NAME = "images/line.png";

	@Override
	public String getButtonImageFileName() {
		return IMAGE_FILE_NAME;
	}

	@Override
	public List<File> getFillModeFiles() {
		return Collections.emptyList();
	}

	@Override
	public String getToolName() {
		return TOOL_NAME;
	}

	@Override
	public void mouseDragged(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("Illegal model state");
		}
		// get the click code
		int clickCode = pm.getClickCode();
		// if there has been one click
		if (clickCode == PaintModel.ONE_CLICK) {
		// get the correct color (based on right click)
			Color c;
			if (pm.isRightClick()) {
				c = pm.getSecondaryColor();
			} else {
				c = pm.getMainColor();
			}
		// make a new LineDrawable
			LineDrawable line = new LineDrawable(pm.getInitialPoint(), p, c);
		// set the LineDrawable as the current Drawable in the model
			pm.setCurrentDrawable(line);
		} else if (clickCode != PaintModel.IGNORE_ALL) {
		// otherwise, if the click code is not IGNORE_ALL there has been an error
			throw new IllegalStateException("Model in illegal mouse state for mouseDragged.");
		}
		
	}

	@Override
	public void mouseMoved(Point p, PaintModel pm) throws Throwable {
		// just check mode, otherwise nothing to do in this mode
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("Illegal model state");
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

	@Override
	public void mousePressed(Point p, boolean isRightClick, PaintModel pm)
			throws Throwable {
		// if wrong mode: error
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("Illegal model state");
		}
		// get the click code
		int clickCode = pm.getClickCode();
		if (clickCode == PaintModel.NO_CLICKS) {
			// if this is the first click, then set the initial point,
			// set the click code to one click,
			// and tell whether it was a right click
			pm.setInitialPoint(p);
			pm.setIsRightClick(isRightClick);
			pm.setClickCode(PaintModel.ONE_CLICK);
		} else if (clickCode == PaintModel.ONE_CLICK) {
			// if this is the second click, then set the click code to ignore all
			// and set the current drawable to the default do-nothing drawable
			pm.setClickCode(PaintModel.IGNORE_ALL);
			pm.setCurrentDrawable(Drawable.NOTHING);
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			// otherwise, there is an illegal click code, throw an exception
			throw new IllegalStateException("Illegal click code for LineTool");
		}
		// increment the click count
		pm.incrementClickCount();
		
	}

	@Override
	public void mouseReleased(Point p, PaintModel pm) throws Throwable {
		// decrement the click count
		pm.decrementClickCount();
		// if wrong mode: error
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset()) {
			throw new IllegalStateException("Illegal model state");
		}
		// get the click code
		int clickCode = pm.getClickCode();
		if (clickCode == PaintModel.ONE_CLICK) {
			// if there is a valid line that has been dragged
			// call mouseDragged with the current point to force
			// the model to have a LineDrawable with the current point
			mouseDragged(p, pm);
			// tell the LineDrawable that it is the final product and not a step along the way
			LineDrawable d = (LineDrawable) pm.getCurrentDrawing();
			d.thisIsFinalDrawable();
			// send the new final Drawable to the PaintModel
			pm.finalizeDrawing(d);
			// set the click code to ignore all (reset at the end of this method)
			clickCode = PaintModel.IGNORE_ALL;
		} else if (clickCode != PaintModel.IGNORE_ALL) {
			// if illegal click code, error
			throw new IllegalStateException("Illegal click code for LineTool");
		}
		
		if (clickCode == PaintModel.IGNORE_ALL) {
			pm.setClickCode(PaintModel.NO_CLICKS);
		}
	}

}
