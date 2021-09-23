package testNGtcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.*;

import pages.AppPage;
import pages.HeaderFormPage;
import pages.HeaderRecordPage;
import pages.LandingPage;
import pages.LoginPage;
import resources.Base;
import resources.MyUtilities;

public class UserStory1 extends Base {
	
	@Test(priority = 1, enabled=false)
	public void run_US1_TC1 () {
		
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		SoftAssert softAssert = new SoftAssert();
		Map<String,Integer> occurrencesField = MyUtilities.countFormFields(headerSections,headerFieldTypes,hForm);
		//Iteration over values
		for (Integer occurrence : occurrencesField.values()) {
			softAssert.assertEquals((int)occurrence, 1);
		}
		softAssert.assertAll();
	}
	
	@Test(priority = 2,enabled = false)
	public void run_US1_TC2 () {
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		//Retrieve step combination data (containing String fieldName & String input)
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(headerInputData, "US1-TC2-1");
		
		//Build form webelements specifying field types of Header form using a Map containing (fieldname and respective fieldtype)
		Map<String,WebElement> webElements = 	MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		
		//After building webelements input data by passing arguments -> webElements built, input data retrieved, form field types, headerForm instance created
		//fieldTypes is to discriminate what to do sendkeys, click on picklist and click in value, etc
		//header form instance is used in cased is needed to create a subelement as picklist option
		MyUtilities.fillFormFields(webElements, inputData, headerFieldTypes, hForm, driver);
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(webElements.get("Header Name").getAttribute("value").indexOf("A"),-1); //indexOf will return -1 in case string is not present as it should be
		softAssert.assertEquals(webElements.get("Salesforce Manager Email").getAttribute("value").indexOf("A"),-1); //indexOf will return -1 in case string is not present as it should be
		softAssert.assertEquals(webElements.get("Vision & Values").getAttribute("value").indexOf("A"),-1); //indexOf will return -1 in case string is not present as it should be
		softAssert.assertEquals(webElements.get("Measures").getAttribute("value").indexOf("A"),-1); //indexOf will return -1 in case string is not present as it should be
		softAssert.assertEquals(webElements.get("Methods").getAttribute("value").indexOf("A"),-1); //indexOf will return -1 in case string is not present as it should be
		softAssert.assertEquals(webElements.get("Obstacles").getAttribute("value").indexOf("A"),-1); //indexOf will return -1 in case string is not present as it should be
		softAssert.assertAll();
	}
	
	@DataProvider(name = "DataProvEmails")
	public Object[][] getMails(){
		String cellMailsInputs = MyUtilities.getStepCombinationInput(headerInputData, "US1-TC3-1").get("Salesforce Manager Email"); //Retrieve mail inputs under combination "US1-TC3-1"
		List<String> mailInputs = MyUtilities.substringsByDelimiters(cellMailsInputs);
		
		Object[][] data = new Object [mailInputs.size()][1];
		
		for(int i=0;i<mailInputs.size();i++) {
			data[i][0] = mailInputs.get(i); //Iterate list and assign to a new row within data var
		}
		
		return data;
	}
	
	@Test (dataProvider="DataProvEmails",priority=3, enabled = false)
	public void run_US1_TC3 (String mailVal) {
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(headerInputData, "US1-TC3-1");
		
		Map<String,WebElement> webElements = MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		
		MyUtilities.fillFormFields(webElements, inputData, headerFieldTypes, hForm, driver);
		
		MyUtilities.eraseTextFieldNWrite(webElements.get("Salesforce Manager Email"), mailVal); //Erase value within textfield and input a new value
		hForm.clickSave();
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(hForm.getErrorIcon().size(), 1);
		softAssert.assertAll();
	}
	
	@DataProvider(name = "DataProvUS1TC4")
    public Iterator<Object[]> createDataforTestUS1TC4() {
        List<Map<String,String>> lom = new ArrayList<Map<String,String>>();
        
        for(int i=1;i<8;i++) {
        	String combinationCode = "US1-TC4-"+i;
        	lom.add(MyUtilities.getStepCombinationInput(headerInputData, combinationCode));
        	//Will be added combinations from US1-TC4-1 up to US1-TC4-7
        }
        
        Collection<Object[]> dp = new ArrayList<Object[]>();
        for(Map<String,String> map:lom){
            dp.add(new Object[]{map});
        }
        return dp.iterator();
    }
	
    @Test(dataProvider = "DataProvUS1TC4",priority=4,enabled = false)
	public void run_US1_TC4 (Map<String,String> mapData) {
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		app.clickNew();
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		String fieldBlank = MyUtilities.findKeyBlankValue(mapData);
		
		Map<String,String> inputData = mapData;
		
		Map<String,WebElement> webElements = MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		
		MyUtilities.fillFormFields(webElements, inputData, headerFieldTypes, hForm, driver);
		
		hForm.clickSave();
		
		String errorField = "noErrorDisplayed"; //Initialize the string in case there's no actual error displayed
		if(hForm.getFormErrors().size()>0) {
			errorField = hForm.getFormErrors().get(0).getText(); //In the error dialog is displayed field name to review
		}
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(errorField, fieldBlank); //Error displayed by saleforce should be equal to blank field from input data
		softAssert.assertAll();
	}
	
    @Test(priority=5,enabled = false)
	public void run_US1_TC5 () {
    	/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		app.clickNew();
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(headerInputData, "US1-TC5-1");
		Map<String,WebElement> webElements = 	MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		MyUtilities.fillFormFields(webElements, inputData, headerFieldTypes, hForm, driver);
		
		hForm.clickSave();
		
		HeaderRecordPage headerRecord = new HeaderRecordPage(driver);
		headerRecord.waitPageLoaded();
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(headerRecord.countSuccessConfirmation(), 1); //There's one sucess confirmation toast present after clicking 'Save'
		softAssert.assertAll();
		
	}
	
	
	
	
}
