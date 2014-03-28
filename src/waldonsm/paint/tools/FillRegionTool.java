package waldonsm.paint.tools;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.Collections;
import java.util.List;

import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.drawables.FillRegionDrawable;
/**
 * This tool implements the Fill Region tool of the program.  The area that is initially clicked is filled with a new color
 * based on the click type and the currently selected colors.  It is equivalent to the paint can tool in MS Paint.
 * @author Shawn Waldon
 *
 */
public class FillRegionTool extends DrawingTool {
	
	private static final String TOOL_NAME = "PAINT_CAN_TOOL";
	private static final String TOOL_IMAGE_FILE_NAME = "images/paintcan.png";

	/**
	 * Returns the image file for this tool
	 */
	@Override
	public String getButtonImageFileName() {
		return TOOL_IMAGE_FILE_NAME;
	}

	/**
	 * Returns the empty list: no fill modes for this tool
	 */
	@Override
	public List<File> getFillModeFiles() {
		return Collections.emptyList();
	}

	/**
	 * Returns the tool name.
	 */
	@Override
	public String getToolName() {
		return TOOL_NAME;
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseDragged(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// does nothing for this tool.
	}
	
	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseClicked(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// does nothing for this tool.
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseMoved(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// does nothing for this tool.
	}

	/**
	 * Makes a new PaintDrawable for where the mouse was pressed and adds it to the model.
	 */
	@Override
	public void mousePressed(Point p, boolean isRightClick, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		Color color;
		if (isRightClick)
			color = pm.getSecondaryColor();
		else
			color = pm.getMainColor();
		pm.finalizeDrawing(new FillRegionDrawable(p, color, pm));
	}

	/**
	 * Does nothing for this tool
	 */
	@Override
	public void mouseReleased(Point p, PaintModel pm) throws Throwable {
		if (pm.getShapeMode() != getToolName().hashCode() + getOffset())
			throw new IllegalStateException("PaintModel is in incorrect mode");
		// does nothing for this tool.
	}

}
