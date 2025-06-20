@CreatePayroll
Feature: Create Payroll

  @CreatePayrollPositiveFlow
  Scenario Outline: User create payroll for existing
  Given User open HRE Website
  And User enter <username> and <password>
  When User click Login Button
  Then User logged in
  
  Given User click payroll menu and choose team payroll
  When User click Create New Payroll and input data
  And User search and select employee
  And User click add employees
  And User verify Net Payment total
  And User click Next to Release Payroll
  #Then User Logout
  
  Examples:
  | username  								    | password		 		 |
  | security@hreasilygroup.com 		| U6h62QM452MGem3	 |