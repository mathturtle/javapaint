package waldonsm.paint.gui.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import waldonsm.paint.gui.PaintFrame;
import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.DrawingTool;
import waldonsm.paint.tools.ToolRegister;
import waldonsm.utils.ExceptionUtils;

/**
 * This class serves as the MouseListener/MouseMotionListener for the PaintPanel, reporting mouse presses, mouse releases, and mouse drags to the PaintModel.
 * 
 * @author Shawn Waldon
 *
 */
//Immutable
public final class PaintListener extends MouseAdapter {
	
	private final PaintModel paintModel;
	private final PaintFrame frame;
	private final ToolRegister toolReg;
	
	/**
	 * Not sure if this is platform independent... so I figured a constant would be easy to change
	 */
	public static final int RIGHT_MOUSE_BUTTON = MouseEvent.BUTTON3;
	
	/**
	 * Creates a new PaintListener which forwards information to the given PaintModel and calls repaint and shows error messages on the given PaintFrame.
	 * @param pm the paintModel to update
	 * @param pframe the PaintFrame to repaint and show error dialogs on.
	 */
	public PaintListener(PaintModel pm, PaintFrame pframe) {
		paintModel = pm;
		frame = pframe;
		toolReg = frame.getTools();
	}
	
	/**
	 * Gets the correct DrawingTool and calls its mousePressed method
	 */
	public void mousePressed(MouseEvent e) {
		try {
			DrawingTool currTool = toolReg.getToolFor(paintModel.getShapeMode());
			if (currTool != null) {
				currTool.mousePressed(getUnscaledPoint(e.getPoint(), paintModel.getScaleFactor()), e.getButton() == RIGHT_MOUSE_BUTTON, paintModel);
				frame.repaint();
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Gets the correct DrawingTool and calls its mouseDragged method
	 */
	public void mouseDragged(MouseEvent e) {
		try {
			DrawingTool currTool = toolReg.getToolFor(paintModel.getShapeMode());
			if (currTool != null) {
				currTool.mouseDragged(getUnscaledPoint(e.getPoint(), paintModel.getScaleFactor()), paintModel);
				frame.repaint();
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		try {
			DrawingTool currTool = toolReg.getToolFor(paintModel.getShapeMode());
			if (currTool != null) {
				currTool.mouseClicked(getUnscaledPoint(e.getPoint(), paintModel.getScaleFactor()), paintModel);
				frame.repaint();
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Gets the correct DrawingTool and calls its mouseReleased method
	 */
	public void mouseReleased(MouseEvent e) {
		try {
			DrawingTool currTool = toolReg.getToolFor(paintModel.getShapeMode());
			if (currTool != null) {
				currTool.mouseReleased(getUnscaledPoint(e.getPoint(), paintModel.getScaleFactor()), paintModel);
				frame.repaint();
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Gets the correct DrawingTool and calls its mouseMoved method
	 */
	public void mouseMoved(MouseEvent e) {
		try {
			DrawingTool currTool = toolReg.getToolFor(paintModel.getShapeMode());
			if (currTool != null) {
				currTool.mouseMoved(getUnscaledPoint(e.getPoint(), paintModel.getScaleFactor()), paintModel);
				frame.repaint();
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Takes the scale of the image being displayed into account when drawing to the model.
	 * <P>
	 * This method divides the coordinates of the point by the scale factor and returns a new Point that represents the resulting position
	 * @param p the Point to un-scale
	 * @param scaleFactor the current scale factor of the model
	 * @return a new Point that is at the coordinates of the old point divided by the scale factor
	 */
	private Point getUnscaledPoint(Point p, int scaleFactor) {
		return new Point(p.x / scaleFactor, p.y / scaleFactor);
	}
	
}
