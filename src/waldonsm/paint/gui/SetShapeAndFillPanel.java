package waldonsm.paint.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class SetShapeAndFillPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SetShapeAndFillPanel(PaintFrame frame) {
		super(new GridLayout(2,1));
		init(frame);
	}
	
	public void init(PaintFrame frame) {
		SetFillModePanel fillPanel = new SetFillModePanel(frame);
		SetShapeModePanel shapePanel = new SetShapeModePanel(frame,fillPanel);
		add(shapePanel);
		add(fillPanel);
	}
}
