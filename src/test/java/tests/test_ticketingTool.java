package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.AfterMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import pages.po_ticketing_tool;
import reusables.methods;
import reusables.base;

public class test_ticketingTool extends po_ticketing_tool {

    private ExtentReports extent;
    private ExtentTest test;
    
    public test_ticketingTool() {
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

    @Test(priority=1)
    public void validCredential() {
        launch_url();
        String test_case_id = "ck_tool_001";
        Map<String, String> tdd = base.getTestDataFromExcel(test_case_id);
        login(tdd.get("mail_id"), tdd.get("pass"));
    }
    @Test(priority=2)
    public void Form_Creation() throws InterruptedException {
    	launch_url();
        String test_case_id = "ck_tool_001";
        Map<String, String> tdd = base.getTestDataFromExcel(test_case_id);
        login(tdd.get("mail_id"), tdd.get("pass"));
        form_submission(tdd.get("profit_center"), tdd.get("material_group"), tdd.get("material_group_description"), tdd.get("material_no"), tdd.get("dist_chnl"), tdd.get("remarks"));      
    	
    }

    @AfterMethod
    public void tearDown() {
        base.closeDriver(); 
        System.out.println("Driver closed");
        extent.flush();
    }
}
