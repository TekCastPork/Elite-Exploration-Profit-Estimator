import org.json.*;

/**
 * This class parses the events from the game and outputs data in an array for later displaying
 * @author TekCastPork
 *
 */
public class EventParser {
	static String bodyClasses[] = {"Metal rich body","High metal content body","Rocky body","Icy body","Rocky ice body","Earthlike body","Water world","Ammonia world","Water giant","Water giant with life",
							"Gas giant with water based life","Gas giant with ammonia based life","Sudarsky class i gas giant","Sudarsky class ii gas giant","Sudarsky class iii gas giant",
							"Sudarsky class iv gas giant","Sudarsky class v gas giant","Helium rich gas giant","Helium gas giant"};
	static String fuelStars[] = {"K","G","B","F","O","A","M"};
	static String dwarfStars[] = {"L","T","Y"};
	static String protoStars[] = {"AeBe","TTS"};
	static String wolfRayetStars[] = {"W","WN","WNC","WC","WO"};
	static String carbonStars[] = {"CS","C","CN","CJ","CHd"};
	static String miscStars[] = {"MS","S","X"};
	static String whiteDwarfStars[] = {"D","DA","DAB","DAO","DAZ","DAV","DB","DBZ","DBV","DO","DOV","DQ","DC","DCV","DX"};
	static String specialStars[] = {"N","H"};
	static String superGiantStars[] = {"A_BlueWhiteSuperGiant","F_WhiteSuperGiant","M_RedSuperGiant","M_RedGiant","K_OrangeGiant","RoguePlanet","Nebula","StellarRemnantNebula","SuperMassiveBlackHole"};
	static String[] events = {"Continued","BuyAmmo","BuyDrones","BuyExplorationData","BuyTradeData","FSDJump","JetConeBoost","LoadGame","MarketBuy","MarketSell","MissionCompleted","ModuleBuy","ModuleRetrieve","ModuleSell","ModuleSellRemote","PayFines","PayLegacyFines","PowerplaySalary","RefuelAll","RefuelPartial","Repair","RepairAll","RestockVehicle","Resurrect","Scan","SellDrones","SellExplorationData","ShipyardBuy","ShipyardSell","FuelScoop","Location"};
	static JSONObject parser;
	/**
	 * Quick function to shorten debug function calls
	 * @param msg - String
	 */
	public static void println(String msg) {
		System.out.println(msg);;
	}

	public static void main(String[] args) {

	}
	
	/**
	 * This function takes in a string containing JSON for later parsing
	 * Returns true if it succeeds or false if it fails
	 * @param data
	 * @return
	 */
	public static boolean inputData(String data) {
		try {			
			parser = new JSONObject(data);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	/**
	 * This function returns a integer that references the event type
	 * @return
	 */
	public static int determineEvent() {
		 String event = parser.getString("event");
		 int returnValue = -1;
		 for(int i = 0; i < events.length; i++) {
			 if(event.equals(events[i])) {
				 println("We found a matching event.");
				 println("Event is:  " + events[i] + "  ("+i+")");
				 returnValue = i;
			 }
		 }
		 return returnValue;
	}
	
	/**
	 * This function parses some initial JSON to determine the StellarBody's class and type
	 * @return String[] - String Array containing StellarBody's class and type
	 */
	public static String[] determineBodyType() {
		println("Determining Stellar Body's Type (Star/Planet");
		println("Trying a Stellar body first");
		String planetType[] = {"","StellarBody"};
		try {
   		 planetType[0] =  parser.getString("PlanetClass"); // run a function that figures out the planet/star's type	   		 
   	 	} catch (Exception e) {
   	 	 planetType[1] = "Star";
   		 e.printStackTrace();
   		 println("PlanetClass doesnt exist....");    	
   		 println("Now trying for StarType");
   	 	}			
		try {
   		 planetType[0] =  parser.getString("StarType"); // run a function that figures out the planet/star's type	
   	 	} catch (Exception e) {
   	     planetType[1] = "Unknown";
   		 e.printStackTrace();
   		 println("StarType doesnt exist....");    		 
   	 	}	
		return planetType;
	}
	/**
	 * This function runs different parsing functions based on the event index, since we don't need to parse the terraform state of a star.
	 * @param type - index of the event
	 */
	public static String[] gatherInfo(int type) {
		String output[] = new String[6];
		switch(type) {
		case 0: // Continued
			println("Continued Event! The file has been closed and a new one is generated, we must re-run the file grabbing function!");
			println("Otherwise we won't get any new JSON to parse.");
			break;
		case 1:
//			println("We bought ammo, lets get the price and modify some variables later on.");
		case 2:
//			println("We bought drones, lets get the price and modify some variables later on.");
		case 3:
//			println("We bought Exploration Data, lets get the price and modify some variables later on.");
		case 4:
//			println("We bought Trade Data, lets get the price and modify some variables later on.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 5:
//			println("We jumped somewhere!");
			output[0] = "Jump";
			output[1] = parser.getString("StarSystem");
			output[2] = Double.toString(parser.getDouble("FuelLevel"));
			break;
		case 6:
//			println("Vegeta, what does the scanner say about his boost level?");
//			println("It's OVER 4!");
			output[0] = "Boost";
			output[1] = Double.toString(parser.getDouble("BoostValue"));
			break;
		case 7:
//			println("The game is loading.");
			output[0] = "Load";
			output[1] = Integer.toString(parser.getInt("Credits"));
			break;
		case 8:
//			println("We bought something from the market");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 9:
//			println("We sold something to the market.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("TotalSale"));
			break;
		case 10:
//			println("We completed a mission.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("Reward"));
			break;
		case 11:
//			println("We bought a module for our ship.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("BuyPrice"));
			break;
		case 12:
//			println("We retrieved a module.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 13:
//			println("We sold a module.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("SellPrice"));
			break;
		case 14:
//			println("We remotely sold a module.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("SellPrice"));
			break;
		case 15:
//			println("We payed some fines.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Amount"));
			break;
		case 16:
//			println("We payed some legacy fines.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Amount"));
			break;
		case 17:
//			println("We got our salary from Powerplay.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("Amount"));
			break;
		case 18:
//			println("We completely refuelled.");
			output[0] = "Fuel";
			output[1] = Double.toString(parser.getDouble("Amount"));
			output[2] = Integer.toString(parser.getInt("Cost"));
			break;
		case 19:
//			println("We partially refuelled.");
			output[0] = "Fuel";
			output[1] = Double.toString(parser.getDouble("Amount"));
			output[2] = Integer.toString(parser.getInt("Cost"));
			break;
		case 20:
//			println("We repaired something.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 21:
//			println("We repaired everything.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 22:
//			println("We restocked our vehicles.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 23:
//			println("We are respawning because we died.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("Cost"));
			break;
		case 24:
			println("We scanned something!");
			println("we need to gather alot of data about the scanned body.");
			String bodyType[] = determineBodyType();
			output[0] = "Scan";
			output[1] = bodyType[0]; // Classification
			output[2] = bodyType[1]; // Class type
			if(bodyType[1].equals("StellarBody")) { //output[3] is the terraform state
				output[3] = parser.getString("TerraformState");
			} else {
				output[3] = "null";
			}
			output[4] = Integer.toString(getBodyValue(bodyType[0],output[3],bodyType[1])); // the worth of the planet
			output[5] = parser.getString("BodyName");
			break;
		case 25:
//			println("We sold some drones.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("TotalSale"));
			break;
		case 26:
//			println("We sold our exploration data.");
			output[0] = "ExplorationSell";
			output[1] = Integer.toString(parser.getInt("BaseValue")+ parser.getInt("Bonus"));
			break;
		case 27:
//			println("We bought something from the shipyard.");
			output[0] = "CashLoss";
			output[1] = Integer.toString(parser.getInt("ShipPrice"));
			break;
		case 28:
//			println("We sold something to the shipyard.");
			output[0] = "CashGain";
			output[1] = Integer.toString(parser.getInt("ShipPrice"));
			break;
		case 29:
//			println("We scooped fuel.");
			output[0] = "FuelScoop";
			output[1] = Double.toString(parser.getDouble("Total"));
			break;
		case 30:
//			println("Grabbing the location.");
			output[0] = "Location";
			output[1] = parser.getString("StarSystem");
		default:
//			println("The Event was not in the list that we care about.");
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
		int[] bodyValueNonTerraform =	{65045	,34310	,928	,1246	,1240	,627885	,301410	,320203	,1824	,1824	,2314	,1721	,7013	,53663	,2693	,2799	,2761	,2095	,2095};
		int[] bodyValueTerraform = 		{65045	,412249	,181104	,1246	,1240	,627885	,694971	,320203	,1824	,1824	,2314	,1721	,7013	,53663	,2693	,2799	,2761	,2095	,2095};
		if(type.equals("Star")) { // The type of body is a Star according to some previous functions that determined the class
			for(int i = 0; i < fuelStars.length; i++) {
				if(classification.equals(fuelStars[i])) {
					println("We found the type in the fuelStars area.");
					outputValue = fuelValues[i];					
				}				
			}
			for(int i = 0; i < dwarfStars.length; i++) {
				if(classification.equals(dwarfStars[i])) {
					println("We found the type in the dwarfStars area.");
					outputValue = dwarfValues[i];					
				}				
			}
			for(int i = 0; i < protoStars.length; i++) {
				if(classification.equals(protoStars[i])) {
					println("We found the type in the protoStars area.");
					outputValue = protoValues[i];					
				}				
			}
			for(int i = 0; i < wolfRayetStars.length; i++) {
				if(classification.equals(wolfRayetStars[i])) {
					println("We found the type in the wolfRayetStars area.");
					outputValue = 2931;					
				}				
			}
			for(int i = 0; i < carbonStars.length; i++) {
				if(classification.equals(carbonStars[i])) {
					println("We found the type in the carbonStars area.");
					outputValue = 2930;					
				}				
			}
			for(int i = 0; i < miscStars.length; i++) {
				if(classification.equals(miscStars[i])) {
					println("We found the type in the miscStars area.");
					outputValue = 2000;					
				}				
			}
			for(int i = 0; i < whiteDwarfStars.length; i++) {
				if(classification.equals(whiteDwarfStars[i])) {
					println("We found the type in the whiteDwarfStars area.");
					outputValue = 34294;					
				}				
			}
			for(int i = 0; i < specialStars.length; i++) {
				if(classification.equals(specialStars[i])) {
					println("We found the type in the specialStars area.");
					outputValue = specialValues[i];					
				}				
			}
			for(int i = 0; i < superGiantStars.length; i++) {
				if(classification.equals(superGiantStars[i])) {
					println("We found the type in the superGiantStars area.");
					outputValue = superGiantValues[i];					
				}				
			}			
		} else { // end Star IF Statement beginning of body area since there are no other options
			for(int i = 0; i < bodyClasses.length; i++) {
				if(classification.equals(bodyClasses[i])) {
					println("We found the type in the bodyClasses area.");
					println("Now we must determine the value depending on the terraform state.");	
					if(!terraformState.equals("null") || !terraformState.equals("")) {
						println("It's terraformable.");
						outputValue = bodyValueTerraform[i];						
					} else {
						println("It is not terraformable.");
						outputValue = bodyValueNonTerraform[i];
					}
				}
			}			
		} // end IF statement system
		return outputValue;		
	} // end of body value function

}
