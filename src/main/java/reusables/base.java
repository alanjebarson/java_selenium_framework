package reusables;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.chrome.ChromeOptions;

public class base {
	protected static WebDriver driver;
	private static ExtentReports extent;

	// Initialize Driver
	public static void initializeDriver(String browserName) {
		if (driver == null) {
			switch (browserName.toLowerCase()) {
			case "chrome":
				ChromeOptions options = new ChromeOptions();

				if (Boolean.getBoolean("headless")) {
					options.addArguments("--headless=new");
					options.addArguments("--no-sandbox");
					options.addArguments("--disable-dev-shm-usage");
					options.addArguments("--window-size=1920,1080");
				}

				driver = new ChromeDriver(options);
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browserName);
			}
			driver.manage().window().maximize();
		}
	}

	// Get WebDriver Instance
	public static WebDriver getDriver() {
		if (driver == null) {
			throw new IllegalStateException("Driver is not initialized. Call initializeDriver() first.");
		}
		return driver;
	}

	// Close Driver
	public static void closeDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	// Get Test Data from Excel
	public static Map<String, String> getTestDataFromExcel(String testCaseId) {
		String baseDir = System.getProperty("user.dir");
		String excelFilePath = Paths.get(baseDir, "resources", "test_data", "Test_Data_Excel.xlsx").toString();

		Map<String, String> testDataDict = new HashMap<>();
		Map<String, String> otherDataDict = new HashMap<>();

		try (FileInputStream file = new FileInputStream(excelFilePath); Workbook workbook = new XSSFWorkbook(file)) {

			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);

			Map<String, Integer> columnIndexMap = new HashMap<>();
			for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
				Cell cell = headerRow.getCell(i);
				if (cell != null) {
					columnIndexMap.put(cell.getStringCellValue().trim(), i);
				}
			}

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null && columnIndexMap.get("testcase_id") != null) {
					Cell testCaseIdCell = row.getCell(columnIndexMap.get("testcase_id"));
					if (testCaseIdCell != null) {
						String testCaseIdValue = "";
						switch (testCaseIdCell.getCellType()) {
						case STRING:
							testCaseIdValue = testCaseIdCell.getStringCellValue().trim();
							break;
						case NUMERIC:
							testCaseIdValue = String.valueOf(testCaseIdCell.getNumericCellValue());
							break;
						default:
							break;
						}

						if (testCaseIdValue.equals(testCaseId)) {
							if (columnIndexMap.get("test_data") != null) {
								Cell testDataCell = row.getCell(columnIndexMap.get("test_data"));
								if (testDataCell != null) {
									String testDataValue = testDataCell.getStringCellValue();
									if (testDataValue != null && !testDataValue.isEmpty()) {
										String[] testDataItems = testDataValue.split("\\|");
										for (String item : testDataItems) {
											if (item.contains(":")) {
												String[] keyValue = item.split(":");
												if (keyValue.length == 2) {
													testDataDict.put(keyValue[0].trim(), keyValue[1].trim());
												}
											}
										}
									}
								}
							}

							for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
								if (!entry.getKey().equals("test_data") && !entry.getKey().equals("testcase_id")) {
									Cell cell = row.getCell(entry.getValue());
									if (cell != null) {
										switch (cell.getCellType()) {
										case STRING:
											otherDataDict.put(entry.getKey(), cell.getStringCellValue().trim());
											break;
										case NUMERIC:
											otherDataDict.put(entry.getKey(),
													String.valueOf(cell.getNumericCellValue()));
											break;
										default:
											break;
										}
									}
								}
							}
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, String> mergedDict = new HashMap<>(testDataDict);
		mergedDict.putAll(otherDataDict);

		System.out.println("Merged Data: " + mergedDict);
		return mergedDict;
	}

	// Initialize Reporting

public static ExtentReports getInstance() {
    if (extent == null) {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String reportName = System.getenv("REPORT_NAME");
        if (reportName == null) {
            reportName = dotenv.get("REPORT_NAME", "Selenium Automation Report");
        }

        String documentTitle = System.getenv("DOCUMENT_TITLE");
        if (documentTitle == null) {
            documentTitle = dotenv.get("DOCUMENT_TITLE", "Automation Test Results");
        }

        String reportPath = Paths.get(
                System.getProperty("user.dir"),
                "test-output",
                "Reports",
                "TestReport.html"
        ).toString();

        new java.io.File(reportPath).getParentFile().mkdirs();

        ExtentSparkReporter reporter =
                new ExtentSparkReporter(reportPath);

        reporter.config().setReportName(reportName);
        reporter.config().setDocumentTitle(documentTitle);

        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    return extent;
}
}
