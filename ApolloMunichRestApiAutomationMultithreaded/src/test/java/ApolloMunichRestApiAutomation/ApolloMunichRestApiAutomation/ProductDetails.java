package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;


import java.io.File;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;


public class ProductDetails {

	static Random rnd = new Random();

    public JSONObject getProductDetails(int productCode) {
        JSONObject productDetail = null;

        RestAssured.baseURI = "https://serviceuat.amhi.in:7002/api/rest/website";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        JSONObject arg0 = new JSONObject();
        JSONObject productDetails = new JSONObject();
        productDetails.put("productCode", productCode + "");
        productDetails.put("productVersion", "1");
        arg0.put("productDetails", productDetails);
        requestParams.put("arg0", arg0);
        request.body(requestParams.toString());
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Basic RUxJWElSV0VCU0lURTp3ZWJsb2dpYzE=");
        try {
            Response response = request.post("/getProductRules");
            // System.out.println("jsn"+requestParams.toString());
            if (response.getStatusCode() >= 200) {
                // System.out.println("Status code is "+response.asString());	
                productDetail = new JSONObject(response.asString());
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return productDetail;
    }

    public static String getAttribute(String attributeName, JSONObject productDetail) {
    	String attribute = "";        
        if (productDetail != null) 
        	attribute = productDetail.getJSONObject("return").getJSONObject("productDetails").get(attributeName).toString();
        return attribute;
    }
    
    public static SortedSet < Integer > getValidSumInsured(JSONObject productDetail) {
        SortedSet < Integer > sumInsured = new TreeSet < Integer > ();
        
        JSONArray sumAssuredArray = productDetail.getJSONObject("return").getJSONObject("productDetails").getJSONArray("SILimit");
        for (int j = 0; j < sumAssuredArray.length(); ++j) {
            JSONObject test = sumAssuredArray.getJSONObject(j);
            int SumAssured = Integer.parseInt(test.get("sumAssuredMultiple").toString());
            sumInsured.add(SumAssured);
        }
        return sumInsured;
    }
    
	synchronized public static String getDeductableAmounts(String productCode){
		String deductableAmount = null;
		List<String> amounts = new ArrayList<String>() ;
		
		try {
			URL path 				= ProductDetails.class.getResource("Deductableamounts.csv");
			FileReader filereader 	= new FileReader(new File(path.getFile()));
			CSVReader csvReader 	= new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			List<String[]> allData 	= csvReader.readAll(); 
			for(int i=0;i<allData.size();++i) {
				String[] row 	= allData.get(i);
				if(row[0].equals(productCode+"")) {
					amounts.add(row[1].toString());
				}			
			}
			if(!amounts.isEmpty())
				deductableAmount = amounts.get(rnd.nextInt(amounts.size()));
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return deductableAmount;
	}

    public static void main(String args[]) {
        
    }
}