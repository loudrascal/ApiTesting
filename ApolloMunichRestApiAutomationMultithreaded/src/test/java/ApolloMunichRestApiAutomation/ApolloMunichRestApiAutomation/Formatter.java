package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Formatter {

	synchronized public static void write(String filePath,String statements) {
		 try { 
			 	File file = new File(filePath);
			  	BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
			  	out.write(statements);
			  	out.close(); 
	        } 
	        catch (IOException e) { 
	            System.out.println("exception occoured while wrting to output file:" + e);
	            return;
	        } 
	}

	synchronized public static void sort(String filePath) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		ArrayList<String> lines = new ArrayList<String>();
		String currentLine = reader.readLine();
        
        while (currentLine != null) {
            lines.add(currentLine);             
            currentLine = reader.readLine();
        }
        Comparator<String> comp = new Comparator<String>() {
            public int compare(String csvLine1, String csvLine2) {
                String test1 = (String) csvLine1.subSequence(0, csvLine1.indexOf(","));
                String test2 = (String) csvLine2.subSequence(0, csvLine2.indexOf(","));
                if(test1.equals("S.No"))
                	return Integer.MIN_VALUE;
                else if(test2.equals("S.No"))
                	return Integer.MAX_VALUE;
                
                return Integer.valueOf(test1).compareTo(Integer.valueOf(test2));
            }
        };
        Collections.sort(lines,comp);
       /* for(String line :lines)
        	write(filePath,line);*/
        reader.close();
       BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (String line : lines)
        {
         //   writer.write(line);             
         //   writer.newLine();
        	
			write(filePath,line);
        	write(filePath,"\n");
        }
        
        writer.close();
	}
	
	synchronized public static void fileWritetoDirectory(JSONObject scn,File directory,String FileName ) throws Exception {
		FileWriter file = new FileWriter(directory+"/"+FileName);
		Gson gson 		= new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp 	= new JsonParser();
		JsonElement je 	= jp.parse(scn.toString());
		String prettyJsonString = gson.toJson(je);
		file.write(prettyJsonString);
		file.flush();
		file.close();
	}
	
	public static void main(String s[]) throws Exception {
		File file = new File("/home/hi.agrawal/1563359513246");
		sort(file.getPath());
		System.out.println(Integer.valueOf("1").compareTo(Integer.valueOf("2")));
	}

}
