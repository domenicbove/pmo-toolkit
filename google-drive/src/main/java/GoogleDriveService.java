import java.io.IOException;
import java.util.List;

public interface GoogleDriveService {

	public void initiateProjectFolder (String clientName, String projectName, List<String> emails) throws IOException;
	
}
