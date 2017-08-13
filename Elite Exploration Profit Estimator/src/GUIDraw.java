import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * This class will be used to take in information from the EventParser and properly display it, since every event uses the same output array, just with different values
 * @author TekCastPork
 *
 */
public class GUIDraw {	

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		        JOptionPane.showMessageDialog(null, "An unhandled crash has caused the GUIDraw class of this program to crash." + System.getProperty("line.separator")
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
	 * This function loads data from the EventParser into a variable within this class so if the EventParser's data changes we have a copy to continue using
	 * @param data
	 */
	public static void inputData(String[] data) {
		Logger.printLog("[GUIDraw]{inputData} We are inputting data for updating visual elements.");
		for(int i = 0; i < Resources.eventData.length; i++) {
			Resources.eventData[i] = data[i];
			Logger.printLog(Resources.eventData[i]);
		}
		Logger.printLog("[GUIDraw]{inputData} Data Loaded.");
		
	}
	/**
	 * This function will look at the EventParser data and figure out what event happened and place data accordingly to the variables used to draw information on the screen
	 */
	public static void updateScreen() {
//		String soundLocations = System.getProperty("user.dir") + File.separator + "Sounds" + File.separator;
		Logger.printLog("[GUIDraw]{updateScreen} We are now going to change variables for the screen based on the event.");
		if(Resources.eventData[0].equals("null") || Resources.eventData[0].equals("") || Resources.eventData[0].equals(null)) {
			Logger.printLog("[GUIDraw]{updateScreen} EventData[0], which contains the event type information for the GUIDraw, was empty! Big NO NO! Not updating screen to prevent crash.");
		} else {
			switch (Resources.eventData[0]) {
			case "CashLoss":
				Logger.printLog("[GUIDraw]{updateScreen} CashLoss event.");
				Resources.currentCredits -= Integer.parseInt(Resources.eventData[1]);
				break;
			case "Jump":
				Logger.printLog("[GUIDraw]{updateScreen} Jump event.");
				Resources.explorationCredits += Resources.systemCredits;
				Resources.systemCredits = 0;
				Resources.currentSystem = Resources.eventData[1];
				Resources.currentFuel = Double.parseDouble(Resources.eventData[2]);				
				if(Resources.currentFuel <= 10 && Resources.currentFuel > 5) {
//					try {
//						if(Display.chckbxEnableSounds.isSelected()) {
//							SoundAlerter.playSound(soundLocations + "lowFuel.wav");
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					Display.fuelLevelBar.setForeground(Color.YELLOW);					
				} else if(Resources.currentFuel <= 5 && Resources.currentFuel > 2) {
//					try {
//						if(Display.chckbxEnableSounds.isSelected()) {
//							SoundAlerter.playSound(soundLocations + "extremeFuel.wav");
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					Display.fuelLevelBar.setForeground(Color.RED);					
				} else if (Resources.currentFuel <= 2) {
//					try {
//						if(Display.chckbxEnableSounds.isSelected()) {
//							SoundAlerter.playSound(soundLocations + "criticalFuel.wav");
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					Display.fuelLevelBar.setForeground(Color.DARK_GRAY);			
				} else {				
					Display.fuelLevelBar.setForeground(Color.GREEN);
				}
				Display.lblSuperCharge.setEnabled(false);
				Display.lblSuperCharge.setVisible(false);
				Resources.hasBoosted = false;
				for(int i = 0; i < Resources.scannedBodyDetails.length; i++  ) {
					Resources.scannedBodyDetails[i] = "";
				}
				break;
			case "Boost":
				Logger.printLog("[GUIDraw]{updateScreen} Boost event.");
				Display.lblSuperCharge.setEnabled(true);
				Display.lblSuperCharge.setVisible(true);
				Resources.hasBoosted = true;	
				Display.fuelLevelBar.setForeground(Color.CYAN);
				break;
			case "Load":
				Logger.printLog("[GUIDraw]{updateScreen} Load event.");
				Resources.currentCredits = Integer.parseInt(Resources.eventData[1]);
				Resources.currentFuel = Double.parseDouble(Resources.eventData[2]);
				Resources.fuelCap = Double.parseDouble(Resources.eventData[3]);
				Logger.printLog("Now testing fuel levels for initial label visibility...");
				Display.fuelLabel.setText((int)Resources.currentFuel+"/"+(int)Resources.fuelCap);
				break;
			case "CashGain":
				Logger.printLog("[GUIDraw]{updateScreen} CashGain event.");
				Resources.currentCredits += Integer.parseInt(Resources.eventData[1]);
				break;
			case "Fuel":
				Logger.printLog("[GUIDraw]{updateScreen} Refuel event.");
				Resources.currentFuel += Double.parseDouble(Resources.eventData[1]);
				Resources.currentCredits -= Integer.parseInt(Resources.eventData[2]);
				break;
			case "Scan":
				Logger.printLog("[GUIDraw]{updateScreen} Scan event! We care about this one alot!");
				Resources.scannedBodyDetails[0] = Resources.eventData[5]; // Name
				Resources.scannedBodyDetails[1] = Resources.eventData[1]; // Class
				Resources.scannedBodyDetails[2] = Resources.eventData[2]; // Type
				Resources.scannedBodyDetails[3] = Resources.eventData[3]; // Terraform
				Resources.scannedBodyDetails[4] = Resources.eventData[4]; // Value
				Resources.systemCredits += Integer.parseInt(Resources.eventData[4]);
				break;
			case "FuelScoop":
				Logger.printLog("[GUIDraw]{updateScreen} FuelScoop event.");
				Resources.currentFuel = Double.parseDouble(Resources.eventData[1]);
				break;
			case "Location":
				Logger.printLog("[GUIDraw]{updateScreen} Location event.");
				Resources.currentSystem = Resources.eventData[1];
				break;
			default:
				Logger.printLog("[GUIDraw]{updateScreen} Could not determine event type, not updating screen data");
				break;
			}
			Logger.printLog("[GUIDraw]{updateScreen} Now updating display labels.");
			Display.lblCurrentSystem.setText("Current System: " + Resources.currentSystem);
			Display.lblCurrentCredits.setText("Current Credits: " + Resources.currentCredits + "cr");
			Display.lblSystemCredits.setText("System Credits: " + Resources.systemCredits + "cr");
			Display.lblExplorationCredits.setText("Exploration Credits: " + Resources.explorationCredits + "cr");
			Display.lblEstimatedTotal.setText("Estimated Total: " + (Resources.currentCredits+Resources.explorationCredits) + "cr");
			Display.lblStellarName.setText("Stellar Body Name: " + Resources.scannedBodyDetails[0]);
			Display.lblClassification.setText("Classification: " + Resources.scannedBodyDetails[1]);
			Display.lblStellarBodyType.setText("Stellar Body Type: " + Resources.scannedBodyDetails[2]);
			Display.lblTerraformStatus.setText("Terraform Status: " + Resources.scannedBodyDetails[3]);
			Display.lblStellarBodyValue.setText("Stellar Body Value: " + Resources.scannedBodyDetails[4]);
			Display.fuelLabel.setText((int)Resources.currentFuel+"/"+(int)Resources.fuelCap);
			Display.fuelLevelBar.setValue((int)Resources.currentFuel);		
		}
	}
	
}
