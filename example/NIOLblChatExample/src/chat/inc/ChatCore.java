package chat.inc;

import java.util.HashMap;
import java.util.Map.Entry;

public class ChatCore {
	public static HashMap<String, String> ClientsNames = new HashMap<>();
	public static String myUsername;
	
	public static String getClientsNamesId(String username){
		for (Entry<String, String> entry : ClientsNames.entrySet()) {
            if (entry.getValue().equals(username)) {
                return entry.getKey();
            }
        }
		return null;
	}
}
