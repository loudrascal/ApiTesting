package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.SortedSet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class ScenarioCreater {
	static int count=0;
	static final int maxMembers=6;
	
	public static void main(String args[]) {
		try {
			FileInputStream fis 	= new FileInputStream(new File("/home/hi.agrawal/Scenario.xlsx"));
			XSSFWorkbook workbook 	= new XSSFWorkbook(fis);
			XSSFSheet spreadsheet 	= workbook.getSheetAt(0);
			int length 				= spreadsheet.getPhysicalNumberOfRows();
			Timestamp timestamp 	= new Timestamp(System.currentTimeMillis());
			File Directory 			= new File("/home/hi.agrawal/"+timestamp.getTime());
			if(Directory.mkdirs()) 	{
				System.out.println("Directory containing all the files created at :"+Directory.getAbsolutePath());
			}
			ProductDetails pd = new ProductDetails();
			String header = "S.No,Product Code,Product Name,No. of adults,No. of minors,File Name,";
			for(int i=0;i<maxMembers;++i) {
				header+="Age,Gender,Relation,Sum Insured,deductible Amount,";
			}
			header+="Status Code,API Status";
			Formatter.write(Directory, header);
			for(int i=1;i<length;++i) {	        
				int productCode = Integer.parseInt(spreadsheet.getRow(i).getCell(1).getRawValue());				
		        JSONObject productDetail = pd.getProductDetails(productCode);		        
		        createScenarios(productDetail,Directory);				
			}
			
			workbook.close();
		}catch (Exception e) {
			System.out.println("Issue in automation, exiting----------------------------");			
			e.printStackTrace();			
		}finally {
			System.out.println("Execution Completed");
			System.exit(0);
		}
	}

	private static void createScenarios(JSONObject productDetail, File directory) throws IOException {
		// TODO Auto-generated method stub
		int maxAdults =4,minAdults=1,minMembers=1,maxMembers=6;
		JSONObject scn = null;

		String planName = ProductDetails.getAttribute("productName", productDetail);
		if(planName.toLowerCase().contains("family") || planName.toLowerCase().contains("floater")) {
			minMembers = 2;
			maxAdults = 2;
		}
		
		
		
		for(int j =minAdults;j<=maxAdults;++j) {
			for(int k = ((j>=minMembers)?0:(minMembers-j));k+j<=maxMembers;++k) {
				SortedSet<Integer> validsumInsured = ProductDetails.getValidSumInsured(productDetail);
				for(Integer cover:validsumInsured) {
					scn = null;		
					Formatter.write(directory, "\n");
					Formatter.write(directory,((++count)+","+ProductDetails.getAttribute("productCode", productDetail)+","));
					Formatter.write(directory,(ProductDetails.getAttribute("productName", productDetail)+","));
					Formatter.write(directory, (j+ ","+k+","));
					String FileName = "Scenario_"+ProductDetails.getAttribute("productCode", productDetail)+"_"+cover.toString()+"_Adult"+j+"_Minor"+k+".json";
					Formatter.write(directory,(FileName+","));
					scn = JsonCreater.createJsonAPI(productDetail,j,k,cover,directory);
					FileWriter file = new FileWriter(directory+"/"+FileName);
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JsonParser jp = new JsonParser();
					JsonElement je = jp.parse(scn.toString());
					String prettyJsonString = gson.toJson(je);
					file.write(prettyJsonString);
					file.flush();
					file.close();
					String url = "https://dev_web_micro.amhi.in/api/elixir/proposal/create";
					Formatter.write(directory,(APITesting.restPostApiTester(scn,url)));
					Formatter.write(directory, ",");
				}
			}
		}
		System.out.println("Execution Completed for Product code:" +ProductDetails.getAttribute("productCode", productDetail) +":"+ProductDetails.getAttribute("productName", productDetail));
		
	}

	

	
	

}

