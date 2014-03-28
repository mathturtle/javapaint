package waldonsm.paint.fillmodes;

/**
 * This class represents the fill modes that DrawingTools may have, which control how
 * the DrawingTools do things.
 * @author Shawn Waldon
 *
 */
public abstract class DrawingFillMode {
	
	private int offset = 0;
	
	/**
	 * 
	 * @return
	 */
	public abstract int getFillModeInt();
	
	public final int getOffset() {
		return offset;
	}
	
	public final void setOffset(int o) {
		offset = o;
	}

}
