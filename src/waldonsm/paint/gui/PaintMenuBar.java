package waldonsm.paint.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Contains the code for the menu bar of the Paint program.  Allows the menus to be set up, and the ActionListeners to be added later.
 * This class's constructor makes the Menus and MenuItems, and the methods allow listeners to be added to those buttons.
 * @author Shawn Waldon
 *
 */
public class PaintMenuBar extends JMenuBar {
	
	/**
	 * Serves as text and action command for the New MenuItem
	 */
	public static final String NEW = "New";
	
	/**
	 * Serves as text and action command for the Open MenuItem
	 */
	public static final String OPEN = "Open";
	
	/**
	 * Serves as text and action command for the Save MenuItem
	 */
	public static final String SAVE = "Save";
	
	/**
	 * Serves as text and action command for the Save As MenuItem
	 */
	public static final String SAVE_AS = "Save As...";
	
	/**
	 * Serves as text and action command for the Save All MenuItem
	 */
	public static final String SAVE_ALL = "Save All";
	
	/**
	 * Serves as text and action command for the Close MenuItem
	 */
	public static final String CLOSE = "Close";
	
	/**
	 * Serves as text and action command for the Close All MenuItem
	 */
	public static final String CLOSE_ALL = "Close All";
	
	/**
	 * Serves as text and action command for the Exit MenuItem
	 */
	public static final String EXIT = "Exit";
	
	/**
	 * Serves as text and action command for the Undo MenuItem
	 */
	public static final String UNDO = "Undo";
	
	/**
	 * Serves as text and action command for the Redo MenuItem
	 */
	public static final String REDO = "Redo";
	
	/**
	 * Serves as text and action command for the Resize MenuItem
	 */
	public static final String RESIZE = "Resize";
	
	/**
	 * The title for the File menu
	 */
	private static final String FILE = "File";
	
	/**
	 * The title for the Edit menu
	 */
	private static final String EDIT = "Edit";

	/**
	 * serves to shut up the eclipse warnings
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final JMenuItem newButton = new JMenuItem(NEW);
	private final JMenuItem openButton = new JMenuItem(OPEN);
	private final JMenuItem saveButton = new JMenuItem(SAVE);
	private final JMenuItem saveAsButton = new JMenuItem(SAVE_AS);
	private final JMenuItem saveAllButton = new JMenuItem(SAVE_ALL);
	private final JMenuItem closeButton = new JMenuItem(CLOSE);
	private final JMenuItem closeAllButton = new JMenuItem(CLOSE_ALL);
	private final JMenuItem exitButton = new JMenuItem(EXIT);
	
	private final JMenuItem undoButton = new JMenuItem(UNDO);
	private final JMenuItem redoButton = new JMenuItem(REDO);
	private final JMenuItem resizeButton = new JMenuItem(RESIZE);
	
	/**
	 * Creates a new PaintMenuBar and sets up all the menus
	 */
	public PaintMenuBar() {
		fileMenu();
		editMenu();
	}
	
	/**
	 * Creates, sets up, and adds the File menu
	 */
	private void fileMenu() {
		JMenu fileMenu = new JMenu(FILE);
		add(fileMenu);
		fileMenu.add(newButton);
		fileMenu.addSeparator();
		fileMenu.add(openButton);
		fileMenu.add(saveButton);
		fileMenu.add(saveAsButton);
		fileMenu.add(saveAllButton);
		fileMenu.addSeparator();
		fileMenu.add(closeButton);
		fileMenu.add(closeAllButton);
		fileMenu.addSeparator();
		fileMenu.add(exitButton);
	}
	
	/**
	 * Creates, sets up, and adds the Edit menu
	 */
	private void editMenu() {
		JMenu editMenu = new JMenu(EDIT);
		add(editMenu);
		editMenu.add(undoButton);
		editMenu.add(redoButton);
		editMenu.addSeparator();
		editMenu.add(resizeButton);
	}
	
	/**
	 * Adds the given ActionListener to the New and Open buttons
	 * @param listener the listener
	 */
	public void addOpenListener(ActionListener listener) {
		newButton.addActionListener(listener);
		openButton.addActionListener(listener);
	}
	
	/**
	 * Adds the given ActionListener to the Save and Save As buttons
	 * @param listener the listener
	 */
	public void addSaveListener(ActionListener listener) {
		saveButton.addActionListener(listener);
		saveAsButton.addActionListener(listener);
		saveAllButton.addActionListener(listener);
	}
	
	/**
	 * Adds the given ActionListener to the Close and Close All buttons
	 * @param listener the listener
	 */
	public void addCloseListener(ActionListener listener) {
		closeButton.addActionListener(listener);
		closeAllButton.addActionListener(listener);
	}
	
	/**
	 * Adds the given ActionListener to the Exit button
	 * @param listener the listener
	 */
	public void addExitListener(ActionListener listener) {
		exitButton.addActionListener(listener);
	}
	
	/**
	 * Adds the given ActionListener to the Undo and Redo buttons
	 * @param listener the listener
	 */
	public void addUndoRedoListener(ActionListener listener) {
		undoButton.addActionListener(listener);
		redoButton.addActionListener(listener);
	}
	
	/**
	 * Adds the given ActionListener to the Resize button
	 * @param listener the listener
	 */
	public void addResizeListener(ActionListener listener) {
		resizeButton.addActionListener(listener);
	}
	
	/**
	 * Enables the Save and Save As buttons if the argument is true, otherwise disables them
	 * @param b whether or not the Save and Save As buttons should be enabled
	 */
	public void setSaveButtonsEnabled(boolean b) {
		saveButton.setEnabled(b);
		saveAsButton.setEnabled(b);
		saveAllButton.setEnabled(b);
	}
	
	/**
	 * Enables the Close and Close All buttons if the argument is true, otherwise disables them
	 * @param b whether or not the Close and Close All buttons should be enabled
	 */
	public void setCloseButtonEnabled(boolean b) {
		closeButton.setEnabled(b);
		closeAllButton.setEnabled(b);
		resizeButton.setEnabled(b);
		undoButton.setEnabled(b);
		redoButton.setEnabled(b);
	}
}
