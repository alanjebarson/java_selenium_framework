package pages;

import io.github.cdimascio.dotenv.Dotenv;
import reusables.methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import reusables.base;

public class po_signup_products {
	private locators locators;
	protected methods methods;
	Dotenv dotenv = Dotenv.load();
	public String url=dotenv.get("URL");
	
	public po_signup_products(methods methods) {
		this.locators= new locators();
		this.methods=methods;
	}
	public void launch_url() {
        methods.openUrl(url);
        methods.assertCondition(
                methods.isElementVisible(locators.SIGNUP_LOGIN),
                "Automation Exercise home page loaded",
                "Automation Exercise home page did not load");
    }

    public void openSignupLogin() {
        methods.click(locators.SIGNUP_LOGIN);
    }

    public void registerNewUser(String name, String email, String password,
                                String firstName, String lastName, String company,
                                String address1, String address2, String state,
                                String city, String zipcode, String mobile) {

        methods.enterText(locators.SIGNUP_NAME, name);
        methods.enterText(locators.SIGNUP_EMAIL, email);
        methods.click(locators.SIGNUP_BUTTON);

        methods.click(locators.GENDER_MR);
        methods.enterText(locators.ACCOUNT_PASSWORD, password);
        methods.selectOptionByValue(locators.DAYS, "24");
        methods.selectOptionByValue(locators.MONTHS, "2");
        methods.selectOptionByValue(locators.YEARS, "2002");

        methods.enterText(locators.FIRST_NAME, firstName);
        methods.enterText(locators.LAST_NAME, lastName);
        methods.enterText(locators.COMPANY, company);
        methods.enterText(locators.ADDRESS1, address1);
        methods.enterText(locators.ADDRESS2, address2);

        // Country is part of the account form. Choose India explicitly.
        methods.selectOption(locators.COUNTRY, "India");

        methods.enterText(locators.STATE, state);
        methods.enterText(locators.CITY, city);
        methods.enterText(locators.ZIPCODE, zipcode);
        methods.enterText(locators.MOBILE_NUMBER, mobile);
        methods.click(locators.CREATE_ACCOUNT);

        methods.assertCondition(
                methods.isElementVisible(locators.ACCOUNT_CREATED),
                "Account Created confirmation is displayed",
                "Account Created confirmation was not displayed");

        methods.click(locators.CONTINUE);
        boolean loggedIn = methods.isElementVisible(locators.LOGGED_IN_AS);
        methods.assertCondition(
                loggedIn,
                "New user is logged in after account creation",
                "Logged in as username was not displayed after registration");

        String actual = base.getDriver().findElement(locators.LOGGED_IN_AS).getText();
        methods.assertCondition(
                actual.contains(name),
                "Correct registered username is displayed: " + name,
                "Expected registered username '" + name + "' but found '" + actual + "'");
    }

    public void logout() {
        methods.click(locators.LOGOUT);
        methods.assertCondition(
                methods.isElementVisible(locators.LOGIN_BUTTON),
                "User logged out and login form is available",
                "Logout did not return the user to the login page");
    }

    public void login(String email, String password, String expectedUsername) {
        methods.enterText(locators.LOGIN_EMAIL, email);
        methods.enterText(locators.LOGIN_PASSWORD, password);
        methods.click(locators.LOGIN_BUTTON);

        boolean loggedIn = methods.isElementVisible(locators.LOGGED_IN_AS);
        methods.assertCondition(
                loggedIn,
                "User logged in successfully",
                "User could not log in with the supplied credentials");

        String actual = base.getDriver().findElement(locators.LOGGED_IN_AS).getText();
        methods.assertCondition(
                actual.contains(expectedUsername),
                "Correct username is displayed after login: " + expectedUsername,
                "Expected username '" + expectedUsername + "' but found '" + actual + "'");
    }

    public void navigateToProducts() {
        methods.click(locators.PRODUCTS_LINK);
        methods.assertCondition(
                methods.isElementVisible(locators.BLUE_TOP_NAME),
                "Products page and Blue Top product are displayed",
                "Products page or Blue Top product was not displayed");
    }

    public void addBlueTopToCart() {
        WebElement productCard = base.getDriver().findElement(locators.BLUE_TOP_CARD);
        new Actions(base.getDriver()).moveToElement(productCard).perform();

        methods.assertCondition(
                methods.isElementVisible(locators.BLUE_TOP_PRICE),
                "Blue Top product price is visible",
                "Blue Top product price is not visible");

        methods.assertCondition(
                base.getDriver().findElement(locators.BLUE_TOP_PRICE).getText().contains("500"),
                "Blue Top price is Rs. 500",
                "Blue Top price did not match the expected Rs. 500");

        methods.click(locators.BLUE_TOP_ADD_TO_CART);
        methods.assertCondition(
                methods.isElementVisible(By.xpath("//*[contains(normalize-space(),'Your product has been added')]")),
                "Product added-to-cart confirmation is displayed",
                "Product added-to-cart confirmation was not displayed");

        methods.click(locators.VIEW_CART);
        methods.assertCondition(
                methods.isElementVisible(locators.CART_BLUE_TOP),
                "Blue Top is displayed in the shopping cart",
                "Blue Top was not found in the shopping cart");

        String cartRow = base.getDriver().findElement(locators.CART_BLUE_TOP).getText();
        methods.assertCondition(
                cartRow.contains("Blue Top") && cartRow.contains("500") && cartRow.contains("1"),
                "Cart contains Blue Top with expected price and quantity",
                "Cart product details did not match: " + cartRow);
    }

    public void checkoutAndPlaceOrder(String comment, String cardName, String cardNumber,
                                      String cvc, String expiryMonth, String expiryYear) {
        methods.click(locators.PROCEED_TO_CHECKOUT);

        // Verify the delivery address and order review section on checkout.
        methods.assertCondition(
                methods.isElementVisible(locators.DELIVERY_ADDRESS)
                        && !base.getDriver().findElement(locators.DELIVERY_ADDRESS).getText().trim().isEmpty(),
                "Delivery address is displayed on checkout",
                "Delivery address was missing on checkout");

        methods.assertCondition(
                methods.isElementVisible(locators.REVIEW_ORDER)
                        && base.getDriver().findElement(locators.REVIEW_ORDER).getText().contains("Blue Top"),
                "Checkout order summary contains Blue Top",
                "Checkout order summary did not contain Blue Top");

        methods.enterText(locators.ORDER_COMMENT, comment);
        methods.click(locators.PLACE_ORDER);

        methods.enterText(locators.NAME_ON_CARD, cardName);
        methods.enterText(locators.CARD_NUMBER, cardNumber);
        methods.enterText(locators.CVC, cvc);
        methods.enterText(locators.EXPIRY_MONTH, expiryMonth);
        methods.enterText(locators.EXPIRY_YEAR, expiryYear);
        methods.click(locators.PAY_CONFIRM_ORDER);

        methods.assertCondition(
                methods.isElementVisible(locators.ORDER_PLACED),
                "Order Placed confirmation is displayed",
                "Order Placed confirmation was not displayed");
    }
	
}
