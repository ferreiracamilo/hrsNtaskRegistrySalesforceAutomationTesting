package pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import resources.MyUtilities;

public class LoginPage {
	
	/**
	* Region Variables
	*/
	
	private WebDriver driver;
	
	@FindBy (xpath = "//input[@id='username']")
	private WebElement username;
	
	@FindBy (xpath = "//input[@id='password']")
	private WebElement password;
	
	/**
	* Region Constructor
	*/
	public LoginPage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	* Region Methods
	*/
	
	/**
	* Execute method to logged into Salesforce passing user's credentials
	* @param userName
	* @param userPassword
	*/
	public void loginToAccount (String userName, String userPassword) throws IOException {
		
		username.sendKeys(userName);
		password.sendKeys(userPassword);
		password.submit();
	}
	
}
