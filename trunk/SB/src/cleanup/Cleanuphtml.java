/**
 * Created by: dagrawal on Jun 19, 2009
 * Email: dagrawal@research.att.com
 */
package cleanup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;

public class Cleanuphtml {
	static Logger logger = Logger.getLogger(Cleanuphtml.class);
	protected String pattern;
	VerseRepository verseRepository;
	public File[] readFiles(String directory){
		File dir = new File(directory);
		File[] files = dir.listFiles();
		    
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.contains(pattern);
	        }
	    };
	    files = dir.listFiles(filter);
	    return files;
	}
	public Cleanuphtml(){
		verseRepository = new VerseRepository();
	}
	public void setPattern(String pattern){
		this.pattern = pattern;
	}
	public String getPattern(){
		return pattern;
	}
	public String getTitle(String text){
		String title = null;
		title = text.split("<title>")[1].split("</title>")[0];
		return title;
	}
	public String getHeading(String text){
		String heading = null;
		try{
			heading = text.split("<div class=\"Verse-Number\">")[1].split("</div>")[0];
		}catch(ArrayIndexOutOfBoundsException e){
			try{
				heading = text.split("<div class=\"Verse-Number-Heading\">")[1].split("</div>")[0];
			}catch(ArrayIndexOutOfBoundsException e1){
				try{
					heading = text.split("<span class=\"Letter-Ref\">")[1].split("</div>")[0];
				}catch(ArrayIndexOutOfBoundsException e2){
					//ignore
				}
			}
		}
		return heading;
	}
	public String getVerseNumber(String text) throws ArrayIndexOutOfBoundsException{
		String verseNumber = null;
		if(text.contains("SB")){
			String[] tmpArray = text.split("SB ");
			//if array has only 1 element then split did not work. try - instead of space
			if(tmpArray.length < 2){
				//logger.warn("Unable to get verseNumber... trying to split by SB- "+text);
				tmpArray = text.split("SB-");
			}
			verseNumber = tmpArray[tmpArray.length-1].split("<")[0];
		}
		return verseNumber;
	}
	/**
	 * This method reads the html files downloaded and saves only individual SB verses
	 * with appropriate name. It also adds the devanagri verse at apropriate places
	 * @param inFile - input file
	 * @param outDir - directory where the output files will be saved.
	 * @throws IOException
	 */
	public void cleanUp(File inFile, String outDir) throws IOException{
		BufferedReader in = null;
		boolean actualText = false;
		FileOutputStream fout = null;
		in = new BufferedReader(new FileReader(inFile));
		String line = null; 
		String outFileName = null;
		String verseNumber = null;
		String title = null;
		fout = new FileOutputStream (outDir+"tmp.html");
		PrintStream ps = new PrintStream(fout);
		//ps.println("<head>");
		//ps.println("<link rel=\"stylesheet\" href=\"folio_unicode.css\" type=\"text/css\"></link>");
		//ps.println("</head>");
		//ps.println("<body>");
		while ((line = in.readLine()) != null) {
			if(line.contains("<title>")){
				title = getTitle(line);
			}
			if(line.equals("<table>")){
				actualText = true; //start of actual content
			}
			if(actualText){
				if(line.contains("</table>")){
					actualText = false; //end of actual content. stop writing any more lines
				}
				if(line.contains("<div class=\"Verse-Number")){
					try {
						verseNumber = getVerseNumber(line);
					} catch (ArrayIndexOutOfBoundsException e) {
						logger.error(inFile.getName()+" does not contain SB");
						break;
					}
					if(verseNumber == null || verseNumber.length() < 1){
						break;
					}
				}
				//save this line in the new file
				else if(!line.contains("<div class=\"Search-Heading\"") && !line.contains("Link to this page:") && !line.equals("<div class=\"Text-Section\">TEXT</div>")){
					ps.println(line);
					if(line.contains("<div class=\"Textnum\">") && verseNumber != null){
						List<String> sanskritVerses = verseRepository.getSanskritVerse(verseNumber);
						if(sanskritVerses == null){
							logger.error("Sanskrit verse not found for "+verseNumber+" in file "+inFile.getName());
						}else{
							for(String sanskritVerse : sanskritVerses){
								ps.println("<div class=\"Devanagari\">"+sanskritVerse+"</div>");
							}
						}
					}
				}
			}
		}
		//ps.println("</body>");
		in.close();
		fout.close();
		File tmpFile = new File(outDir+"tmp.html");
		if(verseNumber != null && verseNumber.length() > 0){
			outFileName = outDir+verseNumber+".html";			
			logger.debug("Input file "+inFile.getName()+" has SB verse "+verseNumber);
			File outFile = new File(outFileName);
			tmpFile.renameTo(outFile);
		//}else if(title.contains("hagavatam") || title.contains("SB") || title.contains("S.B")){
		}else if(title.matches(".+hagavatam.*rabhupada.+ooks.*")){
			logger.warn("Input file "+inFile.getName()+" has "+title);
		}
		tmpFile.delete();
	}
	public static void main(String[] args) {
		Cleanuphtml cleanuphtml = new Cleanuphtml();
		cleanuphtml.setPattern(".html");
		File[] files = cleanuphtml.readFiles(args[0]); 
		for(File inFile : files){
			try {
				cleanuphtml.cleanUp(inFile, args[1]);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
