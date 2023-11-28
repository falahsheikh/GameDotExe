// Liam Major 30223023
// André Beaulieu, UCID 30174544
// Nezla Annaisha 30123223

package managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import observers.order.BarcodeScannerObserver;
import observers.order.ScaleObserver;

public class OrderManager implements IOrderManager, IOrderManagerNotify {

	// hardware references
	protected AbstractSelfCheckoutStation machine;

	// object references
	protected SystemManager sm;

	// object ownership
	protected BarcodeScannerObserver main_bso;
	protected BarcodeScannerObserver handheld_bso;
	protected ScaleObserver baggingarea_so;

	// vars
	protected BigDecimal leniency;
	protected BigDecimal adjustment = BigDecimal.ZERO;
	protected List<Product> products;
	protected BigDecimal actualWeight = BigDecimal.ZERO;
	protected boolean noBaggingRequested = false;
	protected List<IOrderManagerNotify> listeners;
	protected BigDecimal scaleWeightLimit;

	/**
	 * This controls everything relating to adding and removing items from a
	 * customer's order.
	 * 
	 * @param sm       a reference to the parent {@link SystemManager} object
	 * @param leniency the leniency (tolerance) of the {@link ScaleObserver}s
	 * @throws InvalidArgumentSimulationException when either argument is null
	 */
	public OrderManager(SystemManager sm, BigDecimal leniency) {
		// checking arguments
		if (sm == null) {
			throw new InvalidArgumentSimulationException("the system manager cannot be null");
		}

		if (leniency == null) {
			throw new InvalidArgumentSimulationException("the leniency cannot be null");
		}

		// copying a reference to the system manager
		this.sm = sm;

		// saving the leniency
		this.leniency = leniency;

		// creating objects
		main_bso = new BarcodeScannerObserver(this);
		handheld_bso = new BarcodeScannerObserver(this);
		baggingarea_so = new ScaleObserver(this);
		products = new ArrayList<Product>();
		listeners = new ArrayList<IOrderManagerNotify>();
	}

	@Override
	public void configure(AbstractSelfCheckoutStation machine) {
		// saving reference
		this.machine = machine;

		// attaching observers
		this.machine.baggingArea.register(this.baggingarea_so);
		this.machine.mainScanner.register(this.main_bso);
		this.machine.handheldScanner.register(this.handheld_bso);

		// instantiating the scale limit
		scaleWeightLimit = this.machine.baggingArea.getMassLimit().inGrams();
	}

	@Override
	public void notifyBarcodeScanned(IBarcodeScanner scanner, Barcode barcode) {
		// checking for null
		if (barcode == null)
			throw new NullPointerSimulationException("invalid barcode was scanned");

		// getting the item
		BarcodedProduct prod = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);

		// checking for null
		if (prod == null)
			throw new NullPointerSimulationException("barcode doesn't match any known item");

		// adding the item to the order
		products.add(prod);
	}

	/**
	 * Returns the total mass of the order.
	 * 
	 * @return the total mass in grams.
	 */
	public BigDecimal getExpectedMass() {
		BigDecimal total = BigDecimal.ZERO;

		for (Product i : this.products) {
			// checking for null
			if (i == null) {
				throw new NullPointerSimulationException("tried to calculate mass of a null Product");
			}

			// if its a barcodedproduct, we can calculate mass directly
			if (i instanceof BarcodedProduct) {
				BarcodedProduct temp = (BarcodedProduct) i;
				total = total.add(new BigDecimal(temp.getExpectedWeight()));
				continue;
			}

			throw new UnsupportedOperationException("cannot calculate mass of a non-barcoded product");
		}

		return total;
	}

	/**
	 * Gets the current weight adjustment.
	 * 
	 * @return the weight adjustment
	 */
	protected BigDecimal getWeightAdjustment() {
		return this.adjustment;
	}

	/**
	 * Sets the weight adjustment.
	 * 
	 * @param a the weight adjustment
	 */
	protected void setWeightAdjustment(BigDecimal a) {
		this.adjustment = a;
	}

	@Override
	public BigDecimal getTotalPrice() throws NullPointerSimulationException {
		BigDecimal total = BigDecimal.ZERO;

		for (Product i : this.products) {
			// checking for null
			if (i == null) {
				throw new NullPointerSimulationException("tried to calculate mass of a null Product");
			}

			// calculating the price for a barcodeditem
			if (i instanceof BarcodedProduct) {
				// barcodeditems are always sold per unit, therefore we can just add the price
				// directly
				total = total.add(new BigDecimal(i.getPrice()));
			}

			// Temporary exception, while item types other than Barcode are unsupported.
			throw new UnsupportedOperationException();
		}

		// returning the price
		return total;
	}

	@Override
	public void addItemToOrder(Item item, ScanType method) throws OperationNotSupportedException {
		// checking for null
		if (item == null) {
			throw new NullPointerSimulationException("tried to scan a null item");
		}
		if (method == null) {
			throw new NullPointerSimulationException("a null scanning type was passed");
		}

		// figuring out how to scan the item
		if (item instanceof BarcodedItem) {
			this.addItemToOrder((BarcodedItem) item, method);
		}

		else if (item instanceof PLUCodedItem) {
			this.addItemToOrder((PLUCodedItem) item, method);
		}

		else {
			// if we get to here, an invalid item was scanned
			throw new InvalidArgumentSimulationException("invalid item was scanned");
		}
	}

	/**
	 * Simulates adding an {@link BarcodedItem} to the order.
	 * 
	 * @param item   the item to add
	 * @param method the method of scanning
	 */
	protected void addItemToOrder(BarcodedItem item, ScanType method) {
		switch (method) {
		case MAIN:
			this.machine.mainScanner.scan(item);
			break;
		case HANDHELD:
			this.machine.handheldScanner.scan(item);
			break;
		}

		// check if customer wants to bag item (bulky item handler extension)
		if (!noBaggingRequested) {
			this.machine.baggingArea.addAnItem(item);
		}

		// reset bagging request tracker for the next item
		noBaggingRequested = false;

	}

	/**
	 * Simulates adding an {@link PLUCodedItem} to the order.
	 * 
	 * @param item   the item to add
	 * @param method the method of scanning
	 */
	protected void addItemToOrder(PLUCodedItem item, ScanType method) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("adding items by PLU code is not supported yet");
	}

	/**
	 * Removes the specified item from the order, purging it from the list. Informs
	 * all listeners of this event. If the item cannot be found, or is null, then
	 * customer is displayed an error message.
	 * 
	 * @throws OperationNotSupportedException
	 */
	@Override
	public void removeItemFromOrder(Item item) throws OperationNotSupportedException {
		if (item instanceof BarcodedItem) {
			this.removeItemFromOrder((BarcodedItem) item);
		}

		else if (item instanceof PLUCodedItem) {
			this.removeItemFromOrder((PLUCodedItem) item);
		}

		else {
			// if we get to here, an invalid item was scanned
			throw new InvalidArgumentSimulationException("tried to remove a null from the order");
		}
	}

	/**
	 * This removes a {@link BarcodedItem} from the order and the bagging area.
	 * 
	 * @param item the {@link BarcodedItem} to remove
	 */
	protected void removeItemFromOrder(BarcodedItem item) {
		// getting the product
		BarcodedProduct prod = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(item.getBarcode());

		// checking if the item is actually in the order
		if (!this.products.contains(prod)) {
			throw new InvalidArgumentSimulationException("tried to remove item not in the order");
		}

		// removing
		if (this.products.remove(prod)) {
			// TODO: nothing listens for this event (yet)
			for (IOrderManagerNotify listener : listeners) {
				listener.onItemRemovedFromOrder(item);
			}

			// removing the item from the bagging area
			this.machine.baggingArea.removeAnItem(item);
		}
	}

	/**
	 * This removes a {@link PLUCodedItem} from the order and the bagging area.
	 * 
	 * @param item the {@link PLUCodedItem} to remove
	 */
	protected void removeItemFromOrder(PLUCodedItem item) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("removing PLU coded items is not supported yet");
	}

	/**
	 * This method handles a customer's request to add their own bags. The system
	 * gets the mass of the bags and updates the system adjustment and weight
	 * accordingly.
	 *
	 * @param item the bag that is going to be added
	 * @throws InvalidArgumentSimulationException
	 */
	@Override
	public void addCustomerBags(Item bags) throws InvalidArgumentSimulationException {
		// Get the weight of just the bags
		BigDecimal bagWeight = bags.getMass().inGrams();

		// Check if the weight of the bags are valid (greater than 0)
		if (bagWeight.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidArgumentSimulationException("No valid weight is detected for bags.");
		}

		// Check if the bag is too heavy based on scale weight limit
		if (bagWeight.compareTo(this.scaleWeightLimit) > 0) {
			handleBagsTooHeavy();
			return;
		}

		// Update adjustment for weight of bag
		this.adjustment = this.adjustment.add(bagWeight);

		// placing the bags in the bagging area
		this.machine.baggingArea.addAnItem(bags);
	}

	@Override
	public void handleBagsTooHeavy() {

	}

	@Override
	public List<Product> getProducts() throws NullPointerSimulationException {
		return products;
	}

	@Override
	public void onItemRemovedFromOrder(Item item) {
		// Note: Do Not Use! OrderManager calls this for others!
	}

	@Override
	public SessionStatus getState() {
		return sm.getState();
	}

	@Override
	public void onAttendantOverride() {
		// updating the mass
		adjustment = this.getExpectedMass().subtract(actualWeight);

		// unblocking the session
		unblockSession();
	}

	@Override
	public void notifyAttendant(String reason) {
		sm.notifyAttendant(reason);
	}

	/**
	 * This method handles a customer's request to skip bagging for a specific item.
	 * It adjusts the expected weight and updates the weight adjustment, blocks the
	 * session, and notifies the attendant.
	 *
	 * @param item the item for which bagging is skipped
	 */
	@Override
	public void onDoNotBagRequest(Item item) {
		if (item == null) {
			throw new InvalidArgumentSimulationException("a null item was requested not to be bagged");
		}

		// Set the flag for no bagging
		this.noBaggingRequested = true;

		switch (getState()) {
		case NORMAL:
			// Adjust the overall weight adjustment
			adjustment = this.adjustment.add(item.getMass().inGrams());

			// Notify Attendant about the request
			notifyAttendant("do not bag request was received");
			break;
		default:
			// Do nothing in other states
			break;
		}
	}

	@Override
	public void notifyMassChanged(ElectronicScaleListener scale, BigDecimal mass) {
		// checking for null
		if (mass == null)
			throw new NullPointerSimulationException("null mass was sent to OrderManager");

		// saving the mass
		this.actualWeight = mass;

		/**
		 * calculating the magnitude of the difference, the expected weight should be
		 * greater than the expected weight.
		 */
		BigDecimal expected = getExpectedMass().subtract(getWeightAdjustment());
		BigDecimal actual = actualWeight;
		BigDecimal difference = expected.subtract(actual).abs();

		// checking the weight difference
		checkWeightDifference(difference);
	}

	/**
	 * This separation is just to test resolving weight discrepancies.
	 */
	protected void checkWeightDifference(BigDecimal difference) {
		// testing whether or not to block or unblock the session
		switch (getState()) {
		case NORMAL:
			// blocking the session due to a discrepancy
			if (difference.compareTo(leniency) > 0) {
				blockSession();
			}
			break;
		case BLOCKED:
			// unblocking the session
			if (difference.compareTo(leniency) <= 0) {
				unblockSession();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void blockSession() {
		sm.blockSession();
	}

	public void registerListener(IOrderManagerNotify listener) {
		this.listeners.add(listener);
	}

	@Override
	public void unblockSession() {
		sm.unblockSession();
	}
}
