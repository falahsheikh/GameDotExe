# About

The second iteration of the project woooooooooooo.

>Main page is [here](https://d2l.ucalgary.ca/d2l/le/content/544368/viewContent/5929785/View).

## Meeting Minutes
- Here is the meeting minutes for November 7: https://docs.google.com/document/d/1Dv5Zgi6Y99dIDw906sgWSnd7t-U5k9ypVhwesUrOLqE/edit?usp=drivesdk

## Project Structure

- the given code from Walker is in `com.thelocalmarketplace.hardware_0.2.2/` (more bug fixes weeeeeeeeee)
- the code (e.g., the software) that uses the original code base is in `com.thelocalmarketplace.software/`
- the testing suite for said software is in `com.thelocalmarketplace.software.test/`

## Due Dates

>!!! IMPORTANT !!!

- Software: November 15
- Testing: November 18
- Final Review: November 19
- Submission: November 20

## Requirements

>All code is based off of `com.thelocalmarketplace.hardware_0.2` in the Git repository, you **cannot** edit the code in here.

- **cannot** alter the source code from the base project
- create a software project (basically the driver for the code) called `com.thelocalmarketplace.software`
	- must depend on the hardware project
- create a testing project (same thing as assignment 3) called `com.thelocalmarketplace.software.test`
	- must depend on the hardware and software projects
- must implement the following use cases (broken down into various categories) 
	- System Maintenance (Liam), including functionality from previous use cases
		- weight discrepancy
		- pay with coin
  		- add item via. barcode scan
		- start session
	- Order Management (TL: Joshua)
		- remove item (Andre)
		- add own bags (Kear Sang)
  			- bags too heavy use case (Sam)
		- handle bulky item (Nezla)
	- Payment Management (TL: Liam)
		- pay with coin (have to tender change now) (Myra)
		- pay with banknote (and tender change) (Jason)
		- pay with credit/debit via. card swipe (Falah & Simon)
		- create receipt (Falah)
	- Testing (JUnit 4)
        	- each implementor of the use case should develop their own test case
   		- Gurgit will implement test cases for the add item via. XXX use cases
- using the `AbstractSelfCheckoutStation` class so we don't actually have to worry about the varying levels
- need to begin **every** source file with name(s) and UCID number(s)
- (optional) can submit a supplementary one page explanation of how it works
- **have to provide a Git commit history**

## Submission

- the `com.thelocalmarketplace.software` as a zip file
- the `com.thelocalmarketplace.software.test` as a zip file
- (optional) the textual explanation
- the Git commit history
