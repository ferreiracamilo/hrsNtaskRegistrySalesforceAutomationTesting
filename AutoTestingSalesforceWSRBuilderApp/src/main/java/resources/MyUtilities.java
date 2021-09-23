package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.AppPage;
import pages.BodyWSRFormPage;
import pages.HeaderFormPage;

public class MyUtilities {
	
	/**
	* Region Methods
	*/
	
	/**
	* Get a string value given a key from Data Properties file
	* @param key -> values admitted are user,password,url
	*/
	public static String getDataProp (String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\resources\\data.properties");
		prop.load(fis);
		
		String ret = prop.getProperty(key);
		return ret;
	}
	
	/**
	* Alternative click to be applied which is highly recommended for form pages
	* @param element Specify element to click on
	* @param driver Specify webdriver instance
	*/
	public static void moveNclick (WebElement element, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
		element.click();
	}
	
	/**
	* Method to erase text within a textfield and input a new text
	* @param textField Specify element to interact
	* @param input Specify text to input
	*/
	public static void eraseTextFieldNWrite (WebElement textField, String input) {
		textField.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
		textField.sendKeys(input);
	}
	
	/**
	* Method to erase text within a textfield and input a new text
	* @param textField Specify element to interact
	* @param input Specify text to input
	*/
	public static void eraseAllRecords(WebDriver driver){
		//Note if the account is related to other element as Contact record, the contact record will be erased
		AppPage pageTabRecords = new AppPage(driver);
		int qtyTabRecords = pageTabRecords.getThList().size();
		if(qtyTabRecords>0) {
			for (int i = qtyTabRecords; i > 0; i--) {
				WebElement btnMenuRow = pageTabRecords.getTdMenuBtnByIndex(i); 
				btnMenuRow.click();
				pageTabRecords.clickRowDelete();
				pageTabRecords.clickConfirmDelete();
				pageTabRecords.closeSuccessDialog();
			}
		}
	}
	
	/**
	* Retrieve information from excel file as a string multidimensional array
	* <br><mark>Method will accept only file .xls extension contained in files_excel folder within project</mark>
	* <br><mark>All rows must have same dimension in regards its columns</mark>
	* @param fileName without extension
	*/
	public static String [][] readXLfile (String fileName) {
		String pathFileName = System.getProperty("user.dir")+"\\files_excel\\"+fileName+".xls";
		//All excels to get data test or anything else needed will be stored within folder "excels"
		//In addition is needed to specify the name of the file
		String [][] data = resources.Excel.get(pathFileName);
		return data;
	}
	
	/**
	* Given a multidimensional array return the quantity of columns
	*/
	private static int qtyColDimArray(String [][] data) {
		int qtyColumns= data[0].length;
		return qtyColumns;
	}
	
	/**
	* Given a multidimensional array return the quantity of rows
	*/
	private static int qtyRowDimArray(String [][] data) {
		int qtyRows= data.length;
		return qtyRows;
	}
	
	/**
	* Retrieve input (test data) information related to a specific combination for a given test case contained
	* <br><mark>Take into consideration data was extracted from a properly formatted excel file following -> "Input Data Template - Body WSR" & "Input Data Template - Header" both available within resources folder</mark>
	* @param data Array obtained from readExcel
	* @param tcCombination
	* @return Hashtable with entrys for forms
	*/
	public static Map<String,String> getStepCombinationInput (String[][] data, String tcCombination) {
		Map <String,String> returnData = new Hashtable <String,String> ();
		
		for(int row =3; row < qtyRowDimArray(data); row++) {
			String tcCombxl = data[row][qtyColDimArray(data)-1].toLowerCase(); // test case id row to compare with testcase argument
			
			if(tcCombxl.equals( tcCombination.toLowerCase() ) ) {
				//If the place [row] [lastColumn] equals to our to testcase combination argument then iterate the columns
				for (int column = 1; column < qtyColDimArray(data)-1; column++) {
					//returnData.put( HEADER , VALUE )
					// HEADER Following current excel format row is 3 in excel (position 2 in array) and current column from iteration
					// VALUE Following current excel format current row and current column
					returnData.put ( data[2][column], data[row][column] );
				}
			}
		}
		return returnData;
	}
	
	/**
	* Retrieve section names assigned for each field
	* <br><mark>Take into consideration data was extracted from a properly formatted excel file following -> "Input Data Template - Body WSR" & "Input Data Template - Header" both available within resources folder</mark>
	* @param data Array obtained from readExcel
	* @return Hashtable with sections for each field from form
	*/
	public static Map<String,String> getFieldSections (String[][] data) {
		Map <String,String> returnData = new Hashtable <String,String> ();
		
		for(int column = 1; column < qtyColDimArray(data)-1; column++) {
			//start at colum B where values start and ends a column before "INSERT FIELD BETWEEN 1ST COLUMN AND CURRENT COLUMN"
			String valueSection = data[0][column]; //the actual section for respective field are placed in row 0 (at excel row 1)
			String keySection = data[2][column]; //the actual field names adding * if required are placed in row 2 (at excel row 3)
			returnData.put(keySection, valueSection);
		}
		return returnData;	
	}
	
	/**
	* Retrieve field type assigned for each field
	* <br><mark>Take into consideration data was extracted from a properly formatted excel file following -> "Input Data Template - Body WSR" & "Input Data Template - Header" both available within resources folder</mark>
	* @param data Array obtained from readExcel
	* @return Hashtable with field type for each field from form
	*/
	public static Map<String,String> getFieldTypes (String[][] data) {
		Map <String,String> returnData = new Hashtable <String,String> ();
		
		for(int column = 1; column < qtyColDimArray(data)-1; column++) {
			//start at colum B where values start and ends a column before "INSERT FIELD BETWEEN 1ST COLUMN AND CURRENT COLUMN"
			String valueFieldType = data[1][column]; //the actual section for respective field are placed in row 0 (at excel row 1)
			String keyFieldType = data[2][column]; //the actual field names adding * if required are placed in row 2 (at excel row 3)
			returnData.put(keyFieldType, valueFieldType);
		}
		return returnData;	
	}
	
	/**
	* For those values that are enclosed by '[]' a substring will be generated
	* <br><mark>This relies on the scenario that a cell from excel file contains values like -> [myInput1][myInput2][myInput3]</mark>
	* @param str
	* @return List<String>
	*/
	public static List<String>  substringsByDelimiters(String str)	{
		List<String> outputList= new ArrayList<>();
	    
		// Regex to extract the string between two delimiters
	    String regex = "\\[(.*?)\\]";
	 
	    // Compile the Regex.
	    Pattern p = Pattern.compile(regex);
	 
	    // Find match between given string and regular expression using Pattern.matcher()
	    Matcher m = p.matcher(str);
	 
	    // Get the subsequence using find() method
	    while (m.find())
	    {
	    	outputList.add(m.group(1).toString());
	    }
	    
	    return outputList;
	}
	
	/**
	* Based on a given sections and fieldTypes maps from <b>Header form<b>
	* @param sections -> This value has to match with form used in regards field names matter
	* @param fieldTypes -> This value has to match with form used in regards field names matter
	* @param form (you may use HeaderFormPage or BodyWSRFormPage)
	* @return Map containing name of field and its occurence within specified form 
	*/
	public static Map<String,Integer> countFormFields (Map<String,String> sections, Map<String,String > fieldTypes, HeaderFormPage form){
		Map <String,Integer> returnData = new Hashtable <String,Integer> ();
		
		//It will iterate through one of the maps since they're supposed to have same amount of keys as well as same keys
		for(String tupleKey : sections.keySet()) {
			// hForm.getOccurrencesField(section, label, fieldtype)
			//Label name will be equal to tupleKey
			int occurrences= form.getOccurrencesField(sections.get(tupleKey), tupleKey, fieldTypes.get(tupleKey));
			returnData.put(tupleKey, occurrences);
		}
		
		return returnData;
	}
	
	/**
	* Based on a given sections and fieldTypes maps from <b>WSR Form<b>
	* @param sections -> This value has to match with form used in regards field names matter
	* @param fieldTypes -> This value has to match with form used in regards field names matter
	* @param form (you may use HeaderFormPage or BodyWSRFormPage)
	* @return Map containing name of field and its occurence within specified form 
	*/
	public static Map<String,Integer> countFormFields (Map<String,String> sections, Map<String,String > fieldTypes, BodyWSRFormPage form){
		Map <String,Integer> returnData = new Hashtable <String,Integer> ();
		
		//It will iterate through one of the maps since they're supposed to have same amount of keys as well as same keys
		for(String tupleKey : sections.keySet()) {
			// hForm.getOccurrencesField(section, label, fieldtype)
			//Label name will be equal to tupleKey
			int occurrences= form.getOccurrencesField(sections.get(tupleKey), tupleKey, fieldTypes.get(tupleKey));
			returnData.put(tupleKey, occurrences);
		}
		
		return returnData;
	}
	
	/**
	* Based on a given fieldTypes maps from <b>WSR Form<b>
	* @param fieldTypes -> This value has to match with form used in regards field names matter
	* @param form (you may use HeaderFormPage or BodyWSRFormPage)
	* @return Map containing name of field and its webelement generated
	*/
	public static Map<String,WebElement> buildFormWebFields (Map<String,String > fieldTypes, BodyWSRFormPage form){
		Map <String,WebElement> returnData = new Hashtable <String,WebElement> ();
		
		//It will iterate through one of the maps since they're supposed to have same amount of keys as well as same keys
		for(String tupleKey : fieldTypes.keySet()) {
			// form.getField(label, fieldType)
			//Label name will be equal to tupleKey
			WebElement element = form.getField(tupleKey, fieldTypes.get(tupleKey));
			returnData.put(tupleKey, element);
		}
		return returnData;
	}
	
	/**
	* Based on a given fieldTypes maps from <b>Header Form<b>
	* @param fieldTypes -> This value has to match with form used in regards field names matter
	* @param form (you may use HeaderFormPage or BodyWSRFormPage)
	* @return Map containing name of field and its webelement generated
	*/
	public static Map<String,WebElement> buildFormWebFields (Map<String,String > fieldTypes, HeaderFormPage form){
		Map <String,WebElement> returnData = new Hashtable <String,WebElement> ();
		
		//It will iterate through one of the maps since they're supposed to have same amount of keys as well as same keys
		for(String tupleKey : fieldTypes.keySet()) {
			// form.getField(label, fieldType)
			//Label name will be equal to tupleKey
			WebElement element = form.getField(tupleKey, fieldTypes.get(tupleKey));
			returnData.put(tupleKey, element);
		}
		return returnData;
	}
	
	/**
	* Fill a Header Form
	* @param elementList -> Given a map containing {fieldName,WebElement} to reach form elements
	* @param dataInput -> Pass as argument a map {fieldName,inputData} to identify which element must receive which data
	* @param fieldTypes -> This value has to match with form used in regards field names matter
	* @param form HeaderFormPage instance use to create and interact with sublevel element as picklist options
	*/
	public static void fillFormFields (Map<String,WebElement> elementList, Map<String,String> dataInput, Map<String,String> fieldTypes, HeaderFormPage form, WebDriver driver) {
		//Keys from elementList an dataInput must match, for example
		// -> Elementlist match with headerForm and dataInput match with dataInput
		
		for(String tupleKey : elementList.keySet()) {
			if(!dataInput.get(tupleKey).isBlank()) {
				//If input data matching withfield name is not blank then search check type to proceed
				if(fieldTypes.get(tupleKey).equalsIgnoreCase("longtextfield")||fieldTypes.get(tupleKey).equalsIgnoreCase("textfield")) {
					//If field type from field is longtextfield or textfield
					//Within header form textfield and logntextfield are same tags, therefore will work the same
					elementList.get(tupleKey).sendKeys(dataInput.get(tupleKey));
				}
				if(fieldTypes.get(tupleKey).equalsIgnoreCase("lookup")) {
					//If field type from field is lookup
					moveNclick(elementList.get(tupleKey),driver);
					form.getLookupOptionByIndex(tupleKey, Integer.parseInt(dataInput.get(tupleKey))).click(); //value coming from data converted from string to int
				}	
			} //ENDS IF DATA INPUT VALUE IS NOT BLANK
		} // ENDS FOR
	}
	
	/**
	* Fill a Header Form
	* @param elementList -> Given a map containing {fieldName,WebElement} to reach form elements
	* @param dataInput -> Pass as argument a map {fieldName,inputData} to identify which element must receive which data
	* @param fieldTypes -> This value has to match with form used in regards field names matter
	* @param form BodyWSRFormPage instance use to create and interact with sublevel element as picklist options
	* @param driver
	*/
	public static void fillFormFields (Map<String,WebElement> elementList, Map<String,String> dataInput, Map<String,String> fieldTypes, BodyWSRFormPage form, WebDriver driver) {
		//Keys from elementList an dataInput must match, for example
		// -> Elementlist match with headerForm and dataInput match with dataInput
		
		for(String tupleKey : elementList.keySet()) {
			
			if(!dataInput.get(tupleKey).isBlank()) {
				//If input data matching withfield name is not blank then search check type to proceed
				
				if(fieldTypes.get(tupleKey).equalsIgnoreCase("longtextfield")) {
					//If field type from field is longtextfield, within WSR is rich text not a simple input tag
					moveNclick(elementList.get(tupleKey),driver);
					form.getLongTextEditor(tupleKey).sendKeys(dataInput.get(tupleKey)); //After clicking on the text right element will appear a p to write within which is reach through getLongTextEditor
				}
				
				if(fieldTypes.get(tupleKey).equalsIgnoreCase("lookup")) {
					//If field type from field is lookup
					//CHECK IF WORKS
					moveNclick(elementList.get(tupleKey),driver);
					form.getLookupOptionByIndex(tupleKey, Integer.parseInt(dataInput.get(tupleKey))).click(); //value coming from data converted from string to int
				}
				
				if(fieldTypes.get(tupleKey).equalsIgnoreCase("picklist")) {
					//If field type from field is calendar
					elementList.get(tupleKey);
					form.getPicklistItemByName(tupleKey, dataInput.get(tupleKey)).click(); //Click on a specific option from picklist by its inner value
				}
				
				if(fieldTypes.get(tupleKey).equalsIgnoreCase("textfield") || fieldTypes.get(tupleKey).equalsIgnoreCase("calendar")) {
					//If field type from field is picklist
					elementList.get(tupleKey).sendKeys(dataInput.get(tupleKey));
				}
			} //ENDS IF DATA INPUT VALUE IS NOT BLANK
		} // ENDS FOR
	}
	
	/**
	* Given a Map<String,String> corresponding to an input data row from excel look for a blank value and retrieve field name (key)
	* @param inputData -> This value has to match with form used in regards field names matter
	* @return fieldName which has a blank input
	*/
	public static String findKeyBlankValue (Map<String,String> inputData) {
		String keyValueEmpty = null;
		for (Map.Entry<String,String> tuple : inputData.entrySet()) {
			if(tuple.getValue().isBlank()) {
				keyValueEmpty = tuple.getKey(); //name of field is assigned to be returned
			}
		}
		return keyValueEmpty;
	}
	
	//Just for personal reference to get value selected from lookup field for example
	private static String getLookUpText (WebElement lookupEle) {
		return lookupEle.getAttribute("placeholder");
	}
	
	//Just for personal reference to get text sent via sendkeys to a textfield for example
	private static String getInputText (WebElement inputEle) {
		return inputEle.getAttribute("value");
	}
	

	
}
