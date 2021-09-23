package resources;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.testng.annotations.*;

import pages.AppPage;
import pages.LandingPage;
import pages.LoginPage;

public class Base {
	public WebDriver driver;
	
	protected String [][] headerInputData;
	protected String [][] bodyWSRInputData;
	protected Map<String,String> headerSections;
	protected Map<String,String> headerFieldTypes;
	protected Map<String,String> bodyWSRsections;
	protected Map<String,String> bodyWSRfieldTypes;
		
	 /**
	* Initialize driver
	* @param browserName // values -> chrome,firefox,opera
	*/
	public WebDriver initializeDriver(String browserName) throws IOException {

		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\resources\\chromedriver.exe"); //drivertype and driver exe location
			
			//Set option to take out notification
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			
			driver = new ChromeDriver(options); //create chromedriver using as argument options created
		} else if (browserName.equals("firefox")) {
			
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\resources\\geckodriver.exe"); //drivertype and driver exe location
			
			//Set option to take out notification
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(new FirefoxProfile());
			options.addPreference("dom.webnotifications.enabled", false);
			
			driver = new FirefoxDriver(options); //create firefox driver using as argument options created
		} else if (browserName.equals("opera")) {
			System.setProperty("webdriver.opera.driver", System.getProperty("user.dir")+"\\resources\\operadriver.exe"); //drivertype and driver exe location
			
			//Set option to take out notification
			OperaOptions options = new OperaOptions();
			options.addArguments("--disable-notifications");
			
			driver = new OperaDriver(options); //create opera driver using as argument options created
		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		return driver;

	}
	
	/*
	public void getScreenshot(String result) throws IOException
	{
		//To be implemented
		File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File("C://test//"+result+"screenshot.png")); //to be decided location to save screenshots in case is done
	}
	*/
	
	@BeforeSuite
	public void loadGeneralTestData () throws Throwable{
		//Load information from excel files
		headerInputData = MyUtilities.readXLfile("headersInputData");
		bodyWSRInputData = MyUtilities.readXLfile("bodyWSRdata");
		
		//Load section names and field types from header form information
		headerSections = MyUtilities.getFieldSections(headerInputData);
		headerFieldTypes = MyUtilities.getFieldTypes(headerInputData);
		
		//Load section names and field types from header form information
		bodyWSRsections = MyUtilities.getFieldSections(bodyWSRInputData);
		bodyWSRfieldTypes = MyUtilities.getFieldTypes(bodyWSRInputData);
		
		//There's no need to intialize driver to retrieve data from excel file within local folder
	}
	
	@BeforeMethod
	public void a_initializeBrowser() throws Throwable {
		driver = initializeDriver("chrome");
	}
	
	@BeforeMethod
	public void b_logToAccountNaccesApp () throws Throwable {
		
		//Access login salesforce web
		driver.get(MyUtilities.getDataProp("url"));
				
		//Handle LoginPage and log in to user account
		LoginPage loginp = new LoginPage(driver);
		loginp.loginToAccount(MyUtilities.getDataProp("user") , MyUtilities.getDataProp("password") );
		
		//Handle LandingPage
		LandingPage landingp = new LandingPage(driver);
		landingp.waitLoadingLandingPage();
		if(landingp.bodyWSRqty()<1) {
			//Based on community salesforce was identified there're occasions default app won't be used on start, instead last app used
			//https://trailblazer.salesforce.com/ideaView?id=08730000000ktQ7AAI
			landingp.clickOnWaffle(); //get app menu
			landingp.clickOnWSRApp(); //app WSR Builder is clicked
			
			//After clicking the app button assure new page has loaded successfully
			AppPage app = new AppPage(driver);
			app.waitLoadingTab(); //wait until tab is app is loaded
		}
	}
	
	@AfterMethod
	public void closeBrowser () {
		driver.quit();
	}
	
}

