package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;
import org.testng.annotations.AfterMethod;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import pages.po_signup_products;
import reusables.methods;
import reusables.base;

public class test_signup_products extends po_signup_products {

	private ExtentReports extent;
	private ExtentTest test;
	private static String registeredEmail;
	private static String registeredPassword;

	public test_signup_products() {
		super(null);
	}

	@BeforeMethod
	public void setup(Method method) {

		String browser = System.getProperty("browser", "chrome");

		base.initializeDriver(browser);

		System.out.println("Driver initialized with browser: " + browser);

		extent = base.getInstance();

		test = extent.createTest(method.getName());

		methods = new methods(test);

		setMethods(methods);
	}

	@Test(priority = 1)
	public void TC001_VerifyAccountCreationLogoutAndLogin() {
		launch_url();
		openSignupLogin();

		// Generate a unique email so every fresh TC001 run uses a new account.
		registeredEmail = methods.generateRandomEmail();
		registeredPassword = methods.generateRandomPassword();
		Map<String, String> testData = base.getTestDataFromExcel("TC001");
		registerNewUser(testData.get("user_name"), registeredEmail, registeredPassword, testData.get("first_name"),
				testData.get("last_name"), testData.get("company"), testData.get("address1"), testData.get("address2"),
				testData.get("state"), testData.get("city"), testData.get("pincode"), testData.get("phone_number"));

		logout();
		login(registeredEmail, registeredPassword, testData.get("user_name"));
	}

	@Test(priority = 2, dependsOnMethods = "TC001_VerifyAccountCreationLogoutAndLogin")
	public void TC002_VerifyProductSelectionAndSuccessfulCheckout() {
		// TC001 creates the account. This test reuses that account.
		if (registeredEmail == null || registeredPassword == null) {
			throw new IllegalStateException("Run TC001 before TC002 in the same TestNG execution.");
		}
		Map<String, String> testData = base.getTestDataFromExcel("TC002");
		launch_url();
		openSignupLogin();
		login(registeredEmail, registeredPassword, testData.get("user_name"));
		navigateToProducts();
		addBlueTopToCart();

		// These are sample values for the site's demonstration checkout flow.
		// Use only authorized test payment data.
		checkoutAndPlaceOrder(testData.get("comment"), testData.get("user_name"), testData.get("card_number"),
				testData.get("cvv"), testData.get("valid_month"), testData.get("valid_year"));
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {

		if (result.getStatus() == ITestResult.FAILURE) {

			try {
				String testName = result.getMethod().getMethodName();

				String timestamp = java.time.LocalDateTime.now()
						.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

				String screenshotDir = Paths.get(System.getProperty("user.dir"), "test-output", "screenshots")
						.toString();

				File directory = new File(screenshotDir);

				if (!directory.exists()) {
					directory.mkdirs();
				}

				String screenshotPath = Paths.get(screenshotDir, testName + "_" + timestamp + ".png").toString();

				File screenshot = ((TakesScreenshot) base.getDriver()).getScreenshotAs(OutputType.FILE);

				Files.copy(screenshot.toPath(), Paths.get(screenshotPath), StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Failure screenshot saved: " + screenshotPath);

				if (test != null) {
					test.fail(result.getThrowable());
					test.addScreenCaptureFromPath(screenshotPath);
				}

			} catch (Exception e) {
				System.out.println("Screenshot capture failed: " + e.getMessage());
				e.printStackTrace();
			}
		}

		base.closeDriver();

		if (extent != null) {
			extent.flush();
		}
	}
}
