package waldonsm.utils.builders;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public final class GridBagConstraintsBuilder {
	
	private int defaultX, defaultY, defaultGridWidth, defaultGridHeight, defaultAnchor, defaultFill, defaultIPadX, defaultIPadY;
	private double defaultWeightX, defaultWeightY;
	private int defaultInsetTop, defaultInsetLeft, defaultInsetRight, defaultInsetBottom;
	
	private int gridX, gridY, gridWidth, gridHeight,anchor,fill,ipadX,ipadY;
	private double weightX,weightY;
	
	private int insetTop, insetLeft, insetRight, insetBottom;
	
	public GridBagConstraintsBuilder() {
		gridX = gridY = 0;
		ipadX = ipadY= 0;
		anchor = GridBagConstraints.CENTER;
		fill = GridBagConstraints.NONE;
		weightX = weightY = 0;
		insetTop = insetLeft = insetRight = insetBottom = 0;
		setDefaults();
	}
	
	public GridBagConstraintsBuilder(GridBagConstraints defaults) {
		defaultX = defaults.gridx;
		defaultY = defaults.gridy;
		defaultGridWidth = defaults.gridwidth;
		defaultGridHeight = defaults.gridheight;
		defaultIPadX = defaults.ipadx;
		defaultIPadY = defaults.ipady;
		defaultAnchor = defaults.anchor;
		defaultFill = defaults.fill;
		defaultWeightX = defaults.weightx;
		defaultWeightY = defaults.weighty;
		Insets insets = defaults.insets;
		defaultInsetTop = insets.top;
		defaultInsetLeft = insets.left;
		defaultInsetBottom = insets.bottom;
		defaultInsetRight = insets.right;
		restoreDefaults();
	}
	
	public void setDefaults() {
		defaultX = gridX;
		defaultY = gridY;
		defaultGridWidth = gridWidth;
		defaultGridHeight = gridHeight;
		defaultAnchor = anchor;
		defaultFill = fill;
		defaultIPadX = ipadX;
		defaultIPadY = ipadY;
		defaultWeightX = weightX;
		defaultWeightY = weightY;
		defaultInsetTop = insetTop;
		defaultInsetBottom = insetBottom;
		defaultInsetLeft = insetLeft;
		defaultInsetRight = insetRight;
	}
	
	public void restoreDefaults() {
		gridX = defaultX;
		gridY = defaultY;
		gridWidth = defaultGridWidth;
		gridHeight = defaultGridHeight;
		anchor = defaultAnchor;
		fill = defaultFill;
		ipadX = defaultIPadX;
		ipadY = defaultIPadY;
		weightX = defaultWeightX;
		weightY = defaultWeightY;
		insetTop = defaultInsetTop;
		insetBottom = defaultInsetBottom;
		insetLeft = defaultInsetLeft;
		insetRight = defaultInsetRight;
	}
	
	public GridBagConstraintsBuilder setLocation(int x, int y) {
		this.gridX = x;
		this.gridY = y;
		return this;
	}
	
	public GridBagConstraintsBuilder setGridWidthAndHeight(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		return this;
	}
	
	public GridBagConstraintsBuilder setXAndYWeights(double weightX, double weightY) {
		this.weightX = weightX;
		this.weightY = weightY;
		return this;
	}
	
	public GridBagConstraintsBuilder setInsets(Insets insets) {
		setInsets(insets.top, insets.left, insets.bottom, insets.right);
		return this;
	}
	
	public GridBagConstraintsBuilder setInsets(int top, int left, int bottom, int right) {
		this.insetTop = top;
		this.insetLeft = left;
		this.insetRight = right;
		this.insetBottom = bottom;
		return this;
	}
	
	public GridBagConstraintsBuilder setIPadXAndY(int ipadX, int ipadY) {
		this.ipadX = ipadX;
		this.ipadY = ipadY;
		return this;
	}

	public int getGridX() {
		return gridX;
	}

	public GridBagConstraintsBuilder setGridX(int gridX) {
		this.gridX = gridX;
		return this;
	}

	public int getGridY() {
		return gridY;
	}

	public GridBagConstraintsBuilder setGridY(int gridY) {
		this.gridY = gridY;
		return this;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public GridBagConstraintsBuilder setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
		return this;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public GridBagConstraintsBuilder setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
		return this;
	}

	public int getAnchor() {
		return anchor;
	}

	public GridBagConstraintsBuilder setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	public int getFill() {
		return fill;
	}

	public GridBagConstraintsBuilder setFill(int fill) {
		this.fill = fill;
		return this;
	}

	public int getIpadX() {
		return ipadX;
	}

	public GridBagConstraintsBuilder setIpadX(int ipadX) {
		this.ipadX = ipadX;
		return this;
	}

	public int getIpadY() {
		return ipadY;
	}

	public GridBagConstraintsBuilder setIpadY(int ipadY) {
		this.ipadY = ipadY;
		return this;
	}

	public double getWeightX() {
		return weightX;
	}

	public GridBagConstraintsBuilder setWeightX(double weightX) {
		this.weightX = weightX;
		return this;
	}

	public double getWeightY() {
		return weightY;
	}

	public GridBagConstraintsBuilder setWeightY(double weightY) {
		this.weightY = weightY;
		return this;
	}

	public int getInsetTop() {
		return insetTop;
	}

	public GridBagConstraintsBuilder setInsetTop(int insetTop) {
		this.insetTop = insetTop;
		return this;
	}

	public int getInsetLeft() {
		return insetLeft;
	}

	public GridBagConstraintsBuilder setInsetLeft(int insetLeft) {
		this.insetLeft = insetLeft;
		return this;
	}

	public int getInsetRight() {
		return insetRight;
	}

	public GridBagConstraintsBuilder setInsetRight(int insetRight) {
		this.insetRight = insetRight;
		return this;
	}

	public int getInsetBottom() {
		return insetBottom;
	}

	public GridBagConstraintsBuilder setInsetBottom(int insetBottom) {
		this.insetBottom = insetBottom;
		return this;
	}

	public GridBagConstraints build() {
//		System.out.printf("%d\t%d\t%d\t%d\t%f\t%f\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n", gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insetTop, insetLeft, insetBottom, insetRight, ipadX, ipadY);
		GridBagConstraints result =new GridBagConstraints(gridX,gridY,gridWidth,gridHeight,weightX,weightY,anchor,fill,new Insets(insetTop,insetLeft,insetBottom,insetRight),ipadX,ipadY);
		restoreDefaults();
		return result;
	}

}
