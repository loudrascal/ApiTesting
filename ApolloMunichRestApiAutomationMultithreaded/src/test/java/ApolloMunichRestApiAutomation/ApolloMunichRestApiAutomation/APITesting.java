package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;


import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class APITesting implements Runnable {
	
	JSONObject scenario = null;
	String url 			= null,entry;
	File directory		= null;
	
	APITesting(JSONObject scenario,String url,File directory, String entry){
		this.scenario 	= scenario;
		this.url 		= url;
		this.directory 	= directory;
		this.entry 		= entry;
	}
	
	public void run() {
		String result = "";
		RestAssured.baseURI = url;
        RequestSpecification request = RestAssured.given();
        request.body(scenario.toString());
        request.header("Content-Type", "application/json");
        request.header("userid", "test.test");
        try {
            Response response = request.post("");
            result = ResponseHandling.getAPIResult(response);            
        } catch (Exception e) {
        	result = e.getMessage();
        }
        entry+=result+",";
		Formatter.write((directory.getPath()+"/output.csv"), entry);
	}
	
	public String getStringfromJSONArray(Object object2) {
		String result="";
		JSONArray res = new JSONArray(object2.toString());
		for(Object message:res) {
			result+=message.toString()+"||";
		}
		return result.replaceAll(",", ".");
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String data = new String(Files.readAllBytes(Paths.get("/home/hi.agrawal/1563255602681/Scenario_11152_500000_Adult1_Minor1.json")));
		
//		JSONObject test = new JSONObject(data);
//		String url = "https://dev_web_micro.amhi.in/api/elixir/proposal/create";
	//	System.out.println(restPostApiTester(test,url));
	}



}
