import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Resources {
	public static String versionInfo = "A 0.0.7"; // Update this with each new version
	static String filename = "";
	static String filepath = "";
	static String inputLine = "";
	static String previousLine = "";
	static String configLines[] = new String[100];
	static int lineCount;
	static int currentLine = 0;
	static File l;
	static Calendar cal = Calendar.getInstance();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
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
	
	public static String currentSystem = "";
	public static double fuelCap = 32;
	public static double currentFuel = 32;
	public static int currentCredits = 0;
	public static int explorationCredits = 0;
	public static int systemCredits = 0;
	public static String eventData[] = new String[6];
	public static String scannedBodyDetails[] = {"","","","","0"}; // Name,Class,Type,Terraformable?,Value
	public static boolean hasBoosted = false;
	public static List<String> lines;
	public static String fileLocation = null;
	
}
