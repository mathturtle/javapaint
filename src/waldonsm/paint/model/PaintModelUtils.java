package waldonsm.paint.model;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import waldonsm.paint.gui.PaintFrame;
import waldonsm.paint.gui.PaintMenuBar;

public class PaintModelUtils {
	

	/**
	 * JFileChooser FileFilters for the saveModelToFile method
	 */
	private static final FileFilter JPG_FILTER = new FileNameExtensionFilter("JPG Images", "jpg", "JPG");
	private static final FileFilter GIF_FILTER = new FileNameExtensionFilter("GIF Images", "gif", "GIF");
	private static final FileFilter PNG_FILTER = new FileNameExtensionFilter("PNG Images", "png", "PNG");
	private static final FileFilter ZDLIF_FILTER = new FileNameExtensionFilter("ZDLIF Files", "zdlif", "ZDLIF");
	
	/**
	 * The JFileChooser for the saveModelToFile method
	 */
	private static final JFileChooser jfc;
	
	static { // set up the JFileChooser
		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(JPG_FILTER);
		jfc.setFileFilter(GIF_FILTER);
		jfc.setFileFilter(ZDLIF_FILTER);
		jfc.setFileFilter(PNG_FILTER);
	}
	
	/**
	 * Returns a new PaintModel hard reference with a transparent background with the specified width and height
	 * @param width the width of the desired PaintModel
	 * @param height the height of the desired PaintModel
	 * @return a new PaintModel hard reference with the specified width and height and a transparent background 
	 */
	public static PaintModel createNewTransparentModel(int width, int height) {
		return new PaintModelImpl(width, height, false);
	}
	
	/**
	 * Returns a new PaintModel hard reference with a white background with the specified width and height
	 * @param width the width of the desired PaintModel
	 * @param height the height of the desired PaintModel
	 * @return a new PaintModel hard reference with the specified width and height and a white background
	 */
	public static PaintModel createNewModelWithWhiteBackground(int width, int height) {
		return new PaintModelImpl(width, height, true);
	}
	
	/**
	 * Creates a new PaintModel based on the data in the specified file
	 * @param f the file to read
	 * @return a PaintModel with data based on the specified file
	 * @throws IOException if there is an error reading the file
	 * @throws ClassNotFoundException if there is an error reading a ZDLIF file
	 */
	public static PaintModel createNewModelFromFile(File f) throws IOException, ClassNotFoundException {
		return new PaintModelImpl(f, getFormatOfFile(f));
	}

	/**
	 * Returns a new WeakReference to the given PaintModel to be used elsewhere in the program
	 * @param modelReference the PaintModel to be WeakReferenced
	 * @return a new PaintModel weak reference with the given PaintModel wrapped inside
	 */
	public static PaintModel getModelWrapperFromReference(PaintModel modelReference) {
		if (modelReference instanceof PaintModelWrapper) {
			return modelReference;
		}
		return new PaintModelWrapper((PaintModelImpl)modelReference);
	}
	
	/**
	 * Saves a model to a file. <P>
	 * If the parameter actionCommand equals "Save", it tries to use the file in the model. <P>
	 * Otherwise it prompts the user for a file, throwing up the save dialog on the specified PaintFrame
	 * @param model the model to be saved.
	 * @param actionCommand the string command - if this equals "Save" then it tries to do a silent save
	 * @param frame the frame to throw up a save dialog to (if necessary)
	 * @return true if the save was successful
	 */
	public static boolean saveModelToFile(PaintModel model, String actionCommand, PaintFrame frame) {
		jfc.setMultiSelectionEnabled(false);
		File f = model.getSaveFile();
		jfc.setSelectedFile(f);
		if (actionCommand.equals(PaintMenuBar.SAVE) && f != null) {
			PaintModel.Format format = getFormatOfFile(f);
			return doSave(model, f, format, frame);
		} else {
			if (jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				f = jfc.getSelectedFile();
				PaintModel.Format format;
				if (jfc.getFileFilter() == JPG_FILTER) {
					format = PaintModel.Format.JPG;
				} else if (jfc.getFileFilter() == GIF_FILTER) {
					format = PaintModel.Format.GIF;
				} else if (jfc.getFileFilter() == ZDLIF_FILTER) {
					format = PaintModel.Format.ZDLIF;
				} else {
					// Default to PNG
					format = PaintModel.Format.PNG;
				}
				boolean doSave = true;
				if (f.exists()) {
					int sel = JOptionPane.showConfirmDialog(frame, "This file already exists.\nOverwrite it?", "WARNING: File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (sel == JOptionPane.NO_OPTION || sel == JOptionPane.CLOSED_OPTION)
						doSave = false;
				}
				if (doSave) {
					return doSave(model, f, format, frame);
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the format of the file passed in based on the FileFilters given
	 * @param f the file to test
	 * @return the PaintModel.Format object the describes the File's format
	 */
	private static PaintModel.Format getFormatOfFile(File f) {
		PaintModel.Format format;

		if (JPG_FILTER.accept(f))
			format = PaintModel.Format.JPG;
		else if (GIF_FILTER.accept(f))
			format = PaintModel.Format.GIF;
		else if (ZDLIF_FILTER.accept(f))
			format = PaintModel.Format.ZDLIF;
		else // Default to PNG
			format = PaintModel.Format.PNG;
		return format;
	}
	
	/**
	 * Actually saves the model, called by the saveModelToFile method
	 * @param model the model to save
	 * @param f the file to save it to
	 * @param format the format to use
	 * @param frame the frame to throw up error dialogs to
	 * @return true if the model was successfully saved.
	 */
	private static boolean doSave(PaintModel model, File f, PaintModel.Format format, PaintFrame frame) {

		if (f != null) {
			try {
				if (model != null) {
					model.saveModelToFile(f, format);
					return true;
				}
			} catch (IOException t) {
				JOptionPane.showMessageDialog(frame, "There was an error while saving: " + t.getMessage(), "ERROR: " + t.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
		return false;
	}
	
	
	public static void openModelFromFiles(PaintFrame frame) {
		jfc.setMultiSelectionEnabled(true);
		if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File[] files = jfc.getSelectedFiles();
			if (files != null) {
				for (File f : files) {
					try {
						frame.addTab(f);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frame, "Error opening file: " + f.getName(), "ERROR: Could not open/read file", JOptionPane.ERROR_MESSAGE);
					} catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(frame, "Error reading file: " + f.getName(), "ERROR while reading ZDLIF file", JOptionPane.ERROR_MESSAGE);
					} catch (ClassCastException e) {
						JOptionPane.showMessageDialog(frame, "Wrong file type: " + f.getName() + " is not a ZDLIF file", "ERROR: Wrong file type", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

}
