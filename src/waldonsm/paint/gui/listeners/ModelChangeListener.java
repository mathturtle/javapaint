package waldonsm.paint.gui.listeners;


/**
 * An interface to be implemented by parts of the GUI that need to be notified of model changes.
 * @author Shawn Waldon
 *
 */
public interface ModelChangeListener {

	/**
	 * Called on registered ModelChangeListeners to notify them of changes to the model
	 * @param code the code describing the change to the model: should be either <code>PaintModel.CODE_MODEL_CHANGED</code>,
	 * <code>PaintModel.CODE_MODEL_SAVED</code>, <code>PaintModel.CODE_MODEL_SIZE_CHANGED</code>, or <code>PaintModel.CODE_MODEL_RESCALED</code>
	 */
	void modelChanged(int code);
}
