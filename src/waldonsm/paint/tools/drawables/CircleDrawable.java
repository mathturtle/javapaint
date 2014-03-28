package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;

public class CircleDrawable implements Drawable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int count = 0;
	
	private static int getCount() {
		return count;
	}
	
	private static void incrementCount() {
		count++;
	}
	
	private static final String DEFAULT_NAME = "Circle";
	
	private final String name;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private boolean isFilled;
	
	private Color borderColor;
	private Color fillColor;

	public CircleDrawable(int x, int y, int width, int height,
			boolean isFilled, Color borderColor,
			Color fillColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isFilled = isFilled;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		
		name = DEFAULT_NAME + getCount();
	}
	
	public void thisIsFinalDrawable() {
		incrementCount();
	}

	public void draw(Graphics2D g) {
		if (isFilled) {
			g.setColor(fillColor);
			g.fillOval(x, y, width, height);
		}
		g.setColor(borderColor);
		g.drawOval(x, y, width, height);
	}
	
	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	

}
