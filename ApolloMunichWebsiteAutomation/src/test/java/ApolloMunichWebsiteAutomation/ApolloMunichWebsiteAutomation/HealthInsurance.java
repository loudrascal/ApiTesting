package ApolloMunichWebsiteAutomation.ApolloMunichWebsiteAutomation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class HealthInsurance {
	WebDriver driver;
	@BeforeTest
	public void launchBrowser() {
		System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
	    driver = new ChromeDriver();
	    driver.get("https://staging.apollomunichinsurance.com/");
	}
	
	
  @Test(priority = 0)
  public void VerifyTitle() throws InterruptedException {
	 String expectedtitle = "Apollo Munich | Online Health Insurance Company in India";
	 String actualtitle = driver.getTitle();
	 Assert.assertEquals(actualtitle, expectedtitle);
   
  }
  @Test(priority = 2)
  public void findBrokenLinks() throws Exception {
	  
	 int count = 0;
	  String url = "";
      HttpURLConnection huc = null;
      int respCode = 200;
      List<WebElement> links = driver.findElements(By.tagName("a"));
      Iterator<WebElement> it = links.iterator();
      while(it.hasNext()){
          url = it.next().getAttribute("href");
         if(url == null || url.isEmpty()){ // 	  System.out.println("URL is either not configured for anchor tag or it is empty");
              continue;
          }
          if(!url.contains("apollomunich")){ //      System.out.println("URL belongs to another domain, skipping it.");
              continue;
          }          
          try {
              huc = (HttpURLConnection)(new URL(url).openConnection());             
              huc.setRequestMethod("HEAD");             
              huc.connect();              
              respCode = huc.getResponseCode();             
        /*      if(respCode >= 400){
                  System.out.println(url+" is a broken link");
                  ++count;
              }
              else{  //System.out.println(url+" is a valid link");
              }
              
         */
              System.out.println("URL :"+url +"\t------- ResponseCode :" + respCode );
                  
          } catch (MalformedURLException e) {
             e.printStackTrace();
             Assert.fail();
          } catch (IOException e) {
             e.printStackTrace();
             Assert.fail();
          }
      }
      if(count>0)
    	  Assert.fail();
  }
  
  @Test(priority = 1)
  public void closeAdvertisement() {
	  try {
		  WebElement advertisment = driver.findElement(By.xpath("//iframe[contains(@id,'notification')]/parent::div[contains(@id,'notification')]"));	  
		  driver.switchTo().frame(advertisment);
		  driver.findElement(By.xpath("//*[@id='div-close']")).click();
		  }catch(NoSuchElementException | ElementNotSelectableException | ElementNotInteractableException e) {
			  System.out.println(e.toString());
		  }
  }
  
  @AfterTest
  public void closeBrowser() {
	  driver.quit();
  }
  

}
