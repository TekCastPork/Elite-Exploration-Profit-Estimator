import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BigBrother {
	static long pointer = 0;
	public static boolean watch() throws IOException {
		File logFile = new File(Resources.filepath);
		long fileLength = logFile.length();
		boolean returnVal = false;
		if(fileLength < pointer) {
			System.out.println("Stuff was lost.");
			pointer = fileLength;
		} else if(fileLength > pointer) {
			System.out.println("Stuff was added!");
			RandomAccessFile raf = new RandomAccessFile(logFile,"r");
			raf.seek(pointer);
			String line = null;
			while((line = raf.readLine()) != null) {
				System.out.println("Message: " + line);
			}
			returnVal = true;
			pointer = raf.getFilePointer();
			raf.close();
		}
		return returnVal;
		
	}

}