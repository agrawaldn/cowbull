/**
 * Created by: dagrawal on Jul 31, 2009
 * Email: dagrawal@research.att.com
 */
package cleanup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class VerseRepository {
		
	static Logger logger = Logger.getLogger(VerseRepository.class);
	private Map<String, List<String>> sanskritMap = new TreeMap<String, List<String>>();
	public static final String VERSES_WRITE_LOCATION = "C:\\dagrawal\\workspace\\SB\\Verses\\verses1-9.txt";
	
	/*This is the the input file for creating verse repository "sanskritMap"
	 */
	public static final String FINAL_VERSES_FOR_READ = "C:\\dagrawal\\workspace\\SB\\Verses\\verses.txt";
	public enum SBFile{
		Canto1			(1,"C:\\dagrawal\\workspace\\SB\\text\\Canto1.txt"),
		Canto2			(2,"C:\\dagrawal\\workspace\\SB\\text\\Canto2.txt"),
		Canto3			(3,"C:\\dagrawal\\workspace\\SB\\text\\Canto3.txt"),
		Canto4			(4,"C:\\dagrawal\\workspace\\SB\\text\\Canto4.txt"),
		Canto5			(5,"C:\\dagrawal\\workspace\\SB\\text\\Canto5.txt"),
		Canto6			(6,"C:\\dagrawal\\workspace\\SB\\text\\Canto6.txt"),
		Canto7			(7,"C:\\dagrawal\\workspace\\SB\\text\\Canto7.txt"),
		Canto8			(8,"C:\\dagrawal\\workspace\\SB\\text\\Canto8.txt"),
		Canto9			(9,"C:\\dagrawal\\workspace\\SB\\text\\Canto9.txt");
//		Canto10			(10,"C:\\dagrawal\\workspace\\SB\\text\\Canto10.txt");
		
		int canto;
		String fileLocation;
		
		SBFile(int canto, String fileLocation) {
			this.canto = canto;
			this.fileLocation = fileLocation;
		}

		public String getFileLocation() {
			return fileLocation;
		}
		public int getCanto(){
			return canto;
		}
	}
	
	public VerseRepository(){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(FINAL_VERSES_FOR_READ));
			buildSanskritMap(in);
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
	
	public List<String> getSanskritVerse(String verseNumber){
		return sanskritMap.get(verseNumber);
	}
	public Map<String, List<String>> getSanskritMap(){
		return sanskritMap;
	}
	public void buildSanskritMap(BufferedReader in) throws IOException{
		String verseKey = null;
//		String cantoChapter = null;
		//int startVerse, endVerse;
		List<String> verseValueList = null;
//		List<String> splitVerseList = null;
		String line = null;
		while ((line = in.readLine()) != null) {
			if(!line.startsWith("TEXT")){
				//System.out.println(line);
				verseValueList.add(escapeHTMLCharacters(line));
//				if(cantoChapter!= null){
//					splitVerseList.add(escapeHTMLCharacters(line));
//					if(line.matches(".*\\d+ \\)\\)")){
//						String verse = line.split("\\)\\) ")[1].split(" ")[0];
//						sanskritMap.put(cantoChapter+verse, splitVerseList);
//						splitVerseList = new ArrayList<String>();
//					}
//				}
			}
			else{
				if(verseKey != null){
					sanskritMap.put(verseKey, verseValueList);
				}
//				cantoChapter = null;
				verseValueList = new ArrayList<String>();
				verseKey = line.split(" ")[1];
				//System.out.println(verseKey);
				
//				if(verseKey.contains("-")){
//					String[] verseParts = verseKey.split("-");
//					String[] cantoChapterVerse = verseParts[0].split("\\.");
//					cantoChapter = cantoChapterVerse[0]+"."+cantoChapterVerse[1]+".";
//					splitVerseList = new ArrayList<String>();
//				}
			}
		}//add the last verse in the map.
		if(verseKey != null){
			sanskritMap.put(verseKey, verseValueList);
		}
	}
	private String escapeHTMLCharacters(String text){
		String escapedText = null;
		escapedText = text.replace("<", "&#60;");
//		escapedText = text.replace(">", "&#62;");
		return escapedText;
	}
	public void seperateSanskritVerses(SBFile f, PrintStream out){
		int canto = f.getCanto();
		int chapter = 0;
		int startVerse = 0;
		String verse = "";
		String verseKey = "";
		//List<String> verseValueList = null;
		boolean verseStart = false;
		BufferedReader in = null;
		try{
			in = new BufferedReader(new FileReader(f.getFileLocation()));
			String line = null;
			int endVerse = 0;
			while ((line = in.readLine()) != null) {
				if(verseStart){
					if(!line.startsWith("Copyright") && !line.matches("\\d+") && !line.startsWith("copyright") && !line.startsWith("Distributed by www.e-vedas.com")){
						//System.out.println(line);
						out.println(line);
						//verseValueList.add(line);
					}
				}
				if(line.matches("TEXT \\d+") || line.matches("TEXTS \\d+.\\d+")){
					verseStart = true;
					//verseValueList = new ArrayList<String>();
					//if text # is 1 then it is next chapter
					if(line.contains("TEXTS")){
						verse = line.split("TEXTS ")[1];
						try{
							startVerse = Integer.parseInt(verse.split("–")[0]);
							endVerse = Integer.parseInt(verse.split("–")[1]);
						}catch(NumberFormatException e){
							startVerse = Integer.parseInt(verse.split("-")[0]);
							endVerse = Integer.parseInt(verse.split("-")[1]);
						}
					}else{
						verse = line.split("TEXT ")[1];
						startVerse = Integer.parseInt(verse);
					}
					if(startVerse == 1){
						chapter++;
					}
					verseKey = "TEXT "+canto+'.'+chapter+'.'+verse;
					//System.out.println(verseKey);
					out.println(verseKey);
				}
				if((line.matches(".*"+endVerse+" \\)\\)") && endVerse > 0) 
						|| (line.matches(".*\\d+ \\)\\)") && endVerse == 0)){
					verseStart = false;
					endVerse = 0;
					//sansKritMap.put(verseKey, verseValueList);
				}
			}
		}catch(IOException ioe){
			logger.error("File "+f.getFileLocation()+" could not be read.");
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	/**
	 * This method reads the SB text files and extracts 
	 * devanagri verses from it and saves in VERSES_WRITE_LOCATION 
	 */
	public static void seperateSanskritVerses(){
		VerseRepository vr = new VerseRepository();
		FileOutputStream fout = null;
		PrintStream out = null;
		try {
			fout = new FileOutputStream (VERSES_WRITE_LOCATION);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
		out = new PrintStream(fout);
		for (SBFile file : SBFile.values()){
			vr.seperateSanskritVerses(file, out);
		}
		out.close();
		try {
			fout.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	public static void main(String[] args) {
		//seperateSanskritVerses();
		VerseRepository vr = new VerseRepository();		
//		for(String key : vr.getSanskritMap().keySet()){
//			System.out.println(key);
			for(String value: vr.getSanskritVerse("10.13.64")){
				System.out.println(value);
			}
//		}
	}

}
