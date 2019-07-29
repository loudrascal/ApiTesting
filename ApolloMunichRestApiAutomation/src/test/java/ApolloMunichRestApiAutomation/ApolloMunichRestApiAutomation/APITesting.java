package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;


import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class APITesting {

	public static String restPostApiTester(JSONObject scenario,String url) {
		String result = "";
		RestAssured.baseURI = url;
        RequestSpecification request = RestAssured.given();
        JSONObject responseRecived = null;
        request.body(scenario.toString());
        request.header("Content-Type", "application/json");
        request.header("userid", "test.test");
        try {
            Response response = request.post("");
            // System.out.println("jsn"+requestParams.toString());
            if (response.getStatusCode() >= 200) {
                // System.out.println("Status code is "+response.asString());	
            	responseRecived = new JSONObject(response.asString());
            }
            result ="Status Code:"+response.getStatusCode();
            int statusApi = (Integer) responseRecived.get("statusCode");
            if(statusApi==1) {
            	JSONArray error = responseRecived.getJSONArray("error");
            	for(int i=0;i<error.length();++i) {
            		JSONObject errorObject = error.getJSONObject(i);
            		String errorMessage = errorObject.get("errorMessage").toString();
            		result +=",Fail";
            		result +=", Error Message:"+errorMessage;
            	}            	
            }
            if(statusApi==0){
            	JSONArray body =  responseRecived.getJSONArray("body");
            	for(int i=0;i<body.length();++i) {
            		JSONObject proposal = body.getJSONObject(i);
            		JSONObject proposalCreationResponse = proposal.getJSONObject("proposalCreationResponse");
            		result +=",Pass";
            		result +=", proposalNumber:"+proposalCreationResponse.get("proposalNumber");
            		result +=", applicationNumber:"+proposal.get("applicationNumber");
            		result +=", revisedPremium:"+proposalCreationResponse.get("revisedPremium");
            		result +=", proposalWarnings:"+getStringfromJSONArray(proposalCreationResponse.get("proposalWarnings"));            		
            		result +=", nstpflag:"+proposal.get("nstpflag");
            	}
            }
        } catch (Exception e) {
        	result = e.getMessage();
        }
        
		return result;
	}
	
	public static String getStringfromJSONArray(Object object2) {
		String result="";
		JSONArray res = new JSONArray(object2.toString());
		for(Object message:res) {
			result+=message.toString()+"||";
		}
	//	System.out.println(result);
		return result.replaceAll(",", ".");
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String data = new String(Files.readAllBytes(Paths.get("/home/hi.agrawal/1563255602681/Scenario_11152_500000_Adult1_Minor1.json")));
		
		JSONObject test = new JSONObject(data);
		String url = "https://dev_web_micro.amhi.in/api/elixir/proposal/create";
		System.out.println(restPostApiTester(test,url));
	}

}
