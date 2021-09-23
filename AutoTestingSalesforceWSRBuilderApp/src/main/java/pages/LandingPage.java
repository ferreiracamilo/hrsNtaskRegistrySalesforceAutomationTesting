package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage {
	/**
	* Region Variables
	*/
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	@FindBy (xpath = "//button[@class='slds-button slds-show']")
	private WebElement waffleMenuButton;
	
	@FindBy (xpath = "//div[contains(@id, 'al-menu-dropdown-apps-id-')] //one-app-launcher-menu-item[.='WSR builder']")
	private WebElement waffleWSRButton;
	
	/**
	* Region Constructor
	*/
	public LandingPage(WebDriver driver) {
		this.driver=driver;
		this.wait =new WebDriverWait(this.driver, 40);
		PageFactory.initElements(driver, this);
	}
	
	/**
	* Region Getters
	*/
	
	public void clickOnWaffle () {
		waitElement(waffleMenuButton);
		waffleMenuButton.click();
	}
	
	/**
	* Before using this method is needed to click on waffle
	*/
	public void clickOnWSRApp () {
		waitElement(waffleWSRButton);
		waffleWSRButton.click();
	}
	
	/**
	* Verify record url has been loaded successfully
	*/
	public void waitLoadingLandingPage () {
		this.wait.until(ExpectedConditions.urlContains("lightning/page/home"));
	}
	
	public int bodyWSRqty () {
		int qty = driver.findElements(By.xpath("//*[.='Body WSRs']")).size();
		return qty;
	}
	
	public void waitElement (WebElement ele) {
		this.wait.until(ExpectedConditions.visibilityOf(ele));
	}
	
}
