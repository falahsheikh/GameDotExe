// Jun Heo - 30173430
// Brandon Smith - 30141515
// Katelan Ng - 30144672
// Muhib Qureshi - 30076351
// Liam Major - 30223023

package driver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.naming.OperationNotSupportedException;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import managers.ScanType;
import managers.SessionStatus;
import managers.SystemManager;
import powerutility.PowerGrid;
import utils.CardHelper;
import utils.DatabaseHelper;
import utils.DriverHelper;
import utils.SelfCheckoutTypes;

// start session use case

public class Driver {

	// hardware references

	// object references

	// object ownership
	private SystemManager system;
	private AbstractSelfCheckoutStation machine;
	private CardIssuer cardIssuer;
	private static Scanner scanner = new Scanner(System.in);

	// vars
	private List<Item> items;
	private BigDecimal leniency = BigDecimal.ONE;
	private Card card;

	// denominations
	private final BigDecimal[] coinDenominations = new BigDecimal[] { new BigDecimal(0.01), new BigDecimal(0.05),
			new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1), new BigDecimal(2) };
	private final BigDecimal[] banknoteDenominations = new BigDecimal[] { new BigDecimal(5), new BigDecimal(10),
			new BigDecimal(20), new BigDecimal(50) };

	public Driver(SelfCheckoutTypes type) {
		// configuring the machine (need to do this before we initialize it)
		DriverHelper.configureMachine(coinDenominations, banknoteDenominations, 100, 1000);

		// create the machine itself
		this.machine = DriverHelper.createMachine(type);

		// creating the system manager
		cardIssuer = CardHelper.createCardIssuer();
		card = CardHelper.createCard(cardIssuer);
		this.system = new SystemManager(cardIssuer, leniency);
	}

	public void setup() {
		// configuring the system
		this.system.configure(this.machine);

		// so that no power surges happen
		PowerGrid.engageUninterruptiblePowerSource();

		// plug in and turn on the machine
		this.machine.plugIn(PowerGrid.instance());
		this.machine.turnOn();

		this.items = new ArrayList<Item>();
	}

	/**
	 * Adds a random item to the order.
	 * 
	 * @throws OperationNotSupportedException this should never happen
	 */
	private void scanItem() throws OperationNotSupportedException {
		System.out.println("Press Enter to Scan an Item\n" + "Note: Item is random.");
		scanner.nextLine();

		BarcodedItem newItem = DatabaseHelper.createRandomBarcodedItem();
		this.system.addItemToOrder(newItem, ScanType.MAIN);
		this.items.add(newItem);
	}

	/**
	 * Removes the specifed item from the order.
	 * 
	 * @throws OperationNotSupportedException this should never happen
	 */
	private void removeItem() throws OperationNotSupportedException {
		String msg = new String("Please Select Item to Remove:");
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof BarcodedItem) {
				BarcodedItem bi = (BarcodedItem) items.get(i);
				BarcodedProduct b = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(bi.getBarcode());

				// if product is null, throw error
				if (b == null)
					throw new NullPointerSimulationException("could not find");

				msg += "\n" + (i + 1) + ") " + b.getDescription();
			} else {
				// We currently don't accept items that aren't Barcoded
				throw new UnsupportedOperationException();
			}
		}
		System.out.println(msg);
		String input = scanner.nextLine();

		// store the choice of the user
		Integer choice;

		// try to parse integer
		try {
			choice = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Input must be a valid number!");
			return;
		}
		if (choice > items.size() || choice <= 0) {
			System.out.println("Input must be a valid number!");
			return;
		}

		Item chosenItem = items.get(choice - 1);
		this.system.removeItemFromOrder(chosenItem);
		this.items.remove(chosenItem);

	}

	/*
	 * This is the method for the payment process. While (remaining balance - price)
	 * > 0, prompts user to insert coins. Otherwise, asks if user wants a receipt,
	 * then ends session.
	 */
	private void payForOrder() {
		System.out.println("Total price: " + system.getTotalPrice() + " cents");
		System.out.println("Valid coins/cash:");

		// printing all valid denominations
		for (BigDecimal denom : this.banknoteDenominations) {
			System.out.println(denom.toPlainString());
		}

		for (BigDecimal denom : this.coinDenominations) {
			System.out.println(denom.toPlainString());
		}

		System.out.println(); // new line

		// loop to receive cash
		while (system.remainingBalance().compareTo(BigDecimal.ZERO) >= 0) {
			// printing
			System.out.println("Outstanding balance: " + system.remainingBalance() + " cents");
			System.out.println("Insert coin: ");
			BigDecimal value = scanner.nextBigDecimal();

			// in case of bad value
			if (value.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			try {
				// TODO: anything greater than the biggest coin denomination will be treated as
				// a banknote
			} catch (Exception e) {

			}
		}

		// user has paid, ask if the user wants a receipt
		System.out.print("Do you want your receipt printed? (y or n): ");
		String input = scanner.next();
		if (input.contains("y") || input.contains("Y")) {
			// TODO: print receipt
		}

		System.out.println("Session ended. Have a nice day!");
	}

	/**
	 * This displays the items in the order.
	 */
	private void displayItems() {
		String msg = new String("----- ORDER -----");
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof BarcodedItem) {
				BarcodedItem bi = (BarcodedItem) items.get(i);
				BarcodedProduct b = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(bi.getBarcode());

				// if product is null, throw error
				if (b == null)
					throw new NullPointerSimulationException("could not find");

				msg += "\n- " + b.getDescription();
			} else {
				// We currently don't accept items that aren't Barcoded
				throw new UnsupportedOperationException();
			}
		}
		msg += "\n----- END -----\n";
		System.out.println(msg);
		System.out.println("Press Enter to Continue");
		scanner.nextLine();
	}

	/*
	 * Main method of program
	 */
	public static void main(String[] args) throws OperationNotSupportedException {
		// create driver class
		Driver d = new Driver(DriverHelper.chooseMachineType());

		// setup driver class
		d.setup();

		// ready for customer input
		while (d.system.getState() != SessionStatus.PAID) {
			System.out.print(
					"1) Add an item\n2) Remove an Item\n3) Cause weight discrepancy\n4) Pay (Insert cash) and Checkout\n5) View order\n6) Cancel Session\n\n>> ");
			String input = scanner.nextLine();

			// store the choice of the user
			Integer choice;

			// try to parse integer
			try {
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				choice = 0;
			}

			switch (choice) {
			case 1: // add item
				d.scanItem();
				break;
			case 2: // remove item
				d.removeItem();
				break;
			case 3: // cause discrepancy
				break;
			case 4: // display total balance, update when coin is inserted
				d.payForOrder();
				break;
			case 5:
				d.displayItems();
				break;
			case 6: // user wants to quit session early
				System.out.println("Session ended. Have a nice day!");
				d.system.notifyPaid();
				break;
			default:
				break;
			}
		}

		// posting the credit card transactions
		d.system.postTransactions();

		// exiting
		System.out.flush();
		System.exit(0);
	}
}
