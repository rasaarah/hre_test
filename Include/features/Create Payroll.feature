@CreatePayroll
Feature: Create Payroll

  @CreatePayrollPositiveFlowBulk
  Scenario Outline: User create new payroll for existing employee
	  Given User click payroll menu and choose team payroll
	  When User click Create New Payroll and input data
	  And User search and select employee
	  And User click add employees
	  And User click Next to Release Payroll
	  And User verify Net Payment total
	  
	  Examples:
	  | username  								    | password		 		 |
	  | security@hreasilygroup.com 		| U6h62QM452MGem3	 |
  
  @CreatePayrollPositiveFlowSingle
  Scenario Outline: User create new payroll for existing employee
	  Given User click payroll menu and choose team payroll
	  When User click Create New Payroll and input data single
	  
	  Examples:
	  | username  								    | password		 		 |
	  | security@hreasilygroup.com 		| U6h62QM452MGem3	 |
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  