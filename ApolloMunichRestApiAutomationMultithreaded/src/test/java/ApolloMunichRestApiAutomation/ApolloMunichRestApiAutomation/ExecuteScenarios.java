package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.File;
import java.util.SortedSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class ExecuteScenarios {

	// Method to create and execute Scenarios based on Product 
	public static void createScenarios(JSONObject productDetail, File directory) throws Exception {
		int maxAdults 	= 4,
			minAdults	= 1,
			minMembers	= 1,
			maxMembers	= 6;
		JSONObject scn 	= null;

		String planName = ProductDetails.getAttribute("productName", productDetail);
		if(planName.toLowerCase().contains("family") || planName.toLowerCase().contains("floater")) {
			minMembers 	= 2;
			maxAdults 	= 2;
		}
		
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
		
		for(int adults =minAdults;adults<=maxAdults;++adults) {
			for(int minors = ((adults>=minMembers)?0:(minMembers-adults));minors+adults<=maxMembers;++minors) {
				SortedSet<Integer> validsumInsured = ProductDetails.getValidSumInsured(productDetail);
				for(Integer cover:validsumInsured) {
					String FileName = "Scenario_"+ProductDetails.getAttribute("productCode", productDetail)+"_"+cover.toString()+"_Adult"+adults+"_Minor"+minors+".json";
					String entry = "\n"+(++(ScenarioCreater.count)) +","+ProductDetails.getAttribute("productCode", productDetail)+","+ProductDetails.getAttribute("productName", productDetail)+","+adults+ ","+minors+","+FileName+",";					
					Object[] res = JsonCreater.createJsonAPI(productDetail,adults,minors,cover,directory);
					scn = (JSONObject) res[0];
					entry+=res[1];
					Formatter.fileWritetoDirectory(scn, directory, FileName);
					String url = "https://dev_web_micro.amhi.in/api/elixir/proposal/create";
					APITesting result = new APITesting(scn,url,directory,entry);
					executor.execute(result);					
				}
			}
		}
		
		executor.shutdown();
		while (!executor.awaitTermination(500, TimeUnit.MILLISECONDS));
		//Formatter.sort(directory.getPath()+"/output.csv");
		System.out.println("Execution Completed for Product code:" +ProductDetails.getAttribute("productCode", productDetail) +":"+ProductDetails.getAttribute("productName", productDetail));		
	}
}
