package testScripts;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import freemarker.template.SimpleDate;

public class TestScript02 {

	public WebDriver driver;
	private static String baseUrl;
	static By locatedElement = By.className("menusubnav");
	static By homePage = By.name("frmLogin");
	
	@DataProvider(name="GuruTest")
	public Object[][] testData() {
			return new Object[][] {
				{Util.USER_NAME, Util.PASSWD},
				{"invalid", "valid"},
				{"valid", "invalid"},
				{"invalid", "invalid"}
			};
		
		}

	@BeforeTest(enabled = true)
	public void initialize() {
		File pathToBinary = new File(Util.FIREFOX_PATH);
		FirefoxBinary ffbinary = new FirefoxBinary(pathToBinary);

		String destinationFile = System.getProperty("user.dir") + "\\reports\\" + ".png";
		
//		Create new firefoxProfile for Testing
		FirefoxProfile firefoxProfile = new FirefoxProfile();

		// Setup Firefox driver
		driver = new FirefoxDriver();
		baseUrl = Util.BASE_URL;

		// Implicit wait
		driver.manage().timeouts().implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);

//		driver.get(baseUrl + "/V4/");
	}
	
	@BeforeMethod
	public void launchBrowser() {
		driver.get(baseUrl + "/V4/");
	}

	@AfterTest(enabled=true)
	public void tearDown() {
		driver.quit();
	}
	
	@Test(dataProvider = "GuruTest", enabled=true)
	public void testLogin(String username, String password) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		String expectedMessage = Util.EXPECT_MESSAGE;
		By message = By.xpath("//td[contains(text(),'Manger Id : mngr')]");
		String actualTitle;
		String alertTitle;

			// Enter username
			driver.findElement(By.name("uid")).clear();
			driver.findElement(By.name("uid")).sendKeys(username);

			// Enter Password
			driver.findElement(By.name("password")).clear();
			driver.findElement(By.name("password")).sendKeys(password);

			// Click Login
			driver.findElement(By.name("btnLogin")).click();
			
			try {
				Alert alt = driver.switchTo().alert();
				alertTitle = alt.getText();
				Assert.assertEquals(alertTitle, Util.EXPECT_ERROR);
				alt.dismiss();
				System.out.println("Invalid Credentials TEST PASSED!");
			} catch (NoAlertPresentException e) {
				//Get text displayed on login page
				String pageText = driver.findElement(By.tagName("tbody")).getText();
				// Extract the dynamic text mngrXXXX on page		
				String[] parts = pageText.split(Util.PATTERN);
				String dynamicText = parts[1];
				
				wait.until(ExpectedConditions.presenceOfElementLocated(locatedElement));
				actualTitle = driver.getTitle();
				
				// Check that the dynamic text is of pattern mngrXXXX
				// First 4 characters must be "mngr"
				Assert.assertTrue(dynamicText.substring(1, 5).contains(Util.FIRST_PATTERN));

				// remain stores the "XXXX" in pattern mngrXXXX
				String remain = dynamicText.substring(dynamicText.length() - 4);
				// Check remain string must be numbers;
				Assert.assertTrue(remain.matches(Util.SECOND_PATTERN));
				//Check the title of the page
				Assert.assertEquals(actualTitle, Util.EXPECT_TITLE);
				//Check the presence of the message
				Assert.assertTrue(driver.findElement(message).getText().contains(Util.EXPECT_MESSAGE));
				System.out.println("Valid Creds Test PASSED!");	
				Util.takeSnapShot(driver, Util.SCRSHOT_PATH);
				
			} 	
	}
	
	@Parameters({"validUname", "validPwd"})
	@Test(enabled=false)
	public void validateMgrID(@Optional() String username, @Optional() String password) {
		String expectedMessage = Util.EXPECT_MESSAGE;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		By message = By.xpath("//td[contains(text(),'Manger Id : mngr')]"); 
		
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(username);
		
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(password);
		
		driver.findElement(By.name("btnLogin")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(locatedElement));
		
		Assert.assertTrue(driver.findElement(message).getText().contains(Util.EXPECT_MESSAGE));

	}

}
