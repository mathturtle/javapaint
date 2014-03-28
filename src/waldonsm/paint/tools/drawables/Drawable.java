package waldonsm.paint.tools.drawables;

import java.awt.Graphics2D;
import java.io.Serializable;

public interface Drawable extends Serializable {
	
	static final Drawable NOTHING = new DefaultDrawable();
	
	void draw(Graphics2D g);
	
	String getName();
	
}
