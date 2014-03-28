package waldonsm.paint.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class acts as a register for all the DrawingTools in the program.  The DrawingTools may be added via the
 * addNewDrawingToolClass method, and the next time the program is started, the new tool will appear on the
 * SetShapeModePanel.
 * 
 * @author Shawn Waldon
 *
 */
public class ToolRegister {
	
	/**
	 * The file that stores all of the DrawingTool class names
	 * <P>
	 * IF YOU VALUE THE CORRECT USE OF THIS PROGRAM, DO NOT MESS WITH THE .extensions FILE!!!!!!!!!!
	 */
	private static final File toolsFile = new File(".extensions");
	
	private final Map<Integer,DrawingTool> tools = new HashMap<Integer,DrawingTool>();
	
	/**
	 * Creates a new ToolRegister.
	 */
	public ToolRegister() {
		populateToolsMap();
	}

	/**
	 * Returns the currently registered DrawingTools in a Collection
	 * @return the currently registered DrawingTools in a Collection
	 */
	public Collection<DrawingTool> getDrawingTools() {
		return Collections.unmodifiableCollection(tools.values());
	}
	
	/**
	 * Returns an unmodifiable copy of the internal map of Integers to DrawingTools
	 * @return
	 */
	public Map<Integer,DrawingTool> getToolsMap() {
		return Collections.unmodifiableMap(tools);
	}
	
	/**
	 * Initializes the ToolRegister with the DrawingTools from the file
	 */
	private void populateToolsMap() {
		try {
			Scanner scan = new Scanner(toolsFile);
			while (scan.hasNextLine()) {
				String s = scan.nextLine().trim();
				if (s.length() != 0) {
					addDrawingToolClass(Class.forName(s).asSubclass(DrawingTool.class));
				}
			}
		} catch (FileNotFoundException fe) {
			// Don't delete my system files... and you should never get here
		} catch (Exception e) {
			// Don't screw around with my system files... and you should never get here
		}
	}
	
	/**
	 * Gets the DrawingTool with the Integer code
	 * @param toolKey the integer tool code
	 * @return the DrawingTool corresponding to that code
	 */
	public DrawingTool getToolFor(int toolKey) {
		return tools.get(toolKey);
	}
	
	/**
	 * Checks the given Class for correctness and if it is correct adds it to the internal map
	 * @param c the class to add
	 * @return true if the Tool was successfully added.
	 */
	private boolean addDrawingToolClass(Class<? extends DrawingTool> c) {
		try {
		DrawingTool d = c.newInstance();
		if (DrawingTool.testOutFileNames(d)) {
			int i = d.getToolName().hashCode();
			while (tools.containsKey(i)) {
				i = i++;
				d.setOffset(d.getOffset()+1);
			}
			tools.put(i, d);
			return true;
		} else
			return false;
		} catch (Throwable t) {
			return false;
		}
	}
	
	/**
	 * Checks the Class created from the given String as being a correct DrawingTool.
	 * <P>
	 * If the given classname corresponds to a DrawingTool and the tool checks out,
	 * this method will add it to the file of tools and return true, otherwise it
	 * returns false.
	 * @param toolClassName
	 * @return
	 */
	public boolean addNewDrawingToolClass(String toolClassName) {
		try {
			Class<?> c = Class.forName(toolClassName);
			Class<? extends DrawingTool> toolClass = c.asSubclass(DrawingTool.class);
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(toolsFile, true));
				pw.println(toolClass.getName());
			} finally {
				if (pw != null)
					pw.close();
			}
			return addDrawingToolClass(toolClass);
		} catch (Exception e) {
			return false;
		}
	}
}
