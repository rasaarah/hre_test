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

	def createNewPayroll() {
		KeywordUtil.logInfo("No draft exists, creating one")
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Create New Payroll']"))
		WebUI.takeFullPageScreenshot()
		WebUI.setText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='name']"), "Test 01")
		WebUI.takeFullPageScreenshot()
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Save']"))
		WebUI.takeFullPageScreenshot()
	}

	@Given("User click payroll menu and choose team payroll")
	def clickPayrollMenu() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//span[.='Payroll']"))
		WebUI.takeFullPageScreenshot()
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//a[.='Team Payroll']"))
		WebUI.takeFullPageScreenshot()
	}

	@When("User click Create New Payroll and input data")
	def clickCreateNew() {
		//Check if there is any draft, if does then delete the drafts. If doesn't, then create new payroll
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
				createNewPayroll()
				break
			}
		}
	}

	//Get data based on excel in Data Files, using specific header to locate
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

	//Iteration to fetch employee name based on data size in data file used (all)
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

	//Get total net payment amount based on data file used
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
				KeywordUtil.logInfo("Net Payment of " + hashDTS.get("Name").toString() + " = " + netPayment)
			} else {
				KeywordUtil.logInfo("Skipping invalid number = " + netPaymentStr)
			}
		}

		KeywordUtil.logInfo("Total Net Payment Excel = " + totalNetPayment)

		//Change total net payment format from excel -> web
		String formattedTotal = String.format("%,.2f", totalNetPayment)

		//Get Currency used based on data files
		String curr = ""
		for (int i = 0; i < listHashMapDTS.size(); i++) {
			HashMap hashDTS = listHashMapDTS.get(i)
			curr = hashDTS.get("Curr")
		}

		//Combine currency and net payment as verification the correct amount
		String webText = WebUI.getText(
				new TestObject().addProperty('xpath', ConditionType.EQUALS, "//span[.='" + curr + " " + formattedTotal + "']"),
				FailureHandling.STOP_ON_FAILURE
				)

		KeywordUtil.logInfo("Net Payment Web = " + webText)

		String webNumberStr = webText.replaceAll("[^0-9.]", "")
		double totalNetPaymentWeb = Double.parseDouble(webNumberStr)

		WebUI.verifyEqual(totalNetPayment, totalNetPaymentWeb)
	}

	@And("User click Next to Release Payroll")
	def releasePayroll() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "(//button[@class='btn btn-success btn-sm ml-auto'][normalize-space()='Next'])[1]"))
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[normalize-space()='Release Payslips']"))
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[@class='btn btn-success']"))

		//Verify new payroll is released using dynamic time stamp
		WebUI.verifyElementPresent(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//p[starts-with(normalize-space(), 'Released on') and contains(., 'Click here to recall')]"),2)
	}


	@Then("User Logout")
	def userLogout() {
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//button[@class='flex text-sm rounded-full focus:outline-none']/img[1]"))
		WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//a[.='Sign Out']"))

		//Verify user is logged out
		WebUI.verifyElementPresent(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//input[@id='id_email']"),2)

		WebUI.closeBrowser()
	}

	//========single
	def getNetPaymentSingle(HashMap hashDTS) {
		List<HashMap> listHashMapDTS = handleTestData.readTestData(locatorExcel, sheetName, true)

		String netPaymentSingle = hashDTS.get("Net Payment")

		return netPaymentSingle
	}

	@When("User click Create New Payroll and input data single")
	def callCreateNew() {
		List<HashMap> listHashMapDTS = handleTestData.readTestData(locatorExcel, sheetName, true)

		for (int i = 0; i < listHashMapDTS.size(); i++) {
			HashMap hashDTS = listHashMapDTS.get(i)

			clickCreateNew()
			getEmployeeName(hashDTS)
			addEmployee()
			releasePayroll()
			
			//Get Net Payment of Employee
			String netPaymentStr = hashDTS.get("Net Payment").toString().replace(",", "").trim()
			KeywordUtil.logInfo("Net Payment of " + hashDTS.get("Name").toString() + " = " + netPaymentStr)

			//Change total net payment format from excel -> web
			double number = Double.parseDouble(netPaymentStr)
			
			String formattedSingle = String.format("%,.2f", number)
			
			KeywordUtil.logInfo("Net Payment Excel Formatted = " + formattedSingle)
	
			//Get Currency used based on data files
			String curr = ""
			curr = hashDTS.get("Curr")
			
			String combinedNetPaymentExcel = curr + " " + formattedSingle
	
			//Combine currency and net payment as verification the correct amount
			String webText = WebUI.getText(new TestObject().addProperty('xpath', ConditionType.EQUALS, "(//td[starts-with(normalize-space(), 'SGD ')])[10]"))
	
			KeywordUtil.logInfo("Net Payment Web = " + webText)
				
			WebUI.verifyEqual(combinedNetPaymentExcel, webText)
			
			//Back to main team payroll page
			WebUI.click(new TestObject().addProperty('xpath', ConditionType.EQUALS, "//a[.='Team Payroll']"))


		}
	}
}












