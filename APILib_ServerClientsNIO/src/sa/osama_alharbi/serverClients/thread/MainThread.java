package sa.osama_alharbi.serverClients.thread;

import javafx.application.Platform;
import sa.osama_alharbi.serverClients.model.AppType;
import sa.osama_alharbi.serverClients.model.Setting;

public class MainThread{
	public static void getThread(Runnable run){
		int AppTypeNum = Setting.getThreadType();
		switch (AppTypeNum) {
		case AppType.CONSOLE:
			consoleThread(run);
			break;
		case AppType.JAVAFX:
			javaFxThread(run);
			break;
		case AppType.SWING:
			System.err.println("MainThread Error");
			break;
		case AppType.ANDROID:
			System.err.println("MainThread Error");
			break;
		default:
		}
	}
	
	private static void javaFxThread(Runnable run){
		Platform.runLater(run);
	}
	
	private static void consoleThread(Runnable run){
		new Thread(run).start();
	}

}
