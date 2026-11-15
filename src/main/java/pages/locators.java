package pages;

import org.openqa.selenium.By;

public class locators {
	protected final By EMAIL = By.xpath("//input[@id='email']");
	protected final By PASSWORD = By.xpath("//input[@id='password']");
	protected final By LOGIN_BTN = By.xpath("//button[@id='login']");
	protected final By Login_Assert = By.xpath("//div[contains(text(),'Dashboard')]");
	protected final By Generate_Ticket = By.xpath("//span[contains(text(),'Generate Ticket ')]");
	protected final By MT_EC_Form = By.xpath("//span[contains(text(),'Schemes Updation - MT/EC')]");
	protected final By Profit_Center = By.xpath("//select[@name='profit_center']");
	protected final By Profit_Center_SearchBox = By.xpath("//input[@role='searchbox']");
	protected final By Material_Group = By.xpath("//select[@name='material_group']");
	protected final By Material_Group_Description = By.xpath("//select[@name='mat_grp1_description']");
	protected final By Material_No = By.xpath("//select[@name='material_no']");
	protected final By Dist_chnl = By.xpath("//select[@name='dis_chnl_mt_mtec']");
	protected final By Start_Date = By.xpath("//input[@id='start_date']");
	protected final By End_Date = By.xpath("//input[@id='end_date']");
	protected final By Remarks = By.xpath("//textarea[@id='remarks']");
	protected final By Upload_File = By.xpath("//input[@type='file']");
	protected final By Send_Btn = By.xpath("//button[@id='submitbtn']");
	protected final By Save_Assert = By.xpath("//strong[contains(text(),'Saved Successfully')]");
	protected final By Dashboard = By.xpath("//span[text()='Dashboard']");
	protected final By Dashboard_Pending = By.xpath("//h5[text()='Pending']/ancestor::div[@class='col yellow_card']//h1/b");
	
}
