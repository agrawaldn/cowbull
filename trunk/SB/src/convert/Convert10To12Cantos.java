/**
 * Created by: dagrawal on Nov 20, 2009
 * Email: dagrawal@research.att.com
 * 
 * this program reads text file and prints html for cantos 10-part2 to 12.
 * To disablr english transliterian go to method printVerse()
 */
package convert;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Convert10To12Cantos {
	Logger logger = Logger.getLogger(getClass());
	public static final int MAX_QUOTED_VERSE_LENGTH = 52;
	public static final String INPUT_FILE = "C:\\dagrawal\\workspace\\SB\\text\\canto10_part2.txt";
	public static final String OUTPUT_FILE = "C:\\dagrawal\\workspace\\SB\\output\\canto10_part2.html";
	boolean divTagOpen = false;
	public static final int MAX_LENGTH_FOR_COLAPSING_DEVNAGRI = 50;
	public Convert10To12Cantos(){
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new FileReader(INPUT_FILE));
			FileOutputStream fout = new FileOutputStream (OUTPUT_FILE);
			PrintStream out = new PrintStream(fout);
			logger.info("Generating file "+OUTPUT_FILE);
			doTheJob(in, out);
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
	private void doTheJob(BufferedReader in, PrintStream out) throws IOException{
		List<String> verse = new ArrayList<String>();
		List<String> quotedVerse = new ArrayList<String>();
		int lengthPreviousLine = 0,lengthCurrentLine;
		String section = "";
		String text = "";
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"print.css\" type=\"text/css\"></link>");
		out.println("</head>");
		out.println("<body>");
		String line;
		while ((line = in.readLine()) != null) {
			lengthCurrentLine = line.length();
			if(line.startsWith("Çrémad-Bhägavatam Canto ")){
				out.print("<div class=\"Cantonum\">");
				out.print(escapeHTMLCharacters(line));
				out.println("</div>");
				section = "";
			}else if(line.matches("\\d+\\. \\D+")){
				logger.debug(line);
				printParagraph(quotedVerse,100,lengthPreviousLine,line,out,"Chapnum",true);
				out.println("</div>");
				divTagOpen = false;
				out.print("<div class=\"First\">");//for the chapter summary
				divTagOpen = true;
				section = "";
			}else if(line.startsWith("TEXT")){
				text = line;
				logger.debug(line);
				//printParagraph(quotedVerse,100,lengthPreviousLine,line,out,"Textnum",true);
				printParagraph(quotedVerse,100,lengthPreviousLine,null,out,null,true);
				if(divTagOpen){
					out.println("</div>");
					divTagOpen = false;
				}
				section = "VERSE"; 
			}else if(line.startsWith("SYNONYMS")){
				printVerse(verse, out, text);
				verse = new ArrayList<String>();
				out.print("<div class=\"Synonyms-Section\">");
				out.print(line);
				out.println("</div>");
				out.print("<div class=\"Synonyms\">");
				divTagOpen = true;
				section = "SYNONYMS";
			}else if(line.startsWith("TRANSLATION")){
				out.println("</div>");
				divTagOpen = false;
				out.print("<div class=\"Verse-Section\">");
				out.print(line);
				out.println("</div>");
				out.print("<div class=\"Translation\">");
				divTagOpen = true;
				section = "TRANSLATION";
			}else if(line.startsWith("PURPORT")){
				out.println("</div>");
				divTagOpen = false;
				out.print("<div class=\"Verse-Section\">");
				out.print(line);
				out.println("</div>");
				out.print("<div class=\"First\">");
				divTagOpen = true;
				section = "PURPORT";
			}else if(section.equals("VERSE")){
				verse.add(escapeHTMLCharacters(line));
			}else if(section.equals("SYNONYMS") || section.equals("TRANSLATION")){
				out.print(escapeHTMLCharacters(" "+line));
			}else{//for PURPORT section and all other
				printParagraph(quotedVerse,lengthCurrentLine,lengthPreviousLine,line,out,"First", false);
			}
			lengthPreviousLine = lengthCurrentLine;
		}
		out.println("</body>");
	}
	private void printParagraph(List<String> quotedVerse,int lengthCurrentLine, int lengthPreviousLine, String line, PrintStream out, String nextDivClass, boolean newSection){
		if(lengthCurrentLine <= MAX_QUOTED_VERSE_LENGTH || 
				(lengthCurrentLine<lengthPreviousLine && 
						(lengthPreviousLine-lengthCurrentLine/lengthPreviousLine <= 0.8))){
			quotedVerse.add(line);//this could be a line of quoted verse or just last line of paragraph
		}else{
			if(quotedVerse.size()>1){
				//end of quoted verse. print all the line of quoted verse now
				for(int i=0;i<quotedVerse.size();i++){
					if(shortLineInEnglish(quotedVerse.get(i))){
						if(!divTagOpen){
							out.print("<div class=\"First\">");
							divTagOpen = true;
						}
						out.print(escapeHTMLCharacters(" "+quotedVerse.get(i)));
					}else{
						if(divTagOpen){
							out.println("</div>");
							divTagOpen = false;
						}
						out.print("<div class=\"Verse-in-purp\">");
						divTagOpen = true;
						out.print(escapeHTMLCharacters(quotedVerse.get(i)));
						out.println("</div>");
						divTagOpen = false;
					}
				}
				quotedVerse.clear();
				if(!newSection && !divTagOpen){//if it is a new section then dont print because we are printing it below anyway
					out.print("<div class=\""+nextDivClass+"\">");
					divTagOpen = true;
				}
			}else if(quotedVerse.size()==1){
				//end of paragraph
				out.print(escapeHTMLCharacters(" "+quotedVerse.remove(0)));
				out.println("</div>");
				divTagOpen = false;
				if(!newSection){//if it is a new section then dont print because we are printing it below anyway
					out.print("<div class=\""+nextDivClass+"\">");
					divTagOpen = true;
				}
			}
			if(newSection){
				if(divTagOpen){
					out.println("</div>");
					divTagOpen = false;
				}
				if(nextDivClass!=null){
					out.print("<div class=\""+nextDivClass+"\">");
					divTagOpen = true;
				}
			}
			if(line != null){
				out.print(escapeHTMLCharacters(" "+line));
			}
		}
	}
	/**
	 * 
	 * returns true when a short line is not a quoted sanskrit verse, 
	 * but just a short line in english
	 * @param line
	 * @return
	 */
	private boolean shortLineInEnglish(String line){
		boolean shortLineInEnglish = false;
		if(line.matches(".+\\:")||
				line.matches(".+\\.")||
				line.matches(".+\\.'")||
				line.matches(".+\\.\"")||
				line.matches(".+\\.'\"")||
				line.matches(".+\\.' \"")||
				line.matches(".+\\?")||
				line.matches(".+\\?'")||
				line.matches(".+\\?\"")||
				line.matches(".+\\?'\"")||
				line.matches(".+\\?' \"")||
				line.matches(".+\\!")||
				line.matches(".+\\!\"")||
				line.matches(".+\\!'")||
				line.matches(".+\\!' \"")||
				line.matches(".+\\!'\"")){
			shortLineInEnglish = true;
		}
		return shortLineInEnglish;
	}
	/**
	 * 
	 * prints both english transliterian and devanagri 
	 */  
	private void printVerse(List<String> verse,PrintStream out, String text){
//		Map<Integer,String> lineNoVerseMap = new HashMap<Integer,String>();
		int lines = verse.size();
		if(lines%2>0){
			logger.error("Number of lines in Shloka "+text+" = " +lines);
			System.exit(1);
		}
		String verseNo = text.split(" ")[1];
//		if(text.contains("TEXTS")){
//			String[] startEndPair = verseNo.split("-");
//			int noOfVerses = Integer.parseInt(startEndPair[1])-Integer.parseInt(startEndPair[0])+1;
//			int linesInEachVerse = (lines/2)/noOfVerses;
//			for(int j=1;j<=noOfVerses;j++){
//				lineNoVerseMap.put(j*linesInEachVerse-1, ""+(Integer.parseInt(startEndPair[0])+(j-1)));
//			}
//		}else{
//			lineNoVerseMap.put(lines/2-1, verseNo);
//		}
		//List<String> verseDevnagri = verse.subList(0, lines/2);
		List<String> verseDevnagri = verse.subList(0, lines/2);
		if(canBeColapsed(verseDevnagri)){
			verseDevnagri = collapseVerse(verse.subList(0, lines/2),text);
		}
		//Comment this loop to skip printing devnagri for verse
		for(int i=0;i<verseDevnagri.size();i++){
			out.print("<div class=\"dev-rm\">");
			out.print(verseDevnagri.get(i));
			if(i == verseDevnagri.size()-1){//print shloka number in the end of last line
				out.print(" // "+verseNo+" //");	
			}
			out.println("</div>");
		}
		//Comment this loop to skip printing english transliterian for verse
		for(int j=lines/2;j<lines;j++){
			out.print("<div class=\"Verse-Text\">");
			out.print(verse.get(j));
			out.println("</div>");

		}
	}
	private boolean canBeColapsed(List<String> verseLineList){
		boolean canBeColapsed = false;
		int maxLength = 0;
		for(String line : verseLineList){
			if(line.length() > maxLength)
				maxLength = line.length();
		}
		if(maxLength <= MAX_LENGTH_FOR_COLAPSING_DEVNAGRI)
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
	private String escapeHTMLCharacters(String text){
		String escapedText = null;
		escapedText = text.replace("<", "&#60;");
//		escapedText = text.replace(">", "&#62;");
		return escapedText;
	}
	public static void main(String[] args) {
		Convert10To12Cantos obj = new Convert10To12Cantos();
	}

}
