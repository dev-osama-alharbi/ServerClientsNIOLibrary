package sa.osama_alharbi.serverClients.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileCache {


	public static void createNewFilecashe(String filePath) {

		try {
			Path path = Paths.get(new URI(filePath));//"file:///Users/osama_al-harbi/Desktop/cashFolder/aa.part1.osSC"));
			
			final Path tmp = path.getParent();
			if (tmp != null) // null will be returned if the path has no parent
			    Files.createDirectories(tmp);
			
			if (Files.exists(path)) {
				Files.delete(path);
				Files.createFile(path);
			} else {
//				Files.delete(path);
				Files.createFile(path);
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertIntoFilecashe(String filePath, byte[] data, int partFrom, int buffSize, int fileLength) {
		
		//worning ===> convert to long for larg files
		try {
			Path path = Paths.get(new URI(filePath));
			FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE,StandardOpenOption.APPEND);
			ByteBuffer bb = ByteBuffer.wrap(data);
//			if((channel.size()+buffSize) <= fileLength){
//				int lengthStartFrom = (buffSize * partFrom) - buffSize;
//			}else{
//				int lengthStartFrom = buffSize
//			}
			while (bb.hasRemaining()) {
				channel.write(bb, channel.size());
			}
			bb.clear();
			bb = null;
			channel.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static File readFilecashe(String filename) {
		return new File(filename);
	}
}
