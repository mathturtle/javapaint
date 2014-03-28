package waldonsm.paint.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import waldonsm.paint.gui.events.NewModelEvent;
import waldonsm.paint.gui.listeners.NewModelListener;
import waldonsm.paint.model.PaintModel;
import waldonsm.paint.model.PaintModelUtils;
import waldonsm.paint.tools.ToolRegister;
import waldonsm.utils.ExceptionUtils;

/**
 * The frame of the GUI for the Java Paint program.
 * 
 * @author Shawn Waldon
 *
 */
public class PaintFrame extends JFrame {

	/**
	 * To shut up the eclipse warnings
	 */
	private static final long serialVersionUID = 1L;



	private final CopyOnWriteArrayList<NewModelListener> listeners = new CopyOnWriteArrayList<NewModelListener>();

	private transient final SaveModelListener saveListener = new SaveModelListener();
	private transient final NewPanelCreator openListener = new NewPanelCreator();
	private transient final TabbedPaneListener tabChangeListener = new TabbedPaneListener();
	private transient final CloseModelListener closeListener = new CloseModelListener();
	private transient final UndoRedoListener undoListener = new UndoRedoListener();
	private transient MouseAdapter mouseStatusListener;

	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final GetDimensionsDialog newModelDialog = new GetDimensionsDialog(this, false);
	private final GetDimensionsDialog resizeDialog = new GetDimensionsDialog(this, true);
	
	private final ToolRegister tools = new ToolRegister();



	private PaintMenuBar menuBar;


	/**
	 * Creates a new paint frame and sets its fields --the init() method is called to create the GUI
	 */
	private PaintFrame() {
		super("JavaPaint");
		init();
	}

	/**
	 * initializes the GUI of the paintFrame
	 */
	private void init() {
		addNewModelListener(saveListener);
		addNewModelListener(undoListener);
		setJMenuBar(createMenuBar());
		add(new SetShapeAndFillPanel(this), BorderLayout.WEST);
		add(new StatusBar(this), BorderLayout.SOUTH);
		//		// everything added to this method should go above here
		tabbedPane.addChangeListener(tabChangeListener);
		add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.setPreferredSize(new Dimension(500,500));
		tabbedPane.setSize(new Dimension(500,500));
		addWindowListener(new WindowCloseListener());
		pack();
	}

	/**
	 * Creates and returns the JMenuBar setup for the PaintFrame
	 * @return the JMenuBar for the PaintFrame
	 */
	private JMenuBar createMenuBar() {
		menuBar = new PaintMenuBar();
		menuBar.addSaveListener(saveListener);
		menuBar.addOpenListener(openListener);
		menuBar.addCloseListener(closeListener);
		menuBar.addExitListener(new ExitListener());
		menuBar.addUndoRedoListener(undoListener);
		menuBar.addResizeListener(new ResizeListener());
		menuBar.setCloseButtonEnabled(false);
		menuBar.setSaveButtonsEnabled(false);
		return menuBar;
	}


	/**
	 * Adds a NewModelListener to the list of listeners notified when a new model is being used.
	 * @param nml the NewModelListener to be added to the list
	 */
	public final void addNewModelListener(NewModelListener nml) {
		listeners.add(nml);
	}

	/**
	 * Removes a NewModelListener from the list of listeners notified when a new model is being used if it is in the list, otherwise does nothing.
	 * @param nml the NewModelListener to be removed
	 */
	public void removeNewModelListener(NewModelListener nml) {
		listeners.remove(nml);
	}

	/**
	 * Fires an event to notify all NewModelListeners that a new model is being used.
	 * @param model the new model that will be used until this method is called again
	 */
	final void fireNewModelEvent(PaintModel model) {
		NewModelEvent event = new NewModelEvent(model);
		for (NewModelListener listener : listeners) {
			listener.newModel(event);
		}
	}

	/**
	 * Creates, sets up, and shows the PaintFrame
	 */
	public static void showPaintFrame() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PaintFrame frame = new PaintFrame();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Adds a tab containing a new PaintPanel with the specified properties
	 * @param h the height of the new image
	 * @param w the width of the new image
	 * @param fillWhite true if the new image should have a white background
	 */
	public void addTab(int w, int h, boolean fillWhite) {
		PaintPanel panel = new PaintPanel(w, h, this, tabbedPane, fillWhite);
		panel.addMouseMotionListener(mouseStatusListener);
		JScrollPane scrollPane = new JScrollPane(panel);
		panel.setWrapperScrollPane(scrollPane);
		tabbedPane.add(panel.getModel().getName(), scrollPane);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() -1);
	}

	/**
	 * Adds a tab containing a new PaintPanel with the imag from the given file
	 * @param f the file to read the image from
	 * @throws IOException if the file does not exist or cannot be accessed
	 * @throws ClassNotFoundException if a zdlif file cannot be read due to an object I/O error
	 */
	public void addTab(File f) throws IOException, ClassNotFoundException {
		PaintPanel panel = new PaintPanel(f, this, tabbedPane);
		panel.addMouseMotionListener(mouseStatusListener);
		JScrollPane scrollPane = new JScrollPane(panel);
		panel.setWrapperScrollPane(scrollPane);
		tabbedPane.add(panel.getModel().getName(), scrollPane);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() -1);
	}

	/**
	 * Iterates through the open tabs and finds all the unsaved PaintModels, returning them in an ArrayList
	 * @return an ArrayList containing all of the unsaved PaintModels
	 */
	private ArrayList<PaintModel> getUnsavedModels() {
		ArrayList<PaintModel> unsavedModels = new ArrayList<PaintModel>();
		int tabs = tabbedPane.getTabCount();
		PaintPanel p;
		for (int i = 0; i < tabs; i++) {
			p = (PaintPanel) ((JScrollPane)tabbedPane.getComponentAt(i)).getViewport().getView();
			if (p.getModel().hasChanged()) {
				unsavedModels.add(p.getModel());
			}
		}
		return unsavedModels;
	}

	/**
	 * Closes the model at the given index in the JTabbedPane <P>
	 * Also, since the JTabbedPane class only fires a ChangeEvent when the index changes, not when the tab changes, this method simulates firing
	 * the event by calling the tabChangeListener's stateChanged method with a null argument
	 * @param index the index to close
	 */
	private void closeModel(int index) {
		tabbedPane.remove(index);
		tabChangeListener.stateChanged(null);
	}

	/**
	 * Sets the listener that will be used to show a mouse status on the status bar
	 * @param l the MouseMotionListener from the status bar that will be used to update the mouse info on the status bar
	 */
	public void setMouseStatusListener(MouseAdapter l) {
		mouseStatusListener = l;
	}
	
	/**
	 * Returns the ToolRegister being used by this instance of the PaintFrame
	 * @return the ToolRegister being used by this instance of the PaintFrame
	 */
	public ToolRegister getTools() {
		return tools;
	}
	
	private void closeCurrentModel() {
		JScrollPane js  = (JScrollPane) tabbedPane.getSelectedComponent();
		PaintPanel panel = (PaintPanel) js.getViewport().getView();
		PaintModel model = panel.getModel();
		if (model.hasChanged()) {
			int selection = JOptionPane.showOptionDialog(PaintFrame.this, "This image is unsaved.\nClose it anyway?", "WARNING: Unsaved Image", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Don't Save", "Cancel", "Save"}, "Save");
			if (selection == 0) { // User selected "Don't Save"
				panel.closeModel();
				closeModel(tabbedPane.getSelectedIndex());
			} else if (selection == 1 || selection == JOptionPane.CLOSED_OPTION) { // User selected "Cancel" or close the dialog
				// DO NOTHING
			} else if (selection == 2) { // User selected "Save"
				if (PaintModelUtils.saveModelToFile(model, "Save", PaintFrame.this)) { // If save was successful
					panel.closeModel();
					closeModel(tabbedPane.getSelectedIndex());
				}
			} else {
				throw new IllegalStateException("HOW THE &*(%*^$)%#$*!!!!!");
			}
		} else { // If the model has already been saved
			panel.closeModel();
			closeModel(tabbedPane.getSelectedIndex());
		}
	}
	

	/**
	 * This method resizes the current PaintModel to a new size based on the given dimensions
	 * @param width the new width for the PaintModel's image
	 * @param height the new height for the PaintModel's image
	 */
	public void resizeCurrentTabTo(int width, int height) {
		saveListener.model.setSize(width, height);
	}

	/**
	 * A listener to save models to a file
	 * @author Shawn Waldon
	 *
	 */
	private final class SaveModelListener implements ActionListener, NewModelListener {

		private PaintModel model;

		public SaveModelListener() {
			model = null;
		}

		/**
		 * Implements the Save, Save As, and Save All commands, with the following results:
		 * If the user clicked Save, it tries to save the model to a previously written file for that model, otherwise acts like Save As.
		 * If the user clicked Save As, shows a JFileChooser and uses a user specified file destination.
		 * If the user clicked Save All, it gets a list of all the unsaved models and performs a Save with each one.
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				if (! e.getActionCommand().equals(PaintMenuBar.SAVE_ALL)) {
					PaintModelUtils.saveModelToFile(model, e.getActionCommand(), PaintFrame.this);
				} else {
					ArrayList<PaintModel> models = getUnsavedModels();
					for (PaintModel pm : models) {
						PaintModelUtils.saveModelToFile(pm, PaintMenuBar.SAVE, PaintFrame.this);
					}
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Changes the SaveModelListener's current model, so that it saves the current model
		 */
		public void newModel(NewModelEvent e) {
			model = e.getNewModel();
		}

	}
	
	private class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new WindowCloseListener().windowClosing(null);
		}
	}

	/**
	 * The listener that will ask the user to save unused images when they attept to close the window.
	 * @author Shawn Waldon
	 *
	 */
	private class WindowCloseListener extends WindowAdapter {

		/**
		 * Gets the unsaved images, and if there are any, asks the user if they wish to save before exiting.
		 */
		public void windowClosing(WindowEvent e) {
			try {
				ArrayList<PaintModel> unsavedModels = getUnsavedModels();
				if (unsavedModels.size() > 0) {
					int selection = JOptionPane.showOptionDialog(PaintFrame.this, "There are unsaved images.\nExit?", "WARNING: Unsaved Images", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {"Don't Save", "Cancel", "Save"}, "Save");
					if (selection == 0) { // User selected "Don't Save"
						System.exit(0);
					} else if (selection == 1 || selection == JOptionPane.CLOSED_OPTION) { // User selected "Cancel"
						// DO NOTHING
					} else if (selection == 2) { // User selected "Save"
						boolean exit = true;
						for (PaintModel model : unsavedModels) {
							exit &= PaintModelUtils.saveModelToFile(model, "Save", PaintFrame.this);
						}
						if (exit) {
							System.exit(0);
						}
					} else {
						throw new IllegalStateException("HOW THE &*(%*^$)%#$*!!!!!");
					}
				} else {
					System.exit(0);
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * The listener that will handle the New and Open options on the File menu
	 * @author Shawn Waldon
	 *
	 */
	private class NewPanelCreator implements ActionListener {
		/**
		 * Shows a GetDimensionsDialog if the New option on the menu is selected, or open a file if the Open option is selected
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				if (e.getActionCommand().equals(PaintMenuBar.NEW)) {
					newModelDialog.setVisible(true);
				} else if (e.getActionCommand().equals(PaintMenuBar.OPEN)) {
					PaintModelUtils.openModelFromFiles(PaintFrame.this);
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
				t.printStackTrace(System.err);
			}
		}
	}

	/**
	 * The listener that fires NewModelEvents when the tab of the JTabbedPane changes.
	 * @author Shawn Waldon
	 *
	 */
	private class TabbedPaneListener implements ChangeListener {
		/**
		 * Fires NewModelEvents when the tab of the JTabbedPane changes <P>
		 * must take a null argument, because that is how a closing tab will be simulated.
		 */
		public void stateChanged(ChangeEvent ce) {
			try {
				boolean hasOpenModels = tabbedPane.getTabCount() != 0;
				if (tabbedPane.getTabCount() > 0) {
					JScrollPane js  = (JScrollPane) tabbedPane.getSelectedComponent();
					PaintPanel panel = (PaintPanel) js.getViewport().getView();
					panel.removeMouseMotionListener(mouseStatusListener);
					panel.addMouseMotionListener(mouseStatusListener);
					panel.removeMouseListener(mouseStatusListener);
					panel.addMouseListener(mouseStatusListener);
					fireNewModelEvent(panel.getModel());
				} else {
					fireNewModelEvent(null);
				}
				//				System.out.println(tabbedPane.getTabCount());
				menuBar.setSaveButtonsEnabled(hasOpenModels);
				menuBar.setCloseButtonEnabled(hasOpenModels);
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * A listener that will run the close buttons "Close" and "Close All"
	 * @author Shawn Waldon
	 *
	 */
	private class CloseModelListener implements ActionListener {

		/**
		 * If the actionCommand is "Close", closes the currently open model.
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				if (e.getActionCommand().equals(PaintMenuBar.CLOSE)) {
					closeCurrentModel();
				} else if (e.getActionCommand().equals(PaintMenuBar.CLOSE_ALL)) {
					while (tabbedPane.getTabCount() > 0) {
						closeCurrentModel();
					}
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * The listener for undoing and redoing changes to the model.
	 * @author Shawn Waldon
	 *
	 */
	private class UndoRedoListener implements ActionListener, NewModelListener {
		
		private PaintModel model = null;
		
		/**
		 * Forwards the action command to the doUndoRedo method for implementing undo and redo actions.
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				doUndoRedo(e.getActionCommand());
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
		
		/**
		 * If the command is PaintMenuBar.UNDO, then undoes the last action via the model's undoLastAction method, otherwise
		 * if the command is PaintMenuBar.REDO, then redoes the last undo via the model's redoLastAction method.
		 * @param command the command, should be either PaintMenuBar.UNDO or PaintMenuBar.REDO
		 */
		public void doUndoRedo(String command) {
			if (command.equals(PaintMenuBar.UNDO)) {
				if (model != null)
					model.undoLastAction();
			} else if (command.equals(PaintMenuBar.REDO)) {
				if (model != null)
					model.redoLastAction();
			} else throw new IllegalArgumentException("Illegal argument to doUndoRedo: " + command);
			repaint();
		}
		
		/**
		 * Receives new model events and sets the new model
		 */
		public void newModel(NewModelEvent e) {
			model = e.getNewModel();
		}
	}
	
	/**
	 * The listener to the Resize button
	 * @author Shawn Waldon
	 *
	 */
	private class ResizeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				PaintModel model = saveListener.model;
				resizeDialog.setDefaults(model.getHeight(), model.getHeight());
				resizeDialog.setVisible(true);
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(PaintFrame.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

}
