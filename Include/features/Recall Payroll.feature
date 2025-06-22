Feature: Recall Payroll and edit employee salary data

  @RecallPayrollPositiveScenario
  Scenario Outline: User recall and edit employee summary
	  Given User click payroll menu and choose team payroll
	  When User choose desired record to recall
	  And User choose adjust payroll and choose employee
	  And User change employee data
	  And User save changed data
	  And User verify net payment
	  Then User verify employees new payslip
	  
	  Examples:
	  | username  								    | password		 		 |
	  | security@hreasilygroup.com 		| U6h62QM452MGem3	 |