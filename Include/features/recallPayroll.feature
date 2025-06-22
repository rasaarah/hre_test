Feature: Recall Payroll

  @RecallPayroll
  Scenario Outline: User recall and edit employee summary
	  Given User open HRE Website
	  And User enter <username> and <password>
	  When User click Login Button
	  Then User logged in
	  
	  Given User click payroll menu and choose team payroll
	  When User choose desired record to recall
	  And User choose adjust payroll and choose employee
	  And User change employee data
	  And User save changed data
	  And User verify net payment
	  Then User verify employees new payslip
#	  Then User Logout
	  
	  Examples:
	  | username  								    | password		 		 |
	  | security@hreasilygroup.com 		| U6h62QM452MGem3	 |