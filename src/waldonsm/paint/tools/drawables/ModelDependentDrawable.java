package waldonsm.paint.tools.drawables;

import waldonsm.paint.model.PaintModel;

public interface ModelDependentDrawable extends Drawable {

	void setModelToUse(PaintModel image);
}
