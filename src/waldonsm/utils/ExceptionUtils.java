package waldonsm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ExceptionUtils {

	/**
	 * Takes a Throwable and returns the data printed out by the printStackTrace method as a String for display (including any newlines);
	 * @param t the Throwable
	 * @return the Throwable's stackTrace as a String.
	 */
	public static String getStackTraceString(Throwable t) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos, true);
		t.printStackTrace(ps);
		ps.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		Scanner scan = new Scanner(bis);
		StringBuilder builder = new StringBuilder();
		while(scan.hasNextLine()) {
			builder.append(scan.nextLine());
			if (scan.hasNextLine())
				builder.append("\n");
		}
		return builder.toString();
	}
	
	/**
	 * Tests the ExceptionUtils class
	 */
	public static void testMain() {
		try {
			throw new IllegalStateException("WHAT THE *#($#$*");
		} catch (IllegalStateException e) {
			e.printStackTrace(System.err);
			String stackTrace = getStackTraceString(e);
			System.out.println(stackTrace);
		}
	}
}
