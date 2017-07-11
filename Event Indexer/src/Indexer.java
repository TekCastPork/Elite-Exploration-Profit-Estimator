import processing.core.*;
import javax.swing.JOptionPane;

public class Indexer extends PApplet {

	public static void main(String[] args) {
		PApplet.main("Indexer");
	}
	
	public void settings() {
		
	}
	
	public void setup() {
		println("This program will generate a index table system for the events that the profit estimator will handle.");
		println("Lets grab the event string from the user via a dialog window.");
		JOptionPane.showMessageDialog(null, "This program is designed to output to the console one, it will not create a file.", "Profit Estimator Event Indexer", JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(null, "The next window will ask for a event string for the indexer to analyze and format in such a way" + System.getProperty("line.separator") + "that the Profit Estimator will work.", "Profit Estimator Event Indexer", JOptionPane.INFORMATION_MESSAGE);
		String input = JOptionPane.showInputDialog(null, "Input your event string now.", "Profit Estimator Event Indexer", JOptionPane.INFORMATION_MESSAGE);
		println(input);
		String inputParse[] = input.split(",");
		for(int i = 0; i < inputParse.length; i++) {
			println("["+i+"]   " + inputParse[i]);
		}
	}
	
	public void draw() {
		System.exit(0);		
	}

}
