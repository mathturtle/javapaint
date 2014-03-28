package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

public class FinalPolygonDrawable implements Drawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_NAME = "Polygon";
	
	private static int counter = 0;
	
	private static int getCount() {
		return ++counter;
	}
	
	private final String name;
	private final Polygon polygon;
	private final int[] xs;
	private final int[] ys;
	private Color borderColor;
	private Color fillColor;
	private boolean fillShape;

	public FinalPolygonDrawable(List<Point> clicksList, Color borderColor, Color fillColor, boolean fill) {
		xs = getXVals(clicksList);
		ys = getYVals(clicksList);
		polygon = new Polygon(xs, ys, clicksList.size());
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		fillShape = fill;
		name = DEFAULT_NAME + getCount();
	}
	
	static int[] getXVals(List<Point> points) {
		int[] xs = new int[points.size()];
		for (int i = 0; i < xs.length; i++) {
			xs[i] = points.get(i).x;
		}
		return xs;
	}
	
	static int[] getYVals(List<Point> points) {
		int[] ys = new int[points.size()];
		for (int i = 0 ; i < ys.length; i++) {
			ys[i] = points.get(i).y;
		}
		return ys;
	}

	public void draw(Graphics2D g) {
		if (fillShape) {
			g.setColor(fillColor);
			g.fill(polygon);
		}
		g.setColor(borderColor);
		g.draw(polygon);

	}

	public String getName() {
		return name;
	}

}
