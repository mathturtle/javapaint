package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * This Drawable implements the behavior of setting a certain set of points to a specific color.  The points may be added individually,
 * unlike many other Drawables, and the points will be drawn onto the image as 1x1 rectangles.
 * @author Shawn Waldon
 *
 */
public class PencilDrawable implements Drawable {

	/**
	 * to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_NAME = "Scribble_";
	
	private static int count = 0;
	
	
	private final ArrayList<Point> points;
	private final Color color;
	private final String name;
	
	/**
	 * Creates a new PencilDrawable with the given Color and initial point
	 * @param initialPoint
	 * @param color
	 */
	public PencilDrawable(Point initialPoint, Color color) {
		points = new ArrayList<Point>();
		points.add(new Point(initialPoint));
		this.color = color;
		name = DEFAULT_NAME + (count++);
	}
	
	/**
	 * Adds the given point to the list of Points to draw
	 * @param p
	 */
	public void addPoint(Point p) {
		points.add(new Point(p));
	}

	/**
	 * Draws the points (or more specifically, lines between the points
	 */
	public void draw(Graphics2D g) {
		g.setColor(color);
		Point prev = points.get(0);
		Point curr = prev;
		g.drawLine(curr.x, curr.y, prev.x, prev.y);
		for (int i = 1; i < points.size(); i++) {
			prev = curr;
			curr = points.get(i);
			g.drawLine(curr.x, curr.y, prev.x, prev.y);
		}
	}

	/**
	 * Returns the name of this Drawable
	 */
	public String getName() {
		return name;
	}

}
