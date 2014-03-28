package waldonsm.paint.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import waldonsm.paint.gui.events.NewModelEvent;
import waldonsm.paint.gui.listeners.NewModelListener;
import waldonsm.paint.gui.listeners.PaintListener;
import waldonsm.paint.model.PaintModel;
import waldonsm.utils.ExceptionUtils;

/**
 * The class to implement the GUI of a color palate which is used to change the colors used to draw.
 * @author Shawn Waldon
 *
 */
public final class ColorChangePanel extends JPanel implements NewModelListener {

	/**
	 * to shut up the warnings
	 */
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_HEIGHT = 41;
	private static final int DEFAULT_WIDTH = 181;

	private static final int BOX_SIZE = 20;
	private static final int FIRST_BOX_X = 40;
	private static final int MAIN_COLOR_X = 4;
	private static final int MAIN_COLOR_Y = 4;
	private static final int SECOND_COLOR_X = 16;
	private static final int SECOND_COLOR_Y = 16;

	private PaintModel model;

	private Color primaryColor = Color.black;
	private Color secondaryColor = Color.white;

	private int currWidth, currHeight;

	private boolean oddNumColors = false;
	private final PaintFrame frame;
	private final SliderDialog dialog;

	
	private final ArrayList<Color> colors1 = new ArrayList<Color>();
	private final ArrayList<Color> colors2 = new ArrayList<Color>();

	/**
	 * Creates a new ColorChangePanel that will affect the current model of the given frame
	 * @param frame
	 */
	public ColorChangePanel(PaintFrame frame) {
		model = null;
		this.frame = frame;
		dialog = new SliderDialog(frame, 0, 0, this);
		frame.addNewModelListener(this);
		currWidth = DEFAULT_WIDTH;
		currHeight = DEFAULT_HEIGHT;
		setSizeToCurrentDims();
		initColors();
		addMouseListener(new ColorChanger());
	}

	private void setSizeToCurrentDims() {
		setSize(currWidth, currHeight);
		setPreferredSize(new Dimension(currWidth, currHeight));
	}

	/**
	 * Sets up the colors array with the default colors
	 */
	private void initColors() {
		colors1.add(Color.BLACK);
		colors1.add(Color.DARK_GRAY);
		colors1.add(Color.BLUE);
		colors1.add(Color.GREEN);
		colors1.add(Color.YELLOW);
		colors1.add(Color.ORANGE);
		colors1.add(Color.RED);
		colors2.add(Color.WHITE);
		colors2.add(Color.LIGHT_GRAY);
		colors2.add(Color.CYAN);
		colors2.add(Color.MAGENTA);
		colors2.add(Color.PINK);
		colors2.add(Color.GRAY);
		colors2.add(new Color(66,0,99));
	}

	/**
	 * Changes which PaintModel that this ChangeColorPanel updates and sets the model's colors to the current Colors shown on the panel
	 */
	public void newModel(NewModelEvent nme) {
		model = nme.getNewModel();
		if (model != null) {
			model.setMainColor(primaryColor);
			model.setSecondaryColor(secondaryColor);
		}
	}

	/**
	 * Provides the instructions needed to paint this part of the GUI
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// Draw the boxes showing current colors off to the left
		g2.setColor(secondaryColor);
		g2.fillRect(SECOND_COLOR_X, SECOND_COLOR_Y, BOX_SIZE, BOX_SIZE);
		g2.setColor(Color.black);
		g2.drawRect(SECOND_COLOR_X, SECOND_COLOR_Y, BOX_SIZE, BOX_SIZE);
		g2.setColor(primaryColor);
		g2.fillRect(MAIN_COLOR_X, MAIN_COLOR_Y, BOX_SIZE, BOX_SIZE);
		g2.setColor(Color.black);
		g2.drawRect(MAIN_COLOR_X, MAIN_COLOR_Y, BOX_SIZE, BOX_SIZE);

	
		int y = 0;
		for (int i = 0; i < colors1.size(); i++) {
			g2.setColor(colors1.get(i));
			g2.fillRect(FIRST_BOX_X+BOX_SIZE*i, BOX_SIZE*y, BOX_SIZE, BOX_SIZE);
			g2.setColor(Color.black);
			g2.drawRect(FIRST_BOX_X+BOX_SIZE*i, BOX_SIZE*y, BOX_SIZE, BOX_SIZE);
		}
		y = 1;
		for (int i = 0; i < colors2.size(); i++) {
			g2.setColor(colors2.get(i));
			g2.fillRect(FIRST_BOX_X+BOX_SIZE*i, BOX_SIZE*y, BOX_SIZE, BOX_SIZE);
			g2.setColor(Color.black);
			g2.drawRect(FIRST_BOX_X+BOX_SIZE*i, BOX_SIZE*y, BOX_SIZE, BOX_SIZE);
		}
	}

	/**
	 * Tells the SliderDialog to show itself, using the given x and y if a replace is called for
	 * @param x
	 * @param y
	 */
	private void showAddColorDialog(int x, int y) {
		dialog.showWithNewCoords(x, y);
	}

	/**
	 * Adds the given color to this panel's selection of colors
	 * @param c the new color
	 */
	public void addColor(Color c) {
		if (!oddNumColors) {
			currWidth += 20;
			setSizeToCurrentDims();
			colors1.add(c);
			oddNumColors = true;
			getParent().validate();
		} else {
			colors2.add(c);
			oddNumColors = false;
		}
		repaint();
	}

	/**
	 * Replaces the old color at the given coordinates with the new color passed in
	 * @param newColor the new Color
	 * @param x the row of colors
	 * @param y the position along said row
	 */
	public void replaceColor(Color newColor, int x, int y) {
		if ( y == 0) {
			colors1.set(x, newColor);
		} else if ( y == 1) {
			if ( x < colors2.size()) {
				colors2.set(x, newColor);
			} else if ( x == colors2.size()) {
				colors2.add(newColor);
			}
		}
		repaint();
	}

	/**
	 * A listener that takes mouse clicks and changes the colors accordingly
	 * @author Shawn Waldon
	 *
	 */
	private class ColorChanger extends MouseAdapter {
		/**
		 * If the mouse was clicked in the appropriate area for selecting colors, then this method changes the colors accordingly.
		 * If it detects a double click, then it pops up a window to add a new color.
		 */
		public void mouseClicked(MouseEvent me) {
			try {
				Point p = new Point(me.getPoint());
				int x = p.x, y = p.y;
				if (me.getClickCount() == 1) {
					if (x >= 40) { // If the user clicked a point on the color selection part of the panel.
						x -= 40;
						y = y/20;
						x = x/20;
						Color selection = null;
						if (y == 0) {
							selection = colors1.get(x);
						} else if (y == 1) {
							if (x < colors2.size())
								selection = colors2.get(x);
							else return;
						} else {
							return;
						}
						if (me.getButton() == PaintListener.RIGHT_MOUSE_BUTTON) {
							secondaryColor = selection;
							if (model != null)
								model.setSecondaryColor(selection);
						} else {
							primaryColor = selection;
							if (model != null)
								model.setMainColor(selection);
						}
						repaint();
					}
				} else if (me.getClickCount() == 2) {
					if (x >= 40) { // If the user clicked a point on the color selection part of the panel.
						x -= 40;
						y = y/20;
						x = x/20;
						showAddColorDialog(x,y);
					}
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
