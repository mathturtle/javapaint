package waldonsm.paint.gui.listeners;

import waldonsm.paint.gui.events.NewModelEvent;

/**
 * This must be implemented by all GUI pieces that need notification of a new model becoming active.
 * Each piece of the GUI that implements this interface should register itself with the PaintFrame's addNewModelListener() method.
 * @author shawn
 *
 */
public interface NewModelListener {
	
	/**
	 * This method should cause all changes to the model caused by this component to change the new model as well as changing the state of the new model to reflect the current state of this component.
	 * @param nme the NewModelEvent with a reference to the new model
	 */
	void newModel(NewModelEvent nme);
}
