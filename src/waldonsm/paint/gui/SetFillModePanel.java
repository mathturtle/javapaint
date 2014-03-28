package waldonsm.paint.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import waldonsm.paint.gui.events.NewModelEvent;
import waldonsm.paint.gui.listeners.NewModelListener;
import waldonsm.paint.model.PaintModel;
import waldonsm.paint.tools.DrawingTool;
import waldonsm.paint.tools.ToolRegister;
import waldonsm.utils.ExceptionUtils;

/**
 * This class is the panel which will have buttons to control the fill mode of the PaintModel.
 * @author Shawn Waldon
 *
 */
public class SetFillModePanel extends JPanel implements NewModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PaintModel model;
	private final PaintFrame frame;
	private final ToolRegister toolReg;

	private ArrayList<JButton> buttonsList;
	
	private HashMap<List<File>, ArrayList<JButton>> buttonsCache;
	
	private int fillMode;

	private final ActionListener listener;
	
	/**
	 * Creates a new SetFillModePanel which will throw error dialogs to the specified PaintFrame
	 * @param frame the PaintFrame to throw error dialogs up to
	 */
	public SetFillModePanel(PaintFrame frame) {
		this.frame = frame;
		toolReg = frame.getTools();
		fillMode = 0;
		model = null;
		listener = new SetFillButtonListener();
		buttonsList = new ArrayList<JButton>();
		buttonsCache = new HashMap<List<File>, ArrayList<JButton>>();
		// make sure this panel isn't resizing shrinking to nothing on us
		setMinimumSize(new Dimension(70,100));
		setPreferredSize(new Dimension(70,100));
		init();
	}
	
	/**
	 * Initializes the SetFillModePanel's GUI, with its three buttons
	 */
	private void init() {
		frame.addNewModelListener(this);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	/**
	 * Resets the SetFillModePanel with new buttons from the given code's DrawingTool
	 * <P>
	 * Gets the DrawingTool for the given code and gets its list of files for fill modes.
	 * Creates a button from each of these image files and sets up the SetFillModePanel to
	 * use these buttons. 
	 * @param drawingToolCode
	 */
	public void changeButtonsTo(int drawingToolCode) {
		DrawingTool d = toolReg.getToolFor(drawingToolCode);
		if (d != null) {
			List<File> fileList = d.getFillModeFiles();

			if (buttonsCache.containsKey(fileList)) {
				buttonsList = buttonsCache.get(fileList);
			} else {
				buttonsList = new ArrayList<JButton>();
				for (int i = 0; i < fileList.size(); i++) {
					// commented code tested to see size of images here
//					try {
//						Image im =ImageIO.read(fileList.get(i));
//						System.out.println("H: " + im.getHeight(null));
//						System.out.println("W: " + im.getWidth(null) + "\n");
//					} catch (Exception e) {
//						
//					}
					JButton button = new JButton(new ImageIcon(fileList.get(i).getAbsolutePath()));
					button.setActionCommand("" + i);
					button.addActionListener(listener);
					buttonsList.add(button);
				}
				buttonsCache.put(fileList, buttonsList);
			}
			removeAll();
			setLayout(new FlowLayout());
			for (JButton b : buttonsList) {
				add(b);
			}
			fillMode = 0;
			if (model != null)
				model.setFillMode(0);
			validate();
			repaint();
		}
	}
	
	/**
	 * Returns the current fillMode of the SetFillModePanel
	 * @return the current fillMode of the SetFillModePanel
	 */
	public int getFillMode() {
		return fillMode;
	}
	
	public void setFillMode(Integer newMode) {
		if (newMode == null) {
			fillMode = 0;
		} else {
			fillMode = newMode.intValue();
		}
		if (model != null) {
			model.setFillMode(fillMode);
		}
	}

	/**
	 * Specified by NewModelListener, changes this SetFillModePanel to change the new model. (Also sets the new model to be consistent with the current state of the panel's buttons.
	 */
	public void newModel(NewModelEvent nme) {
		try {
			model = nme.getNewModel();
			if (model != null) {
				model.setFillMode(fillMode);
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * This inner class actually changes the fill mode of the model to the new mode given by the button pressed. 
	 * @author Shawn Waldon
	 *
	 */
	private class SetFillButtonListener implements ActionListener {
		
		/**
		 * Changes the fill mode of the model to the fill mode specified by the button pressed.
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				fillMode = Integer.parseInt(e.getActionCommand());
				if (model != null) {
					model.setFillMode(fillMode);
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}	
	}

}
