import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.handleElement.HandleTestData
import com.handleElement.VerifyElement
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

import org.openqa.selenium.Keys

import createPayroll

class recallPayroll {
	HandleTestData handleTestData = new HandleTestData()
	VerifyElement verifyElement = new VerifyElement()

	String locatorExcel = 'Test Data.xlsx'
	String sheetName = 'Test Name'
	String curr = "SGD"
	
	List<HashMap> listHashMapDTS = handleTestData.readTestData(locatorExcel, sheetName, true)
	
	@When("User choose desired record to recall")
	def chooseRecord() {
		//Check if there is any draft, if does then delete the drafts. If doesn't, choose payroll to edit
		boolean elementFound = false
		int index = 1

		while (true) {
			String editxpath = "(//span[@class='badge-orange'][normalize-space()='Draft'])[1]"
			TestObject approvalLimitField = new TestObject().addProperty('xpath', ConditionType.EQUALS, editxpath)

			if (WebUI.verifyElementPresent(approvalLimitField, 3, FailureHandling.OPTIONAL)) {
				WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//tbody/tr[1]/td[8]/div[1]//*[name()='svg']//*[name()='path' and contains(@fill,'currentCol')]"))
				WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[contains(.,'Delete Payroll')]"))
				WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Delete']"))

				elementFound = true
				index++
			} else {
				String testName = handleTestData.readFromCell(locatorExcel, sheetName, 0, 1)
				WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//td[contains(text(), '${testName}')]"))
				
				KeywordUtil.logInfo("Test Name = " + testName)
				break
			}
		}
	}
	
	@And("User choose adjust payroll and choose employee")
	def chooseAdjustment() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//a[.='Click here']"))
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[@class='btn btn-success']"))
		
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//span[.='Adjust Payroll']"))
	}
	
	@And("User change employee data")
	def changeEmployeeData() {
		//Get data from excel
		String employeeName = handleTestData.readFromCell(locatorExcel, sheetName, 1, 1)
		String extraDutyAllowanceEdit = handleTestData.readFromCell(locatorExcel, sheetName, 2, 1)
		
		WebUI.setText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='search']"), employeeName)
		
		KeywordUtil.logInfo("Employe Name to Change = " + employeeName)
		KeywordUtil.logInfo("Extra Duty Allowance Change = " + extraDutyAllowanceEdit)
		
		WebUI.sendKeys(null, Keys.chord(Keys.ENTER))
		WebUI.delay(2.5)
		
		WebUI.scrollToElement(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//div[@class='flex space-between p-4 border-t border-gray-300']/button[@class='btn btn-success btn-sm ml-auto']"), 2)
		WebUI.scrollToElement(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//div[@class='table-responsive max-h-full  shadow-none border-0']//tr[2]/td[.='SGD']"), 2)
		
		//Edit employee data
		WebUI.click(new TestObject('sgdInput').addProperty('xpath', ConditionType.EQUALS, "//table//tr[2]/td[4]//input"))
		WebUI.delay(2.5)
		WebUI.sendKeys(new TestObject('sgdInput').addProperty('xpath', ConditionType.EQUALS, "//table//tr[2]/td[4]//input"), Keys.chord(Keys.CONTROL, 'a'))
		WebUI.sendKeys(new TestObject('sgdInput').addProperty('xpath', ConditionType.EQUALS, "//table//tr[2]/td[4]//input"), Keys.chord(Keys.DELETE))

		WebUI.setText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//table//tr[2]/td[4]//input"), extraDutyAllowanceEdit)
		WebUI.delay(1.5)
	}
	
	@And("User save changed data")
	def saveChanges() {
		WebUI.click(new TestObject('sgdInput').addProperty('xpath', ConditionType.EQUALS, "//div[@class='table-responsive shadow-none mt-2']"))
		WebUI.delay(0.5)
		
		WebUI.click(new TestObject('sgdInput').addProperty('xpath', ConditionType.EQUALS, "//div[@class='flex space-between p-4 border-t border-gray-300']/button[@class='btn btn-success btn-sm ml-auto']"))
		WebUI.click(new TestObject('sgdInput').addProperty('xpath', ConditionType.EQUALS, "(//button[@class='btn btn-success btn-sm ml-auto'][normalize-space()='Next'])[1]"))		
	}
	
	@And("User verify net payment")
	def verifyNetPaymentChanged() {
		List<HashMap> listHashMapDTS = handleTestData.readTestData(locatorExcel, 'Sheet1', true)
		
		WebUI.scrollToElement(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//th[.='Net Payment']"), 2)
		KeywordUtil.logInfo("Net payment element is found")
		
		//Get total net payment from excel
		double totalNetPaymentExcel = 0.0
		
		for (int i = 0; i < listHashMapDTS.size(); i++) {
			HashMap hashDTS = listHashMapDTS.get(i)
			String netPaymentStr = hashDTS.get("Net Payment").toString().replace(",", "").trim()
		
			if (netPaymentStr.isNumber()) {
				double netPayment = Double.parseDouble(netPaymentStr)
				totalNetPaymentExcel += netPayment
				KeywordUtil.logInfo("Net Payment of " + hashDTS.get("Name").toString() + " = " + netPayment)
			} else {
				KeywordUtil.logInfo("Skipping invalid number = " + netPaymentStr)
			}
		}
		KeywordUtil.logInfo("Total Net Payment from Excel = " + totalNetPaymentExcel)
		
		//Add total payment in excel sheet1 and the changes
		double  extraDutyAllowanceEdit = handleTestData.readFromCell(locatorExcel, sheetName, 2, 1).toDouble()
		double  paymentChanged = totalNetPaymentExcel + extraDutyAllowanceEdit
		
		KeywordUtil.logInfo("Current Total = " + paymentChanged)
		
		//Change total net payment format from excel format to web format
		String formattedTotal = String.format("%,.2f", paymentChanged)
		
		KeywordUtil.logInfo("Formatted Current Total = " + paymentChanged)
		
		//Combine currency and net payment as verification the correct amount
		String webText = WebUI.getText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//span[.='" + curr + " " + formattedTotal + "']"), FailureHandling.STOP_ON_FAILURE)
		KeywordUtil.logInfo("Current Net Payment from Web = " + webText)
		
		String webNumberStr = webText.replaceAll("[^0-9.]", "")
		
		double totalNetPaymentWeb = Double.parseDouble(webNumberStr)
		
		WebUI.verifyEqual(paymentChanged, totalNetPaymentWeb)
		KeywordUtil.logInfo("Net Payment Match!")
	}
	
	@Then("User verify employees new payslip")
	def verifyNewPayslip() {
		//Verify new pay slip of the employee
		TestObject inputSearch = new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='search']")
		
		WebUI.executeJavaScript(
			"arguments[0].scrollIntoView({ behavior: 'auto', block: 'start' });",
			Arrays.asList(WebUI.findWebElement(inputSearch, 10))
		)
		
		String employeeName = handleTestData.readFromCell(locatorExcel, sheetName, 1, 1)
		WebUI.setText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='search']"), employeeName)
		WebUI.sendKeys(null, Keys.chord(Keys.ENTER))
		WebUI.delay(3)
		
		String generatedNet = WebUI.getText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//tbody/tr[2]/td[5]/span"), FailureHandling.STOP_ON_FAILURE)
		KeywordUtil.logInfo("Generated NetPayment = " + generatedNet)
		
		//Get payment changed excel
		double  extraDutyAllowanceEdit = handleTestData.readFromCell(locatorExcel, sheetName, 2, 1).toDouble()
		
		KeywordUtil.logInfo("Current Total = " + extraDutyAllowanceEdit)
		
		//Change total net payment format from excel format to web format
		String formattedTotal = String.format("%,.2f", extraDutyAllowanceEdit)
		String addedCurrency = curr + " " + formattedTotal
		
		KeywordUtil.logInfo("Formatted Current Total = " + addedCurrency)
		
		//Verify match with excel
		WebUI.verifyEqual(generatedNet, addedCurrency)
		KeywordUtil.logInfo("Net Payment Changed!")
		
		
		WebUI.scrollToElement(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//a[.='Team Payroll']"), 3)
		
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Release Payslips']"))
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[@class='btn btn-success']"))
		
		//Verify new payroll is released using dynamic time stamp
		WebUI.verifyElementPresent(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//p[starts-with(normalize-space(), 'Released on') and contains(., 'Click here to recall')]"),2)
		
	}
}






