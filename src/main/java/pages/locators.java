package pages;

import org.openqa.selenium.By;

public class locators {
	 // Common navigation
    protected final By SIGNUP_LOGIN = By.xpath("//a[contains(@href,'/login')]");
    protected final By LOGGED_IN_AS = By.xpath("//a[contains(.,'Logged in as')]");
    protected final By LOGOUT = By.xpath("//a[contains(@href,'/logout')]");

    // Signup
    protected final By SIGNUP_NAME = By.cssSelector("input[data-qa='signup-name']");
    protected final By SIGNUP_EMAIL = By.cssSelector("input[data-qa='signup-email']");
    protected final By SIGNUP_BUTTON = By.cssSelector("button[data-qa='signup-button']");
    protected final By GENDER_MR = By.id("id_gender1");
    protected final By ACCOUNT_PASSWORD = By.id("password");
    protected final By DAYS = By.id("days");
    protected final By MONTHS = By.id("months");
    protected final By YEARS = By.id("years");
    protected final By FIRST_NAME = By.id("first_name");
    protected final By LAST_NAME = By.id("last_name");
    protected final By COMPANY = By.id("company");
    protected final By ADDRESS1 = By.id("address1");
    protected final By ADDRESS2 = By.id("address2");
    protected final By COUNTRY = By.id("country");
    protected final By STATE = By.id("state");
    protected final By CITY = By.id("city");
    protected final By ZIPCODE = By.id("zipcode");
    protected final By MOBILE_NUMBER = By.id("mobile_number");
    protected final By CREATE_ACCOUNT = By.cssSelector("button[data-qa='create-account']");
    protected final By ACCOUNT_CREATED = By.cssSelector("h2[data-qa='account-created']");
    protected final By CONTINUE = By.cssSelector("a[data-qa='continue-button']");

    // Login
    protected final By LOGIN_EMAIL = By.cssSelector("input[data-qa='login-email']");
    protected final By LOGIN_PASSWORD = By.cssSelector("input[data-qa='login-password']");
    protected final By LOGIN_BUTTON = By.cssSelector("button[data-qa='login-button']");

    // Products and cart
    protected final By PRODUCTS_LINK = By.xpath("//a[contains(@href,'/products')]");
    protected final By BLUE_TOP_CARD = By.xpath(
            "//div[contains(@class,'product-image-wrapper')][.//p[normalize-space()='Blue Top']]");
    protected final By BLUE_TOP_NAME = By.xpath("//div[contains(@class,'productinfo')]/p[normalize-space()='Blue Top']");
    protected final By BLUE_TOP_PRICE = By.xpath(
            "//div[contains(@class,'product-image-wrapper')][.//p[normalize-space()='Blue Top']]//h2");
    protected final By BLUE_TOP_ADD_TO_CART = By.xpath(
            "//div[contains(@class,'product-image-wrapper')]//p[text()='Blue Top']//parent::div[@class='overlay-content']//a[contains(.,'Add to cart')]");
    protected final By VIEW_CART = By.xpath("//a[normalize-space()='View Cart']");
    protected final By CART_BLUE_TOP = By.xpath("//tr[contains(@id,'product-')][.//a[normalize-space()='Blue Top']]");
    protected final By PROCEED_TO_CHECKOUT = By.xpath("//a[normalize-space()='Proceed To Checkout']");
    protected final By DELIVERY_ADDRESS = By.id("address_delivery");
    protected final By REVIEW_ORDER = By.id("cart_info");

    // Checkout and payment
    protected final By ORDER_COMMENT = By.name("message");
    protected final By PLACE_ORDER = By.xpath("//a[normalize-space()='Place Order']");
    protected final By NAME_ON_CARD = By.name("name_on_card");
    protected final By CARD_NUMBER = By.name("card_number");
    protected final By CVC = By.name("cvc");
    protected final By EXPIRY_MONTH = By.name("expiry_month");
    protected final By EXPIRY_YEAR = By.name("expiry_year");
    protected final By PAY_CONFIRM_ORDER = By.id("submit");
    protected final By ORDER_PLACED = By.xpath("//*[self::h2 or self::b][contains(normalize-space(),'Order Placed!')]");
}
