import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.BevelBorder;

public class Display {

	public JFrame frmEliteDangerousExploration;
	public static JProgressBar fuelLevelBar = new JProgressBar();;
	public static JLabel fuelLabel = new JLabel("Fuel");
	public static JLabel lblSuperCharge = new JLabel("Your Frame Shift Drive is supercharged.");
	public static JLabel lblFuelLevelsCritical;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Display window = new Display();
					window.frmEliteDangerousExploration.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		        JOptionPane.showMessageDialog(null, "An unhandled crash has caused the Display class of this program to crash." + System.getProperty("line.separator")
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
	 * Create the application.
	 */
	public Display() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static JLabel lblCurrentSystem = new JLabel("Current System: ");
	public static JLabel lblCurrentCredits = new JLabel("Current Credits: ");
	public static JLabel lblSystemCredits = new JLabel("System Credits: ");
	public static JLabel lblEstimatedTotal = new JLabel("Estimated Total: ");
	public static JLabel lblExplorationCredits = new JLabel("Exploration Credits: ");
	public static JLabel lblBodyDetails = new JLabel("Scanned Body Details:");
	public static JLabel lblStellarName = new JLabel("Stellar Body Name: ");
	public static JLabel lblClassification = new JLabel("Classification: ");
	public static JLabel lblStellarBodyType = new JLabel("Stellar Body Type: ");
	public static JLabel lblTerraformStatus = new JLabel("Terraform Status: ");
	public static JLabel lblStellarBodyValue = new JLabel("Stellar Body Value: ");
	private final JLabel lblVersioninfo = new JLabel("VersionInfo");
	private void initialize() {
		frmEliteDangerousExploration = new JFrame();
		frmEliteDangerousExploration.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmEliteDangerousExploration.setBackground(new Color(0, 0, 102));
		frmEliteDangerousExploration.setTitle("Elite: Dangerous Exploration Data Profit Estimator");
		frmEliteDangerousExploration.setBounds(100, 100, 570, 700);
		frmEliteDangerousExploration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEliteDangerousExploration.getContentPane().setLayout(null);
		
		JLabel lblTekcastporksEliteDangerous = new JLabel("TekCastPork's Elite: Dangerous Exploration Data Profit Estimator");
		lblTekcastporksEliteDangerous.setFont(new Font("Roboto", Font.BOLD | Font.ITALIC, 17));
		lblTekcastporksEliteDangerous.setBounds(6, 6, 573, 16);
		frmEliteDangerousExploration.getContentPane().add(lblTekcastporksEliteDangerous);
		fuelLevelBar.setForeground(Color.GREEN);
		
		fuelLevelBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		fuelLevelBar.setMinimumSize(new Dimension(10, 20));
		fuelLevelBar.setMaximumSize(new Dimension(32767, 20));
		fuelLevelBar.setPreferredSize(new Dimension(150, 20));
		fuelLevelBar.setName("Fuel Level");
		fuelLevelBar.setOrientation(SwingConstants.VERTICAL);
		fuelLevelBar.setValue((int) Resources.currentFuel);
		fuelLevelBar.setBounds(16, 388, 47, 267);
		fuelLevelBar.setMinimum(0);
		fuelLevelBar.setMaximum((int) Resources.fuelCap);
		frmEliteDangerousExploration.getContentPane().add(fuelLevelBar);
		
		JLabel lblFuelLevels = new JLabel("Fuel Levels");
		lblFuelLevels.setHorizontalAlignment(SwingConstants.CENTER);
		lblFuelLevels.setBounds(6, 360, 69, 16);
		frmEliteDangerousExploration.getContentPane().add(lblFuelLevels);
		
		fuelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fuelLabel.setBounds(6, 373, 69, 16);
		frmEliteDangerousExploration.getContentPane().add(fuelLabel);
		
		
		lblCurrentSystem.setHorizontalAlignment(SwingConstants.LEFT);
		lblCurrentSystem.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblCurrentSystem.setBounds(6, 34, 453, 16);
		frmEliteDangerousExploration.getContentPane().add(lblCurrentSystem);
		
		
		lblCurrentCredits.setHorizontalAlignment(SwingConstants.LEFT);
		lblCurrentCredits.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblCurrentCredits.setBounds(6, 73, 453, 16);
		frmEliteDangerousExploration.getContentPane().add(lblCurrentCredits);
		
		
		lblSystemCredits.setHorizontalAlignment(SwingConstants.LEFT);
		lblSystemCredits.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblSystemCredits.setBounds(6, 101, 453, 16);
		frmEliteDangerousExploration.getContentPane().add(lblSystemCredits);
		
		
		lblExplorationCredits.setHorizontalAlignment(SwingConstants.LEFT);
		lblExplorationCredits.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblExplorationCredits.setBounds(6, 129, 453, 16);
		frmEliteDangerousExploration.getContentPane().add(lblExplorationCredits);
		
		
		lblEstimatedTotal.setHorizontalAlignment(SwingConstants.LEFT);
		lblEstimatedTotal.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblEstimatedTotal.setBounds(6, 157, 453, 16);
		frmEliteDangerousExploration.getContentPane().add(lblEstimatedTotal);
		
		
		lblBodyDetails.setFont(new Font("Roboto", Font.BOLD | Font.ITALIC, 20));
		lblBodyDetails.setBounds(75, 388, 214, 36);
		frmEliteDangerousExploration.getContentPane().add(lblBodyDetails);
		
		
		lblStellarName.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblStellarName.setBounds(75, 436, 441, 16);
		frmEliteDangerousExploration.getContentPane().add(lblStellarName);
		
		
		lblClassification.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblClassification.setBounds(75, 492, 441, 16);
		frmEliteDangerousExploration.getContentPane().add(lblClassification);
		
		
		lblStellarBodyType.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblStellarBodyType.setBounds(75, 464, 441, 16);
		frmEliteDangerousExploration.getContentPane().add(lblStellarBodyType);
		
		
		lblTerraformStatus.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblTerraformStatus.setBounds(75, 520, 441, 16);
		frmEliteDangerousExploration.getContentPane().add(lblTerraformStatus);
		
		
		lblStellarBodyValue.setFont(new Font("Roboto", Font.PLAIN, 16));
		lblStellarBodyValue.setBounds(75, 548, 441, 16);
		frmEliteDangerousExploration.getContentPane().add(lblStellarBodyValue);
		
		
		lblSuperCharge.setFont(new Font("Roboto", Font.BOLD | Font.ITALIC, 20));
		lblSuperCharge.setBounds(82, 360, 360, 24);
		frmEliteDangerousExploration.getContentPane().add(lblSuperCharge);
		lblVersioninfo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersioninfo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblVersioninfo.setBounds(438, 639, 104, 16);
		lblVersioninfo.setText(Resources.versionInfo);
		
		lblFuelLevelsCritical = new JLabel("ALERT: FUEL LEVELS CRITICAL!!");
		lblFuelLevelsCritical.setHorizontalAlignment(SwingConstants.CENTER);
		lblFuelLevelsCritical.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 30));
		lblFuelLevelsCritical.setBounds(10, 238, 534, 67);
		frmEliteDangerousExploration.getContentPane().add(lblFuelLevelsCritical);
		
		frmEliteDangerousExploration.getContentPane().add(lblVersioninfo);
		frmEliteDangerousExploration.addWindowListener( new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        JFrame frame = (JFrame)e.getSource();
		 
	        	Logger.printLog("The exit button was pushed, since that is the only time I'm called, closing log file.");
	        	Logger.printLog("Saving credit info and stuff so we can preload stuff next time.");
	        	try {
					Saver.saveData();
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
	    		Logger.printLog("-----------------------------------------------------");
	    		Logger.printLog("Program Terminated with Exit button.");
	    		Logger.logger.flush();
	    		Logger.logger.close();
	        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        	try {
//					PrintWriter saveFunction = new PrintWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLogs" + File.separator + "Unhandled Crash"+sdf.format(cal.getTime())+".log");
//				} catch (FileNotFoundException e2) {
//					e2.printStackTrace();
//				}
	        	
	        	try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        	
	        	System.exit(0);
	        	
	        }
		});
	}
}
