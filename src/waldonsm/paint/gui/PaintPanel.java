package waldonsm.paint.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import waldonsm.paint.gui.listeners.ModelChangeListener;
import waldonsm.paint.gui.listeners.PaintListener;
import waldonsm.paint.model.PaintModel;
import waldonsm.paint.model.PaintModelUtils;

/**
 * This class is an extension of JPanel that holds and displays a PaintModel
 * @author Shawn Waldon
 *
 */
class PaintPanel extends JPanel implements ModelChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This is the ONLY hard reference to the PaintModelImpl being used and should never be used or passed to any method other than PaintModelUtils.getModelWrapperFromReference()
	 */
	private Object hardModelReference;
	
	/**
	 * This is the WeakReference to the PaintModel and should be the one used everywhere.
	 */
	private PaintModel model;
	
	/**
	 * This is the PaintFrame which this PaintPanel will call fireNewModelEvent on when it gets a new model
	 */
	private final PaintFrame frame;
	
	/**
	 * This is a reference to the current PaintListener for this panel, so that the listener can be removed and cleaned up in one of the newModel methods
	 */
	private transient PaintListener listener;
	
	private transient BufferedImage scratch;
	private transient Raster scratchData;
	
	private final JTabbedPane tabbedPane;
	private JScrollPane scrollPaneWrapper = null;
	private boolean saved = true;
	
	/**
	 * Creates a new PaintPanel with a model of the specified width and height, and optionally a transparent background.
	 * @param width the width of the model to be created by this PaintPanel
	 * @param height the height of the model to be created by this PaintPanel
	 * @param frame the frame which this PaintPanel will fire newModelEvents through
	 * @param transparentModel true if the new model to be created should have a white background, (false for transparent background).
	 */
	public PaintPanel(int width, int height, PaintFrame frame, JTabbedPane pane, boolean fillWhite) {
		super();
		this.frame = frame;
		tabbedPane = pane;
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		if (!fillWhite) {
			newTransparentModel(width, height);
		} else {
			newBlankModel(width, height);
		}
	}
	
	/**
	 * Creates a new PaintPanel with an image read in from a file
	 * @param f the file to read in
	 * @param frame the PaintFrame to fire newModelEvents through
	 * @param pane the JTabbedPane that this PaintPanel will edit tabs on to show that an image is modified and unsaved.
	 * @throws IOException If there is an IOException in reading the file
	 * @throws ClassNotFoundException If there is an error reading a zdlif file
	 */
	public PaintPanel(File f, PaintFrame frame, JTabbedPane pane) throws IOException, ClassNotFoundException {
		super();
		this.frame = frame;
		tabbedPane = pane;
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		newModelFromFile(f);
	}
	
	/**
	 * Resizes the panel based on the current dimensions of the image and the scale factor.
	 */
	private void resizePanel() {
		Dimension d = model.getEffectiveSize();
		int w = (d.width >= 475) ? d.width + 25 : 500;
		int h = (d.height >= 475) ? d.height + 25 : 500;
		setSize(w,h);
		setPreferredSize(new Dimension(w,h));
		if (scrollPaneWrapper != null)
			scrollPaneWrapper.validate();
		d = model.getSize();
		if (scratch == null || d.width != scratch.getWidth() || d.height != scratch.getHeight()) {
			scratch = new BufferedImage(d.width, d.height, BufferedImage.TYPE_4BYTE_ABGR);
			scratchData = scratch.getData();
		}
	}
	
	/**
	 * Sets this PaintPanel's model to a new model with a transparent background with the specified size <P>
	 * This method also calls fireNewModelEvent() on the associated frame and changes the panel's PaintListener to one that updates the new model
	 * @param width
	 * @param height
	 */
	public final void newTransparentModel(int width, int height) {
		removeMouseListener(listener);
		removeMouseMotionListener(listener);
		hardModelReference = PaintModelUtils.createNewTransparentModel(width, height);
		model = PaintModelUtils.getModelWrapperFromReference((PaintModel)hardModelReference);
		listener = new PaintListener(model, frame);
		model.addModelChangeListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		resizePanel();
	}
	
	/**
	 * Sets this PaintPanel's model to a new model with a white background with the specified size <P>
	 * This method also changes the panel's PaintListener to one that updates the new model
	 * @param width
	 * @param height
	 */
	public final void newBlankModel(int width, int height) {
		removeMouseListener(listener);
		removeMouseMotionListener(listener);
		hardModelReference = PaintModelUtils.createNewModelWithWhiteBackground(width, height);
		model = PaintModelUtils.getModelWrapperFromReference((PaintModel)hardModelReference);
		listener = new PaintListener(model, frame);
		model.addModelChangeListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		resizePanel();
	}
	
	/**
	 * Sets this PaintPanel's model to a model created from the specified file<P>
	 * This method also changes the panel's PaintListener to one that updates the new model
	 * @param f the File to create the PaintModel from
	 * @throws IOException if there is an error reading the file
	 * @throws ClassNotFoundException if there is an error reading a ZDLIF file
	 */
	public final void newModelFromFile(File f) throws IOException, ClassNotFoundException {
		removeMouseListener(listener);
		removeMouseMotionListener(listener);
		hardModelReference = PaintModelUtils.createNewModelFromFile(f);
		model = PaintModelUtils.getModelWrapperFromReference((PaintModel)hardModelReference);
		listener = new PaintListener(model, frame);
		model.addModelChangeListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		resizePanel();
	}
	
	/**
	 * This should be the JScrollPane which contains this Panel and was added to the JTabbedPane
	 * @param scPane the JScrollPane
	 */
	void setWrapperScrollPane(JScrollPane scPane) {
		scrollPaneWrapper = scPane;
	}
	
	/**
	 * Returns the wrapper JScrollPane for this PaintPanel
	 * @return the wrapper JScrollPane for this PaintPanel
	 */
	JScrollPane getWrapperScrollPane() {
		return scrollPaneWrapper;
	}
	
	/**
	 * Changes the panel's label to add/take off the * to indicate an unsaved model
	 */
	public void modelChanged(int code) {
		if (scrollPaneWrapper == null)
			return;
		int index = tabbedPane.indexOfComponent(scrollPaneWrapper);
		if (code == PaintModel.CODE_MODEL_CHANGED) {
			if (saved) {
				tabbedPane.setTitleAt(index, "*" + model.getName());
				saved = false;
			}
		} else if (code == PaintModel.CODE_MODEL_SAVED) {
			if (!saved) {
				tabbedPane.setTitleAt(index, model.getName());
				saved = true;
			}
		} else if (code == PaintModel.CODE_MODEL_SIZE_CHANGED || code == PaintModel.CODE_MODEL_RESCALED) {
			resizePanel();
		}
		tabbedPane.repaint();
	}
	
	/**
	 * Draws the Panel, first adding a dark grey background box, then a light grey box for the model background, and then drawing the image from the model.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// draw dark gray box
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		// draw light grey box
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, model.getEffectiveSize().width, model.getEffectiveSize().height);
		
		// draws the current image (used with all potential code except the last one (i.e the actual code)
//		g2.drawImage(getScaledImageToDraw(model.getMainImage()), 0, 0, null);
		// draws the current in-progress drawable
		
		// ignores scaling issues
//		model.getCurrentDrawing().draw(g2);
		
		// very laggy
//		scratch.setData(scratchData);
//		model.getCurrentDrawing().draw(scratch.createGraphics());
//		g2.drawImage(getScaledImageToDraw(scratch), 0, 0, null);
		
		// scales too smooth -- looks funny
//		AffineTransform aff = g2.getTransform();
//		g2.scale(model.getScaleFactor(), model.getScaleFactor());
//		model.getCurrentDrawing().draw(g2);
//		g2.setTransform(aff);
		
		// looks weird -- skinny temp line and fat normal line
//		if (model.getCurrentDrawing() instanceof LineDrawable)
//			((LineDrawable) model.getCurrentDrawing()).drawScaled(g2, model.getScaleFactor());
		
		// scale the graphics object then draw the images... bingo!
		if (model.getScaleFactor() != 1) {
			AffineTransform aff = g2.getTransform();
			g2.scale(model.getScaleFactor(), model.getScaleFactor());

			scratch.setData(scratchData);
			g2.drawImage(model.getMainImage(), 0, 0, null);
			model.getCurrentDrawing().draw(scratch.createGraphics());
			g2.drawImage(scratch, 0, 0, null);

			g2.setTransform(aff);
		} else { // the scale factor is one
			g2.drawImage(model.getMainImage(), 0, 0, null);
			model.getCurrentDrawing().draw(g2);
		}
		// end test
	}
	
	// unneeded with new changes
//	/**
//	 * Gets a copy of the image that is scaled correctly
//	 * @return a scaled copy of the image...
//	 */
//	private Image getScaledImageToDraw(BufferedImage im) {
//		if (model.getScaleFactor() == 1)
//			return im;
//		int width = im.getWidth() * model.getScaleFactor();
//		int height = im.getHeight() * model.getScaleFactor();
//		return im.getScaledInstance(width, height, Image.SCALE_FAST);
//	}
	
	/**
	 * Returns the current PaintModel for this panel
	 * @return
	 */
	public PaintModel getModel() {
		return model;
	}
	
	/**
	 * Sets the model hard reference to null, allowing the model to be garbage collected.
	 */
	public void closeModel() {
		hardModelReference = null;
	}

	
	
	

}
