package testNGtcs;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import pages.AppPage;
import pages.HeaderFormPage;
import pages.HeaderRecordPage;
import resources.Base;
import resources.MyUtilities;

public class UserStory2 extends Base{
	
	@Test(priority=1,enabled = false)
	public void run_US2_TC1 () {
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		MyUtilities.eraseAllRecords(driver); //All records created will be erased
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		/* --- A header record is created with full data --- */
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(headerInputData, "US1-TC5-1");
		Map<String,WebElement> webElements = 	MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		MyUtilities.fillFormFields(webElements, inputData, headerFieldTypes, hForm, driver);
		hForm.clickSave();
		
		/* --- Create a headerRecord obj and wait until is loaded the header record page --- */
		HeaderRecordPage headerRecord = new HeaderRecordPage(driver);
		headerRecord.waitPageLoaded();
		headerRecord.closeToast();
		
		/* Access again the headers tab page */
		app.clickTabHeaders();
		app.waitLoadingTab();
		app.clickRowArrow(1); //Since it's ensured there's only one record arrow menu button will be clicked based on html doc's index
		app.clickRowEdit();
		
		/* Handle the form displayed from record created */
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		String userOptionSelected = hForm.getField("User", "lookup").getAttribute("placeholder"); //Get current status of user field webelement placeholder
		hForm.eraseLookupValue("User");
		int userOptionsQTY = hForm.getLookupOptions("User").size(); 
		hForm.clickCancel();
		
		/* --- Get current session's user name --- */
		app = new AppPage(driver);
		app.waitLoadingTab();
		app.clickProfileButton();
		String userName = app.getProfileName();
		
		/* --- Assure session's user name match with userSelected from record created and that quantity user lookup options are equal to one only --- */
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(userOptionsQTY, 1);
		softAssert.assertEquals(userOptionSelected, userName); 
		softAssert.assertAll();
	}
	
	@Test(priority=2,enabled = false)
	public void run_US2_TC2 () {
		
		/* Header 1 */
		
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabHeaders();
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		MyUtilities.eraseAllRecords(driver);
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		HeaderFormPage hForm = new HeaderFormPage(driver);
		hForm.waitLoadingForm(); //wait til' form page is loaded successfully
		
		/* -- Input data to all fields and save the record --*/
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(headerInputData, "US2-TC2-1");
		Map<String,WebElement> webElements1 = MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		MyUtilities.fillFormFields(webElements1, inputData, headerFieldTypes, hForm, driver);
		hForm.clickSave();
		
		/* -- Close toast with success notification in header record page -- */
		HeaderRecordPage hrp = new HeaderRecordPage(driver);
		hrp.closeToast(); //Close confirmation toast
		app.clickTabHeaders();
		
		/* Header 2 */
		app.waitLoadingTab(); //After clicking headers tab wait til' page is loaded
		app.clickNew();
		Map<String,WebElement> webElements2 = MyUtilities.buildFormWebFields(headerFieldTypes, hForm);
		MyUtilities.fillFormFields(webElements2, inputData, headerFieldTypes, hForm, driver);
		hForm.clickSave();
		
		/* --- Assure session's user name match with userSelected from record created and that quantity user lookup options are equal to one only --- */
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(hForm.getErrorIcon().size(), 1); //Stop icon must be present in html doc and displayed, therefore size should be equal to 1. Otherwise system created a second header record not complying with business rules
		softAssert.assertAll();
	}
	
}
