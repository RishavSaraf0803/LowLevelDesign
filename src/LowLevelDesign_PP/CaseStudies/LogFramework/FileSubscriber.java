package logframework;

import java.io.FileWriter;

public class FileSubscriber implements Subscriber {

	private final FileWriter fileWriter;
	
	public FileSubscriber(FileWriter fileWriter) {
		super();
		this.fileWriter = fileWriter;
	}

	@Override
	public void update(String message) {
		// TODO Auto-generated method stub
		try {
		fileWriter.write(message + "\n");
		fileWriter.flush();
		}
		catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
