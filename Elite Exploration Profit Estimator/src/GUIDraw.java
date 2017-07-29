
/**
 * This class will be used to take in information from the EventParser and properly display it, since every event uses the same output array, just with different values
 * @author TekCastPork
 *
 */
public class GUIDraw {
	public static String currentSystem = "";
	public static double fuelCap = 32;
	public static double currentFuel = 32;
	public static int currentCredits = 0;
	public static int explorationCredits = 0;
	public static int systemCredits = 0;
	public static String eventData[] = new String[6];
	public static String scannedBodyDetails[] = {"","","","","0"}; // Name,Class,Type,Terraformable?,Value
	public static boolean hasBoosted = false;
	
	public static void println(String msg) {
		System.out.println(msg);;
	}

	public static void main(String[] args) {
		
	}
	/**
	 * This function loads data from the EventParser into a variable within this class so if the EventParser's data changes we have a copy to continue using
	 * @param data
	 */
	public static void inputData(String[] data) {
		for(int i = 0; i < eventData.length; i++) {
			eventData[i] = data[i];
		}
		
	}
	/**
	 * This function will look at the EventParser data and figure out what event happened and place data accordingly to the variables used to draw information on the screen
	 */
	public static void updateScreen() {
		switch (eventData[0]) {
		case "CashLoss":
			println("CashLoss event.");
			currentCredits -= Integer.parseInt(eventData[1]);
			break;
		case "Jump":
			println("Jump event.");
			explorationCredits += systemCredits;
			systemCredits = 0;
			currentSystem = eventData[1];
			currentFuel = Double.parseDouble(eventData[2]);
			hasBoosted = false;
			break;
		case "Boost":
			println("Boost event.");
			hasBoosted = true;			
			break;
		case "Load":
			println("Load event.");
			currentCredits = Integer.parseInt(eventData[1]);
			break;
		case "CashGain":
			println("CashGain event.");
			currentCredits += Integer.parseInt(eventData[1]);
			break;
		case "Fuel":
			println("Refuel event.");
			currentFuel += Double.parseDouble(eventData[1]);
			currentCredits -= Integer.parseInt(eventData[2]);
			break;
		case "Scan":
			println("Scan event! We care about this one alot!");
			scannedBodyDetails[0] = eventData[5]; // Name
			scannedBodyDetails[1] = eventData[1]; // Class
			scannedBodyDetails[2] = eventData[2]; // Type
			scannedBodyDetails[3] = eventData[3]; // Terraform
			scannedBodyDetails[4] = eventData[4]; // Value
			systemCredits += Integer.parseInt(eventData[4]);
			break;
		case "FuelScoop":
			println("FuelScoop event.");
			currentFuel = Double.parseDouble(eventData[1]);
			break;
		case "Location":
			println("Location event.");
			currentSystem = eventData[1];
			break;
		default:
			println("Could not determine event type, not updating screen data");
			break;
		}
		Display.lblCurrentSystem.setText("Current System: " + currentSystem);
		Display.lblCurrentCredits.setText("Current Credits: " + String.format("%d", currentCredits) + "cr");
		Display.lblSystemCredits.setText("System Credits: " + String.format("%d", systemCredits) + "cr");
		Display.lblExplorationCredits.setText("Exploration Credits: " + String.format("%d", explorationCredits) + "cr");
		Display.lblEstimatedTotal.setText("Estimated Total: " + String.format("%d", (currentCredits+explorationCredits)) + "cr");
		Display.lblStellarName.setText("Stellar Body Name: " + scannedBodyDetails[0]);
		Display.lblClassification.setText("Classification: " + scannedBodyDetails[1]);
		Display.lblStellarBodyType.setText("Stellar Body Type: " + scannedBodyDetails[2]);
		Display.lblTerraformStatus.setText("Terraform Status: " + scannedBodyDetails[3]);
		Display.lblStellarBodyValue.setText("Stellar Body Value: " + String.format("%d", scannedBodyDetails[4]));
		Display.fuelLabel.setText((int)currentFuel+"/"+(int)fuelCap);
		Display.fuelLevelBar.setValue((int)currentFuel);		
	}
	
}
