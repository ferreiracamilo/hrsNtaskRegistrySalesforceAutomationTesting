package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HeaderFormPage {
	
	/**
	* Region Variables
	*/
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	//Get section container based section name
	private static final String sectionContainer = "//h3 //span[.='$SectionName'] //ancestor::force-record-layout-section";
	
	//Interactable elements level 1
	private static final String lvl1formLookup = "//label[text()='$LabelName'] /following-sibling::div //lightning-primitive-icon //*[name()='svg' and @data-key='search' or @data-key='close'] //ancestor::lightning-base-combobox //input";
	private static final String lvl1formTextField = "//label[text()='$LabelName'] /following-sibling::div //input"; 
	private static final String lvl1formLongTextField = "//label[text()='$LabelName'] /following-sibling::div //textarea";
	
	//Interactable elements level 2, depends on clicking a Level 1 Element
	private static final String lvl2formLookUpErase = "//label[text()='$LabelName'] /following-sibling::div //lightning-primitive-icon //*[name()='svg' and @data-key='close'] //ancestor::button"; //In order to clear value selected is needed to click button containing svg 'X'
	private static final String lvl2formLookupOptionByName = "//label[text()='$LabelName'] /following-sibling::div //li //lightning-base-combobox-item /descendant-or-self::span[@class='slds-truncate' and .='$OptionName'][1]";
	private static final String lvl2formLookupOptions = "//label[text()='$LabelName'] /following-sibling::div //li //lightning-base-combobox-item /descendant-or-self::span[@class='slds-truncate'][1]";
	private static final String lvl2formLookupOptionByIndex = "//label[text()='$LabelName'] /following-sibling::div //li //lightning-base-combobox-item [$Index]"; 
	
	@FindBy(xpath="//div[contains(@class, 'windowViewMode-normal')] //button[@name='SaveEdit' or @title='Save']")
	  private WebElement formBtnSave;
	
	@FindBy(xpath="//div[contains(@class, 'windowViewMode-normal')] //button[@name='CancelEdit']")
	  private WebElement formBtnCancel;
	
	//In case qty is below than 1 means record created or edited successfully
	@FindBy(xpath="//div[contains(@class, 'windowViewMode-normal')] //button[@name='SaveEdit' or @title='Save']")
	  private List<WebElement> qtyBtnSave;
	
	@FindBy(xpath="//button[@title=\"Close error dialog\"]")
	  private WebElement btnCloseErrorDialog; //Every time there's an error met by filling the form this button appears
	
	@FindBy(xpath="//button[contains(@class,'page-error-button')]")
	  private List <WebElement> formErrorIcon; //Every time there's an error met by filling the form this icon would turn visible
	
	@FindBy(xpath="//force-record-edit-error //ul //li //a")
	  private List <WebElement> listErrorFields;
	
	/**
	* Region Constructor
	*/
	public HeaderFormPage (WebDriver driver) {
		this.driver=driver;
		this.wait =new WebDriverWait(this.driver, 40);
		PageFactory.initElements(driver, this); //supress if pagefactory is not used
	}
	
	/**
	* Region Methods
	*/
	
	public void clickSave () {
		waitElement(formBtnSave);
		formBtnSave.click();
	}
	
	public void clickCancel () {
		waitElement(formBtnCancel);
		formBtnCancel.click();
	}
	
	public void closeErrorDialog() {
		waitElement(btnCloseErrorDialog);
		btnCloseErrorDialog.click();
	}
	
	public void eraseLookupValue(String labelName) {
		WebElement lookupFieldXbutton = driver.findElement(By.xpath(lvl2formLookUpErase.replace("$LabelName", labelName)));
		lookupFieldXbutton.click();
	}
	
	/**
	* After clicking on 'Save' if there's an issue or empty required field system will show on a ul list each field to review
	*/
	public List<WebElement> getFormErrors (){
		return listErrorFields;
	}
	
	/**
	* After clicking on 'Save' if there's an issue or empty required field system will show a stop icon
	*/
	public List<WebElement> getErrorIcon () {
		return formErrorIcon;
	}
	
	/**
	* Get a webelement field from form under a specific section, labelname and type
	* @param sectionName Specifiy name of the section that contains the field you want to reach
	* @param labelName Specifiy name of the label above the field you want to reach
	* @param fieldType Specify field type from following values: 
	* 									  <mark>lookup,textfield,longtextfield</mark>
	* @return WebElement will be <b>null</b> if <b>fieldType, sectionName or labelName don't match to an actual element from form</b>
	*/
	public WebElement getField (String labelName, String fieldType) {
		WebElement element = null;
		String typeLow = fieldType.trim().toLowerCase();
		String path;
		switch(typeLow) {
			case "lookup":
				path = lvl1formLookup.replace("$LabelName", labelName);
				element = driver.findElement(By.xpath(path));
				break;
			case "textfield":
				path = lvl1formTextField.replace("$LabelName", labelName);
				element = driver.findElement(By.xpath(path));
				break;
			case "longtextfield":
				path = lvl1formLongTextField.replace("$LabelName", labelName);
				element = driver.findElement(By.xpath(path));
				break;
		}
		return element;
	}
	
	/**
	* Get occurrences of a webelement field from form under a specific section, labelname and type
	* @param sectionName Specifiy name of the section that contains the field you want to reach
	* @param labelName Specifiy name of the label above the field you want to reach
	* @param fieldType Specify field type from following values: 
	* 									  <mark>lookup,textfield,longtextfield</mark>
	* @return WebElement will be <b>null</b> if <b>fieldType, sectionName or labelName don't match to an actual element from form</b>
	*/
	public int getOccurrencesField (String sectionName, String labelName, String fieldType) {
		int qty = 0;
		String typeLow = fieldType.trim().toLowerCase();
		String path;
		switch(typeLow) {
			case "lookup":
				path = sectionContainer.replace("$SectionName",sectionName) + lvl1formLookup.replace("$LabelName", labelName);
				qty = driver.findElements(By.xpath(path)).size();
				break;
			case "textfield":
				path = sectionContainer.replace("$SectionName",sectionName) + lvl1formTextField.replace("$LabelName", labelName);
				qty = driver.findElements(By.xpath(path)).size();
				break;
			case "longtextfield":
				path = sectionContainer.replace("$SectionName",sectionName) + lvl1formLongTextField.replace("$LabelName", labelName);
				qty = driver.findElements(By.xpath(path)).size();
				break;
		}
		return qty;
	}
	
	/**
	* Bare in mind after you click save and record is created/edited sucessfully, size would be less than zero
	*/
	public int getBtnSaveQty () {
		return qtyBtnSave.size();
	}
	
	/**
	* Get webelement for a specific option available to select from a Lookup field by its inner text
	* <mark>It is necessary to click  the lookup field before using this method</mark>
	* @param labelName specify field label's name
	* @param optionText Specifiy index of lookup option to reach
	* @return WebElement will be <b>null</b> if <b>labelName doesn't match to an actual element from form</b>
	*/
	public WebElement getLookupOptionByInnerText (String labelName, String optionText) {
		String path = lvl2formLookupOptionByName;
		path = path.replace("$LabelName", labelName);
		path = path.replace("$OptionName", optionText);
		WebElement element = driver.findElement(By.xpath( path ));
		return element;
	} 
	
	/**
	* Get webelement for a specific option available to select from a Lookup field by its index
	* <mark>It is necessary to click  the lookup field before using this method</mark>
	* @param labelName specify field label's name
	* @param index Specifiy index of lookup option to reach
	* @return WebElement will be <b>null</b> if <b>labelName doesn't match to an actual element from form</b>
	*/
	public WebElement getLookupOptionByIndex (String labelName, int index) {
		String path = lvl2formLookupOptionByIndex;
		path = path.replace("$LabelName", labelName);
		path = path.replace("$Index", Integer.toString(index));
		
		WebElement element = driver.findElement(By.xpath( path ));
		return element;
	}
	
	/**
	* Get options available to select from a Lookup field
	* <mark>It is necessary to click  the lookup field before using this method</mark>
	* @param labelName specify field label's name
	* @return List<WebElement> will be <b>null</b> if <b>labelName doesn't match to an actual element from form</b>
	*/
	public List<WebElement> getLookupOptions(String labelName){
		List <WebElement> webList = driver.findElements(By.xpath(lvl2formLookupOptions.replace("$LabelName", labelName)));
		return webList;
	}
	
	/**
	* Verify record url has been loaded successfully
	*/
	public void waitLoadingForm () {
		this.wait.until(ExpectedConditions.urlContains("FHeader__c%2Flist%3FfilterName"));
		waitElement(formBtnSave); //Since the url is not sufficient to ensure form is loaded, is waiting also for save button
	}
	
	public void waitElement (WebElement ele) {
		this.wait.until(ExpectedConditions.visibilityOf(ele));
	}
	
}
