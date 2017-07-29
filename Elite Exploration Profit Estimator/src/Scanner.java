import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import processing.core.*;
import javax.swing.JOptionPane;



/**
 * This is the main class that draws everything to the initial GUI
 * @author TekCastPork
 *
 */
public class Scanner extends PApplet{
	PFont secondaryFont,primaryFont;
	String filename = "";
	String filepath = "";
	String inputLine = "";
	String previousLine = "";
	String fileLines[] = new String[500010]; // The journal file can only hit since 500k before a new file is made, so I added a few extra slots incase of failure
	int lineCount;
	int currentLine = 0;
	File l;

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

		        String filename = System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Unhandled Crashes"+sdf.format(cal.getTime())+".log";
		        
		        PrintStream writer;
		        try {
		            writer = new PrintStream(filename, "UTF-8");
		            writer.println(e.getClass() + ": " + e.getMessage());
		            for (int i = 0; i < e.getStackTrace().length; i++) {
		                writer.println(e.getStackTrace()[i].toString());
		            }

		        } catch (Exception e1) {
		            e1.printStackTrace();
		        }

		    }
		});
		
		PApplet.main("Scanner");
	}
	
	public void settings() {
		size(250,40);
	}

	
	public void setup() {

		JOptionPane.showMessageDialog(null, "This program is still in SUPER ALPHA! Expect bugs and crashes.", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
		primaryFont = createFont("Georgia",24);
		secondaryFont = createFont("Arial",20);
		getLogFile(System.getProperty("user.home") + File.separator + "Saved Games" + File.separator + "Frontier Developments" + File.separator + "Elite Dangerous"); // Better method for getting to the journal files that is adaptive to different filesystems, Windows only, and assuming default location
		println("Grabbed file name is: " + filename);
		println("Path: " + filepath);
		fileLines = loadStrings(filepath);
		lineCount = fileLines.length;
		try {
		println("["+(lineCount-1)+"]"+"{"+lineCount+"}"+fileLines[lineCount-1]);
		} catch(Exception e) {
			handleCrashEvents(e);
		}
		println("Length is: " + lineCount);				
		println("Because we are setting up lets quickly scan the lines for a LoadGame event to gather some initial data.");
//		setupActions();		
		String printLine[] = fileLines[fileLines.length-1].split(",");
		for(int i = 0; i < printLine.length; i++) {
	        println("["+i+"]   " + printLine[i]);
	    }
		try {
		previousLine = fileLines[fileLines.length-1];
		} catch (Exception e) {
			handleCrashEvents(e);
		}
		Display.main(args);
	}

	public void draw() {
		clear(); // clear the window of anything
		background(80,80,80); // set the background color
		textSize(16);
		text("Do not close this window.",5,15);
		text("It will close the program.",5,35);
		fill(0,0,0);
		fileLines = loadStrings(filepath);
	    println("What is on the last line?");
	    try {
	    for(int i = 0; i < fileLines.length; i++) {
	    	inputLine = fileLines[i];
	    	
	    }
	    } catch(Exception e) {
	    	handleCrashEvents(e);
	    }
		if(inputLine.equals(previousLine)) { // if we read a dupe
		      println("Yo daug we read a dupe line!");
	    } else {
	      println("This line is different!");
	      EventParser.inputData(inputLine);
	      String bodyInfo[] = EventParser.gatherInfo(EventParser.determineEvent());
	      for(int i = 0; i < bodyInfo.length; i++) {
	    	  println("["+i+"]   " + bodyInfo[i]);
	      }
	      GUIDraw.inputData(bodyInfo);
	      GUIDraw.updateScreen();
	      println("We are done parsing info.");
	      previousLine = inputLine;
	    }
		textFont(secondaryFont);
		
		
	} // end of draw function
	
	/**
	 * This is a function that searches a directory and finds the "most recent" file from Frontier's verbose logging.
	 * Really it's just finding the file with the name closest to Z because numbers.
	 * This version of the function is modified from the original.
	 * @param path - String || String of the path to the files to search and analyze
	 * @author Daniel Shiffman
	 * 
	 * from https://processing.org/examples/directorylist.html
	 */
	public void getLogFile(String path) {

		   println("Listing all filenames in a directory: ");
		   String[] filenames = listFileNames(path);
		   printArray(filenames);

		   println("\nListing info about all files in a directory: ");
		   File[] files = listFiles(path);
		   for (int i = 0; i < files.length; i++) {
		     File f = files[i];    
		     println("Name: " + f.getName());
		     println("Is directory: " + f.isDirectory());
		     println("Size: " + f.length());
		     String lastModified = new Date(f.lastModified()).toString();
		     println("Last Modified: " + lastModified);
		     println("-----------------------");
		     filename = f.getName();
		     filepath = f.getAbsolutePath();
		   }
		 }
	
	/**
	 *  This function is used as part of getLogFile. This function finds the files to use.
	 * @param dir - path to files to search
	 * @return String Array of file information
	 * @author Daniel Shiffman
	 */
	public String[] listFileNames(String dir) {
	   File file = new File(dir);
	   if (file.isDirectory()) {
	     String names[] = file.list();
	     return names;
	   } else {
	     // If it's not a directory
	     return null;
	   }
	 }

	public void handleCrashEvents(Exception e) {

		PrintWriter writer;
		println("OHSNAP WE CRASHED!!!!");
		println("WE BETTER TELL THE USER!");
		int chosen = JOptionPane.showConfirmDialog(null, "Elite: Dangerous Exploration Data Profit Estimator has" + System.getProperty("line.separator") + "crashed. Error: " + e.toString() + System.getProperty("line.separator") + "Would you like to submit an error report?", "CRITICAL FATAL 2ERROR", JOptionPane.ERROR_MESSAGE);
		if(chosen == 0) {
			println("Yay user hit yes! Lets make that error report");
			writer = createWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Handled Crashes"+month()+"-"+day()+"-"+year()+"_"+hour()+"-"+minute()+".log");
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("{ERROR REPORT FOR ELITE: DANGEROUS EXPLORATION DATA PROFIT ESTIMATOR}");
			writer.println("This error report was generated on: " + month()+"/"+day()+"/"+year()+"(MM/DD/YY)");
			writer.println("This error report was generated due to the following error: " + e.toString());
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Stack Trace:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			e.printStackTrace(writer);
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Current State of Variables:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("currentCredits     " + GUIDraw.currentCredits);
			writer.println("currentFuel        " + GUIDraw.currentFuel);
			writer.println("currentLine        " + currentLine);
			writer.println("currentSystem      " + GUIDraw.currentSystem);
			writer.println("explorationCredits " + GUIDraw.explorationCredits);
			writer.println("filename           " + filename);
			writer.println("filepath           " + filepath);
			writer.println("fuelCap            " + GUIDraw.fuelCap);
			writer.println("inputLine          " + inputLine);
			writer.println("lineCount          " + lineCount);
			writer.println("previousLine       " + previousLine);
			writer.println("systemCredits      " + GUIDraw.systemCredits);
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Error log completed.");
			writer.flush();
			writer.close();
			JOptionPane.showMessageDialog(null, "Error report created at C:\\Elite Exploration Estimator\\crashes." + System.getProperty("line.separator")+"Please include your PC's specs with the error report and your OS.", "CRITICAL FATAL ERROR", JOptionPane.INFORMATION_MESSAGE);
			System.exit(2);
			
		} else {
			println("User did not hit YES, closing program instead of handling error.");
			System.exit(1);
		}
	}
}


