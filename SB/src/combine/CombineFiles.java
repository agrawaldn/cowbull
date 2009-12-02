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
	public static final String INPUT_FILE = "C:\\dagrawal\\personal\\httrac\\SB\\order_canto";
	public static final String OUTPUT_FILE = "C:\\dagrawal\\personal\\httrac\\SB\\final\\canto";
	public static final String INPUT_DIR = "C:\\dagrawal\\personal\\httrac\\SB\\output\\";
	public CombineFiles(){
		for(int i=1;i<=10;i++){
			combine(i);
		}
	}
	public CombineFiles(int canto){
		combine(canto);
	}
	private void combine(int canto){
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new FileReader(INPUT_FILE+canto+".txt"));
			FileOutputStream fout = new FileOutputStream (OUTPUT_FILE+canto+".html");
			PrintStream out = new PrintStream(fout);
			logger.info("Generating file "+OUTPUT_FILE+canto+".html");
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
		out.println("<link rel=\"stylesheet\" href=\"doc.css\" type=\"text/css\"></link>");
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
			}else if(line.contains("Summary")){
				//out.print("<div class=\"Textnum\">");
				//out.print("Summary");
				//out.println("</div>");
				verseFileName = line+".html";
				out.println(readContents(INPUT_DIR+verseFileName));
			}else if(line.contains("Introduction")){
				//out.print("<div class=\"Chapnum\">");
				//out.print(line);
				//out.println("</div>");
				verseFileName = line+".html";
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
		CombineFiles cf = new CombineFiles(1); 
	}

}
