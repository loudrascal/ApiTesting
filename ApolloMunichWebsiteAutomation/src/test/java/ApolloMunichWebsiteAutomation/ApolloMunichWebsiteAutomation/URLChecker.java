package ApolloMunichWebsiteAutomation.ApolloMunichWebsiteAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class URLChecker {

    public static void main(String[] args) throws Exception {
        long timestamp = System.currentTimeMillis();
        FileInputStream fisinput = new FileInputStream(new File("/home/hi.agrawal/URLs_for_status.xlsx"));
        XSSFWorkbook iworkbook = new XSSFWorkbook(fisinput);
        XSSFSheet ispreadsheet = iworkbook.getSheetAt(0);
        int length = ispreadsheet.getLastRowNum();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

        for (int i = 1; i <= length; ++i) {
            String url = ispreadsheet.getRow(i).getCell(0).toString();
        //	String url = "https://www.apollomunichinsurance.com/";
            Task task = new Task(url);
            executor.execute(task);
        }
        iworkbook.close();
        executor.shutdown();
        while (!executor.awaitTermination(50, TimeUnit.MILLISECONDS));
        timestamp = (System.currentTimeMillis()) - timestamp;
        System.out.println("time taken :" + timestamp);
        System.out.println("Execution completed Successfully Please check file :"+ Task.file +"for outputs");
        System.out.println("Total URLs checked :"+length);
        System.out.println("Count with response codes 200's are :"+Task.code200);
        System.out.println("Count with response codes 300's are :"+Task.code300);
        System.out.println("Count with response codes 400's are :"+Task.code400);
        System.out.println("Count with response codes 500's are :"+Task.code500);
        System.out.println("Count with exception are :"+Task.exception);

    }

}