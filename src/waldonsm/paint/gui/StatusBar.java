package waldonsm.paint.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The panel that will be along the bottom of the PaintFrame GUI with the ColorChangePanel and status.
 * @author Shawn Waldon
 *
 */
public final class StatusBar extends JPanel {

	/**
	 * to shut up the warnings in eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	private final ColorChangePanel colorPanel;
	private final JLabel mouseLocation = new JLabel();
	
	/**
	 * Creates a new StatusBar that will be attached to the given PaintFrame
	 * @param frame
	 */
	public StatusBar(PaintFrame frame) {
		super(new BorderLayout());
		frame.setMouseStatusListener(new MouseTracker());
		colorPanel = new ColorChangePanel(frame);
		add(colorPanel, BorderLayout.WEST);
		add(mouseLocation, BorderLayout.EAST);
	}
	
	/**
	 * The MouseMotionListener that will monitor the mouse and update the mouse location in the StatusBar
	 * @author Shawn Waldon
	 *
	 */
	private class MouseTracker extends MouseAdapter {
		
		private int x;
		private int y;
		
		/**
		 * Sets the text of the label to the point the mouse is at
		 */
		public void mouseMoved(MouseEvent e) {
			mouseLocation.setText("" + e.getPoint().x + ", " + e.getPoint().y);
		}
		
		/**
		 * Sets the initial point of the rectangle, and sets the label to the dimensions mode
		 */
		public void mousePressed(MouseEvent e) {
			x = e.getPoint().x;
			y = e.getPoint().y;
			mouseDragged(e);
		}
		
		/**
		 * Sets the text back to the point when the mouse is released
		 */
		public void mouseReleased(MouseEvent e) {
			mouseMoved(e);
		}
		
		/**
		 * Sets the text of the label to the dimensions of the rectangle dragged
		 */
		public void mouseDragged(MouseEvent e) {
			mouseLocation.setText("" + Math.abs(e.getPoint().x - x) + " x " + Math.abs(e.getPoint().y - y));
		}
	}
	
}
