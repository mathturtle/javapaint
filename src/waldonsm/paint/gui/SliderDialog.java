package waldonsm.paint.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import waldonsm.utils.ExceptionUtils;
import waldonsm.utils.builders.GridBagConstraintsBuilder;

/**
 * A class that will be a dialog for color selection, with JSliders and a color viewing area
 * @author Shawn Waldon
 *
 */
public class SliderDialog extends JDialog implements ActionListener, ChangeListener, DocumentListener {

	/**
	 * to shut up the warnings
	 */
	private static final long serialVersionUID = 1L;

	private static final GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder(new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
	
	private final PaintFrame frame;

	private final JSlider aSlider = new JSlider(0,255);
	private final JSlider rSlider = new JSlider(0,255);
	private final JSlider gSlider = new JSlider(0,255);
	private final JSlider bSlider = new JSlider(0,255);
	
	private final JTextField aField = new JTextField(3);
	private final JTextField rField = new JTextField(3);
	private final JTextField gField = new JTextField(3);
	private final JTextField bField = new JTextField(3);
	
	private final JButton OK_BUTTON = new JButton("OK");
	private final JButton CANCEL_BUTTON = new JButton("Cancel");
	private final JCheckBox checkBox = new JCheckBox("Overwrite old color");
	

	private int x, y;
	private final ColorChangePanel changePanel;
	
	private boolean changing = false;
	
	/**
	 * Creates a new SliderDialog modal on the specified frame, with specified x, y, and reporting ColorChangePanel.
	 * @param frame the PaintFrame to show this Dialog on
	 * @param x the x position in the Color lists
	 * @param y the y position in the Color lists
	 * @param panel the ColorChangePanel to add the Color to.
	 */
	public SliderDialog(PaintFrame frame, int x, int y, ColorChangePanel panel) {
		super(frame, true);
		this.frame = frame;
		setLayout(new GridBagLayout());
		this.x = x;
		this.y = y;
		this.changePanel = panel;
		initParts();
		setUpGUI();
		setLocationRelativeTo(frame);
	}
	
	/**
	 * Initializes the non-GUI related things, such as setting up listeners and initial values for fields
	 */
	private void initParts() {
		changing = true;
		OK_BUTTON.addActionListener(this);
		CANCEL_BUTTON.addActionListener(this);
		aSlider.addChangeListener(this);
		aSlider.setValue(255);
		rSlider.addChangeListener(this);
		gSlider.addChangeListener(this);
		bSlider.addChangeListener(this);
		aField.setText("" + aSlider.getValue());
		rField.setText("" + rSlider.getValue());
		gField.setText("" + gSlider.getValue());
		bField.setText("" + bSlider.getValue());
		aField.getDocument().addDocumentListener(this);
		rField.getDocument().addDocumentListener(this);
		gField.getDocument().addDocumentListener(this);
		bField.getDocument().addDocumentListener(this);
		changing = false;
	}
	
	/**
	 * Sets up the GUI of the SliderDialog
	 */
	private void setUpGUI() {
		add(new JLabel("Transparency: "), builder.setFill(GridBagConstraints.NONE).setLocation(1, 0).setInsetLeft(8).setInsetTop(100).setAnchor(GridBagConstraints.WEST).build());
		add(aSlider, builder.setGridWidth(3).setLocation(2, 0).setInsetTop(100).setWeightX(1.0).build());
		add(aField, builder.setLocation(5, 0).setInsetTop(100).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).build());
		
		add(new JLabel("Red: "), builder.setFill(GridBagConstraints.NONE).setLocation(1, 1).setInsetLeft(8).setAnchor(GridBagConstraints.WEST).build());
		add(rSlider, builder.setGridWidth(3).setLocation(2, 1).setWeightX(1.0).build());
		add(rField, builder.setLocation(5, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).build());
		
		add(new JLabel("Green: "), builder.setFill(GridBagConstraints.NONE).setLocation(1, 2).setInsetLeft(8).setAnchor(GridBagConstraints.WEST).build());
		add(gSlider, builder.setGridWidth(3).setLocation(2, 2).setWeightX(1.0).build());
		add(gField, builder.setLocation(5, 2).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).build());
		
		add(new JLabel("Blue: "), builder.setFill(GridBagConstraints.NONE).setLocation(1, 3).setInsetLeft(8).setAnchor(GridBagConstraints.WEST).build());
		add(bSlider, builder.setGridWidth(3).setLocation(2, 3).setWeightX(1.0).build());
		add(bField, builder.setLocation(5, 3).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).build());
		
		add(checkBox, builder.setLocation(2, 4).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).build());
		
		add(OK_BUTTON, builder.setLocation(3, 5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).build());
		add(CANCEL_BUTTON, builder.setLocation(4, 5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).build());
		pack();
	}
	
	/**
	 * Paints the box of color at the top of the dialog
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		int width = getSize().width;
		g2.setColor(Color.BLACK);
		g2.drawLine(0, 20, width, 100);
		g2.drawLine(0, 100, width, 20);
		g2.setColor(getCurrentColor());
		g2.fillRect(0, 0, width, 101);
	}
	
	/**
	 * Returns a new Color object based on the values of the sliders
	 * @return a new Color object based on the values of the sliders
	 */
	private Color getCurrentColor() {
		return new Color(rSlider.getValue(), gSlider.getValue(), bSlider.getValue(), aSlider.getValue());
	}
	
	/**
	 * Sets the x and y the setColor method is passed to a new value and shows the SliderDialog <P>
	 * Also recenters the dialog on the frame
	 * @param x
	 * @param y
	 */
	public void showWithNewCoords(int x, int y) {
		this.x = x;
		this.y = y;
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	/**
	 * If the source is the OK button, makes a new Color and adds/replaces with the appropriate method 
	 * on the ColorChangePanel, then hides the dialog.  Otherwise (Cancel button press), just hides the dialog.
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == OK_BUTTON) {
				if (! checkBox.isSelected()) { 
					changePanel.addColor(getCurrentColor());
				} else {
					changePanel.replaceColor(getCurrentColor(), x, y);
				}
				this.setVisible(false);
			} else  { // if (e.getSource() == CANCEL_BUTTON) {
				this.setVisible(false);
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Sets the text fields to the new value of the sliders
	 */
	public void stateChanged(ChangeEvent e) {
		try {
			if (!changing) {
				changing = true;
				if (e.getSource() == rSlider) {
					rField.setText("" + rSlider.getValue());
				} else if (e.getSource() == gSlider) {
					gField.setText("" + gSlider.getValue());
				} else if (e.getSource() == bSlider) {
					bField.setText("" + bSlider.getValue());
				} else if (e.getSource() == aSlider) {
					aField.setText("" + aSlider.getValue());
				}
				changing = false;
				repaint();
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			changing = false;
		}
	}

	/**
	 * Forwards to processTextChange
	 */
	public void changedUpdate(DocumentEvent e) {
		processTextChange(e);
	}

	/**
	 * Forwards to processTextChange
	 */
	public void insertUpdate(DocumentEvent e) {
		processTextChange(e);
	}

	/**
	 * Forwards to processTextChange
	 */
	public void removeUpdate(DocumentEvent e) {
		processTextChange(e);
	}
	
	/**
	 * Sets the sliders to the new values of the text fields, and if an error occurs resets the text fields
	 */
	private void processTextChange(DocumentEvent e) {
		try {
			if (!changing) {
				changing = true;
				if (e.getDocument() == rField.getDocument()) {
					if (rField.getText().length() > 0) {
						int num = Integer.parseInt(rField.getText());
						if ( num > 255) {
							num = 255;
							rField.setText("255");
						} else if (num < 0) {
							num = 0;
							rField.setText("0");
						}
						rSlider.setValue(num);
					}
				} else if (e.getDocument() == gField.getDocument()) {
					if (gField.getText().length() > 0) {
						int num = Integer.parseInt(gField.getText());
						if ( num > 255) {
							num = 255;
							gField.setText("255");
						} else if (num < 0) {
							num = 0;
							gField.setText("0");
						}
						gSlider.setValue(num);
					}
				} else if (e.getDocument() == bField.getDocument()) {
					if (bField.getText().length() > 0) {
						int num = Integer.parseInt(bField.getText());
						if ( num > 255) {
							num = 255;
							bField.setText("255");
						} else if (num < 0) {
							num = 0;
							bField.setText("0");
						}
						bSlider.setValue(num);
					}
				} else if (e.getDocument() == bField.getDocument()) {
					if (aField.getText().length() > 0) {
						int num = Integer.parseInt(aField.getText());
						if ( num > 255 ) {
							num = 255;
							aField.setText("255");
						} else if (num < 0) {
							num = 0;
							aField.setText("0");
						}
						aSlider.setValue(num);
					}
				}
				changing = false;
				repaint();
			}
		} catch (NumberFormatException ne) {
			changing = true;
			rField.setText("" + rSlider.getValue());
			gField.setText("" + gSlider.getValue());
			bField.setText("" + bSlider.getValue());
			aField.setText("" + aSlider.getValue());
			changing = false;
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(this, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			changing = false;
		}
	}
	
}
