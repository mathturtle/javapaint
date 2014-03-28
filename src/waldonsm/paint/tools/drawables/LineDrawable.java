package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * A Drawable for drawing lines between two given points.
 * @author Shawn Waldon
 *
 */
public final class LineDrawable implements Drawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int count = 0;
	
	/**
	 * Gets the count variable (count of final lines drawn, used in naming LineDrawables)
	 * @return
	 */
	private static int getCount() {
		return count;
	}
	
	/**
	 * Increments the count (a new line drawable was created)
	 */
	private static void incrementCount() {
		count++;
	}
	
	private static final String DEFAULT_NAME = "Line";
	
	private final String name;
	
	private final int x1;
	private final int x2;
	private final int y1;
	private final int y2;
	private final Color color;
	
	/**
	 * Creates a new LineDrawable that draws a line between the specified points and with the given color
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param color the color to draw the line
	 */
	public LineDrawable(Point p1, Point p2, Color color) {
		x1 = p1.x;
		x2 = p2.x;
		y1 = p1.y;
		y2 = p2.y;
		this.color = color;
		name = DEFAULT_NAME + getCount();
	}
	
	/**
	 * Tells this LineDrawable that it is in the final drawables list.
	 */
	public void thisIsFinalDrawable() {
		incrementCount();
	}

	/**
	 * Draws the line into the give graphics object
	 */
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}
	
	// used with a potential scaling technique
//	public void drawScaled(Graphics2D g, int scaleFactor) {
//		g.setColor(color);
//		g.drawLine(x1*scaleFactor, y1*scaleFactor, x2*scaleFactor, y2*scaleFactor);
//	}

	/**
	 * Returns the string identifier for this LineDrawable
	 */
	public String getName() {
		return name;
	}

}
