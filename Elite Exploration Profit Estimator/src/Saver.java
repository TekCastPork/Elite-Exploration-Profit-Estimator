import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Saver {

	public static void main(String[] args) {

	}
	
	public static void saveData() throws FileNotFoundException {
		Logger.printLog("Saver function called! Lets save stuff.");
		PrintWriter saver = new PrintWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Config" + File.separator + "Configuration.cfg");
		saver.print("{  \"lastLocation\": \""+Resources.currentSystem +
					  "\",\"currentCredits\": "+Resources.currentCredits +
					  ",\"systemCredits\": "+Resources.systemCredits + 
					  ",\"explorationCredits\": "+Resources.explorationCredits +
					  ",\"totalCredits\": "+(Resources.explorationCredits+Resources.currentCredits) + 
					  ",\"fuelLevel\": "+Resources.currentFuel + "}");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		saver.flush();
		saver.close();
		Logger.printLog("{  \"lastLocation\": \""+Resources.currentSystem +
				  "\",\"currentCredits\": "+Resources.currentCredits +
				  ",\"systemCredits\": "+Resources.systemCredits + 
				  ",\"explorationCredits\": "+Resources.explorationCredits +
				  ",\"totalCredits\": "+(Resources.explorationCredits+Resources.currentCredits) + 
				  ",\"fuelLevel\": "+Resources.currentFuel + "}   has been saved to the config file.");
		
	}

}
