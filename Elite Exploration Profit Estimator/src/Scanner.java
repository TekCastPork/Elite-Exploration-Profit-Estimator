import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import processing.core.*;
import javax.swing.JOptionPane; // will be eventually used for error handling and crash stuff
import org.json.*;



public class Scanner extends PApplet{
	PFont secondaryFont,primaryFont;
	String filename = "";
	String filepath = "";
	String inputLine = "";
	String previousLine = "";
	String scannedBodyDetails[] = {"","","","0"}; // Name,Type,Terraformable?,Value
	JSONObject parser;

	//All of the planetary body classes

	String bodyClasses[] = {"Metal rich body","High metal content body","Rocky body","Icy body","Rocky ice body","Earthlike body","Water world","Ammonia world","Water giant","Water giant with life",
	                        "Gas giant with water based life","Gas giant with ammonia based life","Sudarsky class i gas giant","Sudarsky class ii gas giant","Sudarsky class iii gas giant",
	                        "Sudarsky class iv gas giant","Sudarsky class v gas giant","Helium rich gas giant","Helium gas giant"};
	String fuelStars[] = {"K","G","B","F","O","A","M"};
	String dwarfStars[] = {"L","T","Y"};
	String protoStars[] = {"AeBe","TTS"};
	String wolfRayetStars[] = {"W","WN","WNC","WC","WO"};
	String carbonStars[] = {"CS","C","CN","CJ","CHd"};
	String miscStars[] = {"MS","S","X"};
	String whiteDwarfStars[] = {"D","DA","DAB","DAO","DAZ","DAV","DB","DBZ","DBV","DO","DOV","DQ","DC","DCV","DX"};
	String specialStars[] = {"N","H"};
	String superGiantStars[] = {"A_BlueWhiteSuperGiant","F_WhiteSuperGiant","M_RedSuperGiant","M_RedGiant","K_OrangeGiant","RoguePlanet","Nebula","StellarRemnantNebula","SuperMassiveBlackHole"};
	boolean foundLoad = false;

	//There's alot isn't there?

	String fileLines[] = new String[500010]; // The journal file can only hit since 500k before a new file is made, so I added a few extra slots incase of failure
	int lineCount;
	int currentLine = 0;
	String currentSystem = "";
	File l;
	double fuelCap = 32;
	double currentFuel = 32;
	int currentCredits = 0;
	int explorationCredits = 0;
	int systemCredits = 0;

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

		        String filename = "C:/crashlogs/"+sdf.format(cal.getTime())+".txt";

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
		size(500,500);		
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
		setupActions();		
		String printLine[] = fileLines[fileLines.length-1].split(",");
		for(int i = 0; i < printLine.length; i++) {
	        println("["+i+"]   " + printLine[i]);
	    }
		try {
		previousLine = fileLines[fileLines.length-1];
		} catch (Exception e) {
			handleCrashEvents(e);
		}
		parser = new JSONObject();
	}
	//TODO Redo entire event system here after finishing EventParser class
	public void draw() {
		clear(); // clear the window of anything
		background(80,80,80); // set the background color
		fill(0,0,0);
		textFont(primaryFont);
		textAlign(LEFT,TOP);
		text("TekCastPork's Elite: Dangerous Exploration "+ System.getProperty("line.separator") + "Profit Estimation Program V0.1",1,1);
		textAlign(LEFT);
		textSize(20);
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
	      println("We are done parsing info.");
	      previousLine = inputLine;
	    }
		fill(40,40,40);
		rect(40,410,48+255,50);
		fill(20,20,20);
		rect(50,420,48+235,30);	
		textFont(secondaryFont);
		text("Fuel:" + currentFuel + "/" + fuelCap,50,400);
		if(currentFuel <= 10) {
			fill(255,0,0);
			rect(50,420,45+constrain(map((float)currentFuel,0,(float)fuelCap,0,235),0,235),30);			
		} else {
			fill(0,255,0);
			rect(50,420,45+constrain(map((float)currentFuel,0,(float)fuelCap,0,235),0,235),30);
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
			writer = createWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLog"+month()+"-"+day()+"-"+year()+"_"+hour()+"-"+minute()+".log");
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("{ERROR REPORT FOR ELITE: DANGEROUS EXPLORATION DATA PROFIT ESTIMATOR}");
			writer.println("This error report was generated on: " + month()+"/"+day()+"/"+year()+"(MM/DD/YY)");
			writer.println("This error report was generated due to the following error: " + e.toString());
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Stack Trace:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			e.printStackTrace(writer);
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Current State of all Global Variables:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("currentCredits     " + currentCredits);
			writer.println("currentFuel        " + currentFuel);
			writer.println("currentLine        " + currentLine);
			writer.println("currentSystem      " + currentSystem);
			writer.println("explorationCredits " + explorationCredits);
			writer.println("filename           " + filename);
			writer.println("filepath           " + filepath);
			writer.println("fuelCap            " + fuelCap);
			writer.println("inputLine          " + inputLine);
			writer.println("lineCount          " + lineCount);
			writer.println("previousLine       " + previousLine);
			writer.println("systemCredits      " + systemCredits);
			writer.println("------------------------------------------------------------------------------------------------------------");
			writer.println("Current State of all Global Array Variables:");
			writer.println("------------------------------------------------------------------------------------------------------------");
			for(int i = 0; i < bodyClasses.length; i++) {
				writer.println("bodyClasses["+i+"]     " + bodyClasses[i]);
			}
			for(int i = 0; i < carbonStars.length; i++) {
				writer.println("carbonStars["+i+"]     " + carbonStars[i]);
			}
			for(int i = 0; i < dwarfStars.length; i++) {
				writer.println("dwarfStars["+i+"]     " + dwarfStars[i]);
			}
			for(int i = 0; i < fileLines.length; i++) {
				writer.println("fileLines["+i+"]     " + fileLines[i]);
			}
			for(int i = 0; i < fuelStars.length; i++) {
				writer.println("fuelStars["+i+"]     " + fuelStars[i]);
			}
			for(int i = 0; i < miscStars.length; i++) {
				writer.println("miscStars["+i+"]     " + miscStars[i]);
			}
			for(int i = 0; i < protoStars.length; i++) {
				writer.println("protoStars["+i+"]     " + protoStars[i]);
			}
			for(int i = 0; i < scannedBodyDetails.length; i++) {
				writer.println("scannedBodyDetails["+i+"]     " + scannedBodyDetails[i]);
			}
			for(int i = 0; i < specialStars.length; i++) {
				writer.println("specialStars["+i+"]     " + specialStars[i]);
			}
			for(int i = 0; i <superGiantStars.length; i++) {
				writer.println("superGiantStars["+i+"]     " + superGiantStars[i]);
			}
			for(int i = 0; i < whiteDwarfStars.length; i++) {
				writer.println("whiteDwarfStars["+i+"]     " + whiteDwarfStars[i]);
			}
			for(int i = 0; i < wolfRayetStars.length; i++) {
				writer.println("wolfRayetStars["+i+"]     " + wolfRayetStars[i]);
			}
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

	public void setupActions() {
		for(int i = 0; i < fileLines.length; i++) {
			String loadParse[] = fileLines[i].split(",");
			if(loadParse[1].equals(" \"event\":\"LoadGame\"") && foundLoad == false) {
				foundLoad = true;
				println("We found the LoadGame event!");
				println("Lets parse it out.");
				String tempParse[] = loadParse[8].split(":");
				fuelCap = Double.parseDouble((tempParse[1].replaceAll("^\"|\"$", "")));
				tempParse = loadParse[7].split(":");
		        currentFuel = Double.parseDouble((tempParse[1].replaceAll("^\"|\"$", "")));
		        println("Loaded fuel levels. Current level is "+currentFuel+"/"+fuelCap);
		        tempParse = loadParse[11].split(":");
		        for(int j = 0; j < tempParse.length; j++) {
		        	println(tempParse[j]);
		        }
		        currentCredits = Integer.parseInt(tempParse[1]);
				
			} else if(loadParse[1].equals(" \"event\":\"Location\"") == true) { // We jumped to a new system, we can use this to autosubmit the value
		    	  println("We are loading in! Lets get the location so we can show it.");
		    	  String systemParse[] = fileLines[i].split(",");
		    	  String galaxyParse[] = systemParse[3].split(":");
		    	  currentSystem = galaxyParse[1];
		    	  currentSystem = currentSystem.replaceAll("^\"|\"$", "");
		    	  //This is the end of the Location event IF statement		        
			  }	else if(loadParse[1].equals(" \"event\":\"FSDJump\"") == true) { // We jumped to a new system
		    	  println("We are loading in! Lets get the location so we can show it.");
		    	  String systemParse[] = fileLines[i].split(",");
		    	  String galaxyParse[] = systemParse[2].split(":");
		    	  currentSystem = galaxyParse[1];
		    	  currentSystem = currentSystem.replaceAll("^\"|\"$", "");
		    	  //This is the end of the Location event IF statement		        
			  }			
		}
	}
}

