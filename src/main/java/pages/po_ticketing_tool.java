package pages;

import io.github.cdimascio.dotenv.Dotenv;
import reusables.methods;

public class po_ticketing_tool {
	private locators locators;
	protected methods methods;
	Dotenv dotenv = Dotenv.load();
	public String url=dotenv.get("URL");
	
	public po_ticketing_tool(methods methods) {
		this.locators= new locators();
		this.methods=methods;
	}
	public void launch_url() {
		methods.openUrl(url);
		
	}
	public void login(String mail, String pass) {
		methods.enterText(locators.EMAIL, mail);
		methods.enterText(locators.PASSWORD, pass);
		methods.click(locators.LOGIN_BTN);
		methods.assertCondition(methods.isElementVisible(locators.Login_Assert), "User can Login using valid Credentials", "User cannot Login using valid Credentials properly");
	}
	public void form_submission(String Profile_Center, String material_group, String material_group_description, String material_no, String dist_chnl, String remarks) throws InterruptedException {
		methods.click(locators.Generate_Ticket);
		methods.click(locators.MT_EC_Form);
		methods.refreshPage();
		methods.selectOption(locators.Profit_Center, Profile_Center);
		methods.selectOption(locators.Material_Group, material_group);
		methods.selectOption(locators.Material_Group_Description, material_group_description);
		methods.selectOption(locators.Material_No, material_no);
		methods.selectOption(locators.Dist_chnl, dist_chnl);
		methods.currentDate(locators.Start_Date);
		methods.endDate(locators.End_Date);
		methods.uploadFile(locators.Upload_File, "sample.pdf");
		methods.enterText(locators.Remarks, remarks);
		methods.click(locators.Send_Btn);
		methods.assertCondition(methods.isElementVisible(locators.Save_Assert), "Ticket Generated Successfully", "Ticket not Generated Successfully");
	}
	
	
}
