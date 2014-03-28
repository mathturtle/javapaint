package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class InProgressPolygonDrawable implements Drawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int[] xVals;
	private final int[] yVals;
	private final Color lineColor;
	
	public InProgressPolygonDrawable(List<Point> points, Color lineColor) {
		xVals = FinalPolygonDrawable.getXVals(points);
		yVals = FinalPolygonDrawable.getYVals(points);
		this.lineColor = lineColor;
	}

	public void draw(Graphics2D g) {
		g.setColor(lineColor);
		for (int i = 0; i < xVals.length -1; i++) {
			g.drawLine(xVals[i], yVals[i], xVals[i+1], yVals[i+1]);
		}

	}

	public String getName() {
		return null;
	}

}
