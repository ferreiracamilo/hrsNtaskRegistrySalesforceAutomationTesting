package pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppPage {
	/**
	* Region Variables
	*/
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	private static final String xpathRecord = "(//table[@role='grid'] /tbody /tr /th /span //a[.='$recordName])[$Index]";
	private static final String xpathRecordMenuByTHname = "(//table[@role='grid'] /tbody /tr /th /span //a[.='$InnerText'] /ancestor::tr /td[last()])[$Index]";
	private static final String xpathRecordMenuByIndex = "(//table[@role='grid'] /tbody /tr /th /span //a /ancestor::tr /td[last()])[$Index]";
	private static final String xpathOneTab = "//one-app-nav-bar-item-root[@data-assistive-id='operationId'] //a[contains(@href,'/lightning/') and @tabindex='0'] //span[.='$TabName'] //ancestor::one-app-nav-bar-item-root";
	
	private static final String tdMenuBtnByIndex = "(//table[@role='grid'] //tbody //tr //th //span //a /ancestor::tr /td[last()])[$Index]";
	
	@FindBy (xpath = "//span[contains(@class,'userProfileCardTriggerRoot')] //button //ancestor::li")
	private WebElement btnUserProfile;
	
	@FindBy (xpath = "//h1[@class='profile-card-name'] //a[@class='profile-link-label']")
	private WebElement userProfileName;
	
	@FindBy (xpath = "//div[@title='New']")
	private WebElement btnNew;
	
	@FindBy (xpath = "//div[contains(@class,'visible')] //div[@role='menu'] //div[@title='Edit'] /ancestor::a")
	private WebElement btnRowEdit;
	
	@FindBy (xpath = "//div[contains(@class,'visible')] //div[@role='menu'] //div[@title='Delete'] /ancestor::a")
	private WebElement btnRowDelete;
	
	@FindBy (xpath = "//one-app-nav-bar-item-root[@data-assistive-id='operationId'] //a[contains(@href,'/lightning/') and @tabindex='0'] //span[.='Body WSRs'] //ancestor::one-app-nav-bar-item-root")
	private WebElement btnTabBodyWSR;
	
	@FindBy (xpath = "//one-app-nav-bar-item-root[@data-assistive-id='operationId'] //a[contains(@href,'/lightning/') and @tabindex='0'] //span[.='Headers'] //ancestor::one-app-nav-bar-item-root")
	private WebElement btnTabHeaders;
	
	@FindBy (xpath = "//table[@role='grid'] /tbody /tr /th /span //a")
	private List<WebElement> thList;
	
	//If you request to delete a record account pop up will show displaying this button
	@FindBy (xpath = "//div[@class='modal-footer slds-modal__footer'] //button[.='Delete']")
	private WebElement confirmDeleteBTN;
	
	//After editing, creating, deleting (after clicking on confirmDeleteBTN) is needed to close success confirmation dialog
	@FindBy (xpath = "//div[contains(@class, 'slds-theme--success slds-notify')] //button[contains(@class,'slds-button slds-button_icon toastClose')]")
	private WebElement closeConfirmSucessBTN;
	
	/**
	* Region Constructor
	*/
	public AppPage (WebDriver driver) {
		this.driver=driver;
		this.wait = new WebDriverWait(this.driver, 40);
		PageFactory.initElements(driver, this);
	}
	
	/**
	* Region Methods
	*/
	
	public void clickProfileButton () {
		waitElement(btnUserProfile);
		btnUserProfile.click();
	}
	
	/**
	* <mark> It's needed to click on profile button before using this method <mark>
	*/
	public String getProfileName () {
		waitElement(userProfileName);
		return userProfileName.getText();
	} 
	
	/**
	* Get a unique menu button under row by order (index) in html doc
	* <br><b>You must access a tab before using this method and should have some record available already</b>
	* @param index  -> Starts by 1
	*/
	public WebElement getTdMenuBtnByIndex (int index) {
		WebElement ret = null;
		String path = tdMenuBtnByIndex.replace("$Index", Integer.toString(index));
		ret = driver.findElement(By.xpath(path));
		return ret;
	}
	
	/**
	* This method will retrieve all Th tags present within tab's table
	* <br>Take into consideration for example it's possible to create two account records under same AccountName
	* <br><b>You must access a tab before using this method and should have some record available already</b>
	*/
	public List<WebElement> getThList (){
		return thList;
	}
	
	/**
	* Execute method to click on button 'New'
	*/
	public void clickNew() {
		waitElement(btnNew);
		btnNew.click();
	}
	
	/**
	* Execute method to click on tab 'BodyWSR'
	*/
	public void clickTabBodyWSR() {
		waitElement(btnTabBodyWSR);
		btnTabBodyWSR.click();
	}
	
	/**
	* Execute method to click on tab 'Headers'
	*/
	public void clickTabHeaders() {
		waitElement(btnTabHeaders);
		btnTabHeaders.click();
	}
	
	/**
	* Before using this method is needed to click on arrow menu button at row level
	*/
	public void clickRowEdit() {
		waitElement(btnRowEdit);
		btnRowEdit.click();
	}
	
	/**
	* Before using this method is needed to click on arrow menu button at row level
	*/
	public void clickRowDelete() {
		waitElement(btnRowDelete);
		btnRowDelete.click();
	}
	
	/**
	* Before using this method is needed to click on Delete in arrow menu button at row level
	*/
	public void clickConfirmDelete() {
		waitElement(confirmDeleteBTN);
		confirmDeleteBTN.click();
	}
	
	/**
	* After confirming a delete request a dialog shows up, with this method you're able to close dialog
	*/
	public void closeSuccessDialog() {
		waitElement(closeConfirmSucessBTN);
		closeConfirmSucessBTN.click();
	}
		
	/**
	* Click on arrow button at row level of record you specify by its name
	* @param recordName refers to record name (tag a)
	* @param index based on recordName occurences within table, starting by 1
	*/
	public void clickRowArrow (String recordName, int index) {
		String path = xpathRecordMenuByTHname.replace("$InnerText", recordName);
		path = path.replace("$Index", Integer.toString(index));
		
		driver.findElement(By.xpath(path)).click();
	}
	
	/**
	* Click on arrow button at row level of record you specify by its index on the html document
	* @param index based on recordName occurences within table, starting by 1
	*/
	public void clickRowArrow (int index) {
		String path = xpathRecordMenuByIndex.replace("$Index", Integer.toString(index));
		driver.findElement(By.xpath(path)).click();
	}
	
	/**
	* Click on record link (tag a)
	* @param recordName refers to record name (tag a)
	* @param index based on recordName occurences within table, starting by 1
	*/
	public void clickTableRecord (String recordName, int index) {
		String path = xpathRecord.replace("$InnerText", recordName);
		path = path.replace("$Index", Integer.toString(index));
		
		driver.findElement(By.xpath(path)).click();
	}
	
	/**
	* Verify tab within app url has loaded successfully 
	* Take in consideration after clicking an app usually gets open a default tab called Home, therefore this method works also to check if app has loaded
	*/
	public void waitLoadingTab () {
		//this.wait.until(ExpectedConditions.urlContains("?"));
		this.wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("?"),ExpectedConditions.urlContains("/lightning/page/home")));
	}
	
	public void waitElement (WebElement ele) {
		this.wait.until(ExpectedConditions.visibilityOf(ele));
	}
	
	public void waitRecordList () {
		this.wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//table[@role='grid'] /tbody /tr /th /span //a']"), 0)); //Edit button has to be present at least once
	}
	
	
	
}
