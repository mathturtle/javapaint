package waldonsm.paint.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import waldonsm.utils.ExceptionUtils;
import waldonsm.utils.builders.GridBagConstraintsBuilder;

/**
 * A dialog to prompt the user for the dimensions of a new PaintModel.
 * @author Shawn Waldon
 *
 */
public class GetDimensionsDialog extends JDialog {

	private static final GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder(new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));

	/**
	 * to shut up the eclipse warnings
	 */
	private static final long serialVersionUID = 1L;

	private final JTextField heightField = new JTextField(5);
	private final JTextField widthField = new JTextField(5);
	private final JCheckBox shouldHaveBGround = new JCheckBox("Opaque white background");

	private final PaintFrame frame;

	private boolean hasError = false;
	private final JLabel errorLabel = new JLabel("Input numbers only please!");
	private final ActionListener listener;

	/**
	 * Creates a new GetDimensionsDialog on the specified PaintFrame which will either make a new tab, or resize the current tab
	 * @param frame the PaintFrame that this GetDimensionsDialog is modal on
	 * @param shouldResize true if the Dialog should resize the current tab, false if it should create a new tab based on its dimensions
	 */
	public GetDimensionsDialog(PaintFrame frame, boolean shouldResize) {
		super(frame, true);
		this.frame = frame;
		if (shouldResize)
			listener = new ResizeCurrentTabListener();
		else
			listener = new MakeNewTabListener();
		init(!shouldResize);
	}

	/**
	 * initializes the components of the Dialog and puts them in the correct places
	 * @param shouldMakeNewNotResize true if this dialog should be used to make a new panel on the main window, false if it resizes the current panel's model
	 */
	private void init(boolean shouldMakeNewNotResize) {
		setLayout(new GridBagLayout());
		errorLabel.setForeground(Color.red);
		add(new JLabel("Please input the size " + ((shouldMakeNewNotResize) ? "of the new image and indicate whether or not it needs an opaque white background." : "the image should be resized to.")),
				builder.setFill(GridBagConstraints.HORIZONTAL).setGridWidth(4).setWeightX(1.0).setInsets(5, 5, 5, 5).build());
		add(new JLabel("Width: "), builder.setAnchor(GridBagConstraints.NORTHWEST).setLocation(0, 1).setInsetLeft(5).setWeightX(.25).setFill(GridBagConstraints.HORIZONTAL).build());
		add(widthField, builder.setLocation(1, 1).setWeightX(.25).setFill(GridBagConstraints.HORIZONTAL).build());
		add(new JLabel("Height: "), builder.setLocation(2, 1).setWeightX(.25).setFill(GridBagConstraints.HORIZONTAL).build());
		add(heightField, builder.setLocation(3, 1).setFill(GridBagConstraints.HORIZONTAL).setWeightX(.25).setInsetRight(5).build());
		if (shouldMakeNewNotResize)
			add(shouldHaveBGround, builder.setLocation(0, 2).setFill(GridBagConstraints.HORIZONTAL).setWeightX(1.0).setGridWidth(4).build());
		shouldHaveBGround.setSelected(false);
		JButton ok = new JButton("OK");
		ok.addActionListener(listener);
		widthField.addActionListener(listener);
		heightField.addActionListener(listener);
		add(ok, builder.setLocation(1, 4).setGridWidth(2).build());
		pack();
	}

	/**
	 * Resets the dialog before it is shown and then calls the superclass's setVisible method
	 */
	public void setVisible(boolean b) {
		if (b) {
			setLocationRelativeTo(frame);
			remove(errorLabel);
			validate();
			pack();
			hasError = false;
		}
		super.setVisible(b);
	}
	
	/**
	 * Sets the value stored in the height and width text fields
	 * @param height the height to use
	 * @param width the width to use
	 */
	public void setDefaults(int height, int width) {
		heightField.setText(Integer.toString(height));
		widthField.setText(Integer.toString(width));
	}
	
	/**
	 * A listener that adds a new Tab to the PaintFrame
	 * @author Shawn Waldon
	 *
	 */
	private class MakeNewTabListener implements ActionListener {
		/**
		 * If the width and height fields contain numbers, this calls a the PainFrame.addTab method, otherwise does some error handling
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				try {
					int width = Integer.parseInt(widthField.getText());
					int height = Integer.parseInt(heightField.getText());
					frame.addTab(width, height, shouldHaveBGround.isSelected());
					setVisible(false);
				} catch (NumberFormatException nfe) {
					if (!hasError) {
						add(errorLabel, builder.setLocation(0, 3).setWeightX(1.0).setGridWidth(4).setInsets(0, 5, 0, 5).build());
						hasError = true;
						validate();
						pack();
					}
				} 
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(GetDimensionsDialog.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * A listener that resizes the current tab of the PaintFrame
	 * @author Shawn Waldon
	 *
	 */
	private class ResizeCurrentTabListener implements ActionListener {
		/**
		 * If the width and height fields contain numbers, this resizes the current PaintModel to the new dimensions, otherwise it just does
		 * some error handling
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				try {
					int width = Integer.parseInt(widthField.getText());
					int height = Integer.parseInt(heightField.getText());
					frame.resizeCurrentTabTo(width, height);
					setVisible(false);
				} catch (NumberFormatException nfe) {
					if (!hasError) {
						add(errorLabel, builder.setLocation(0, 3).setWeightX(1.0).setGridWidth(4).setInsets(0, 5, 0, 5).build());
						hasError = true;
						validate();
						pack();
					}
				} 
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(GetDimensionsDialog.this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
