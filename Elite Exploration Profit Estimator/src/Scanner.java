import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import javax.swing.JOptionPane;



/**
 * This is the main class that draws everything to the initial GUI
 * @author TekCastPork
 *
 */
public class Scanner {
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        JOptionPane.showMessageDialog(null, "An unhandled crash has caused the main java class of this program to crash." + System.getProperty("line.separator")
		        + "The program will probably appear to keep running, but in reality it is not." + System.getProperty("line.separator")
		        + "Please submit any crash reports in the CrashLog folder (only the most recent ones please) as well as your log file.", "ALERT!", JOptionPane.ERROR_MESSAGE);
		        String filename = System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLogs" + File.separator + "Unhandled Crash"+Resources.sdf.format(Resources.cal.getTime())+".log";
		        
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
		setup();
		while(true) {
			draw();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	public static void initialChecks() {
		if(Installer.doesExist(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator") == true) { // If the data folder exists
			System.out.println("The data folder does exist, there is no need to make it.");			
		} else {
			System.out.println("The data folder doesn't exist! We must make it before everything else crashes!");
			System.out.println("Lets inform the user!");
			JOptionPane.showMessageDialog(null, "This program has detected that this is the first time it has ever been ran." + System.getProperty("line.separator") + 
												"Before we can continue we must make some required directories." + System.getProperty("line.separator") + 
												"These directories will be places in your C:/Users/<Your_Username_here> folder.", "Initial Setup", JOptionPane.INFORMATION_MESSAGE);
			JOptionPane.showMessageDialog(null, "If these files get lost, moved, or deleted, all accumulated profit estimation data WILL be lost! Please don't delete these files!", "Initial Setup", JOptionPane.WARNING_MESSAGE);
			try {
				Installer.makeFolder(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator");
				Installer.makeFolder(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Config");
				Installer.makeFile(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Config" + File.separator + "Configuration.cfg", "{  \"lastLocation\": No Location Specified,\"currentCredits\": 0,\"systemCredits\": 0,\"explorationCredits\": 0,\"totalCredits\": 0,\"fuelLevel\": 0}");
				Installer.makeFolder(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLogs");
				Installer.makeFolder(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Logs");				
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Something failed during the creation of the required files!" + File.separator + 
													"Exception: " + e.getStackTrace().toString(), "ERROR", JOptionPane.WARNING_MESSAGE);
			}
			JOptionPane.showMessageDialog(null, "All of the required file have been created!", "Initial Setup", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void setup() {
		initialChecks();
		Logger.init(); // start the logger so it will actually log stuff
		Logger.printLog("TekCastPork's Elite: Dangerous Exploration Data Profit Estimator");
		Logger.printLog("This log file was generated from program version " + Resources.versionInfo);
		Logger.printLog("----------------------------------------------------------"); // write some header text to the top of the log
		JOptionPane.showMessageDialog(null, "This program is still in ALPHA! Expect the occasional bug and/or crash.", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(null, "Please start your instance of Elite: Dangerous first, then click ok on this popup." + System.getProperty("line.separator") + "This makes sure that the correct journal file is read.", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
		try {
			Loader.loadConfig();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		// Run a custom function that can grab the log file for me.
		getLogFile(System.getProperty("user.home") + File.separator + "Saved Games" + File.separator + "Frontier Developments" + File.separator + "Elite Dangerous");
		Logger.printLog("Grabbed file name is: " + Resources.filename);
		try {
			Resources.lines = Files.readAllLines(Paths.get(Resources.filepath), Charset.forName("ISO-8859-1"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Resources.lineCount = Resources.lines.size();
		Logger.printLog("Length is: " + Resources.lineCount);				
		Logger.printLog("Because we are setting up lets quickly scan the lines for a LoadGame event to gather some initial data.");
		Logger.printLog("We should also search for a location event to figure out where we are.");
		for(int i = 0; i < Resources.lines.size(); i++) {
	        Logger.printLog("["+i+"]   " + Resources.lines.get(i));
	        EventParser.inputData(Resources.lines.get(i));
	        int eventNumber = EventParser.determineEvent();
	        if(eventNumber == 7) {
	        	Logger.printLog("We found the LoadGame event.");
	        	GUIDraw.inputData(EventParser.gatherInfo(7));
	        	GUIDraw.updateScreen();
	        } else if(eventNumber == 30) {
	        	Logger.printLog("We found the Location event.");
	        	GUIDraw.inputData(EventParser.gatherInfo(30));
	        	GUIDraw.updateScreen();
	        }
	    }
		try {
			Resources.previousLine = Resources.lines.get(Resources.lines.size()-1);
		} catch (Exception e) {
			handleCrashEvents(e);
		}
		Logger.printLog("All initial data gathering has been executed, starting display...");
		Logger.printLog("Remember: file name is: " + Resources.filepath);
		Display.lblSuperCharge.setEnabled(false);
		Display.lblSuperCharge.setVisible(false);
		Display.main();
	}

	public static void draw() {
		try {
			if(BigBrother.watch() == true) { // If BigBrother saw you write in that private diary :P
				try {
					if(Resources.lines.size() > 300) {
						Logger.printLog("The lines List has gotten pretty big, cleaning it out before parsing to help a little with RAM.");
						Resources.lines.clear();
					}
					Resources.lines = Files.readAllLines(Paths.get(Resources.filepath), Charset.forName("ISO-8859-1"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(int i = 0; i < Resources.lines.size(); i++) {
			    	Resources.inputLine = Resources.lines.get(i);	    	
			    }
				if(Resources.inputLine.equals(Resources.previousLine)) { // if we read a dupe
			    } else {
			      Logger.printLog("This new line is different!");
			      EventParser.inputData(Resources.inputLine);
			      String bodyInfo[] = EventParser.gatherInfo(EventParser.determineEvent());
			      Logger.printLog("We have gathered info on the event.");
			      for(int i = 0; i < bodyInfo.length; i++) {
			    	  Logger.printLog("["+i+"]   " + bodyInfo[i]);
			      }
			      GUIDraw.inputData(bodyInfo);
			      Logger.printLog("We have inputted data to the GUI.");
			      GUIDraw.updateScreen();
			      Logger.printLog("We have updated the screen variabled used by GUIDraw");
			      Logger.printLog("We are done parsing info.");
			      Resources.previousLine = Resources.inputLine;
			    }				
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
	public static void getLogFile(String path) {
		   Logger.printLog("getLogFile has been called to determine the log file to load (last in the directory really...)");
		   Logger.printLog("Listing all filenames in a directory: ");
		   String[] filenames = listFileNames(path);
		   for(int i = 0; i < filenames.length; i++) {
			   System.out.println(filenames[i]);
		   }
		   Logger.printLog("Listing info about all files in a directory: ");
		   File pathway = new File(path);
		   File[] files = pathway.listFiles();
		   for (int i = 0; i < files.length; i++) {
		     File f = files[i];    
		     Logger.printLog("Name: " + f.getName());
		     Logger.printLog("Is directory: " + f.isDirectory());
		     Logger.printLog("Size: " + f.length());
		     String lastModified = new Date(f.lastModified()).toString();
		     Logger.printLog("Last Modified: " + lastModified);
		     Logger.printLog("-----------------------");
		     Resources.filename = f.getName();
		     Resources.filepath = f.getAbsolutePath();
		   }
		 }
	
	/**
	 *  This function is used as part of getLogFile. This function finds the files to use.
	 * @param dir - path to files to search
	 * @return String Array of file information
	 * @author Daniel Shiffman
	 */
	public static String[] listFileNames(String dir) {
	   File file = new File(dir);
	   if (file.isDirectory()) {
	     String names[] = file.list();
	     return names;
	   } else {
	     // If it's not a directory
	     return null;
	   }
	 }

	public static void handleCrashEvents(Exception e) {

		PrintWriter writer = null;
		Logger.printLog("OHSNAP WE CRASHED!!!!");
		Logger.printLog("WE BETTER TELL THE USER!");
		int chosen = JOptionPane.showConfirmDialog(null, "Elite: Dangerous Exploration Data Profit Estimator has" + System.getProperty("line.separator") + "crashed. Error: " + e.toString() + System.getProperty("line.separator") + "Would you like to submit an error report?", "CRITICAL FATAL 2ERROR", JOptionPane.ERROR_MESSAGE);
		if(chosen == 0) {
			Logger.printLog("Yay user hit yes! Lets make that error report");
			try {
				writer = new PrintWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLogs" + File.separator + "Handled Crash"+Resources.sdf.format(Resources.cal.getTime())+".log");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("{ERROR REPORT FOR ELITE: DANGEROUS EXPLORATION DATA PROFIT ESTIMATOR}");
			writer.println("This error report was generated in version " + Resources.versionInfo + " of the program.");
			writer.println("This error report was generated on: " + Resources.sdf.format(Resources.cal.getTime())+"(YYYY/MM/DD_HHmmss)");
			writer.println("This error report was generated due to the following error: " + e.toString());
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Stack Trace:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			e.printStackTrace(writer);
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Current State of Variables:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("currentCredits     " + Resources.currentCredits);
			writer.println("currentFuel        " + Resources.currentFuel);
			writer.println("currentLine        " + Resources.currentLine);
			writer.println("currentSystem      " + Resources.currentSystem);
			writer.println("explorationCredits " + Resources.explorationCredits);
			writer.println("filename           " + Resources.filename);
			writer.println("filepath           " + Resources.filepath);
			writer.println("fuelCap            " + Resources.fuelCap);
			writer.println("inputLine          " + Resources.inputLine);
			writer.println("lineCount          " + Resources.lineCount);
			writer.println("previousLine       " + Resources.previousLine);
			writer.println("systemCredits      " + Resources.systemCredits);
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Error log completed.");
			writer.flush();
			writer.close();
			JOptionPane.showMessageDialog(null, "Error report created at C:\\Elite Exploration Estimator\\crashes." + System.getProperty("line.separator")+"Please include your PC's specs with the error report and your OS.", "CRITICAL FATAL ERROR", JOptionPane.INFORMATION_MESSAGE);
			System.exit(2);
			
		} else {
			Logger.printLog("User did not hit YES, closing program instead of handling error.");
			System.exit(1);
		}
	}
}


