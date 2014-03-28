package waldonsm.paint.tools.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Queue;

import waldonsm.paint.model.PaintModel;

/**
 * This Drawable fills a region (recursively defined to be the given point and all adjacent points of the same color) with
 * a new Color.  It does this by acting on the actual image and ignoring the Graphics object passed to it.  The draw method
 * does nothing with the given graphics object
 * @author Shawn Waldon
 *
 */
public class FillRegionDrawable implements ModelDependentDrawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int x, y;
	private final Color newColor;
	private transient PaintModel model;
	private final String name;
	
	/**
	 * The default name of this kind of Drawable
	 */
	private static final String DEFAULT_NAME = "FillRegion-";
	
	/**
	 * The count of FillRegionDrawables that have been created
	 */
	private static int count = 0;
	
	/**
	 * Returns the number in the count variable and increments the count
	 * @return the number in the count variable and increments the count
	 */
	private static int getNextCount() {
		return count++;
	}
	
	/**
	 * Creates a new FillRegionDrawable at the specified Point, that will change the image to have the specified color
	 * and will take data from and write data to the given BufferedImage
	 * @param p the Point to start changing the color at
	 * @param c the new Color to use
	 * @param model the PaintModel that this should manipulate
	 */
	public FillRegionDrawable(Point p, Color c, PaintModel m) {
		x = p.x;
		y = p.y;
		newColor = c;
		model = m;
		name = DEFAULT_NAME + getNextCount();
	}
	
	public void setModelToUse(PaintModel pm) {
		model = pm;
	}

	/**
	 * Ignores the passed Graphics2D object and instead directly manipulates the rgb data of the model's image that it takes data from
	 */
	public void draw(Graphics2D g) {
		BufferedImage image = model.getMainImage();
		if (x >= image.getWidth() || y >= image.getHeight())
			return;
		
		int initRGB = image.getRGB(x, y);
		int i, j;
		int newRGB = newColor.getRGB();
//		System.out.println(initRGB);
//		System.out.println(newRGB);
		if (initRGB == newRGB) {
			return;
		}
		
		// make the queues
		Queue<Integer> queueX = new ArrayDeque<Integer>();
		Queue<Integer> queueY = new ArrayDeque<Integer>();
		// add the initial point to the queues
		queueX.offer(x);
		queueY.offer(y);
		
		// while there are still elements on the queues...
		while (! queueX.isEmpty()) {
			// get the coordinates
			i = queueX.poll();
			j = queueY.poll();
			
			// if the coordinates are valid
			if (i >= 0 && j >= 0 && i < image.getWidth() && j < image.getHeight()) {
				// if the color at (i,j) is the color we are changing
				if (image.getRGB(i, j) == initRGB) {
					// change the color
					image.setRGB(i, j, newRGB);
					// put the neighboring pixels on the queue.
					// south neighbor
					queueX.offer(i);
					queueY.offer(j+1);
					// north neighbor
					queueX.offer(i);
					queueY.offer(j-1);
					// east neighbor
					queueX.offer(i+1);
					queueY.offer(j);
					// west neighbor
					queueX.offer(i-1);
					queueY.offer(j);
				}
			}
		}

	}

	/**
	 * Returns a string that identifies this FillRegionDrawable
	 */
	public String getName() {
		return name;
	}

}
