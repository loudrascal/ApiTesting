package ApolloMunichWebsiteAutomation.ApolloMunichWebsiteAutomation;

import java.io.File;
import java.io.FileInputStream;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class URLChecker2 {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	
    	
        try {
        	Long timestamp = System.currentTimeMillis();
            PrintStream o = new PrintStream(new File("/home/hi.agrawal/reponses.csv")); 
            System.setOut(o);
            System.out.println("S.No,URL,Response");
            FileInputStream fisinput = new FileInputStream(new File("/home/hi.agrawal/URLs_for_status.xlsx"));
            XSSFWorkbook iworkbook = new XSSFWorkbook(fisinput);
            XSSFSheet ispreadsheet = iworkbook.getSheetAt(0);
            int length = ispreadsheet.getLastRowNum();
            String respCode ="";
            int code200=0,code400=0,code500=0,code300=0,exception=0;
            for (int i = 1; i <= 1000; ++i) {
                String url = ispreadsheet.getRow(i).getCell(0).toString();
                try {
                HttpURLConnection huc = (HttpURLConnection)(new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                respCode = huc.getResponseCode()+"";
                if(respCode.startsWith("5"))
                	++code500;
                else if(respCode.startsWith("4"))
                	++code400;
                else if(respCode.startsWith("2"))
                	++code200;
                else if(respCode.startsWith("3"))
                	++code300;
                
                }catch(Exception e) {
                	respCode= e.getMessage();
                	++exception;
                }
                System.out.println(i+","+url+","+respCode);
            }

            iworkbook.close();
            System.setOut(new PrintStream(System.out));
            System.out.println("Execution completed Successfully Please check file URL_Responses.xlsx for outputs");
            System.out.println("Total URLs checked :"+length);
            System.out.println("Count with response codes 200's are :"+code200);
            System.out.println("Count with response codes 300's are :"+code300);
            System.out.println("Count with response codes 400's are :"+code400);
            System.out.println("Count with response codes 500's are :"+code500);
            System.out.println("Count with exception are :"+exception);
            timestamp = (System.currentTimeMillis()-timestamp);
            
            System.out.println("Execution Completed, Total time taken:"+timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}