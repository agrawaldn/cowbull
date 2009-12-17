/**
 * Created by: dagrawal on Aug 19, 2009
 * Email: dev.agrawal@gmail.com
 */
package cleanup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class TitleRepository {

	Logger logger = Logger.getLogger(getClass());
	public static final String TITLES_FOR_READ = "C:\\dagrawal\\workspace\\SB\\text\\titles.txt";
	private Map<String, String> titleMap = new TreeMap<String, String>();
	
	public TitleRepository(){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(TITLES_FOR_READ));
			buildTitleMap(in);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	public void buildTitleMap(BufferedReader in) throws IOException{
		String line;
		while ((line = in.readLine()) != null) {
			String[] arr = line.substring(2).split(": ");
			titleMap.put(arr[0], arr[1]);
			System.out.println(arr[0]+" --> "+arr[1]);
		}
	}
	
	public static void main(String[] args) {
		TitleRepository tr = new TitleRepository(); 

	}

}
