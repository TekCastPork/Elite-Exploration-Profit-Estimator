import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * While not a real true installer this class will hopefully make the setup process easier by making required files
 * @author TekCastPork
 *
 */
public class Installer {
	
	public static void makeFolder(String folderName) throws IOException {
		File folder = new File(folderName);
		if(folder.mkdir()) {
			System.out.println("We made the directory: " + folderName);
		} else {
			throw new IOException("Failed to create directory " + folderName);
		}		
	}
	
	public static void makeFile(String fileName, String data) throws IOException {
		File file = new File(fileName);
		if(file.createNewFile()) {
			System.out.println("We made the file: " + file);
		} else {
			throw new IOException("Failed to create directory " + file);
		}
		if(data.equals(null)) {
			System.out.println("There was no data given to write to the file.");
		} else {
			System.out.println("Some data was given for us to write to the file, now writing...");
			PrintWriter writer = new PrintWriter(file);
			writer.print(data);
			writer.flush();
			writer.close();
			System.out.println("Data was written.");
		}
		
	}
	
	public static boolean doesExist(String folderName) {
		Path pathToFile = Paths.get(folderName);
		boolean returnVal = false;
		if(Files.exists(pathToFile)) {
			System.out.println("The folder/file: " + folderName + " does exist.");
			returnVal = true;
		} else {
			System.out.println("The folder/file: " + folderName + " doesn't exist.");
		}
		return returnVal;
		
	}

}
