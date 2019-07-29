package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class MemberData {
	String 	firstName,middleName,lastName,email,title,memberType,gender,relation,maritalStatus,gstinNumber,clientCode,
			city,state,pincode,country, occupation,birthDate,addressline1,addressLine2,addressLine3,sacCode=null,sumInsured,
			isChecked,mqField,name,description,smoke,gutka,alcohol,hazardousOccupation,medicalSubStandardDecision;
	long 	phone;
	int 	height,weight,annualIncome,age,question_id;
	boolean smokerFlag = false;
	static 	Random 	rnd		= new Random();
	JSONObject address,contactInformation,idProof,mdeQuestions,basicInfo,question;
	JSONArray questionList,subQuestions;
	
	public static String generateRelation()
	{
		String relations[] = {"Mother","Father","Father-in-law","Mother","Mother-in-law","Spouse"};
		String relation = relations[rnd.nextInt(relations.length)];
		return relation;
	}
	public void createMember(int age,boolean gender,String relation,String sumInsured ) {
		firstName 			= randomString();
		middleName 			= randomString();
		lastName 			= randomString();
		email 				= firstName+lastName+"@"+randomString()+"."+randomString();
		title 				= (gender)?"Mr":"Mrs";
		memberType 			= (age>18)?"ADULT":"CHILD";
		this.gender 		= (gender)?"Male":"Female";
		this.relation	 	= relation;
		maritalStatus		= (age>18)?((rnd.nextBoolean())?"Married":"Single"):"Single";
		gstinNumber 		= "";
		clientCode			= "ClientCode";
		try {
			URL path 				= this.getClass().getResource("City.csv");
			FileReader filereader	= new FileReader(new File(path.getFile())); 
			CSVReader csvReader 	= new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			List<String[]> allData 	= csvReader.readAll(); 
			String[] row 			= allData.get(rnd.nextInt(allData.size()));
	        city 	= row[0];
	        state 	= row[1]; 
	        pincode = row[2];
	        country = row[3];
		}catch(Exception e) { 
	        city 	= "delhi";
	        state 	= "delhi";
	        pincode = "110003";
	        country = "India";
		} 
		smokerFlag = (age>18)?rnd.nextBoolean():false;
		//occupation 		= "OCCUPATIONCLASS_"+(rnd.nextInt(3)+1);
		occupation 		= "OCCUPATIONCLASS_1";
		int DOM			= LocalDate.now().getDayOfMonth();
		String dayM		= (DOM<10)?("0"+DOM):(DOM+"");
		int MON			= LocalDate.now().getMonthValue();
		String getM		= (MON<10)?("0"+MON):(MON+"");
		birthDate 		= dayM+"/"+getM+"/"+(LocalDate.now().getYear()-age);
		addressline1 	= "House no :" + randomNumber(3, rnd.nextInt(10)) +","+ " Block :" + randomNumber(2, rnd.nextInt(10)) + "," ;
		addressLine2 	= randomString() +" Society, " + randomString()+" Nagar";
		addressLine3 	= "Area :" + randomString() + ", "+city ;

		phone 			= randomNumber(10, rnd.nextInt(3)+3);
		int num 		= rnd.nextInt(100);
		height 			= (age>14)?(num + 100):(30 + age*12);
		int bmi 		= rnd.nextInt(40)+10; // need to get boundary vakue
		weight 			= height*height*bmi/10000;
		annualIncome 	= (int) randomNumber(7, rnd.nextInt(2));
		this.age		= age;
		this.sumInsured = sumInsured;
		
		address = new JSONObject();
		address.put("addressLine1", addressline1);
		address.put("addressLine2", addressLine2);
		address.put("addressLine3", addressLine3);
		address.put("city", city);
		address.put("state", state);
		address.put("pincode", pincode);
		address.put("country", country);
		
		contactInformation = new JSONObject();
		contactInformation.put("email",email);
		contactInformation.put("number",phone);
		contactInformation.put("contactType","MOBILE");
		
		idProof = new JSONObject();
		idProof.put("proofNumber", "WSDEP1111J");
		idProof.put("proofTypeEnum", "PAN");
		
		basicInfo = new  JSONObject();
		basicInfo.put("age",this.age);
		basicInfo.put("sacCode", JSONObject.NULL);
		basicInfo.put("firstName", firstName);
		basicInfo.put("middleName", middleName);
		basicInfo.put("lastName", lastName);
		basicInfo.put("gender", this.gender);
		basicInfo.put("title", title);
		basicInfo.put("memberType", memberType);
		basicInfo.put("smokerFlag", smokerFlag);
		basicInfo.put("occupation", occupation);
		basicInfo.put("dateOfBirth", birthDate);
		basicInfo.put("birthDate", birthDate);
		basicInfo.put("clientCode", clientCode+"1");
		basicInfo.put("relation", relation);
		basicInfo.put("relationship", relation);
		basicInfo.put("address", address);
		basicInfo.put("contactInformation", contactInformation);
		basicInfo.put("maritalStatus", maritalStatus);
		basicInfo.put("idProof", idProof);
		basicInfo.put("annualIncome", annualIncome);
		basicInfo.put("height", height);
		basicInfo.put("weight", weight);
		basicInfo.put("product", JSONObject.NULL);
		basicInfo.put("gstinNumber", gstinNumber);
		
		questionList = new JSONArray();
		for(int i=1;i<=12;++i) {
			question = new JSONObject();
			subQuestions = new JSONArray();
			question.put("question_id", i);
			question.put("isChecked","N");
			question.put("mqField", "");
			question.put("name",JSONObject.NULL );
			question.put("description", JSONObject.NULL);
			question.put("subQuestions", subQuestions);
			questionList.put(question);
		}
		smoke = "No-No-No";
		gutka = "No-No-No";
		alcohol = "No-No-No";
		hazardousOccupation = "No";
		medicalSubStandardDecision = "Y";
		if(age>20) {
			boolean habbit = rnd.nextBoolean();
			if(habbit) {
				smoke 	= "Yes-"+(rnd.nextInt(12)+1)+"-"+(LocalDate.now().getYear()-age+18);
			}
			habbit = rnd.nextBoolean();
			if(habbit) {
				gutka 	= "Yes-"+(rnd.nextInt(12)+1)+"-"+(LocalDate.now().getYear()-age+18);
			}
			habbit = rnd.nextBoolean();
			if(habbit) {
				alcohol = "Yes-"+(rnd.nextInt(12)+1)+"-"+(LocalDate.now().getYear()-age+18);
			}
		}
		mdeQuestions = new JSONObject();
		mdeQuestions.put("smoke", smoke);
		mdeQuestions.put("gutka", gutka);
		mdeQuestions.put("alcohol", alcohol);
		mdeQuestions.put("hazardousOccupation", hazardousOccupation);
		mdeQuestions.put("medicalSubStandardDecision", medicalSubStandardDecision);
		mdeQuestions.put("questionList", questionList);
	}
	
	public static String randomString() {
		String random 	= "";		
		int length 		= rnd.nextInt(6)+3;
		for(int i=0;i<length;++i) {
			char c 		= (char)(rnd.nextInt(26)+97);
			random+=c;
		}
		return random;
	}
	
	private static long randomNumber(int length, int StartingDigit) {
		// TODO Auto-generated method stub
		String number = ""+StartingDigit;
		
		for(int i=1;i<length;++i) {
			number 	 += rnd.nextInt(10);
		}
		long num 	  = Long.parseLong(number);
		return num;
	}
	
	public String toString() {
		String memberInfo = "";
		
		return memberInfo;
	}
}
