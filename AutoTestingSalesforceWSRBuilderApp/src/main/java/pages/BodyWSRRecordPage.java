package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BodyWSRRecordPage {
	
	/**
	* Region Variables
	*/
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	@FindBy (xpath = "//button[.='Submit to Manager']")
	private WebElement btnSubmitToManager;
	
	@FindBy (xpath = "//button[.='Save']")
	private WebElement btnSave;
	
	@FindBy (xpath = "//div[contains(@class,'toastContainer')] //div[@role='alert'] //button")
	private WebElement btnToastClose;
	
	@FindBy (xpath = "//div[contains(@class,'toastContainer')] //div[@role='alert'] //span[contains(@class,'toastMessage')]")
	private WebElement spanToastMsg;
	
	@FindBy (xpath = "//div[@data-key='success']")
	private List<WebElement> successConfirmation;
	
	/**
	* Region Constructor
	*/
	public BodyWSRRecordPage (WebDriver driver) {
		this.driver=driver;
		this.wait =new WebDriverWait(this.driver, 40);
		PageFactory.initElements(driver, this);
	}
	
	/**
	* Region Methods
	*/
	
	public int countSuccessConfirmation () {
		return successConfirmation.size();
	}
	
	/**
	* Verify record url has been loaded successfully
	*/
	public void waitLoadingPage () {
		//this.wait.until(ExpectedConditions.urlContains("?"));
		this.wait.until(ExpectedConditions.and(ExpectedConditions.urlContains("/lightning/r/Body_WSR__c/"),ExpectedConditions.urlContains("/view")));
	}
	
	public void clickSubmitToManager() {
		waitElement(btnSubmitToManager);
		btnSubmitToManager.click();
	}
	
	/**
	* After clicking on Submit To Manager button is needed to confirm action
	*/
	public void clickSave() {
		waitElement(btnSave);
		btnSave.click();
	}
	
	public void closeToast () {
		waitElement(btnToastClose);
		btnToastClose.click();
	}
	
	public String getToastText () {
		return spanToastMsg.getText();
	}
	
	public void waitElement (WebElement ele) {
		this.wait.until(ExpectedConditions.visibilityOf(ele));
	}
	
	public void waitPageLoaded() {
		this.wait.until(ExpectedConditions.urlContains("view"));
		this.wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//button[@name='Edit']"), 0)); //Edit button has to be present at least once
	}
	
}
