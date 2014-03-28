package waldonsm.paint.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import waldonsm.paint.exceptions.BadReferenceException;
import waldonsm.paint.gui.listeners.ModelChangeListener;
import waldonsm.paint.tools.drawables.Drawable;

/**
 * A subclass of WeakReference modified to implement PaintModel and wrap the given PaintModelImpl. <P>
 * This 'model' relays all calls to the underlying model for its behavior.
 * @author Shawn Waldon
 *
 */
class PaintModelWrapper extends WeakReference<PaintModelImpl> implements PaintModel {
	
	static enum IdiotProof {
		IDIOTPROOF;
	}
	
	/**
	 * The error message to be passed into the BadReferenceException created in this class's get() method.
	 */
	private static final String BAD_REFERENCE_MESSAGE = "This WeakReference's object has gone out of scope.  Some part of the program still thinks a closed model is open and is trying to change it.";
	
	/**
	 * Creates a new PaintModelWrapper around the given PaintModelImpl
	 * @param model the PaintModelImpl to be wrapped
	 */
	PaintModelWrapper(PaintModelImpl model) {
		super(model);
	}
	
	/**
	 * Overrides get to throw a BadReferenceException when it otherwise would have returned null
	 * @throws BadReferenceException when the underlying object has been garbage collected
	 * @return the underlying PaintModelImpl
	 */
	public PaintModelImpl get() {
		PaintModelImpl impl = super.get();
		if (impl != null)
			return impl;
		throw new BadReferenceException(BAD_REFERENCE_MESSAGE);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public Drawable getCurrentDrawing() {
		return get().getCurrentDrawing();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public BufferedImage getMainImage() {
		return get().getMainImage();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public Dimension getSize() {
		return get().getSize();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void redrawImage() {
		get().redrawImage();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public Color getMainColor() {
		return get().getMainColor();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setMainColor(Color c) {
		get().setMainColor(c);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public Color getSecondaryColor() {
		return get().getSecondaryColor();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setSecondaryColor(Color c) {
		get().setSecondaryColor(c);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public boolean hasChanged() {
		return get().hasChanged();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void saveModelToFile(File f, Format format) throws IOException {
		get().saveModelToFile(f, format);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getShapeMode() {
		return get().getShapeMode();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setShapeMode(int mode) {
		get().setShapeMode(mode);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getFillMode() {
		return get().getFillMode();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setFillMode(int fillMode) {
		get().setFillMode(fillMode);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void decrementClickCount() {
		get().decrementClickCount();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void finalizeDrawing(Drawable newFinalDrawing) {
		get().finalizeDrawing(newFinalDrawing);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getClickCode() {
		return get().getClickCode();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getClickCount() {
		return get().getClickCount();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void incrementClickCount() {
		get().incrementClickCount();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setClickCode(int code) {
		get().setClickCode(code);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setCurrentDrawable(Drawable d) {
		get().setCurrentDrawable(d);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void addToPointsList(Point p) {
		get().addToPointsList(p);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public Point getInitialPoint() {
		return get().getInitialPoint();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public List<Point> getPointsList() {
		return get().getPointsList();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setInitialPoint(Point p) {
		get().setInitialPoint(p);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public boolean isRightClick() {
		return get().isRightClick();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setIsRightClick(boolean isRightClick) {
		get().setIsRightClick(isRightClick);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public File getSaveFile() {
		return get().getSaveFile();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setSize(int w, int h) {
		get().setSize(w, h);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public String getName() {
		return get().getName();
	}
	
	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void addModelChangeListener(ModelChangeListener l) {
		get().addModelChangeListener(l);
	}
	
	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void removeModelChangeListener(ModelChangeListener l) {
		get().removeModelChangeListener(l);
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void clearPointsList() {
		get().clearPointsList();
		
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getScaleFactor() {
		return get().getScaleFactor();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void setScaleFactor(int scaleFactor) {
		get().setScaleFactor(scaleFactor);
	}
	
	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public Dimension getEffectiveSize() {
		return get().getEffectiveSize();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void redoLastAction() {
		get().redoLastAction();
	}

	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public void undoLastAction() {
		get().undoLastAction();
	}
	
	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getHeight() {
		return get().getHeight();
	}
	
	/**
	 * Implemented by calling the same method on the underlying PaintModelImpl
	 */
	public int getWidth() {
		return get().getWidth();
	}
}
