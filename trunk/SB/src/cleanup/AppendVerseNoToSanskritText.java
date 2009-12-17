/**
 * Created by: dagrawal on Dec 15, 2009
 * Email: dev.agrawal@gmail.com
 */
package cleanup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AppendVerseNoToSanskritText {

	Logger logger = Logger.getLogger(getClass());
	public static final String INPUT_FILE = "C:\\dagrawal\\workspace\\SB\\Verses\\verses1-10.txt";
	public static final String OUTPUT_FILE = "C:\\dagrawal\\workspace\\SB\\Verses\\verses.txt";
	
	public Object getFileHandle(String filePath, String rw) throws FileNotFoundException{
		Object ret = null;
			if(rw.equals("r")){
				ret = new BufferedReader(new FileReader(filePath));
			}else{
				FileOutputStream fout = new FileOutputStream (filePath);
				ret = new PrintStream(fout);
			}
		return ret;
	}
	
	public void doTheJob() throws IOException{
		String line = null;
		String verse = null;
		List<String> verseLineList = new ArrayList<String>();
		PrintStream out = (PrintStream)getFileHandle(OUTPUT_FILE,"w");
		BufferedReader in = (BufferedReader)getFileHandle(INPUT_FILE,"r");
		while ((line = in.readLine()) != null) {
			if(line.startsWith("TEXT")){
				if(!verseLineList.isEmpty()){
					appendVerseNoAndPrint(verse, verseLineList, out);
				}else{
					logger.warn("verseLineList is empty for "+verse);
				}
				verseLineList.clear();
				verse = line.split(" ")[1];
				out.println(line);
			}else{
				verseLineList.add(line);
			}
		}
		appendVerseNoAndPrint(verse, verseLineList, out);
	}
	public void appendVerseNoAndPrint(String verse, List<String> verseLineList,PrintStream out){
		String verseNumber = verse.split("\\.")[2];
		for(int i=0;i<verseLineList.size();i++){
			String line = verseLineList.get(i);
			if((verseLineList.size()-i == 1) && (!line.matches(".*\\)\\)"))){
				out.println(line+" )) "+verseNumber+" ))");
			}else
				out.println(line);
		}
	}
	public static void main(String[] args) {
		AppendVerseNoToSanskritText obj = new AppendVerseNoToSanskritText();
		try {
			obj.doTheJob();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
