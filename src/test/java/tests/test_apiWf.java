package tests;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import reusables.base;
import reusables.methods;

public class test_apiWf {

    ExtentReports extent;
    ExtentTest test;

    @BeforeMethod
    public void setup(Method method) {
        extent = base.getInstance();
        test = extent.createTest(method.getName());
    }

    @Test
    public void createUserAccount() {
        String email = methods.generateRandomEmail();
        String password = methods.generateRandomPassword();
        String name = methods.generateRandomName();

        test.log(Status.INFO, "Creating user with email: " + email);

        Response response = RestAssured.given()
                .baseUri("https://automationexercise.com")
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", name)
                .formParam("email", email)
                .formParam("password", password)
                .formParam("title", "Mr")
                .formParam("birth_date", "10")
                .formParam("birth_month", "5")
                .formParam("birth_year", "1998")
                .formParam("firstname", "Test")
                .formParam("lastname", "User")
                .formParam("company", "QA Company")
                .formParam("address1", "Test Street")
                .formParam("address2", "Test Area")
                .formParam("country", "India")
                .formParam("zipcode", "600001")
                .formParam("state", "Tamil Nadu")
                .formParam("city", "Chennai")
                .formParam("mobile_number", "9876543210")
                .post("/api/createAccount");

        test.log(Status.INFO, "Response: " + response.asString());

        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 201);
        Assert.assertEquals(response.jsonPath().getString("message"), "User created!");
        test.log(Status.PASS, "User account created successfully.");
    }

    @Test
    public void searchProduct() {
        test.log(Status.INFO, "Searching for product: top");

        Response response = RestAssured.given()
                .baseUri("https://automationexercise.com")
                .contentType("application/x-www-form-urlencoded")
                .formParam("search_product", "top")
                .post("/api/searchProduct");

        test.log(Status.INFO, "Response: " + response.asString());

        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 200);
        List<Map<String, Object>> products = response.jsonPath().getList("products");
        Assert.assertTrue(products.size() > 0, "No products found.");

        boolean found = false;
        for (Map<String, Object> product : products) {
            if (product.get("name").toString().equalsIgnoreCase("Blue Top")) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Blue Top was not found in the search results.");
        test.log(Status.PASS, "Blue Top was found in search results.");
    }

    @AfterMethod
    public void tearDown() {
        extent.flush();
    }
}
