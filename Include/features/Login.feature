@Login
Feature: Login to Website

  @PositiveFlow
  Scenario Outline: User open website, input username and password then click login button
  Given User open HRE Website
  And User enter <username> and <password>
  When User click Login Button
  Then User logged in
  
  Examples:
  | username  								    | password		 		 |
  | security@hreasilygroup.com 		| U6h62QM452MGem3	 |