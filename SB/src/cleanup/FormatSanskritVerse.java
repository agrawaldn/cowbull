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

public class FormatSanskritVerse {

	Logger logger = Logger.getLogger(getClass());
	private static final String INPUT_FILE = "C:\\dagrawal\\workspace\\SB\\Verses\\verses1-10.txt";
	private static final String OUTPUT_FILE = "C:\\dagrawal\\workspace\\SB\\Verses\\verses.txt";
	
	private Object getFileHandle(String filePath, String rw) throws FileNotFoundException{
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
		int canto = 0, chapNo = 0;
		List<String> verseLineList = new ArrayList<String>();
		PrintStream out = (PrintStream)getFileHandle(OUTPUT_FILE,"w");
		BufferedReader in = (BufferedReader)getFileHandle(INPUT_FILE,"r");
		while ((line = in.readLine()) != null) {
			if(line.startsWith("TEXT")){
				if(!verseLineList.isEmpty()){
					if((canto == 10 && chapNo > 1) || canto > 10){
						formatVerse(verse, verseLineList, out, "/");
					}else
						formatVerse(verse, verseLineList, out, ")");
				}else{
					logger.warn("verseLineList is empty for "+verse);
				}
				verseLineList.clear();
				verse = line.split(" ")[1];
				canto = Integer.parseInt(verse.split("\\.")[0]);
				chapNo = Integer.parseInt(verse.split("\\.")[1]);
				out.println(line);
			}else{
				verseLineList.add(line);
			}
		}
		if((canto == 10 && chapNo > 1) || canto > 10){
			formatVerse(verse, verseLineList, out, "/");
		}else
			formatVerse(verse, verseLineList, out, ")");
	}
	private void formatVerse(String verse, List<String> verseLineList,PrintStream out, String endChar){
		String verseNumber = verse.split("\\.")[2];
		if(verseNumber.contains("-") && !verseNumber.matches(".*[a-z,A-Z].*+")){
			formatRangeVerse(verseNumber,verseLineList, out, endChar, verse);
		}else{
			try{
				Integer.parseInt(verseNumber);
			}catch(NumberFormatException nfe){
				verseNumber = null;
			}
			formatSingleVerse(verseNumber,verseLineList, out, endChar, verse);
		}
	}
	private void formatSingleVerse(String verseNumber, List<String> verseLineList,PrintStream out, String endChar, String verse){
		String verseEndPattern = ".*\\)\\)";
//		String verseMidEndPattern = ".*\\)";
		if(endChar.equals("/")){
			verseEndPattern = ".*//";
//			verseMidEndPattern = ".*/";
		}
		if(verseLineList.size() >= 4 && canBeColapsed(verseLineList)){
			verseLineList = collapseVerse(verseLineList, verse);
		}
		int size = verseLineList.size();
		for(int i=0;i<size;i++){
			String line = verseLineList.get(i);
			//if this is last line and does not have verse-end pattern then append verse-end
			if((i == size-1) && (!line.matches(verseEndPattern)) && verseNumber != null){
				out.println(line+" "+endChar+endChar+" "+verseNumber+" "+endChar+endChar);
			//mid line for size 3->1, size 5->2, size 2->0, size 4->1
//			}else if(((size%2==0 && i==(size/2)-1) || (size%2!=0 && i==size/2)) 
//					&& (!line.matches(verseMidEndPattern)) && size > 1){
//				out.println(line+" "+endChar);
			}else
				out.println(line);
		}
	}
	private boolean canBeColapsed(List<String> verseLineList){
		boolean canBeColapsed = false;
		int maxLength = 0;
		for(String line : verseLineList){
			if(line.length() > maxLength)
				maxLength = line.length();
		}
		if(maxLength <= 40)
			canBeColapsed = true;
		return canBeColapsed;
	}
	private List<String> collapseVerse(List<String> verseLineList, String verse){
		logger.debug("colapsing "+verse);
		List<String> collapsedVerseLineList = new ArrayList<String>();
		switch(verseLineList.size()){
		case 4: 
			collapsedVerseLineList.add(verseLineList.get(0)+" "+verseLineList.get(1));
			collapsedVerseLineList.add(verseLineList.get(2)+" "+verseLineList.get(3));
			break;
		case 5:
			collapsedVerseLineList.add(verseLineList.get(0));
			collapsedVerseLineList.add(verseLineList.get(1)+" "+verseLineList.get(2));
			collapsedVerseLineList.add(verseLineList.get(3)+" "+verseLineList.get(4));			
			break;
		case 6:
			collapsedVerseLineList.add(verseLineList.get(0)+" "+verseLineList.get(1));
			collapsedVerseLineList.add(verseLineList.get(2)+" "+verseLineList.get(3));
			collapsedVerseLineList.add(verseLineList.get(4)+" "+verseLineList.get(5));
			break;
		case 7:
			collapsedVerseLineList.add(verseLineList.get(0));
			collapsedVerseLineList.add(verseLineList.get(1)+" "+verseLineList.get(2));
			collapsedVerseLineList.add(verseLineList.get(3)+" "+verseLineList.get(4));
			collapsedVerseLineList.add(verseLineList.get(5)+" "+verseLineList.get(6));
			break;
		default:
			logger.error("verse "+verse+" has "+verseLineList.size()+" lines.");
			break;
		}
		return collapsedVerseLineList;
	}
	private void formatRangeVerse(String verseNumber, List<String> verseLineList,PrintStream out, String endChar, String verse){
		int size = verseLineList.size();
		for(int i=0;i<size;i++){
			String line = verseLineList.get(i);
				out.println(line);
		}
	}
	public static void main(String[] args) {
		FormatSanskritVerse obj = new FormatSanskritVerse();
		try {
			obj.doTheJob();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
