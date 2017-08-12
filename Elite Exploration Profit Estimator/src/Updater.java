import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class will look at the github releases page to see if there is a new version available.
 * @author TekCastPork
 *
 */
public class Updater {
	static String inputLine;	
	static String intake;
	static String versionInfo;
	public static void checkUpdates() {
		System.out.println("Checking for a newer version by accessing github... Standby....");			
		URL uri = null;
		try {
			uri = new URL("https://api.github.com/repos/TekCastPork/Elite-Exploration-Profit-Estimator/releases");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        BufferedReader in = null;
		try {
			in = new BufferedReader(
			new InputStreamReader(uri.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

        
        try {
			while ((inputLine = in.readLine()) != null) {
			    System.out.println("Line: " + inputLine);
			    intake = inputLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
        	System.out.println("Before close input " + inputLine);
        	System.out.println("Before close intake " + intake);
			in.close();
			System.out.println("After close input " + inputLine);
			System.out.println("After close intake " + intake);
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("Attempting to put " + inputLine + " into a JSONArray variable for parsing.");
        JSONArray parser = new JSONArray(intake);       
        System.out.println("Done.");
        try {
        	System.out.println("Now grabbing index 0.");
        	JSONObject arrayParse = parser.getJSONObject(0);
        	System.out.println("Grabbed. Gathering version info via tag name.......");
        	System.out.println("Grabbed: " + arrayParse.toString());
        	versionInfo = arrayParse.getString("tag_name");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        if(versionInfo.equals(Resources.versionInfo)) {
        	System.out.println("Version info matches, no new update.");
        } else {
        	System.out.println("LETS YELL AT THE USER, THEY ARE USING A OLD VERSION! HAHA! HAHA!");
        	JOptionPane.showMessageDialog(null, "A new version is available!" + System.getProperty("line.separator") + 
        										"You have version: " + Resources.versionInfo + System.getProperty("line.separator") + 
        										"Newest version is: " + versionInfo, "Welcome!", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("done.");
		
		
	}

}
