package waldonsm.paint;

import javax.swing.UIManager;

import waldonsm.paint.gui.PaintFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Throwable t) {
				System.err.println("Couldn't set up look and feel... sucks for you");
			}
		}
//		new ToolRegister().addNewDrawingToolClass("waldonsm.paint.tools.PencilTool");
//		SetShapeModePanel.testMain();
		PaintFrame.showPaintFrame();
//		ExceptionUtils.testMain();
//		OvalTool.testMain();
	}

}
