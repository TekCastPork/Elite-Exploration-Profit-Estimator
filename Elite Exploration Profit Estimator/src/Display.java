import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.BevelBorder;

public class Display {

	public JFrame frmEliteDangerousExploration;
	public static JProgressBar fuelLevelBar = new JProgressBar();;
	public static JLabel fuelLabel = new JLabel("Fuel");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
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
	private void initialize() {
		frmEliteDangerousExploration = new JFrame();
		frmEliteDangerousExploration.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmEliteDangerousExploration.setBackground(new Color(0, 0, 102));
		frmEliteDangerousExploration.setTitle("Elite: Dangerous Exploration Data Profit Estimator");
		frmEliteDangerousExploration.setBounds(100, 100, 606, 700);
		frmEliteDangerousExploration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEliteDangerousExploration.getContentPane().setLayout(null);
		
		JLabel lblTekcastporksEliteDangerous = new JLabel("TekCastPork's Elite: Dangerous Exploration Data Profit Estimator V A0.0005");
		lblTekcastporksEliteDangerous.setFont(new Font("Roboto", Font.BOLD | Font.ITALIC, 17));
		lblTekcastporksEliteDangerous.setBounds(6, 6, 573, 16);
		frmEliteDangerousExploration.getContentPane().add(lblTekcastporksEliteDangerous);
		
		fuelLevelBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		fuelLevelBar.setMinimumSize(new Dimension(10, 20));
		fuelLevelBar.setMaximumSize(new Dimension(32767, 20));
		fuelLevelBar.setPreferredSize(new Dimension(150, 20));
		fuelLevelBar.setName("Fuel Level");
		fuelLevelBar.setOrientation(SwingConstants.VERTICAL);
		fuelLevelBar.setValue((int) GUIDraw.currentFuel);
		fuelLevelBar.setBounds(16, 388, 47, 267);
		fuelLevelBar.setMinimum(0);
		fuelLevelBar.setMaximum((int) GUIDraw.fuelCap);
		UIManager.put("ProgressBar.foreground", Color.RED);  //colour of progress bar
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
	}
}
