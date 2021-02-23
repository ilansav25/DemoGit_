package tests;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pages.Cart;
import pages.Main;
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


public class SuperPharm_Sanity {

	// Global variables 
	// Add extent reports
	private ExtentReports extent;
	private ExtentTest myTest;
	private static String reportPaht = System.getProperty("user.dir") + "\\test-output\\SuperPharm_Cart.html";

	private WebDriver driver;
	private String baseUrl;
	
	
	//pages
	private Main main;
	private Cart cart;
	

	@BeforeClass
	public void beforeClass() {
		extent = new ExtentReports(reportPaht);
		extent.loadConfig(new File(System.getProperty("user.dir") + "\\resources\\superpharm-extent-config.xml"));
		baseUrl = "https://shop.super-pharm.co.il/";
		driver = GetDriver.getDriver("chrome", baseUrl);
		main = new Main(driver);
		cart = new Cart(driver);
	}

	
	
	@BeforeMethod
	public void beforeMethod(Method method) throws IOException {
		myTest = extent.startTest(method.getName());
		myTest.log(LogStatus.INFO, "Starting test", "Start test");
	}
	
	
	
	/*  Prerequisite: verify cart is empty
	/*  	Given: client is in site superpharm - main page
	 * 		When: client is searching for product, e.g. norofen
	 *  	Then: norfen is in the results
	 *  Hagai, 12/02/2021
	 */
	
	@Test(priority = 1, enabled = true, description = "Check search results in site")
	public void searchForProduct() throws InterruptedException, IOException {	
		Assert.assertTrue(main.searchProduct("אקמול"), "Could notfind product");
		
	}
	
	
	/*  Prerequisite: verify cart is empty
	 * 		Given: client is in site superpharm - main page and perform a search to product
	 * 		When: client is clicking add to cart
	 *  	Then: product is added to cart
	 */
	
	@Test(priority = 2, enabled = true, description = "add product to cart")
	public void addToCart() throws InterruptedException, IOException {	
		Assert.assertTrue(main.addToCart(), "Could not add to cart");
		
	}

	
	/*  Prerequisite: verify cart is not empty
	 * 		Given: Client going to cart
	 * 		When: Remove product
	 *  	Then: Cart is empty
	 */
	
	@Test(priority = 3, enabled = true, description = "Remove product from cart")
	public void removeProductFromCart() throws InterruptedException, IOException {	
		main.goToCart();
		Assert.assertTrue(cart.removeProductFromCart(), "Could not empty cart");
		
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
