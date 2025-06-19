import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

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

import com.handleElement.HandleTestData
import com.handleElement.VerifyElement

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

import org.openqa.selenium.Keys


class createPayroll {
	HandleTestData handleTestData = new HandleTestData()
	VerifyElement verifyElement = new VerifyElement()
	
	String locatorExcel = 'Test Data.xlsx'
	String sheetName = 'Sheet1'
	
	@Given("User click payroll menu and choose team payroll")
	def clickPayrollMenu() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//span[.='Payroll']"))
		WebUI.takeFullPageScreenshot()
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//a[.='Team Payroll']"))
		WebUI.takeFullPageScreenshot()
	}
	
	@When("User click Create New Payroll and input data")
	def clickCreateNew() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Create New Payroll']"))
		WebUI.takeFullPageScreenshot()
		WebUI.setText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='name']"), "Test 01")
		WebUI.takeFullPageScreenshot()
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Save']"))
		WebUI.takeFullPageScreenshot()
	}
	
	def getEmployeeName(HashMap hashDTS) {
		WebUI.setText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='search']"), hashDTS.get("Name"))
		WebUI.takeFullPageScreenshot()
		WebUI.sendKeys(null, Keys.chord(Keys.ENTER))
		WebUI.delay(2)
		WebUI.takeFullPageScreenshot()
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//tbody/tr[2]/td[1]/div[1]/input[1]"))
		WebUI.delay(1)
		WebUI.takeFullPageScreenshot()
	}	
	
	@And("User search and select employee")
	def searchEmployee() {	
		List<HashMap> listHashMapDTS = handleTestData.readTestData(locatorExcel, sheetName, true)

		for (int i = 0; i < listHashMapDTS.size(); i++) {
			HashMap hashDTS = listHashMapDTS.get(i)
			getEmployeeName(hashDTS)
		}
	}
	
	@And("User click add employees")
	def addEmployee() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//div[@class='flex p-4 border-b border-gray-300']/button[@class='btn btn-success btn-sm ml-auto']"))
		WebUI.delay(2)
		WebUI.takeFullPageScreenshot()
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//div[@class='flex space-between p-4 border-b border-gray-300']/button[@class='btn btn-success btn-sm ml-auto']"))
		WebUI.delay(1)
		WebUI.takeFullPageScreenshot()
	}
	
	@And("User verify Net Payment total")
	def verifyNetPayment() {
		List<HashMap> listHashMapDTS = handleTestData.readTestData(locatorExcel, sheetName, true)
		
		double totalNetPayment = 0.0
		
		for (int i = 0; i < listHashMapDTS.size(); i++) {
			HashMap hashDTS = listHashMapDTS.get(i)
			String netPaymentStr = hashDTS.get("Net Payment").toString().replace(",", "").trim()
		
			if (netPaymentStr.isNumber()) {
				double netPayment = Double.parseDouble(netPaymentStr)
				totalNetPayment += netPayment
				println("Net Payment of " + hashDTS.get("Name").toString() + " = " + netPayment)
			} else {
				println("Skipping invalid number = " + netPaymentStr)
			}
		}
		
		println("Total Net Payment = " + totalNetPayment)
				
		String webText = WebUI.getText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//span[.='SGD 8,862.75']"), FailureHandling.STOP_ON_FAILURE)
		String webNumberStr = webText.replaceAll("[^0-9.]", "")
		double totalNetPaymentWeb = Double.parseDouble(webNumberStr)
		
		WebUI.verifyEqual(totalNetPayment, totalNetPaymentWeb)
	}
	
}












