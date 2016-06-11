

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CleanData {

	public static void main(String[] args) throws IOException {
		System.out.println("usage inputfolder outputfile");
		CleanData ex = new CleanData();
		ex.processFolder(args[0],args[1]);
	}
	
	public void processFolder(String inputfolder, String outputfile) throws IOException{
		List<Path> paths = new ArrayList<Path>();
		Files.walk(Paths.get(inputfolder)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		        paths.add(filePath);
		    }
		});
		
		PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
		writer.println("id\tsentiment\treview");
		int id = 0;
		for(Path path : paths){
			File input = path.toFile();
			Document doc = Jsoup.parse(input, "UTF-8", "");
			Elements opinionSignal = doc.select("td.OpinionSignal");
			Elements paragraphs = doc.select("p");
			//Elements date = doc.select("td.date");
			//Elements company = doc.select("td.company");
			//Elements expert = doc.select("td.expert");
			//Elements price = doc.select("td.price");
			System.out.println("Processing " + path);
			
			
			for (int i = 0; i < opinionSignal.size(); i++) {
				if(paragraphs.get(i).text() != null && !paragraphs.get(i).text().trim().equalsIgnoreCase("")){
					writer.println(id + "\t" + opinionSignal.get(i).text() + "\t"+ paragraphs.get(i).text() );
					id++;	
				}
			}
		}
		writer.close();
	}
	
	private int getSentiment(String input){
		if(input.equalsIgnoreCase("BUY")){
			return 1;
		}else if(input.equalsIgnoreCase("BUY on WEAKNESS")){
			return 1;
		}else if(input.equalsIgnoreCase("COMMENT")){
			return -1;
		}else if(input.equalsIgnoreCase("HOLD")){
			return -1;
		}else if(input.equalsIgnoreCase("N/A")){
			return -1;
		}else if(input.equalsIgnoreCase("PARTIAL BUY")){
			return 1;
		}else if(input.equalsIgnoreCase("PAST TOP PICK")){
			return 1;
		}else if(input.equalsIgnoreCase("SELL")){
			return 0;
		}else if(input.equalsIgnoreCase("SELL ON STRENGTH")){
			return 0;
		}else if(input.equalsIgnoreCase("SHORT")){
			return 0;
		}else if(input.equalsIgnoreCase("SPECULATIVE BUY")){
			return 1;
		}else if(input.equalsIgnoreCase("STRONG BUY")){
			return 1;
		}else if(input.equalsIgnoreCase("TOP PICK")){
			return 1;
		}else if(input.equalsIgnoreCase("WAIT")){
			return -1;
		}else if(input.equalsIgnoreCase("WATCH")){
			return -1;
		}else if(input.equalsIgnoreCase("WEAK BUY")){
			return 0;
		}else {
			return -1;
		}
	}

}
