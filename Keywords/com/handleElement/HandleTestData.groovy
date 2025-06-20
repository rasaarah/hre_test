package com.handleElement

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.reader.ExcelFactory
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.formula.EvaluationCell
import org.apache.poi.ss.formula.EvaluationSheet
import org.apache.poi.ss.formula.EvaluationWorkbook
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet
import org.apache.poi.ss.formula.ptg.Area3DPtg
import org.apache.poi.ss.formula.ptg.Area3DPxg
import org.apache.poi.ss.formula.ptg.Ptg
import org.apache.poi.ss.formula.ptg.Ref3DPtg
import org.apache.poi.ss.formula.ptg.Ref3DPxg
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CellValue
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellReference
import org.apache.poi.ss.util.CellUtil
import java.util.Map.Entry

import internal.GlobalVariable

public class HandleTestData {
	static String pathDataFile = RunConfiguration.getProjectDir() + "/Data Files/"

	public List<HashMap> readTestData(String pathFile, String sheetName, boolean isUsingFirstRowAsHeader) throws IOException {
		int i,j
		String pathData = RunConfiguration.getProjectDir() + "/Data Files/"
		String pathFileTestData = pathData + pathFile
		List<HashMap> listHashMap = new ArrayList<HashMap>()

		try {
			TestData data = ExcelFactory.getExcelDataWithDefaultSheet(pathFileTestData, sheetName, isUsingFirstRowAsHeader)

			List<List<Object>> listAllData = data.getAllData()
			KeywordUtil.logInfo("Size data : " + listAllData.size())

			String[] getHeaderColumnName = data.getColumnNames()

			for (i = 0; i < listAllData.size(); i++) {
				List<Object> getListData = listAllData.get(i)
				HashMap<Object, Object> hashMapSetKeyAndValueFromTestData = new HashMap<Object, Object>()

				for (j = 0; j < getHeaderColumnName.length; j++) {
					String keyName = getHeaderColumnName[j]
					String valueData = getListData.get(j)

					if (!keyName.equals(null) || !keyName.equals("")) {
						if (valueData.equals(null) && !keyName.equals(null)) {
							hashMapSetKeyAndValueFromTestData.put(keyName, "")
						} else {
							hashMapSetKeyAndValueFromTestData.put(keyName, valueData)
						}
					}
				}

				listHashMap.add(hashMapSetKeyAndValueFromTestData)
			}
		} catch (Exception e) {
			KeywordUtil.markFailedAndStop(e.getMessage())
		}
		return listHashMap
	}

	public String readFromCell(String pathFile, String sheetName, int colIndex, int rowIndex) {
		String pathData = RunConfiguration.getProjectDir() + "/Data Files/";
		String pathFileTestData = pathData + pathFile;
		File fileExcel = new File(pathFileTestData);
		FileInputStream fis = new FileInputStream(fileExcel);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);

		String cellValue = "";

		try {
			// Get the specified row and cell
			Row row = sheet.getRow(rowIndex);
			Cell cell = (row != null) ? row.getCell(colIndex) : null;

			// Check if cell is not null and retrieve its value
			if (cell != null) {
				switch (cell.getCellType()) {
					case 1: // Corrected enum usage
						cellValue = cell.getStringCellValue();
						break;
					case 2: // Corrected enum usage
						cellValue = String.valueOf(cell.getNumericCellValue());
						break;
					case 3: // Corrected enum usage
						cellValue = String.valueOf(cell.getBooleanCellValue());
						break;
					case 4: // Corrected enum usage
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						CellValue evaluatedCellValue = evaluator.evaluate(cell);
						switch (evaluatedCellValue.getCellType()) {
							case 1:
								cellValue = evaluatedCellValue.getStringValue();
								break;
							case 2:
								cellValue = String.valueOf(evaluatedCellValue.getNumberValue());
								break;
							case 3:
								cellValue = String.valueOf(evaluatedCellValue.getBooleanValue());
								break;
							default:
								cellValue = "";
						}
						break;
					default:
						cellValue = "";
				}
			} else {
				KeywordUtil.logInfo("Cell at row " + rowIndex + ", column " + colIndex + " is empty or null.");
			}
		} catch (Exception e) {
			KeywordUtil.logInfo("Error reading cell: " + e.getMessage());
		} finally {
			workbook.close();
			fis.close();
		}

		return cellValue.trim();
	}
	
	public void writeToCell(String pathFile, String sheetName, String value, int indexColumnDestination, int rowIndex) {
		String pathData = RunConfiguration.getProjectDir() + "/Data Files/"
		String pathFileTestData = pathData + pathFile
		File fileExcel = new File(pathFileTestData)
		FileInputStream fis = new FileInputStream(fileExcel)
		XSSFWorkbook workbook = new XSSFWorkbook(fis)
		XSSFSheet sheet = workbook.getSheet(sheetName)
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator()
		KeywordUtil.logInfo("Open workbook")

		try {
			KeywordUtil.logInfo("NILAI CELL SEBELUM : " + sheet.getRow(rowIndex).getCell(indexColumnDestination).toString())
			sheet.getRow(rowIndex).createCell(indexColumnDestination).setCellValue(value)
			KeywordUtil.logInfo("NILAI CELL SETELAH : " + sheet.getRow(rowIndex).getCell(indexColumnDestination).toString())
			workbook.setForceFormulaRecalculation(true)
			evaluator.evaluateFormulaCell(sheet.getRow(rowIndex).getCell(indexColumnDestination))
		} catch(Exception e) {
			KeywordUtil.logInfo(e.getMessage())
		} finally {
			FileOutputStream fos = new FileOutputStream(fileExcel)
			workbook.write(fos)
			fos.close()
			workbook.close()
			KeywordUtil.logInfo("Close workbook")
		}
	}

}
