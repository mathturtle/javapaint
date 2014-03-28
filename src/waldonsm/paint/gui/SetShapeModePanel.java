package waldonsm.paint.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

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
import waldonsm.paint.tools.drawables.Drawable;
import waldonsm.utils.ExceptionUtils;
import waldonsm.utils.builders.GridBagConstraintsBuilder;

public class SetShapeModePanel extends JPanel implements NewModelListener {
	
	private static final GridBagConstraints DEFAULT_BUTTON_CONSTRAINTS = new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final PaintFrame frame;
	private final SetFillModePanel fillPanel;
	private PaintModel model = null;
	private ToolRegister toolReg;
	
	private final HashMap<Integer, Integer> shapeModeToFillModeCache = new HashMap<Integer, Integer>();
	
	private int shapeMode;
	
	public SetShapeModePanel(PaintFrame frame, SetFillModePanel fillPanel) {
		super(new GridBagLayout());
		this.frame = frame;
		this.fillPanel = fillPanel;
		toolReg = frame.getTools();
		shapeMode = 0;
		init();
	}
	
	private void init() {
		frame.addNewModelListener(this);
		Collection<DrawingTool> tools = toolReg.getDrawingTools();
		GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder(DEFAULT_BUTTON_CONSTRAINTS);
		ActionListener listener = new ShapeModeListener();
		int btnCount = 0;
		JButton select = null;
		
		for (DrawingTool tool : tools) {
			JButton button = new JButton(new ImageIcon(tool.getButtonImageFileName()));
			button.setActionCommand("" + (tool.getToolName().hashCode() + tool.getOffset()));
			button.addActionListener(listener);
			add(button, builder.setLocation(btnCount%2, btnCount/2).build());
			btnCount++;
			if (select == null)
				select = button;
		}
		if (select != null)
			select.doClick();
		
		JPanel fillPanel = new JPanel();
		
		add(fillPanel, builder.setLocation(0, btnCount).setGridWidth(2).setFill(GridBagConstraints.BOTH).setXAndYWeights(1.0, 1.0).build());

		
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void newModel(NewModelEvent nme) {
		try {
			model = nme.getNewModel();
			if (model != null) {
				model.setShapeMode(shapeMode);
				model.setClickCode(PaintModel.NO_CLICKS);
				model.clearPointsList();
				model.setCurrentDrawable(Drawable.NOTHING);
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ShapeModeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				// Store the old shape mode with the last fill mode used by it in the cache
				shapeModeToFillModeCache.put(shapeMode,fillPanel.getFillMode());
				shapeMode = Integer.parseInt(e.getActionCommand());
				if (model != null) {
					model.setShapeMode(shapeMode);
					model.setClickCode(PaintModel.NO_CLICKS);
					model.clearPointsList();
					model.setCurrentDrawable(Drawable.NOTHING);
					frame.repaint();
				}
				fillPanel.changeButtonsTo(shapeMode);
				// restore the fill mode from the cache
				Integer fillMode = shapeModeToFillModeCache.get(shapeMode);
				fillPanel.setFillMode(fillMode);
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(frame, "There was an error:\n" + ExceptionUtils.getStackTraceString(t), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
}
