import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.json.*;

/**
 * This class parses the events from the game and outputs data in an array for later displaying
 * @author TekCastPork
 *
 */
public class EventParser {
	static JSONObject parser;

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		        JOptionPane.showMessageDialog(null, "An unhandled crash has caused the EventParser class of this program to crash." + System.getProperty("line.separator")
		        + "The program will probably appear to keep running, but in reality it is not." + System.getProperty("line.separator")
		        + "Please submit any crash reports in the CrashLog folder (only the most recent ones please) as well as your log file.", "ALERT!", JOptionPane.ERROR_MESSAGE);
		        String filename = System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLogs" + File.separator + "Unhandled Crash"+sdf.format(cal.getTime())+".log";
		        
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

	}
	
	/**
	 * This function takes in a string containing JSON for later parsing
	 * Returns true if it succeeds or false if it fails
	 * @param data
	 * @return
	 */
	public static void inputData(String data) {
		Logger.printLog("We are inputting data from a JSON String into a variable for analysis.");
		try {			
			parser = new JSONObject(data);
			Logger.printLog("We inputted the String.");
		} catch (Exception e) {
			Logger.printLog("Something caused the data to not be inputted properly.");
			for(int i = 0; i < e.getStackTrace().length; i++) {
				Logger.printLog(e.getStackTrace()[i].toString());
			}
		}	
	}
	/**
	 * This function returns a integer that references the event type
	 * @return
	 */
	public static int determineEvent() {
		 Logger.printLog("We need to determine the event that happened");
		 String event = parser.getString("event");
		 int returnValue = -1;
		 for(int i = 0; i < Resources.events.length; i++) {
			 if(event.equals(Resources.events[i])) {
				 Logger.printLog("[EventParser]{determineEvent} We found a matching event.");
				 Logger.printLog("[EventParser]{determineEvent} Event is:  " + Resources.events[i] + "  ("+i+")");
				 returnValue = i;
			 } else {
				 Logger.printLog("[EventParser]{determineEvent} Event " + event + " does not match the indexed event " + Resources.events[i] + "  (" + i + ")");
			 }
		 }
		 return returnValue;
	}
	
	/**
	 * This function parses some initial JSON to determine the StellarBody's class and type
	 * @return String[] - String Array containing StellarBody's class and type
	 */
	public static String[] determineBodyType() {
		Logger.printLog("[EventParser]{determineBodyType} Determining Stellar Body's Type (Star/Planet");
		Logger.printLog("[EventParser]{determineBodyType} Trying a Stellar body first");
		String planetType[] = {"","Stellar Body"};
		try {
   		 planetType[0] =  parser.getString("PlanetClass"); // run a function that figures out the planet/star's type	   		 
   	 	} catch (Exception e) {
   	 	 planetType[1] = "Star";
   		 e.printStackTrace();
   		 Logger.printLog("[EventParser]{determineBodyType} PlanetClass doesnt exist....");    	
   		 Logger.printLog("[EventParser]{determineBodyType} Now trying for StarType");
   	 	}			
		try {
   		 planetType[0] =  parser.getString("StarType"); // run a function that figures out the planet/star's type	
   	 	} catch (Exception e) {
   	     planetType[1] = "Stellar Body";
   		 e.printStackTrace();
   		 Logger.printLog("[EventParser]{determineBodyType} StarType doesnt exist....");    		 
   	 	}	
		return planetType;
	}
	/**
	 * This function runs different parsing functions based on the event index, since we don't need to parse the terraform state of a star.
	 * @param type - index of the event
	 */
	public static String[] gatherInfo(int type) {
		String output[] = {"null","null","null","null","null","null"};
		switch(type) {
		case 0: // Continued
			Logger.printLog("[EventParser]{gatherInfo} Continued Event! The file has been closed and a new one is generated, we must re-run the file grabbing function!");
			Logger.printLog("[EventParser]{gatherInfo} Otherwise we won't get any new JSON to parse.");
			break;
		case 1:
			Logger.printLog("[EventParser]{gatherInfo} We bought ammo, lets get the price and modify some variables later on.");
		case 2:
			Logger.printLog("[EventParser]{gatherInfo} We bought drones, lets get the price and modify some variables later on.");
		case 3:
			Logger.printLog("[EventParser]{gatherInfo} We bought Exploration Data, lets get the price and modify some variables later on.");
		case 4:
			Logger.printLog("[EventParser]{gatherInfo} We bought Trade Data, lets get the price and modify some variables later on.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 5:
			Logger.printLog("[EventParser]{gatherInfo} We jumped somewhere!");
			output[0] = "Jump";
			output[1] = parser.getString("StarSystem");
			output[2] = Double.toString(parser.getDouble("FuelLevel"));
			break;
		case 6:
			Logger.printLog("[EventParser]{gatherInfo} Vegeta, what does the scanner say about his boost level?");
			Logger.printLog("[EventParser]{gatherInfo} It's OVER 4!");
			output[0] = "Boost";
			output[1] = Double.toString(parser.getDouble("BoostValue"));
			break;
		case 7:
			Logger.printLog("[EventParser]{gatherInfo} The game is loading.");
			output[0] = "Load";
			output[1] = Integer.toString(parser.getInt("Credits"));
			output[2] = Double.toString(parser.getDouble("FuelLevel"));
			output[3] = Double.toString(parser.getDouble("FuelCapacity"));
			break;
		case 8:
			Logger.printLog("[EventParser]{gatherInfo} We bought something from the market");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 9:
			Logger.printLog("[EventParser]{gatherInfo} We sold something to the market.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("TotalSale"));
			break;
		case 10:
			Logger.printLog("[EventParser]{gatherInfo} We completed a mission.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("Reward"));
			break;
		case 11:
			Logger.printLog("[EventParser]{gatherInfo} We bought a module for our ship.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("BuyPrice"));
			break;
		case 12:
			Logger.printLog("[EventParser]{gatherInfo} We retrieved a module.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 13:
			Logger.printLog("[EventParser]{gatherInfo} We sold a module.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("SellPrice"));
			break;
		case 14:
			Logger.printLog("[EventParser]{gatherInfo} We remotely sold a module.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("SellPrice"));
			break;
		case 15:
			Logger.printLog("[EventParser]{gatherInfo} We payed some fines.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Amount"));
			break;
		case 16:
			Logger.printLog("[EventParser]{gatherInfo} We payed some legacy fines.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Amount"));
			break;
		case 17:
			Logger.printLog("[EventParser]{gatherInfo} We got our salary from Powerplay.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("Amount"));
			break;
		case 18:
			Logger.printLog("[EventParser]{gatherInfo} We completely refuelled.");
			output[0] = "Fuel";
			output[1] = Double.toString(parser.getDouble("Amount"));
			output[2] = Integer.toString(parser.getInt("Cost"));
			break;
		case 19:
			Logger.printLog("[EventParser]{gatherInfo} We partially refuelled.");
			output[0] = "Fuel";
			output[1] = Double.toString(parser.getDouble("Amount"));
			output[2] = Integer.toString(parser.getInt("Cost"));
			break;
		case 20:
			Logger.printLog("[EventParser]{gatherInfo} We repaired something.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 21:
			Logger.printLog("[EventParser]{gatherInfo} We repaired everything.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 22:
			Logger.printLog("[EventParser]{gatherInfo} We restocked our vehicles.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 23:
			Logger.printLog("[EventParser]{gatherInfo} We are respawning because we died.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 24:
			Logger.printLog("[EventParser]{gatherInfo} We scanned something!");
			Logger.printLog("[EventParser]{gatherInfo} We need to gather alot of data about the scanned body.");
			String bodyType[] = determineBodyType();
			output[0] = "Scan";
			output[1] = bodyType[0]; // Classification
			output[2] = bodyType[1]; // Class type
			Logger.printLog("[EventParser]{gatherInfo} Put info gathered by determineBodyType into output variable index 1 and 2.");
			if(bodyType[1].equals("Stellar Body")) { //output[3] is the terraform state
				Logger.printLog("[EventParser]{gatherInfo} This is a stellar body, not a star! Getting terraform state.");
				output[3] = parser.getString("TerraformState");
			} else {
				Logger.printLog("[EventParser]{gatherInfo} This was a star, no need to gather terraform state.");
				output[3] = "null";
			}
			Logger.printLog("[EventParser]{gatherInfo} Now going to determine value of body.");
			output[4] = Integer.toString(getBodyValue(bodyType[0],output[3],bodyType[1])); // the worth of the planet
			Logger.printLog("[EventParser]{gatherInfo} Finally placing body name into output variable.");
			output[5] = parser.getString("BodyName");
			break;
		case 25:
			Logger.printLog("[EventParser]{gatherInfo} We sold some drones.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("TotalSale"));
			break;
		case 26:
			Logger.printLog("[EventParser]{gatherInfo} We sold our exploration data.");
			output[0] = "ExplorationSell";
			output[1] = Integer.toString(parser.getInt("BaseValue")+ parser.getInt("Bonus"));
			break;
		case 27:
			Logger.printLog("[EventParser]{gatherInfo} We bought something from the shipyard.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("ShipPrice"));
			break;
		case 28:
			Logger.printLog("[EventParser]{gatherInfo} We sold something to the shipyard.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("ShipPrice"));
			break;
		case 29:
			Logger.printLog("[EventParser]{gatherInfo} We scooped fuel.");
			output[0] = "FuelScoop";
			output[1] = Double.toString(parser.getDouble("Total"));
			break;
		case 30:
			Logger.printLog("[EventParser]{gatherInfo} Grabbing the location.");
			output[0] = "Location";
			output[1] = parser.getString("StarSystem");
			break;
		default:
			Logger.printLog("[EventParser]{gatherInfo} The Event was not in the list that we care about.");
			output[0] = "Wasn't in Event List :O";
			break;
		
		}
		return output;		
	}
	/**
	 * This function will determine the value of the scanned body.
	 * @param classification - String - Classification of the Body, regardless of if it is a star or planet
	 * @param terraformState - String - Terraform state of the Body
	 * @param type - String Type of Body (Star or Planet)
	 * @return Integer value of the scanned body
	 */
	public static int getBodyValue(String classification, String terraformState, String type) {
		int outputValue = 0;
		int fuelValues[] = 				{2916,2919,3012,2932,6135,2949,2903};
		int dwarfValues[] = 			{2889,2895,2881};
		int protoValues[] = 			{2000,2881};
		int specialValues[] = 			{54782,60589};
		int superGiantValues[] = 		{2949,2932,2903,2903,2916,2000,2000,2000,60589};
		//								MRB		HMC		RB		IB		RIB		EL		WW		AW		WG		WGL		GGW		GGA		GI		GII		GIII	GIV		GV		HRGG	HGG
		int[] bodyValueNonTerraform =	{65045	,34310	,928	,1246	,1240	,627885	,301410	,320203	,1824	,1824	,2314	,1721	,7013	,53663	,2693	,2799	,2761	,2095	,2095};
		int[] bodyValueTerraform = 		{65045	,412249	,181104	,1246	,1240	,627885	,694971	,320203	,1824	,1824	,2314	,1721	,7013	,53663	,2693	,2799	,2761	,2095	,2095};
		Logger.printLog("[EventParser]{getBodyValue} Now determining body value.");
		if(type.equals("Star")) { // The type of body is a Star according to some previous functions that determined the class
			Logger.printLog("[EventParser]{getBodyValue} The stellar body is a star!");
			for(int i = 0; i < Resources.fuelStars.length; i++) {
				if(classification.equals(Resources.fuelStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the fuelStars area.");
					outputValue = fuelValues[i];					
				}				
			}
			for(int i = 0; i < Resources.dwarfStars.length; i++) {
				if(classification.equals(Resources.dwarfStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the dwarfStars area.");
					outputValue = dwarfValues[i];					
				}				
			}
			for(int i = 0; i < Resources.protoStars.length; i++) {
				if(classification.equals(Resources.protoStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the protoStars area.");
					outputValue = protoValues[i];					
				}				
			}
			for(int i = 0; i < Resources.wolfRayetStars.length; i++) {
				if(classification.equals(Resources.wolfRayetStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the wolfRayetStars area.");
					outputValue = 2931;					
				}				
			}
			for(int i = 0; i < Resources.carbonStars.length; i++) {
				if(classification.equals(Resources.carbonStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the carbonStars area.");
					outputValue = 2930;					
				}				
			}
			for(int i = 0; i < Resources.miscStars.length; i++) {
				if(classification.equals(Resources.miscStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the miscStars area.");
					outputValue = 2000;					
				}				
			}
			for(int i = 0; i < Resources.whiteDwarfStars.length; i++) {
				if(classification.equals(Resources.whiteDwarfStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the whiteDwarfStars area.");
					outputValue = 34294;					
				}				
			}
			for(int i = 0; i < Resources.specialStars.length; i++) {
				if(classification.equals(Resources.specialStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the specialStars area.");
					outputValue = specialValues[i];					
				}				
			}
			for(int i = 0; i < Resources.superGiantStars.length; i++) {
				if(classification.equals(Resources.superGiantStars[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the superGiantStars area.");
					outputValue = superGiantValues[i];					
				}				
			}
			for(int i = 0; i < Resources.bodyClasses.length; i++) {
				if(classification.equals(Resources.bodyClasses[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the bodyClasses area.");
					Logger.printLog("[EventParser]{getBodyValue} This is a star so we are skipping terraform.");
					Logger.printLog("[EventParser]{getBodyValue} The terraform state is: null");
						outputValue = bodyValueNonTerraform[i];
				}
			}
		} else { // end Star IF Statement beginning of body area since there are no other options
			Logger.printLog("[EventParser]{getBodyValue} The stellar body was a planet/gas giant!");
			for(int i = 0; i < Resources.bodyClasses.length; i++) {
				if(classification.equals(Resources.bodyClasses[i])) {
					Logger.printLog("[EventParser]{getBodyValue} We found the type in the bodyClasses area.");
					Logger.printLog("[EventParser]{getBodyValue} Now we must determine the value depending on the terraform state.");	
					Logger.printLog("[EventParser]{getBodyValue} The terraform state is: " + terraformState);
					if(terraformState.equals("null") || terraformState.equals("")) {
						Logger.printLog("[EventParser]{getBodyValue} It is not terraformable.");
						outputValue = bodyValueNonTerraform[i];												
					} else {						
						Logger.printLog("[EventParser]{getBodyValue} It's terraformable.");
						outputValue = bodyValueTerraform[i];
					}
				}
			}			
		} // end IF statement system
		return outputValue;		
	} // end of body value function

}
