# HRE Test Automation Setup

This project contains automated tests for payroll-related scenarios using Katalon Studio and Groovy.

---

## Tools & Technologies

- **Katalon Studio v8.x**  
  Recommended version: 8.x. Newer versions require an Enterprise account to access key features such as feature and test case limits.  
  Katalon is simple, low in complexity, and familiar. It has a supportive community and comes with built-in reporting (with screenshots) without the need for additional plugins. It also supports cross-platform testing in a single tool.

- **Groovy**  
  Groovy is the scripting language used in this project. It has Java-like syntax but is simpler, shorter, and easier to maintain. It's ideal for writing and maintaining test scripts.

---

## Setup Instructions

### 1. Install Dependencies

- Install **Katalon Studio v8.x**  
  Download from: https://github.com/katalon-studio/katalon-studio/releases  
  ⚠️ You must have a Katalon Studio account

- Install **Java 11**

---

### 2. Download the Project

Clone or download the project from GitHub:  
https://github.com/rasaarah/hre_test

---

### 3. Configure Test Data

>Open the following file:  
`hre_test/Data Files/Test Data.xlsx`

- **Sheet1**  
  - You can update this sheet as needed.  
  - A new column named `Currency` is included for future use.

- **Test Name Sheet**  
  - The `Test Name` column: will be auto-filled when the script runs.  **Do not leave this field empty**, or the script will fail.  
  - `Employee`column: Name of the employee to adjust in Scenario 2  
  - `Extra Duty Allowance`: Adjusted amount of extra duty allowance

> ⚠️ Save and close the Excel file before running the test.  
> Opening excel file while running the script will cause error since I used a  FileInputStream library. Although I also use HashMap.
As comparison, HashMap won't cause an error if you open excel file while running the script because HashMap stored in memory so it has no I/O happening during test execution.

---

### 4. Open the Project in Katalon Studio

- Launch Katalon Studio
- Open the downloaded project folder (`hre_test`)
- Navigate to the **Test Suite** section
- Select and run the desired test scenarios

> Reports are only generated when test execute from the **Test Suite**.

---

### 5. Browser Recommendation

Run tests using **Google Chrome** for best compatibility and stability.

- Chrome is the most commonly used browser and ensures the widest coverage
- It also has stable WebDriver support

Make sure your ChromeDriver is up-to-date:  Tools > Update WebDriver > Chrome

---

### 6. Check the Reports

After the test finishes:

Reports are located at:  
`hre_test/Reports/[timestamp]/[Test Suite name]/[timestamp]`

Each test run includes:

- `.html` report
- `.csv` report
- `.pdf` report
- Screenshot images (`.png`)

Make sure you check report settings: Project > Settings > Plugins > Report, check report format as desired

---

## Future Development Plans

1. Add iteration for Scenario 2 (bulk adjustments)
2. Automatically delete released payslip records in the **Team Payroll** table for the same month (to avoid incorrect Net Payment)
3. Add cross-platform verification for downloaded payslips

---

## Evidence
GDrive (contain success test execution in video format): https://drive.google.com/drive/folders/1RHT96u7UoRQoxJFjdih8kYXBQO83_d--?usp=sharing

---