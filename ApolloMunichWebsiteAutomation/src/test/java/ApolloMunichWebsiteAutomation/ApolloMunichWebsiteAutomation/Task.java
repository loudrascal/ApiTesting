package ApolloMunichWebsiteAutomation.ApolloMunichWebsiteAutomation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task implements Runnable {
	String url=null;
	volatile static int code200=0,code400=0,code500=0,code300=0,exception=0;	
	static File file=new File(("/home/hi.agrawal/responses"+System.currentTimeMillis()+".csv"));
	
	Task(String url){
		this.url = url;
	}

	public void run() {
		String respCode="";
		try {
            HttpURLConnection huc = (HttpURLConnection)(new URL(url).openConnection());
            huc.setRequestMethod("HEAD");
            huc.connect();
            respCode = huc.getResponseCode()+"";     
            }catch(Exception e) {
            	respCode= e.getMessage();
            }
			write(url+","+respCode+"\n");
			updatecounter(respCode);
	}
	
	synchronized void updatecounter(String respCode) {
		if(respCode.startsWith("2"))    ++code200;
        else if(respCode.startsWith("3"))    ++code300;
        else if(respCode.startsWith("4")) 
        {  
        	System.out.println(url+","+respCode); 
        	++code400;
        }
        else if(respCode.startsWith("5")) 
		{	System.out.println(url+","+respCode);        
			++code500;
		} 
        else  ++exception;
	}
	synchronized void  write(String string) {
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
			out.write(string);
			out.close();
		}catch(Exception e) {
			System.out.println("Unable to  write to file");
		}
	}
	

}
