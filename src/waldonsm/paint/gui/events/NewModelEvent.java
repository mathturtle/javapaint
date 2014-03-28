package waldonsm.paint.gui.events;

import waldonsm.paint.model.PaintModel;

/**
 * This event is to inform all of the GUI that the current model (the model that the user sees) has changed, and to perform all operations on the new model.
 * @author shawn
 *
 */
// Immutable
public class NewModelEvent {
	
	/**
	 * The new model to be used
	 */
	private final PaintModel model;
	
	/**
	 * Creates a new NewModelEvent, with the new model that is passed in
	 * @param theNewModel the new model to be used
	 */
	public NewModelEvent(PaintModel theNewModel) {
		model = theNewModel;
	}
	
	/**
	 * Returns the new model associated with this event or null if no model is open.
	 * @return the new model associated with this event or null if no model is open.
	 */
	public PaintModel getNewModel() {
		return model;
	}
}
