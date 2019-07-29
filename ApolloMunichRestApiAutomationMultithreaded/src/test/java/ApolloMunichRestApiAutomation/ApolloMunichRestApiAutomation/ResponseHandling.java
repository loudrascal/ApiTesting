package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.restassured.response.Response;

public class ResponseHandling {

	public static String getAPIResult(Response response) {
		// TODO Auto-generated method stub
		JSONObject responseRecived = null;
		String result=""+response.getStatusCode();;
		if (response.getStatusCode() >= 200 && response.getStatusCode()<400) {
             // System.out.println("Status code is "+response.asString());	
         	responseRecived = new JSONObject(response.asString());
	         int statusApi = (Integer) responseRecived.get("statusCode");
	         if(statusApi==1) {
	         	JSONArray error = responseRecived.getJSONArray("error");
	         	for(int i=0;i<error.length();++i) {
	         		JSONObject errorObject = error.getJSONObject(i);
	         		String errorMessage = errorObject.get("errorMessage").toString();
	         		result +=",Fail, Error Message:"+errorMessage;
	         	}            	
	         }
	         if(statusApi==0){
	         	JSONArray body =  responseRecived.getJSONArray("body");
	         	for(int i=0;i<body.length();++i) {
	         		JSONObject proposal = body.getJSONObject(i);
	         		JSONObject proposalCreationResponse = proposal.getJSONObject("proposalCreationResponse");
	         		result +=",Pass, proposalNumber:"+proposalCreationResponse.get("proposalNumber");
	         		result +=", applicationNumber:"+proposal.get("applicationNumber");
	         		result +=", revisedPremium:"+proposalCreationResponse.get("revisedPremium");
	         		result +=", proposalWarnings:"+getStringfromJSONArray(proposalCreationResponse.get("proposalWarnings"));            		
	         		result +=", nstpflag:"+proposal.get("nstpflag");
	         	}
	         }
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

}
