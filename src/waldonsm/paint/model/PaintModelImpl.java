package waldonsm.paint.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import waldonsm.paint.gui.listeners.ModelChangeListener;
import waldonsm.paint.tools.drawables.Drawable;
import waldonsm.paint.tools.drawables.ModelDependentDrawable;

/**
 * Provides the actual implementation for the PaintModel methods.  However the methods should never be called directly on these objects: 
 * these objects should be wrapped in a PaintModelWrapper, which prevents memory leaks by providing a WeakReference to the actual model.
 * Only one reference to this object should ever be kept, and that should be deleted when the model is closed. <P>
 * Except for the constructors, all method comments can be found on the PaintModel interface.
 * @author Shawn Waldon
 *
 */
class PaintModelImpl implements PaintModel {

	private static int unnamedModelCount = 0;
	private static final String defaultName = "Untitled-";

	private Point mouseClickedAt;
	private List<Point> clicksList;
	private int clickCode = NO_CLICKS;
	private int clickCount = 0;
	private Color color1;
	private Color color2;
	private Raster defaultRaster;

	private boolean isRightClick;

	private int shapeMode = 0;
	private int fillMode = 0;
	private int width;
	private int height;
	private int scaleFactor = 1;
	private boolean changed = false;
	private boolean isWhiteBGround;

	private BufferedImage image;
	private List<Drawable> drawables;
	private Stack<Drawable> undoneStack;
	private Drawable newDrawing;
	private String name;

	private final List<ModelChangeListener> listeners;
	private File saveFile;

	/**
	 * Creates a new PaintModelImpl, with the given width and height, and optionally a white background.
	 * @param width the width of the desired PaintModel
	 * @param height the height of the desired PaintModel
	 * @param fillWhite true if the PaintModel should have a white background initially
	 */
	PaintModelImpl(int width, int height, boolean fillWhite) {
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		listeners = new ArrayList<ModelChangeListener>();
		color1 = Color.black;
		color2 = Color.white;
		drawables = new ArrayList<Drawable>();
		undoneStack = new Stack<Drawable>();
		clicksList = new ArrayList<Point>();
		isWhiteBGround = fillWhite;
		if (fillWhite) {
			Graphics2D g = image.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width+1, height+1);
		}
		newDrawing = Drawable.NOTHING;
		defaultRaster = image.getData();
		saveFile = null;
		name = defaultName + unnamedModelCount++;
	}
	
	/**
	 * Creates a new PaintModel from the given file assuming that the file is in the given format
	 * @param f the file to read the model data from
	 * @param format the format that the file is in
	 * @throws IOException if there is an IO error on any of the reads or stream creations
	 * @throws ClassNotFoundException if there is an error reading a ZDLIF file with the ObjectInputStream
	 */
	PaintModelImpl(File f, Format format) throws IOException, ClassNotFoundException {
		drawables = new ArrayList<Drawable>();
		switch (format) {
		case ZDLIF:
			ZipFile zf = null;
			try {
				zf = new ZipFile(f);
				for (Enumeration<? extends ZipEntry> enu = zf.entries(); enu.hasMoreElements();) {
					ZipEntry entry = enu.nextElement();
					readEntry(entry, zf.getInputStream(entry));
				}
			} finally {
				if (zf != null)
					zf.close();
			}
			redrawImage();
			break;
		case PNG:
		case GIF:
		case JPG:
		default:
			image = ImageIO.read(f);
			width = image.getWidth();
			height = image.getHeight();
			isWhiteBGround = true;
			BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			newImage.createGraphics().drawImage(image, 0, 0, null);
			image = newImage;
			defaultRaster = image.getData();
			break;
		}
		color1 = Color.black;
		color2 = Color.white;
		clicksList = new ArrayList<Point>();
		undoneStack = new Stack<Drawable>();
		listeners = new ArrayList<ModelChangeListener>();
		newDrawing = Drawable.NOTHING;
		saveFile = f;
		name = f.getName();
	}
	
	/**
	 * Reads the data from a single ZipEntry on the given ZipInputStream
	 * @param entry the entry that is being read
	 * @param is the InputStream that is reading the current ZipEntry
	 * @throws IOException if there is an error on any of the reads or InputStream creations
	 * @throws ClassNotFoundException if there is an error on the readObject method of the ObjectInputStream
	 */
	private void readEntry(ZipEntry entry, InputStream is) throws IOException, ClassNotFoundException {
		if (entry.getName().equals("DATA")) {
			image = ImageIO.read(is);
			width = image.getWidth();
			height = image.getHeight();
			defaultRaster = image.getData();
		} else if (entry.getName().equals("DRAWINGS")) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(is);
				isWhiteBGround = ois.readBoolean();
				int numDs = ois.readInt();
				for (int i = 0; i < numDs; i++) {
					drawables.add((Drawable)ois.readObject());
				}
				for (Drawable d: drawables) {
					if (d instanceof ModelDependentDrawable) {
						((ModelDependentDrawable)d).setModelToUse(this);
					}
				}
			} finally {
				if (ois != null)
					ois.close();
			}
		}
	}

	public int getClickCode() {
		return clickCode;
	}

	public void setClickCode(int code) {
		clickCode = code;
	}

	public void setCurrentDrawable(Drawable d) {
		newDrawing = d;
	}

	public void incrementClickCount() {
		clickCount++;
	}

	public void decrementClickCount() {
		clickCount--;
	}

	public int getClickCount() {
		return clickCount;
	}


	public void finalizeDrawing(Drawable newDrawing) {
		Graphics2D g = image.createGraphics();
		newDrawing.draw(g);
		drawables.add(newDrawing);
		this.newDrawing = Drawable.NOTHING;

		clearPointsList();
		undoneStack.clear();

		mouseClickedAt = null;
		changed = true;
		clickCode = IGNORE_ALL;
		fireModelChangeCode(CODE_MODEL_CHANGED);
	}
	
	public void clearPointsList() {
		clicksList.clear();
	}

	public Point getInitialPoint() {
		return new Point(mouseClickedAt);
	}

	public void setInitialPoint(Point p) {
		mouseClickedAt = new Point(p);
	}

	public void addToPointsList(Point p) {
		clicksList.add(new Point(p));
	}

	public List<Point> getPointsList() {
		List<Point> result = new ArrayList<Point>(clicksList.size() + 2);
		for (Point p : clicksList) {
			result.add(new Point(p));
		}
		return result;
	}

	public boolean isRightClick() {
		return isRightClick;
	}

	public void setIsRightClick(boolean b) {
		isRightClick = b;
	}

	public void redrawImage() {
		image.setData(defaultRaster);
		Graphics2D g = image.createGraphics();
		for (Drawable d : drawables) {
			d.draw(g);
		}
	}


	public Color getMainColor() {
		return color1;
	}


	public void setMainColor(Color color1) {
		this.color1 = color1;
	}


	public Color getSecondaryColor() {
		return color2;
	}


	public void setSecondaryColor(Color color2) {
		this.color2 = color2;
	}


	public BufferedImage getMainImage() {
		return image;
	}


	public Drawable getCurrentDrawing() {
		return newDrawing;
	}

	public Dimension getSize() {
		return new Dimension(width,height);
	}

	public boolean hasChanged() {
		return changed;
	}

	public void saveModelToFile(File f, Format format) throws IOException {
		if (!f.getName().endsWith("." + format.toString())) {
			// Ensure that the filename ends in the correct format ending
			f = new File(f.getAbsolutePath() + "." + format.toString());
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			if (format == Format.GIF || format == Format.JPG || format == Format.PNG) {
				// Easy part - save with ImageIO
				ImageIO.write(image, format.toString(), fos);
			} else if (format == Format.ZDLIF) {
				// Otherwise, save in my (hopefully) usable format
				ZipOutputStream zos = null;
				try {
					zos = new ZipOutputStream(fos);
					zos.putNextEntry(new ZipEntry("DATA"));
					image.setData(defaultRaster);
					ImageIO.write(image, INTERNAL_FORMAT_OF_ZIPFILE, zos);
					zos.flush();
					zos.closeEntry();
					ObjectOutputStream oos = null;
					try {
						zos.putNextEntry(new ZipEntry("DRAWINGS"));
						oos = new ObjectOutputStream(zos);
						oos.writeBoolean(isWhiteBGround);
						oos.writeInt(drawables.size());
						for (Drawable d : drawables) {
							oos.writeObject(d);
						}
						zos.closeEntry();
					} finally {
						if (oos != null)
							oos.close();
					}
				} finally {
					if (zos != null)
						zos.close();
				}
			}
		} finally {
			if (fos != null)
				fos.close();
		}
		redrawImage();
		saveFile = f;
		name = f.getName();
		changed = false;
		fireModelChangeCode(CODE_MODEL_SAVED);
	}

	public int getShapeMode() {
		return shapeMode;
	}


	public void setShapeMode(int shapeMode) {
		this.shapeMode = shapeMode;
	}

	public int getFillMode() {
		return fillMode;
	}

	public void setFillMode(int fillMode) {
		this.fillMode = fillMode;
	}

	public File getSaveFile() {
		return saveFile;
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
		BufferedImage oldImage = image;
		oldImage.setData(defaultRaster);
		image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = image.createGraphics();
		if (isWhiteBGround) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width+1, height+1);
		}
		g.drawImage(oldImage, 0, 0, null);
		defaultRaster = image.getData();
		redrawImage();
		changed = true;
		fireModelChangeCode(CODE_MODEL_SIZE_CHANGED);
		fireModelChangeCode(CODE_MODEL_CHANGED);
	}

	public String getName() {
		return name;
	}

	public void addModelChangeListener(ModelChangeListener l) {
		listeners.add(l);
	}

	public void removeModelChangeListener(ModelChangeListener l) {
		listeners.remove(l);
	}
	
	/**
	 * A helper method to notify all listeners that the given code has occurred.
	 * @param code should be either PaintModel.CODE_MODEL_CHANGED or PaintModel.CODE_MODEL_SAVED
	 */
	private void fireModelChangeCode(int code) {
		for (ModelChangeListener l : listeners) {
			l.modelChanged(code);
		}
	}
	
	public void setScaleFactor(int scaleFactor) {
		if (scaleFactor <= 0 || scaleFactor > 5) {
			throw new IllegalArgumentException("Illegal scale factor" + scaleFactor);
		}
		this.scaleFactor = scaleFactor;
		fireModelChangeCode(CODE_MODEL_RESCALED);
	}

	public int getScaleFactor() {
		return scaleFactor;
	}
	
	public Dimension getEffectiveSize() {
		return new Dimension((int) (width * scaleFactor), (int) ( height * scaleFactor) );
	}
	
	public void undoLastAction() {
		if (! drawables.isEmpty()) {
			undoneStack.push(drawables.remove(drawables.size()-1));
			redrawImage();
			
			clearPointsList();
			
			mouseClickedAt = null;
			changed = true;
			clickCode = IGNORE_ALL;
			fireModelChangeCode(CODE_MODEL_CHANGED);
		}
	}
	
	public void redoLastAction() {
		if (! undoneStack.isEmpty()) {
			Drawable d = undoneStack.pop();
			Graphics2D g = image.createGraphics();
			d.draw(g);
			drawables.add(d);
			
			clearPointsList();
			
			mouseClickedAt = null;
			changed = true;
			clickCode = IGNORE_ALL;
			fireModelChangeCode(CODE_MODEL_CHANGED);
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
