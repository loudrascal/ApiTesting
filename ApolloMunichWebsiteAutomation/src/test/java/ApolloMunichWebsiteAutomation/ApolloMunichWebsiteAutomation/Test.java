package ApolloMunichWebsiteAutomation.ApolloMunichWebsiteAutomation;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws Exception {
        String url = "https://staging.apollomunichinsurance.com";
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
        	String result = link.absUrl("href");
        	if(result.equals(""))
        		continue;
        	if(!result.contains("apollomunich"))
        		continue;
        	if(result.contains("localhost:4200")) {
        		result = result.replaceAll("localhost:4200", url.replace("https://", ""));
        	}
        	System.out.println(result);
        }
    }

 

}
	
	


