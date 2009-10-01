/**
 * Created by: dagrawal on Aug 19, 2009
 * Email: dev.agrawal@gmail.com
 */
package combine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class CombineFiles {

	Logger logger = Logger.getLogger(getClass());
	public static final String INPUT_FILE = "C:\\dagrawal\\personal\\httrac\\SB\\order_canto1.txt";
	public static final String OUTPUT_FILE = "C:\\dagrawal\\personal\\httrac\\SB\\final\\canto1.html";
	public static final String INPUT_DIR = "C:\\dagrawal\\personal\\httrac\\SB\\output\\";
	public CombineFiles(){
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
	
	public void doTheJob(BufferedReader in, PrintStream out) throws IOException{
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"folio_unicode.css\" type=\"text/css\"></link>");
		out.println("</head>");
		out.println("<body>");
		String verseFileName = "";
		String line;
		while ((line = in.readLine()) != null) {
			if(line.contains("Canto")){
				out.print("<div class=\"Cantonum\">");
				out.print(line);
				out.println("</div>");
			}else if(line.contains("Chapter")){
				out.print("<div class=\"Chapnum\">");
				out.print(line.split("Chapter ")[1]);
				out.println("</div>");
			}else if(line.startsWith("TEXT")){
				if(line.contains("TEXTS")){
					verseFileName = line.split("TEXTS ")[1];
				}else{
					verseFileName = line.split("TEXT ")[1];
				}
				verseFileName += ".html";
				out.println(readContents(INPUT_DIR+verseFileName));
			}
		}
		out.println("</body>");
	}
	private StringBuffer readContents(String filePath){
		StringBuffer contents = new StringBuffer();
		BufferedReader in = null;
		String line;
		try {
			in = new BufferedReader(new FileReader(filePath));
			while ((line = in.readLine()) != null) {
				contents.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
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
		CombineFiles cf = new CombineFiles(); 

	}

}
