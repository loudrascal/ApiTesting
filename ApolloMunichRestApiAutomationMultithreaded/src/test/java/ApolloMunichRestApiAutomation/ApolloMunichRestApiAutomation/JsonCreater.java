package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.File;

import java.util.ArrayList;

import java.util.Random;
import java.util.SortedSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonCreater {
	
	static Random rnd = new Random();
	static ProductDetails pd = new ProductDetails();	
	
	public static  Object[] createJsonAPI(JSONObject productDetail, int adults, int minors,int cover, File directory) {
		MemberData md[] = new MemberData[adults+minors];
		boolean gender = rnd.nextBoolean();
		int age = (rnd.nextInt(30)+30);
		String relation = "Self";
		String sumInsured = cover +"";
		Object[]  covers = {"0"};
		
		ArrayList<String> adultMember = new ArrayList<String>();
		adultMember.add((gender)?"Wife":"Husband");
		boolean decider = rnd.nextBoolean();
		adultMember.add((decider)?"Father":"Father-in-law");
		adultMember.add((decider)?"Mother":"Mother-in-law");
			
        md[0] = new MemberData();
		md[0].createMember(age, gender, relation,sumInsured);
		String planName = ProductDetails.getAttribute("productName", productDetail);
		if(!(planName.toLowerCase().contains("family") || planName.toLowerCase().contains("floater"))) {
			SortedSet<Integer> st =  ProductDetails.getValidSumInsured(productDetail);
			st.removeIf(suminsured -> (suminsured>cover));
			covers = st.toArray();	
		}
			
		for(int i=1;i<adults;++i) {
			md[i] = new MemberData();
			age = (rnd.nextInt(30)+30);
			int key  = rnd.nextInt(adultMember.size());
			relation = adultMember.get(key);
			sumInsured =  covers[rnd.nextInt(covers.length)].toString();
			gender = (relation.contains("Mother")||relation.contains("Wife") )?false:true;
			md[i].createMember(age,gender ,relation,sumInsured);
			adultMember.remove(key);
		}
		
		sumInsured = covers[rnd.nextInt(covers.length)].toString();
		
		for(int i=adults;i<(adults+minors);++i) {
			md[i] = new MemberData();
			age = rnd.nextInt(18)+1;
			gender = rnd.nextBoolean();
			relation = (gender)?"Son":"Daughter";
			md[i].createMember(age, gender, relation,sumInsured);
		}
		Object[] res = formatjSON(md,productDetail,directory);
		return res;
	}
	
	public static Object[] formatjSON(MemberData[] md,JSONObject productDetail,File directory) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		String productCode = ProductDetails.getAttribute("productCode", productDetail);
		result.put("name80D", md[0].firstName +" "+md[0].middleName +" "+ md[0].lastName);
		result.put("city", md[0].city);
		result.put("occupation", md[0].occupation);
		result.put("nomineeName", MemberData.randomString());
		String relationToNominee = MemberData.generateRelation();
		if(relationToNominee.equals("Spouse")) {
			relationToNominee = (md[0].gender.equals("Male"))?"Wife":"Husband";
		}
		result.put("relationToNominee", relationToNominee);
		result.put("proposalComplete", "Y");
		result.put("sessionId", "lqct-4qwp-1551-4272-43557");
		JSONObject proposer = new JSONObject();
		proposer = md[0].basicInfo;
		proposer.put("familySize", md.length);
		result.put("proposer", md[0].basicInfo);

		JSONArray members = new JSONArray();
		String excelEntry = "";
		for(int i=0;i<md.length;++i) {
			JSONObject Member = new JSONObject();
			Member.put("age", md[i].age);
			Member.put("sacCode", (md[i].sacCode==null)?JSONObject.NULL:md[i].sacCode);
			Member.put("clientCode", md[i].clientCode+i);
			Member.put("relation", md[i].relation);
			Member.put("occupation", md[i].occupation);
			Member.put("mdeQuestions", md[i].mdeQuestions);
			Member.put("smoker", md[i].smokerFlag);
			Member.put("basicInfo", md[i].basicInfo);
			JSONObject product = new JSONObject();
			JSONObject baseProduct = new JSONObject();
			baseProduct.put("productCode",productCode);
			baseProduct.put("productName",ProductDetails.getAttribute("productName", productDetail));
			baseProduct.put("sumInsured", md[i].sumInsured);
			String deductibleAmount = ProductDetails.getDeductableAmounts(productCode);
			baseProduct.put("deductibleAmount", ((deductibleAmount==null)?JSONObject.NULL:deductibleAmount));
			excelEntry+=md[i].age +" years,"+md[i].gender + ","+md[i].relation + ","+md[i].sumInsured+","+((deductibleAmount==null)?"":deductibleAmount)+",";			
			JSONArray riderProducts = new JSONArray();
			JSONArray products = new JSONArray();
			product.put("baseProduct", baseProduct);
			product.put("riderProducts", riderProducts);
			products.put(product);
			Member.put("products", products);
			members.put(Member);			
		}
		for(int i=md.length;i<6;++i) {
			excelEntry+=",,,,,";
		}
		
		result.put("members", members);
		result.put("mdeQuestionnaireFlag", "N");
		Object[] res = new Object[2];
		res[0]=result;
		res[1] = excelEntry;
		
		return res;
	}
}
