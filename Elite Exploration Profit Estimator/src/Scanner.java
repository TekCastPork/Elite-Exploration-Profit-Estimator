import java.io.File;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import processing.core.*;



public class Scanner extends PApplet{
	PFont secondaryFont,primaryFont;
	String filename = "";
	String filepath = "";
	String inputLine = "";
	String previousLine = "";

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
	String SuperGiantStars[] = {"A_BlueWhiteSuperGiant","F_WhiteSuperGiant","M_RedSuperGiant","M_RedGiant","K_OrangeGiant","RoguePlanet","Nebula","StellarRemnantNebula","SuperMassiveBlackHole"};

	//There's alot isn't there?

	String fileLines[] = new String[99999999];
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
		PApplet.main("Scanner");
	}
	
	public void settings() {
		size(500,500);		
	}
	
	public void setup() {
		primaryFont = createFont("Georgia",24);
		secondaryFont = createFont("Arial",20);
		getLogFile("C:\\Users\\TekCastPork\\Saved Games\\Frontier Developments\\Elite Dangerous");
		println("Grabbed file name is: " + filename);
		println("Path: " + filepath);
		fileLines = loadStrings(filepath);
		lineCount = fileLines.length;
		println("["+(lineCount-1)+"]"+"{"+lineCount+"}"+fileLines[lineCount-1]);
		println("Length is: " + lineCount);		
		
		println("Because we are setting up lets quickly scan the lines for a LoadGame event to gather some initial data.");
		
		for(int i = 0; i < fileLines.length; i++) {
			String loadParse[] = fileLines[i].split(",");
			if(loadParse[1].equals(" \"event\":\"LoadGame\"")) {
				println("We found the LoadGame event!");
				println("Lets parse it out.");
				String tempParse[] = loadParse[8].split(":");
				fuelCap = Double.parseDouble((tempParse[1].replaceAll("^\"|\"$", "")));
				tempParse = loadParse[7].split(":");
		        currentFuel = Double.parseDouble((tempParse[1].replaceAll("^\"|\"$", "")));
		        println("Loaded fuel levels. Current level is "+currentFuel+"/"+fuelCap);
		        tempParse = loadParse[11].split(":");
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
		String printLine[] = fileLines[fileLines.length-1].split(",");
		for(int i = 0; i < printLine.length; i++) {
	        println("["+i+"]   " + printLine[i]);
	    }
		previousLine = fileLines[fileLines.length-1];
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
		text("Current System:" + currentSystem,5,80);
		text("Current Credits: "+(NumberFormat.getNumberInstance(Locale.US).format(currentCredits))+"cr",5,130);
		text("System Credits: "+(NumberFormat.getNumberInstance(Locale.US).format(systemCredits))+"cr",5,150);
		text("Total Credits: "+(NumberFormat.getNumberInstance(Locale.US).format(currentCredits+systemCredits))+"cr",5,170);
		text("Profit: "+(NumberFormat.getNumberInstance(Locale.US).format((currentCredits+systemCredits)-currentCredits))+"cr",5,190);
//		text("mouseX: " + mouseX + "  mouseY: " + mouseY,10,100);
		fill(40,40,40);
		rect(340,110,65,267);
		fill(20,20,20);
		rect(350,366,45,-247);	
		textFont(secondaryFont);
		textAlign(CENTER);
		text("Fuel",372,70);
		text(currentFuel+"/"+fuelCap+"T",372,100);
		textAlign(LEFT,CENTER);
		fill(255,0,0);
		text("-- E",410,355);
		fill(0,255,0);
		text("-- F",410,120);
		fill(255,255,0);
		text("-- 1/2",410,235);
		textAlign(LEFT);
		if(currentFuel <= 10) {
			fill(255,0,0);
			rect(350,366,45,-(10+constrain(map((float)currentFuel,0,(float)fuelCap,0,235),0,235)));			
		} else {
			fill(0,255,0);
			rect(350,366,45,-(10+constrain(map((float)currentFuel,0,(float)fuelCap,0,235),0,235)));
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
	    println("["+(lineCount-1)+"]"+"{"+lineCount+"}"+fileLines[lineCount-1]);
	    inputLine = fileLines[lineCount-1];
		if(inputLine.equals(previousLine)) { // if we read a dupe
		      println("Yo daug we read a dupe line!");
	    } else {
	      println("This line is different!");
	      previousLine = inputLine;
	      println("Lets analyze the crap outa this line!");
	      println("-------------------------------------");
	      println(inputLine);
	      println("-------------------------------------");
	      String parseLine[] = inputLine.split(",");
	      for(int i = 0; i < parseLine.length; i++) {
	        println("["+i+"]   " + parseLine[i]);
	      }
	      println("Scan? " + parseLine[1].equals(" \"event\":\"Scan\""));
	      println("Hyperspace jump? " + parseLine[1].equals(" \"event\":\"FSDJump\""));
	      println("Loading game? " + parseLine[1].equals(" \"event\":\"LoadGame\""));
	      println("Fuel scoop? " + parseLine[1].equals(" \"event\":\"FuelScoop\""));
	      if(parseLine[1].equals(" \"event\":\"Scan\"") == true) { // We scanned a planet
	    	 String planetType =  getBodyName(parseLine); // run a function that figures out the planet/star's type	    	
	        //This is the end of the scan event IF Statement
	    	 
	      } else if(parseLine[1].equals(" \"event\":\"FSDJump\"") == true) { // We jumped to a new system, we can use this to auto-submit the values
	        println("Yo we jumped to a new system! Lets sumbit the values and say some stuff.");
	        explorationCredits += systemCredits;
	        String fuelParse[] = parseLine[15].split(":");
	        currentFuel = Integer.parseInt(fuelParse[1]);
	        //This is the end of the FSDJump event IF Statement
	        
	      } else if(parseLine[1].equals(" \"event\":\"LoadGame\"") == true) { // We loaded the game, lets get fuel data and current credits
	        println("We found the LoadGame event!");
			println("Lets parse it out.");
			String tempParse[] = parseLine[8].split(":");
			fuelCap = Double.parseDouble((tempParse[1].replaceAll("^\"|\"$", "")));
			tempParse = parseLine[7].split(":");
	        currentFuel = Double.parseDouble((tempParse[1].replaceAll("^\"|\"$", "")));
	        println("Loaded fuel levels. Current level is "+currentFuel+"/"+fuelCap);
	        tempParse = parseLine[11].split(":");
	        currentCredits = Integer.parseInt(tempParse[1]);
	        
	      } else if(parseLine[1].equals(" \"event\":\"FuelScoop\"") == true) { // We scooped star piss
	        println("Yo we scooped some star piss!");
	        println("lets parse this to determine how much we got.");
	        String scoopedFuel[] = parseLine[2].split(":");
	        for(int i = 0; i < scoopedFuel.length; i++) {
	          println("["+i+"]   " + scoopedFuel[i]);
	        }
	        currentFuel += Integer.parseInt(scoopedFuel[1]);
	        //This is the end of the FuelScoop event IF Statement
	      } else if(parseLine[1].equals(" \"event\":\"Location\"") == true) { // We jumped to a new system, we can use this to autosubmit the value
	    	  println("We are loading in! Lets get the location so we can show it.");
	    	  String systemParse[] = parseLine[3].split(":");
	    	  currentSystem = systemParse[1];
	    	  currentSystem = currentSystem.replaceAll("^\"|\"$", "");
	    	  //This is the end of the Location event IF statement		        
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
	public String getBodyName(String Array[]) {
		String parseLine[] = Array;
		println("We scanned a planet or star!");
        println("Now that the line is split, lets parse the data we care about because planet scan");
        println("Lets Start with the planet's name.");
        String bodyName[] = parseLine[2].split(":");
        for(int i = 0; i < bodyName.length; i++) {
          println("["+i+"]   " + bodyName[i]);
        }
        println("Now the body class.");
        String bodyClass[] = parseLine[6].split(":");
        for(int i = 0; i < bodyClass.length; i++) {
          println("["+i+"]   " + bodyClass[i]);
        }
        println("Now is it terraformable?");
        String isTerraformable[] = parseLine[5].split(":");
        for(int i = 0; i < isTerraformable.length; i++) {
          println("["+i+"]   " + isTerraformable[i]);
        }
        String planetClass = ""; // make a local variable for keeping track of the body's class
        println("Lets figure out its value.");
        while(planetClass.equals("")) {
	        for(int i = 0; i < bodyClasses.length; i++) {
	          if((bodyClass[1].replaceAll("\"|/?_- ", "").toLowerCase()).equals((bodyClasses[i].replaceAll("\"|/?_- ", "").toLowerCase()))) {
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
	        	if((bodyClass[1].replace('-', ' ').replace('_',' ').toLowerCase()).equals(fuelStars[i])) { //Lets start with the Stars we scoop from
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
	        	if((bodyClass[1].replace('-', ' ').replace('_',' ').toLowerCase()).equals(dwarfStars[i])) { //Lets start with the Stars we scoop from
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
	        	if((bodyClass[1].replace('-', ' ').replace('_',' ').toLowerCase()).equals(protoStars[i])) { //Lets start with the Stars we scoop from
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
        }
        println("Class: " + planetClass);
        return planetClass;
	}

}

