package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.AfterMethod;

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
    private static final String USERNAME = "Alan Tony";
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
    }

    @Test(priority = 1)
    public void TC001_VerifyAccountCreationLogoutAndLogin() {
        launch_url();
        openSignupLogin();

        // Generate a unique email so every fresh TC001 run uses a new account.
        registeredEmail = methods.generateRandomEmail();
        registeredPassword = methods.generateRandomPassword();

        registerNewUser(
                USERNAME,
                registeredEmail,
                registeredPassword,
                "Alan",
                "Tony",
                "Test Company",
                "Test Address Line 1",
                "Test Address Line 2",
                "Puducherry",
                "Puducherry",
                "605007",
                "9000000000");

        logout();
        login(registeredEmail, registeredPassword, USERNAME);
    }

    @Test(priority = 2, dependsOnMethods = "TC001_VerifyAccountCreationLogoutAndLogin")
    public void TC002_VerifyProductSelectionAndSuccessfulCheckout() {
        // TC001 creates the account. This test reuses that account.
        if (registeredEmail == null || registeredPassword == null) {
            throw new IllegalStateException(
                    "Run TC001 before TC002 in the same TestNG execution.");
        }

        launch_url();
        openSignupLogin();
        login(registeredEmail, registeredPassword, USERNAME);
        navigateToProducts();
        addBlueTopToCart();

        // These are sample values for the site's demonstration checkout flow.
        // Use only authorized test payment data.
        checkoutAndPlaceOrder(
                "Automation test order",
                USERNAME,
                "4111111111111111",
                "123",
                "12",
                "2028");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        base.closeDriver();
        if (extent != null) {
            extent.flush();
        }
}
}
