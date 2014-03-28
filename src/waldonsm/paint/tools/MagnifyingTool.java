package waldonsm.paint.tools;

import java.awt.Point;
import java.io.File;
import java.util.Collections;
import java.util.List;

import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.DrawingTool;

/**
 * This tool implements the zooming features of the PaintModel.  It toggles the model between two states, ZOOMED_OUT and ZOOMED_IN.
 * NOTE: if the model is found in any other state, an exception will be thrown
 * @author Shawn Waldon
 *
 */
public class MagnifyingTool extends DrawingTool {
	
	private static final String TOOL_IMAGE_FILE_NAME = "images/magnifyingGlass2.png";
	private static final String TOOL_NAME = "MAGNIFYINGTOOL";
	
	/**
	 * 500% zoom factor
	 */
	public static final int ZOOMED_IN = 5;
	
	/**
	 * 100% zoom factor
	 */
	public static final int ZOOMED_OUT = 1;

	/**
	 * Returns the image filename for this tool
	 */
	@Override
	public String getButtonImageFileName() {
		return TOOL_IMAGE_FILE_NAME;
	}

	/**
	 * Returns an empty list... for now
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
	 * Toggles the zoom between zoomed in and zoomed out
	 */
	@Override
	public void mouseClicked(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		
		if (pm.getScaleFactor() == ZOOMED_IN) {
			pm.setScaleFactor(ZOOMED_OUT);
		} else if (pm.getScaleFactor() == ZOOMED_OUT) {
			pm.setScaleFactor(ZOOMED_IN);
		} else {
			throw new IllegalStateException("Unsupported zoom level");
		}
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseDragged(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
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
	 * Does nothing for this tool
	 */
	@Override
	public void mousePressed(Point p, boolean isRightClick, PaintModel pm)
			throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseReleased(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
	}

}
