package reusables;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import java.util.UUID;
import java.util.Random;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class methods extends base {
	private ExtentTest test;

	public methods(ExtentTest test) {
		this.test = test;
	}

	public void openUrl(String url) {
		driver.get(url);
	}

	public void waitForElement(By locator, int timeoutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void click(By locator) {
		waitForElement(locator, 3);
		scrollToElement(locator);
		driver.findElement(locator).click();
	}

	public void enterText(By locator, String text) {
		waitForElement(locator, 3);
		scrollToElement(locator);
		driver.findElement(locator).sendKeys(text);
	}

	public boolean isElementVisible(By locator) {
		try {
			waitForElement(locator, 10);
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isElementNotVisible(String locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
		} catch (Exception e) {
			return false;
		}
	}

	public void selectOptionByValue(By locator, String value) {
		try {
			waitForElement(locator, 3);
			scrollToElement(locator);
			Select select = new Select(driver.findElement(locator));
			select.selectByValue(value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to select option by value: " + e.getMessage());
		}
	}

	/**
	 * Selects a dropdown option whether the test data holds the option's value or
	 * its visible text. Tries, in order: exact value, exact visible text, then
	 * visible text ignoring case and spaces. Waits up to 10s for the option, since
	 * dependent dropdowns (e.g. material group after profit center) load their
	 * options via AJAX.
	 */
	public void selectOption(By locator, String option) {
		if (option == null || option.trim().isEmpty()) {
			throw new IllegalArgumentException("No test data value for dropdown " + locator);
		}
		waitForElement(locator, 3);
		scrollToElement(locator);
		String wanted = option.trim();
		try {
			new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(d -> trySelect(new Select(d.findElement(locator)), wanted));
		} catch (TimeoutException e) {
			List<String> available = new Select(driver.findElement(locator)).getOptions().stream().limit(25)
					.map(o -> "[value=" + o.getDomProperty("value") + " | text=" + o.getText().trim() + "]")
					.collect(Collectors.toList());
			throw new RuntimeException("Option '" + wanted + "' not found by value or text in " + locator
					+ ". First options: " + available);
		}
	}

	private boolean trySelect(Select select, String wanted) {
		try {
			List<WebElement> options = select.getOptions();
			String normalized = normalize(wanted);
			for (int pass = 0; pass < 3; pass++) {
				for (int i = 0; i < options.size(); i++) {
					WebElement o = options.get(i);
					String value = o.getDomProperty("value");
					String text = o.getText().trim();
					boolean match = (pass == 0 && wanted.equals(value)) || (pass == 1 && wanted.equals(text))
							|| (pass == 2 && normalized.equals(normalize(text)));
					if (match) {
						select.selectByIndex(i);
						return true;
					}
				}
			}
			return false;
		} catch (StaleElementReferenceException e) {
			return false; // options were re-rendered; the wait will retry
		}
	}

	private static String normalize(String s) {
		return s == null ? "" : s.replaceAll("\\s+", "").toLowerCase();
	}

	public void selectByVisibleText(By locator, String text) {
		try {
			waitForElement(locator, 3);
			scrollToElement(locator);
			Select select = new Select(driver.findElement(locator));
			select.selectByVisibleText(text);
		} catch (Exception e) {
			throw new RuntimeException("Failed to select option by visible text: " + e.getMessage());
		}
	}

	public void selectOptionByIndex(By locator, int index) {
		try {
			waitForElement(locator, 3);
			scrollToElement(locator);
			Select select = new Select(driver.findElement(locator));
			select.selectByIndex(index);
		} catch (Exception e) {
			throw new RuntimeException("Failed to select option by index: " + e.getMessage());
		}
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public void currentDate(By locator) {
		String currentDate = java.time.LocalDate.now().toString();
		WebElement dateField = driver.findElement(locator);
		scrollToElement(locator);
		dateField.clear();

		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1])", dateField,
				currentDate);
	}

	public void endDate(By locator) {
		java.time.LocalDate currentDate = java.time.LocalDate.now();
		java.time.LocalDate futureDate = currentDate.plusDays(8);
		String formattedDate = futureDate.toString();

		WebElement dateField = driver.findElement(locator);
		scrollToElement(locator);

		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', arguments[1])", dateField,
				formattedDate);
	}

	public static String generateRandomEmail() {
		return "testuser_" + UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";
	}

	public static String generateRandomPassword() {

		String password = "Test@" + UUID.randomUUID().toString().substring(0, 8);

		return password;
	}
	
	public static String generateRandomName() {

	    String[] firstNames = {"John", "David", "Alex", "Robert", "Michael", "James"};

	    String[] lastNames = {"Smith", "Kumar", "Johnson", "Williams", "Brown"};

	    Random random = new Random();

	    String firstName = firstNames[random.nextInt(firstNames.length)];

	    String lastName = lastNames[random.nextInt(lastNames.length)];

	    return firstName + " " + lastName;
	}
	
	public void scrollToElement(By locator) {
		WebElement element = driver.findElement(locator);
		if (element.isDisplayed()) {
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'})", element);
		}
	}

	public void uploadFile(By locator, String fileName) {
		File filePath = Paths.get(System.getProperty("user.dir"), "resources", "test_data", fileName).toFile();

		if (!filePath.exists()) {
			throw new RuntimeException("File not found at path: " + filePath);
		}

		WebElement fileInput = driver.findElement(locator);
		scrollToElement(locator);
		fileInput.sendKeys(filePath.getAbsolutePath());
	}

	public int extractNumber(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		if (element != null && element.isDisplayed()) {
			String text = element.getText().trim();
			if (!text.isEmpty()) {
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(text);
				if (matcher.find()) {
					return Integer.parseInt(matcher.group());
				}
			}
		}
		throw new RuntimeException("No numeric value found in element: " + locator);
	}

	public String[] extractWords(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		if (element != null && element.isDisplayed()) {
			String text = element.getText().trim();
			return text.split("\\s+");
		}
		throw new RuntimeException("Element not visible: " + locator);
	}

	public void assertCondition(boolean condition, String successMsg, String failureMsg) {
		if (!condition) {
			String screenshotPath = takeScreenshot(getTestCaseName());
			if (screenshotPath != null) {
				test.log(Status.FAIL, failureMsg);
				test.addScreenCaptureFromPath(screenshotPath);
				System.out.println("Screenshot added to the report: " + screenshotPath);
			} else {
				System.out.println("Failed to capture screenshot.");
			}
			throw new AssertionError(failureMsg);
		}
		test.log(Status.PASS, successMsg);
	}

	private String getTestCaseName() {
		ITestResult result = Reporter.getCurrentTestResult();
		if (result != null) {
			return result.getMethod().getMethodName();
		}
		return "UnknownTest"; // Fallback to avoid null
	}

	private String takeScreenshot(String testCaseName) {
		try {
			// Create timestamp with formatter
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyy-MM-dd_hh-mm-ss");
			String timestamp = java.time.LocalDateTime.now().format(formatter);

			// Create directory if it doesn't exist
			String screenshotDir = Paths.get(System.getProperty("user.dir"), "test-output", "screenshots").toString();
			File dir = new File(screenshotDir);
			if (!dir.exists() && !dir.mkdirs()) {
				System.out.println("Failed to create screenshot directory.");
				return null;
			}

			// Define the screenshot path with timestamp
			String screenshotPath = Paths.get(screenshotDir, testCaseName + "_" + timestamp + ".png").toString();

			// Take screenshot
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(screenshotPath));

			System.out.println("Screenshot saved at: " + screenshotPath);
			return screenshotPath;
		} catch (Exception e) {
			System.out.println("Failed to capture screenshot: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
