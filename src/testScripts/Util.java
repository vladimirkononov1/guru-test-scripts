package testScripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Util {
	
	static String scrShot = new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());

//	public static final String FILE_PATH = "ddtData.xlsx";//File Path
	public static final String FIREFOX_PATH = "C:\\Users\\vladi\\OneDrive\\Documents\\Udemy\\SeleniumWebDriver\\documentsToRead\\E2EProject2\\E2EProject\\GreenKart\\TestScripts\\geckodriver.exe";
	// Setting Base URL
	public static final String BASE_URL = "http://www.demo.guru99.com";
	// ScreenShot location
	public static final String SCRSHOT_PATH = "C:\\Users\\vladi\\OneDrive\\Documents\\Udemy\\SeleniumWebDriver\\documentsToRead\\E2EProject2\\E2EProject\\GreenKart\\TestScripts\\screenshots\\"+scrShot;
	// Time to wait when searching for a GUI element 
	public static final int WAIT_TIME = 30; 
	// Valid account for login
	public static final String USER_NAME = "mngr275032";
	public static final String PASSWD = "gEdajAd";
	// Expected output
	public static final String EXPECT_TITLE = "Guru99 Bank Manager HomePage";
	//Expected allert error
	public static final String EXPECT_ERROR = "User or Password is not valid";
	//Expected Wellcome message
	public static final String EXPECT_MESSAGE = "Manger Id : mngr";
	
    public static final String PATTERN = ":";
    public static final String FIRST_PATTERN = "mngr";
    public static final String SECOND_PATTERN = "[0-9]+";

	
	// You can change the information of your data file here
	public static final String FILE_PATH = "testData.xls"; // File Path
	public static final String SHEET_NAME = "Data"; // Sheet name
	public static final String TABLE_NAME = "testData"; // Name of data table

	public static void takeSnapShot(WebDriver driver, String fileWithPath) throws Exception {
		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		//Call getScreenshotAs method to create image file
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		//Move image file to new destination
		File destFile = new File(fileWithPath);
		//Copy file at destination
		FileUtils.copyFile(srcFile, destFile);
	}


	public static void getDataFromExcel() throws IOException {

		//fileInputStream argument
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\testScripts\\testData.xlsx");

		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int sheets = workbook.getNumberOfSheets();
		
		for(int i=0; i<sheets; i++) {
			if(workbook.getSheetName(i).equalsIgnoreCase("TestData")) {
				XSSFSheet sheet=workbook.getSheetAt(i);
				//Identify Test cases column by scanning the entire 1st row
				Iterator<Row> rows = sheet.iterator();//sheet is collection of rows
				Row firstrow = rows.next();	
				Iterator<Cell> cell = firstrow.cellIterator();//row is collection of cells
				int k=0;
				int column = 0;
				while(cell.hasNext()) {
					Cell value = cell.next();
					if(value.getStringCellValue().equalsIgnoreCase("TestData")) {
						//desired column
						column = k;
					}
					k++;
				}
				while(rows.hasNext()) {
					Row r = rows.next();
//					String uname = "";
					String pwd = "";
//					Iterator<Cell> cv = r.cellIterator();
					if(r.getCell(column).getStringCellValue().equalsIgnoreCase("username")) {
						Iterator<Cell> cv = r.cellIterator();
						int l=0;
						int ce = 0;
						cv.next();
						while(cv.hasNext()) {
							Cell uname = cv.next();
							System.out.println(uname.getStringCellValue());
						}
					}

				}
			}
		}
	}
	

}
