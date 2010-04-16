/**
 * Created by: dagrawal on Apr 9, 2010 3:28:42 PM
 * Email: dagrawal@research.att.com
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.log4j.Logger;

public class PrintSelectVerses {

		Logger logger = Logger.getLogger(getClass());
		public static final String INPUT_FILE = "C:\\dagrawal\\workspace\\SB\\text\\select_verse_list.txt";
		public static final String OUTPUT_FILE = "C:\\dagrawal\\workspace\\SB\\output\\select_verse.html";
		public static final String INPUT_DIR = "C:\\temp\\";

		public PrintSelectVerses(){
			BufferedReader in = null;
			
			try {
				in = new BufferedReader(new FileReader(INPUT_FILE));
				FileOutputStream fout = new FileOutputStream (OUTPUT_FILE);
				PrintStream out = new PrintStream(fout);
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
			out.println("<head>");
			out.println("<link rel=\"stylesheet\" href=\"other.css\" type=\"text/css\"></link>");
			out.println("</head>");
			out.println("<body>");
			String verseFileName = "";
			String line;
			while ((line = in.readLine()) != null) {
				if(line.contains("TEXTS")){
					verseFileName = line.split("TEXTS ")[1];
				}else{
					verseFileName = line.split("TEXT ")[1];
				}
				out.println("<div class=\"Verse-Number-Heading\">"+verseFileName+"</div>"+"\n");
				//TODO print devanagri here
				out.println(readContents(verseFileName));
			}
			out.println("</body>");
		}
		private StringBuffer readContents(String verseNo){
			String filePath = INPUT_DIR+verseNo+".html";
			StringBuffer contents = new StringBuffer();
			BufferedReader in = null;
			String line;
			try {
				in = new BufferedReader(new FileReader(filePath));
				while ((line = in.readLine()) != null) {
					if (line.startsWith("<div class=\"Verse-Text\">")
						|| line.startsWith("<div class=\"Translation\">")
						){
						contents.append(line);
						contents.append("\n");
					}
				}
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage()+" "+filePath);
			} catch(IOException ioe){
				logger.error(ioe.getMessage()+" "+filePath);
			} finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return contents;
		}
		public static void main(String[] args) {
				PrintSelectVerses cf = new PrintSelectVerses();
		}

	}



