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

![Excel Location](https://i.imgur.com/0m3RZrl.png)

- **Sheet1**  Sheet
  - You can update this sheet contain as needed but do not change the header or cell format
  - A new column named `Currency` is included for future use

![Sheet 1](https://i.imgur.com/6rvkKVJ.png)

- **Test Name**  Sheet
  - The `Test Name` column: will be auto-filled when the script runs.  **Do not leave this field empty**, or the script will fail.  
  - `Employee`column: Name of the employee to adjust in Scenario 2  
  - `Extra Duty Allowance`: Adjusted amount of extra duty allowance, leave it as a string

![Test Name Sheet](https://i.imgur.com/kOU4cxI.png)

> ⚠️ Save and close the Excel file before running the test.  
> Opening excel file while running the script will cause error since I used a  FileInputStream library. Although I also use HashMap.
As comparison, HashMap won't cause an error if you open excel file while running the script because HashMap stored in memory so it has no I/O happening during test execution.

---

### 4. Open the Project in Katalon Studio

- Launch Katalon Studio
- Open the downloaded project folder (`hre_test`)
- Navigate to the **Test Suite** section

![Test Suite Navigate](https://i.imgur.com/0wOH7tx.png)

- Select and run the desired test scenarios

![Test Suite Run](https://i.imgur.com/DY6EOFU.png)

1. Create and Run Payment (Bulk): Contained Scenario 1a
2. Create and Run Payment (Single): Contained Scenario 1b, a variation scenario but with one-by-one iteration
3. Recall and Rerun Payroll: Contained Scenario 2

> Reports are only generated when test executed from the **Test Suite**.

By default, Katalon will run test suite using Chrome driver. To choose another driver:

![Test Suite Run Another Driver](https://i.imgur.com/YRVjpmD.png)
---

### 5. Browser Recommendation

Run tests using **Google Chrome** for best compatibility and stability.

- Chrome is the most commonly used browser and ensures the widest coverage
- It also has stable WebDriver support

Make sure your ChromeDriver (or other driver you prefered) is up-to-date:  Tools > Update WebDriver > Chrome

![WebDriver Update](https://i.imgur.com/9TDuMS1.png)
![WebDriver Updated](https://i.imgur.com/D6gVIfd.png)

---

### 6. Check the Reports

After the test finishes:

Reports are located at:  
`hre_test/Reports/[timestamp]/[Test Suite name]/[timestamp]`

![Report Location](https://i.imgur.com/cZijyHV.png)

![Report PDF](https://i.imgur.com/5LVzW3k.png)

![Report HTML](https://i.imgur.com/kxmTHP0.png)

![Report CSV](https://i.imgur.com/EDWyG0l.png)

Each test run includes:
- `.html` report
- `.csv` report
- `.pdf` report
- Screenshot images (`.png`)

Make sure you check report settings: Project > Settings > Plugins > Report, set report format as desired

![Driver Option](https://i.imgur.com/Vr0QeV8.png)
---

## Future Development Plans

1. Add iteration for Scenario 2 (bulk adjustments)
2. Automatically delete released payslip records in the **Team Payroll** table for the same month (to avoid incorrect Net Payment)
3. Add cross-platform verification for downloaded payslips

---

## Evidence
GDrive (contain success test execution in video format): https://bit.ly/hre_test_evidence

---