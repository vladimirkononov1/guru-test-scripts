package testScripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestScript01 {

	public WebDriver driver;
	private static String baseUrl;
	static By locatedElement = By.className("menusubnav");
	static By homePage = By.name("frmLogin");

//	public static void setUp() throws IOException {
//
//		File pathToBinary = new File(Util.FIREFOX_PATH);
//		FirefoxBinary ffbinary = new FirefoxBinary(pathToBinary);
//
////		Create new firefoxProfile for Testing
//		FirefoxProfile firefoxProfile = new FirefoxProfile();
//
//		// Setup Firefox driver
//		driver = new FirefoxDriver();
//		baseUrl = Util.BASE_URL;
//
//		// Implicit wait
//		driver.manage().timeouts().implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);
//
//		driver.get(baseUrl + "/V4/");
//
//	}

	@BeforeTest(enabled = true)
	public void initialize() {
		File pathToBinary = new File(Util.FIREFOX_PATH);
		FirefoxBinary ffbinary = new FirefoxBinary(pathToBinary);

//		Create new firefoxProfile for Testing
		FirefoxProfile firefoxProfile = new FirefoxProfile();

		// Setup Firefox driver
		driver = new FirefoxDriver();
		baseUrl = Util.BASE_URL;

		// Implicit wait
		driver.manage().timeouts().implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);

		driver.get(baseUrl + "/V4/");
	}	
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	@Test(enabled = false)
	public void loginSimple() {
		WebDriverWait wait = new WebDriverWait(driver, 10);

		String expectedTitle = Util.EXPECT_TITLE;
		// Enter username
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(Util.USER_NAME);

		// Enter Password
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(Util.PASSWD);

		// Click Login
		driver.findElement(By.name("btnLogin")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(locatedElement));

		if (expectedTitle.contains(driver.getTitle())) {
			System.out.println("Test Passed");
		} else {
			System.out.println("Test Failed");
		}

		driver.close();
	}

	@Test(enabled = true) //Getting Data from Excel
	public void getExcelData() throws IOException {
		Util.getDataFromExcel();
	}

	@Parameters({"validUname", "validPwd"})
	@Test(enabled=true)
	public void validUidPwd(@Optional() String validUname, String validPwd) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		String expectedTitle = Util.EXPECT_TITLE;

		// Enter username
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(validUname);

		// Enter Password
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(validPwd);

		// Click Login
		driver.findElement(By.name("btnLogin")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(locatedElement));

		Assert.assertEquals(driver.getTitle(), expectedTitle);
		if(expectedTitle.equalsIgnoreCase(driver.getTitle())) {
			System.out.println("Valid Creds Test PASSED!");
		}
		

	}
	
	@Parameters({"invalidUname", "validPwd"})
	@Test(enabled=true)
	public void invalidUidValidPwd(@Optional String invalidUname, @Optional String validPwd) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		// Enter username
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(invalidUname);

		// Enter Password
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(validPwd);

		// Click Login
		driver.findElement(By.name("btnLogin")).click();

		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alt = driver.switchTo().alert();
			String alertTitle = alt.getText();
			Assert.assertEquals(alertTitle, Util.EXPECT_ERROR);
			alt.dismiss();
			System.out.println("Invalid Username TEST PASSED!");

		} catch (NoAlertPresentException e) {
			// TODO: handle exception
			System.out.println("Test FAILED! Alert is missing");
		}
		

	}
	
	@Parameters({"validUname", "invalidPwd"})
	@Test(enabled=true)
	public void validUidInvalidPwd(@Optional String validUname, @Optional String invalidPwd) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		// Enter username
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(validUname);

		// Enter Password
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(invalidPwd);

		// Click Login
		driver.findElement(By.name("btnLogin")).click();

//		wait.until(ExpectedConditions.presenceOfElementLocated(homePage));

		try {
			wait.until(ExpectedConditions.alertIsPresent());
//			Thread.sleep(3000);
			Alert alt = driver.switchTo().alert();
			String alertTitle = alt.getText();
			Assert.assertEquals(alertTitle, Util.EXPECT_ERROR);
			alt.dismiss();
			System.out.println("Invalid Password TEST PASSED!");

		} catch (NoAlertPresentException e) {
			// TODO: handle exception
			System.out.println("Test FAILED! Alert is missing");
		}
		

	}
	
	@Parameters({"invalidUname", "invalidPwd"})
	@Test(enabled=true)
	public void invalidUidInvalidPwd(@Optional String invalidUname, @Optional String invalidPwd) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		// Enter username
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(invalidUname);

		// Enter Password
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(invalidPwd);

		// Click Login
		driver.findElement(By.name("btnLogin")).click();


		try {
			wait.until(ExpectedConditions.alertIsPresent());
//			Thread.sleep(3000);
			Alert alt = driver.switchTo().alert();
			String alertTitle = alt.getText();
			Assert.assertEquals(alertTitle, Util.EXPECT_ERROR);
			alt.dismiss();
			System.out.println("Invalid Credentials TEST PASSED!");

		} catch (NoAlertPresentException e) {
			// TODO: handle exception
			System.out.println("Test FAILED! Alert is missing");
		}
		

	}

}
