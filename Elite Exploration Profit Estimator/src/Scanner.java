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

	String fileLines[] = new String[99999999]; // While I doubt the commander;s log will get this big, I've set the max size of the array this high just in case.
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
	
	public void draw() {
		clear(); // clear the window of anything
		background(80,80,80); // set the background color
		fill(0,0,0);
		textFont(primaryFont);
		textAlign(LEFT,TOP);
		text("TekCastPork's Elite: Dangerous Exploration "+ System.getProperty("line.separator") + "Profit Estimation Program V0.1",1,1);
		textAlign(LEFT);
		grabEvents(); // call a function that handles events in the commander journal
		textSize(20);
		text("Current System: " + currentSystem,5,90);
		text("Current Credits: "+(NumberFormat.getNumberInstance(Locale.US).format(currentCredits))+"Cr",5,130);
		text("System Credits: "+(NumberFormat.getNumberInstance(Locale.US).format(systemCredits))+"Cr",5,150);
		text("Credits Earned: "+(NumberFormat.getNumberInstance(Locale.US).format(explorationCredits))+"Cr",5,170);
		text("Profit: "+(NumberFormat.getNumberInstance(Locale.US).format((currentCredits+explorationCredits)-currentCredits))+"Cr",5,190);
		text("Scanned Body Name:   " + scannedBodyDetails[0],5,235);
		text("Scanned Body Type:   " + scannedBodyDetails[1],5,265);
		text("Terraforming Status: " + scannedBodyDetails[2],5,295);
		text("Scanned Body Value:  " +(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(scannedBodyDetails[3])))+"Cr",5,325);
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
	 * This function looks at the read line from the journal log to see what just happened, and reacts accordingly.
	 * There are no parameters for this function.
	 * @author TekCastPork
	 */
	public void grabEvents() {
		println("Let's analyse the file we picked.");
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
	      previousLine = inputLine;
	      println("Lets analyze the crap outa this line!");
	      println("-------------------------------------");
	      println(inputLine);
	      println("-------------------------------------");
	      JSONObject parser = new JSONObject(inputLine);
	      String eventType = parser.getString("event");
	      
	      if(eventType.equals("Scan") == true) { // We scanned a planet
	    	  String planetType = "";
	    	  boolean isStar = false;
	    	 try {
	    		 planetType =  getBodyType(parser.getString("PlanetClass")); // run a function that figures out the planet/star's type	
	    		 isStar = false;
	    	 } catch (Exception e) {
	    		 e.printStackTrace();
	    		 println("PlanetClass doesnt exist, must be a star.");
	    		 try {
	    		 planetType = getBodyType(parser.getString("StarType"));
	    		 isStar = true;
	    		 } catch (Exception e1) {
	    			 e1.printStackTrace();	
	    			 isStar = false;
	    		 }	    		 
	    	 }
	    	 int planetValue = getBodyValue(planetType);
	    	 systemCredits += planetValue;
	    	 println("As it would seem, this stellar body's class is: " + planetType);
	    	 println("It's value is: " + planetValue);
	    	 scannedBodyDetails[1] = planetType;
	    	 scannedBodyDetails[3] = Integer.toString(planetValue);
	    	 println("Now is it terraformable?");
	    	 String terraform = "";
	    	 if(isStar = false) {
	    		 terraform = parser.getString("TerraformState");
	    	 } else {
	    		 terraform = "";
	    	 }
	         scannedBodyDetails[2] = terraform;
	         println("And lets get the body's name.");
	         String bodyName = parser.getString("BodyName");	         
	         scannedBodyDetails[0] = bodyName;
	        //This is the end of the scan event IF Statement
	    	 
	      } else if(eventType.equals("FSDJump") == true) { // We jumped to a new system, we can use this to auto-submit the values
	        println("Yo we jumped to a new system! Lets sumbit the values and say some stuff.");
	        explorationCredits += systemCredits;
	        systemCredits = 0;
	        currentFuel = parser.getDouble("FuelLevel");
	        currentSystem = parser.getString("StarSystem");
	        scannedBodyDetails[0] = "";
	        scannedBodyDetails[1] = "";
	        scannedBodyDetails[2] = "";
	        scannedBodyDetails[3] = "0";
	        //This is the end of the FSDJump event IF Statement
	        
	      } else if(eventType.equals("LoadGame") == true) { // We loaded the game, lets get fuel data and current credits
	        println("We found the LoadGame event!");
			fuelCap = parser.getDouble("FuelCapacity");
	        currentFuel = parser.getDouble("FuelLevel");
	        println("Loaded fuel levels. Current level is "+currentFuel+"/"+fuelCap);
	        currentCredits = parser.getInt("Credits");
	        
	      } else if(eventType.equals("FuelScoop") == true) { // We scooped star piss
	        println("Yo we scooped some star piss!");
	        println("lets parse this to determine how much we got.");
	        currentFuel += parser.getDouble("Scooped");
	        //This is the end of the FuelScoop event IF Statement
	      } else if(eventType.equals("Location") == true) { // We jumped to a new system, we can use this to autosubmit the value
	    	  println("We are loading in! Lets get the location so we can show it.");
	    	  currentSystem = parser.getString("StarSystem");
	    	  //This is the end of the Location event IF statement		        
		  } else if(eventType.equals("SellExplorationData") == true) {// We sold our Cartography data
			  println("We sold the data, lets update some variables.");
			  println("We made " + explorationCredits + "credits during this exploration route.");
			  println("Lets add this to the currentCredits variable");
			  currentCredits += explorationCredits; 
			  println("We now have " + currentCredits + " (changed from " + (currentCredits-explorationCredits)+")");
			  println("Now resetting the exploration credits variable.");
			  explorationCredits = 0;
			  println("exploration credits reset to zero.");

	    	  //This is the end of the SellExplorationData event IF statement		        
		  } else if(eventType.equals("RefuelAll")) {
			  println("We bought fuel.");
			  currentCredits -= parser.getInt("Cost");
			  currentFuel += parser.getDouble("Amount");
			// end of Refuel all IF statement
		  }
	    } // this is the end of the else statement that allows us to analyze stuff		
	lineCount = fileLines.length;
	}
	
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
	/**
	 * getBodyName takes in the parsed input from the journal file, and outputs a planetary body classification. This function should only be in the Scan event IF statement.
	 * @param input
	 * @return bodyClass - String of the determined stellar body class
	 */
	public String getBodyType(String input) {
		println("We scanned a planet or star!");
        String planetClass = ""; // make a local variable for keeping track of the body's class
        println("Lets figure out its value.");
        String bodyClass = input;
        while(planetClass.equals("")) {
	        for(int i = 0; i < bodyClasses.length; i++) {
	          if(bodyClass.replaceAll("\"|/?_- ", "").toLowerCase().equals((bodyClasses[i].replaceAll("-_/\",.#@^"," ").toLowerCase()))) {
	            println("We have found the class of the body!");
	            println("It is a: " + bodyClasses[i] + " class planet!");
	            planetClass = bodyClasses[i];
	          }
	        }
	        if(!planetClass.equals("")) {// Lets check to see if we found the class of the star/body
	        	println("We found its class under the planets section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the planets section, lets test all the star types now.");
	        }
	        for(int i = 0; i < fuelStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(fuelStars[i].replaceAll("-_/\",.#@^"," ").toLowerCase())) { //Lets start with the Stars we scoop from
	        		println("We scanned a star that we can scoop fuel from!");
	        		println("Its class is: " + fuelStars[i]);
	        		planetClass = fuelStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the Fuel star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the fuel star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < dwarfStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(dwarfStars[i].replaceAll("-_/\",.#@^"," ").toLowerCase())) { //Lets start with the Stars we scoop from
	        		println("We scanned a dwarf star!");
	        		println("Its class is: " + dwarfStars[i]);
	        		planetClass = dwarfStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the dwarf star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the dwarf star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < protoStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(protoStars[i].replaceAll("-_/\",.#@^"," ").toLowerCase())) { //Lets start with the Stars we scoop from
	        		println("We scanned a proto star!");
	        		println("Its class is: " + protoStars[i]);
	        		planetClass = protoStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the proto star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the proto star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < wolfRayetStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(wolfRayetStars[i].replaceAll("-_/\",.#@^"," ").toLowerCase())) { //Lets start with the Stars we scoop from
	        		println("We scanned a Wolf Rayet star!");
	        		println("Its class is: " + wolfRayetStars[i]);
	        		planetClass = wolfRayetStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the wolf Rayet star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the wolf Rayet star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < carbonStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(carbonStars[i])) { //Lets start with the Stars we scoop from
	        		println("We scanned a Carbon star!");
	        		println("Its class is: " + carbonStars[i]);
	        		planetClass = carbonStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the carbon star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the wolf Rayet star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < miscStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(miscStars[i])) { //Lets start with the Stars we scoop from
	        		println("We scanned a Misc star!");
	        		println("Its class is: " + miscStars[i]);
	        		planetClass = miscStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the misc star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the misc star section, lets test some other star sections now.");
	        }for(int i = 0; i < whiteDwarfStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(whiteDwarfStars[i])) { //Lets start with the Stars we scoop from
	        		println("We scanned a White Dwarf star!");
	        		println("Its class is: " + whiteDwarfStars[i]);
	        		planetClass = whiteDwarfStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the White Dwarf star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the White Dwarf star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < specialStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(specialStars[i])) { //Lets start with the Stars we scoop from
	        		println("We scanned a Misc star!");
	        		println("Its class is: " + specialStars[i]);
	        		planetClass = specialStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the special star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the special star section, lets test some other star sections now.");
	        }
	        for(int i = 0; i < superGiantStars.length; i++) {
	        	if((bodyClass.replaceAll("-_/\",.#@^"," ").toLowerCase()).equals(superGiantStars[i])) { //Lets start with the Stars we scoop from
	        		println("We scanned a Misc star!");
	        		println("Its class is: " + superGiantStars[i]);
	        		planetClass = superGiantStars[i];
	        	}
	        	
	        }
	        if(!planetClass.equals("")) {// Lets check again to see if we found the class of the star/body
	        	println("We found its class under the Super Giant star section, no need to test the rest. Breaking the loop.");
	        	break;
	        } else {
	        	println("This body's class is not in the Super Giant star section.");
	        }
	        println("If we see this console output, it means that thestellar body class was not determinable, restarting the loop.");
        }
        println("Class: " + planetClass);
        return planetClass;
	} // end of getBodyName function
	/**
	 * 
	 * @param bodyType - String
	 * @param isStar - Boolean
	 * @return bodyValue - Integer
	 */
	public int getBodyValue(String bodyType) {
		int result = 0;
		println("Now beginning to figure out the value of the Planetary body.");
		println("Using String: " + bodyType);
		int fuelValues[] = {2916,2919,3012,2932,6135,2949,2903};
		int dwarfValues[] = {2889,2895,2881};
		int protoValues[] = {2000,2881};
		int[][] bodyClassValue = new int[][]{
			{65045	,34310	,928	,1246	,1240	,627885	,301410	,320203	,1824	,1824	,2314	,1721	,7013	,53663	,2693	,2799	,2761	,2095	,2095},
			{65045	,412249	,181104	,1246	,1240	,627885	,694971	,320203	,1824	,1824	,2314	,1721	,7013	,53663	,2693	,2799	,2761	,2095	,2095}

		};
		for(int i = 0; i < bodyClasses.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(bodyClasses[i].replaceAll("\"|/?_- ", "").toLowerCase())) {
				println("We found a match to the type, now if it terraformable?");
				if((scannedBodyDetails[2].replaceAll(".,;: '\"", "").toLowerCase()).equals("terraformable")) {
					println("It's terraformable!");
					println("It's worth: " + bodyClassValue[1][i]);
					result = bodyClassValue[1][i];
				} else {
					println("It's not terraformable...");
					println("It's worth: " + bodyClassValue[0][i]);
					result = bodyClassValue[0][i];
				}
			}
		} // end bodyClass for loop
		for(int i = 0; i < fuelStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(fuelStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: " + fuelValues[i]);			
				result = fuelValues[i];
			}
		} // end fuel stars for loop
		for(int i = 0; i < dwarfStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(dwarfStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: " + dwarfValues[i]);	
				result = dwarfValues[i];
			}
		} // end dwarf stars for loop
		for(int i = 0; i < protoStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(protoStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: " + protoValues[i]);	
				result = protoValues[i];
			}
		} // end proto stars for loop
		for(int i = 0; i < wolfRayetStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(wolfRayetStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: 2931");	
				result = 2931;
			}
		} // end wolfRayet stars for loop
		for(int i = 0; i < carbonStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(carbonStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: 2930");		
				result = 2930;
			}
		} // end carbon stars for loop
		for(int i = 0; i < miscStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(miscStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: 2000");		
				result = 2000;
			}
		} // end misc stars for loop
		for(int i = 0; i < whiteDwarfStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(whiteDwarfStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: 34294");	
				result = 34294;
			}
		} // end whiteDwarf stars for loop
		int specialValues[] = {54782,60589};
		for(int i = 0; i < specialStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(specialStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: " + specialValues[i]);	
				result = specialValues[i];
			}
		} // end special stars for loop
		int superGiantValues[] = {2949,2932,2903,2903,2916,2000,2000,2000,60589};
		for(int i = 0; i < superGiantStars.length; i++) {
			if((bodyType.replaceAll("\"|/\\,.?_- ", "").toLowerCase()).equals(superGiantStars[i].replaceAll("\"|/\\,.?_- ", "").toLowerCase())) {
				println("We found the scanned body value.");
				println("This " + bodyType + "is worth: " + superGiantValues[i]);	
				result = superGiantValues[i];
			}
		} // end special stars for loop
		return result;		
	}
	/**
	 * This function will handle crash events, it will be used to generalize all crash logs  and outputs
	 * @param e - Exception to handle
	 */
	public void handleCrashEvents(Exception e) {

		PrintWriter writer;
		writer = createWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLog"+month()+"-"+day()+"-"+year()+"_"+hour()+"-"+minute()+".log");
		println("OHSNAP WE CRASHED!!!!");
		println("WE BETTER TELL THE USER!");
		int chosen = JOptionPane.showConfirmDialog(null, "Elite: Dangerous Exploration Data Profit Estimator has" + System.getProperty("line.separator") + "crashed. Error: " + e.toString() + System.getProperty("line.separator") + "Would you like to submit an error report?", "CRITICAL FATAL 2ERROR", JOptionPane.ERROR_MESSAGE);
		if(chosen == 0) {
			println("Yay user hit yes! Lets make that error report");
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

