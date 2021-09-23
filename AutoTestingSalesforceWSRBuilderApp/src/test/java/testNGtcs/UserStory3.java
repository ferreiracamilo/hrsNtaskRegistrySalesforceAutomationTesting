package testNGtcs;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import pages.AppPage;
import pages.BodyWSRFormPage;
import pages.BodyWSRRecordPage;
import pages.HeaderRecordPage;
import resources.Base;
import resources.MyUtilities;

public class UserStory3 extends Base {
	
	@Test(priority = 1, enabled=false)
	public void run_US3_TC1() {

		/* -- Handle App Page -- */
		AppPage app = new AppPage(driver);
		app.clickTabBodyWSR();
		app.waitLoadingTab(); // After clicking headers tab wait til' page is loaded
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		BodyWSRFormPage bForm = new BodyWSRFormPage(driver);
		bForm.waitLoadingForm();

		Map<String, Integer> occurrencesField = MyUtilities.countFormFields(bodyWSRsections, bodyWSRfieldTypes, bForm);
		SoftAssert softAssert = new SoftAssert();
		for (Integer occurrence : occurrencesField.values()) {
			softAssert.assertEquals((int) occurrence, 1);
		}
		softAssert.assertAll();
	}
	
	@Test(priority = 2, enabled=true)
	public void run_US3_TC2() {
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabBodyWSR();
		app.waitLoadingTab();
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		BodyWSRFormPage bForm = new BodyWSRFormPage(driver);
		bForm.waitLoadingForm();
		
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(bodyWSRInputData, "US3-TC2-1");
		Map<String,WebElement> webElements = 	MyUtilities.buildFormWebFields(bodyWSRfieldTypes, bForm);
		MyUtilities.fillFormFields(webElements, inputData, bodyWSRfieldTypes, bForm, driver);
		
		bForm.clickSave();
		
		BodyWSRRecordPage bodyRecord = new BodyWSRRecordPage(driver);
		bodyRecord.waitPageLoaded();
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(bodyRecord.countSuccessConfirmation(), 1);
		softAssert.assertAll();
	}
	
	@Test(priority = 3, enabled=true)
	public void run_US3_TC3() {
		//After US3_TC2 do not erase record and create a new record with same data
		
		/* -- Handle App Page --*/
		AppPage app = new AppPage(driver);
		app.clickTabBodyWSR();
		app.waitLoadingTab();
		app.clickNew();
		
		/* -- Create a HeaderFormPage obj and verify form loaded successfully -- */
		BodyWSRFormPage bForm = new BodyWSRFormPage(driver);
		bForm.waitLoadingForm();
		
		Map<String,String> inputData = MyUtilities.getStepCombinationInput(bodyWSRInputData, "US3-TC2-1");
		Map<String,WebElement> webElements = 	MyUtilities.buildFormWebFields(bodyWSRfieldTypes, bForm);
		MyUtilities.fillFormFields(webElements, inputData, bodyWSRfieldTypes, bForm, driver);
		
		bForm.clickSave();
		
		int qtyStopIcon = bForm.getErrorIcon().size();
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(qtyStopIcon, 1);
		softAssert.assertAll();
	}
	
	
	
	
	
	
	
	
	
}
