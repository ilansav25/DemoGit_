package tests;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pages.Cart;
import pages.Main;
import pages.Registration;
import utilites.GetDriver;
import utilites.Utilities;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;


public class SuperPharm_Sanity_Registration_ErrorHandling {

	// Global variables 
	// Add extent reports
	private ExtentReports extent;
	private ExtentTest myTest;
	private static String reportPaht = System.getProperty("user.dir") + "\\test-output\\SuperPharm_Registration.html";

	private WebDriver driver;
	private String baseUrl;
	
	
	//pages
	private Main main;
	private Registration registration;
	

	@BeforeClass
	public void beforeClass() {
		extent = new ExtentReports(reportPaht);
		extent.loadConfig(new File(System.getProperty("user.dir") + "\\resources\\superpharm-extent-config.xml"));
		baseUrl = "https://shop.super-pharm.co.il/";
		driver = GetDriver.getDriver("chrome", baseUrl);
		main = new Main(driver);
		registration = new Registration(driver);
	}

	
	
	@BeforeMethod
	public void beforeMethod(Method method) throws IOException {
		myTest = extent.startTest(method.getName());
		myTest.log(LogStatus.INFO, "Starting test", "Start test");
	}
	
	/*
	Prerequisite: get to site without login
	
	Given: getting to register page 
	When: User click field of first name and get to next field without writing text 
	Then: Error message appear: נא למלא שדה זה
	*/
	
	@Test(priority = 1, enabled = true, description = "check error message text: first name")
	public void verifyFirstNameErrorMsg() throws InterruptedException, IOException {	
		main.goRegistration();
		Assert.assertTrue(registration.verifyErrorsInRegistration_FirstName("נא למלא שדה זה"), "Could not verify error message for first name field");
	
	}
	
	@Test(priority = 2, enabled = true, description = "check error message text: Last name")
	public void verifyLastNameErrorMsg() throws InterruptedException, IOException {	

		Assert.assertTrue(registration.verifyErrorsInRegistration_LastName("נא למלא שדה זה"), "Could not verify error message for first name field");
	
	}
	
	@Test(priority = 3, enabled = true, description = "check error message text: Password")
	public void verifyMissingPasswordErrorMsg() throws InterruptedException, IOException {	

		Assert.assertTrue(registration.verifyErrorsInRegistration_Password("נא להזין סיסמה חזקה בת 6 תווים לפחות, המורכבת מאותיות ומספרים"), "Could not verify error message for password  field");
	}
	
	
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {
			myTest.log(LogStatus.FAIL, "Test failed: " + result.getName());
			myTest.log(LogStatus.FAIL, "Test failed reason: " + result.getThrowable());
			myTest.log(LogStatus.FAIL, myTest.addScreenCapture(Utilities.takeScreenShot(driver)));
		}
		else {
			myTest.log(LogStatus.PASS, result.getName(), "Verify successful ");
			myTest.log(LogStatus.PASS, myTest.addScreenCapture(Utilities.takeScreenShot(driver)));

		}

		myTest.log(LogStatus.INFO, "Finish test", "Finish test ");
		extent.endTest(myTest);
	
		//return to base URL 
		//driver.get(baseUrl);
	}

	@AfterClass
	public void afterClass() {
		extent.flush();
		extent.close();
		driver.quit();

	}

	
}
