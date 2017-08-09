import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 * This class will make a log of the program to help with crash reports/general errors
 * @author TekCastPork
 * @Requires init() , closing functions to be called before system exit.
 *
 */
public class Logger {
	static PrintWriter logger;
	

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread t, Throwable e) {
		        JOptionPane.showMessageDialog(null, "An unhandled crash has caused the Event Logging class of this program to crash." + System.getProperty("line.separator")
		        + "The program will probably appear to keep running, but in reality it is not." + System.getProperty("line.separator")
		        + "Please submit any crash reports in the CrashLog folder (only the most recent ones please) as well as your log file.", "ALERT!", JOptionPane.ERROR_MESSAGE);
		        String filename = System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "CrashLogs" + File.separator + "Unhandled Crash"+Resources.sdf.format(Resources.cal.getTime())+".log";
		        
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
	
	public static void init() {
		try {
			logger = new PrintWriter(System.getProperty("user.home") + File.separator + "Elite Exploration Estimator" + File.separator + "Logs" + File.separator + "Log Files"+Resources.sdf.format(Resources.cal.getTime())+".log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This function logs a message to the log file
	 * @param msg
	 */
	public static void printLog(String msg) {
		System.out.println(msg);
		logger.println(msg);
		logger.flush();		
	}

}
