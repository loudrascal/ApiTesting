package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

public class ScenarioCreater {
	static int count=0;
	static final int maxMembers=6;;
	
	public static void main(String args[]) {
		long starttime = System.currentTimeMillis();
		System.out.println("execution started at :"+starttime);
		try {
			//Read Data from Excel
			FileInputStream fis 	= new FileInputStream(new File("/home/hi.agrawal/Scenario.xlsx"));
			XSSFWorkbook workbook 	= new XSSFWorkbook(fis);
			XSSFSheet spreadsheet 	= workbook.getSheetAt(0);
			int length 			= spreadsheet.getLastRowNum();
			Timestamp timestamp 	= new Timestamp(System.currentTimeMillis());
			File directory 		= new File("/home/hi.agrawal/"+timestamp.getTime()); 

			//Create Directory to store Files and outputs
			if(directory.mkdirs())
				System.out.println("Directory containing all the files created at :"+directory.getAbsolutePath());
			else {
				System.out.println("Issue in creating Directory, please check whether your program has sufficient priviledges \n"
									+ "to create Directory at this location");
				workbook.close();
				throw new Exception();
			}
			
			// Adding header to the output File
			String header = "S.No,Product Code,Product Name,No. of adults,No. of minors,File Name,";
			for(int i=1;i<=maxMembers;++i) 
				header+="Age,Gender,Relation,Sum Insured,deductible Amount,";
			header+="Status Code,API Status";
			Formatter.write((directory.getAbsolutePath()+"/output.csv"), header);
			
			// Executing scenarios one by one from the excel sheet
			ProductDetails pd = new ProductDetails();
			for(int i=1;i<length;++i) {	        
				int productCode = Integer.parseInt(spreadsheet.getRow(i).getCell(1).getRawValue());				
		        JSONObject productDetail = pd.getProductDetails(productCode);		        
		        ExecuteScenarios.createScenarios(productDetail,directory);		       
			}
			
			Formatter.sort(directory.getAbsolutePath()+"/output.csv");
			workbook.close();
		}catch (Exception e) {
			System.out.println("Issue in automation, exiting----------------------------");
			e.printStackTrace();						
		}finally {
		    long millis = System.currentTimeMillis()-starttime;
		    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
		            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		    
			System.out.println("Execution Completed, Totel time taken:" +hms);
			System.exit(0);
		}
	}
}

