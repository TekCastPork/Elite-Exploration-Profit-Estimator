import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;

public class Loader {

	public static void loadConfig() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Config" + File.separator + "Configuration.cfg"));
		JSONObject loader = new JSONObject(lines.get(0));
		Logger.printLog("Now loading all saved data in the configuration file.");
		Resources.currentSystem = loader.getString("lastLocation");
		Resources.currentCredits = loader.getInt("currentCredits");
		Resources.systemCredits = loader.getInt("systemCredits");
		Resources.explorationCredits = loader.getInt("explorationCredits");
		Resources.currentFuel = loader.getDouble("fuelLevel");
	//	Display.chckbxEnableSounds.setSelected(loader.getBoolean("sounds"));
		Logger.printLog("Configuration file loaded.");
	}

}
