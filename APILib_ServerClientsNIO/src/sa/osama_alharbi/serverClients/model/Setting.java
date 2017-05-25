package sa.osama_alharbi.serverClients.model;

public class Setting {
	private static String FolderPath;
	private static int threadType;

	public static String getFolderPath() {
		return FolderPath;
	}

	public static void setFolderPath(String folderPath) {
		FolderPath = folderPath;
	}

	public static void setThreadType(int num) {
		threadType = num;
	}

	public static int getThreadType() {
		return threadType;
	}
}
