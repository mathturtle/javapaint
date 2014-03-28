package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;

public class RectangleDrawable implements Drawable {
	
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
	
	private static final String DEFAULT_NAME = "Recatangle";
	
	private final String name;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private boolean isRoundedRect;
	private boolean isFilledRect;
	
	private Color borderColor;
	private Color fillColor;
	
	private int arcHeight;
	private int arcWidth;

	

	public RectangleDrawable(int x, int y, int width, int height,
			boolean isFilledRect, Color borderColor,
			Color fillColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isFilledRect = isFilledRect;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		
		name = DEFAULT_NAME + getCount();
	}
	
	public void setRoundedRect() {
		isRoundedRect = true;
		arcHeight = height / 6;
		arcWidth = width / 6;
	}
	
	public void thisIsFinalDrawable() {
		incrementCount();
	}

	public void draw(Graphics2D g) {
		if (isRoundedRect) {
			if (isFilledRect) {
				g.setColor(fillColor);
				g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
			}
			g.setColor(borderColor);
			g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		} else {
			if (isFilledRect) {
				g.setColor(fillColor);
				g.fillRect(x, y, width, height);
			}
			g.setColor(borderColor);
			g.drawRect(x, y, width, height);
		}
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

	public boolean isRoundedRect() {
		return isRoundedRect;
	}

	public void setRoundedRect(boolean isRoundedRect) {
		this.isRoundedRect = isRoundedRect;
	}

	public boolean isFilledRect() {
		return isFilledRect;
	}

	public void setFilledRect(boolean isFilledRect) {
		this.isFilledRect = isFilledRect;
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

	public int getArcHeight() {
		return arcHeight;
	}

	public void setArcHeight(int arcHeight) {
		this.arcHeight = arcHeight;
	}

	public int getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}
	
	

}
