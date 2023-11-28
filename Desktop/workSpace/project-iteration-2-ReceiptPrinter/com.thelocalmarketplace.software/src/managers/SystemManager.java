// Liam Major 30223023
// André Beaulieu, UCID 30174544
// Nezla Annaisha

package managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import observers.payment.PaymentType;
import utils.Pair;

/**
 * This class is meant to contain everything that is hardware related, this acts
 * as the main interface to the hardware and the underlying order management and
 * payment management systems.
 * 
 * This delegates all functionality (with some exceptions) to the other manager
 * classes.
 */
public class SystemManager implements ISystemManager, IPaymentManager, IOrderManager {

	// hardware references
	protected AbstractSelfCheckoutStation machine;

	// object references

	// object ownership
	protected PaymentManager pm;
	protected OrderManager om;

	// vars
	protected SessionStatus state;
	protected CardIssuer issuer;
	protected Map<CardData, List<Pair<Long, Double>>> records;

	/**
	 * This object is responsible for the needs of the customer. This is how the
	 * customer is supposed to interact with the system.
	 * 
	 * @param issuer   a card issuer
	 * @param leniency the scale leniency
	 * @throws InvalidArgumentSimulationException when either argument is null
	 */
	public SystemManager(CardIssuer issuer, BigDecimal leniency) {
		// checking arguments
		if (issuer == null) {
			throw new InvalidArgumentSimulationException("the card issuer cannot be null");
		}

		if (leniency == null) {
			throw new InvalidArgumentSimulationException("the leniency cannot be null");
		}

		// setting the state
		setState(SessionStatus.NORMAL);

		// creating the managers
		this.pm = new PaymentManager(this, issuer);
		this.om = new OrderManager(this, leniency);
		this.issuer = issuer; // a reference to the bank
	}

	@Override
	public void configure(AbstractSelfCheckoutStation machine) {
		// saving a reference
		this.machine = machine;

		// configuring the managers
		this.pm.configure(this.machine);
		this.om.configure(this.machine);
	}

	/**
	 * This calculates the outstanding balance the customer must pay for their
	 * order.
	 * 
	 * @return the amount of money still owed by the customer
	 */
	public BigDecimal remainingBalance() {
		return this.getTotalPrice().subtract(this.getCustomerPayment());
	}

	@Override
	public void removeItemFromOrder(Item item) throws OperationNotSupportedException {
		// not restricting this function, this is used to resolve discrepancies
		this.om.removeItemFromOrder(item);
	}

	@Override
	public void insertCoin(Coin coin) {
		// not performing action if session is blocked
		if (getState() != SessionStatus.NORMAL)
			return;

		this.pm.insertCoin(coin);
	}

	@Override
	public void insertBanknote(Banknote banknote) {
		// not performing action if session is blocked
		if (getState() != SessionStatus.NORMAL)
			return;

		this.pm.insertBanknote(banknote);
	}

	@Override
	public BigDecimal getTotalPrice() throws NullPointerSimulationException {
		return this.om.getTotalPrice();
	}

	@Override
	public BigDecimal getCustomerPayment() {
		return this.pm.getCustomerPayment();
	}

	@Override
	public void swipeCreditCard(Card creditCard) {
		// not performing action if session is blocked
		if (state == SessionStatus.BLOCKED)
			return;

		this.pm.swipeCreditCard(creditCard);
	}

	@Override
	public void swipeDebitCard(Card debitCard) {
		// not performing action if session is blocked
		if (state == SessionStatus.BLOCKED)
			return;

		this.pm.swipeDebitCard(debitCard);
	}

	@Override
	public void tenderChange() {
		if (getState() != SessionStatus.NORMAL)
			return;

		this.pm.tenderChange();
	}

	@Override
	public BigDecimal getExpectedMass() {
		return this.om.getExpectedMass();
	}

	@Override
	public List<Product> getProducts() throws NullPointerSimulationException {
		return this.om.getProducts();
	}

	@Override
	public void onAttendantOverride() {
		this.om.onAttendantOverride();
	}

	@Override
	public void onDoNotBagRequest(Item item) {
		this.om.onDoNotBagRequest(item);
	}

	@Override
	public void addItemToOrder(Item item, ScanType method) throws OperationNotSupportedException {
		// not performing action if session is blocked
		if (getState() != SessionStatus.NORMAL)
			return;

		this.om.addItemToOrder(item, method);
	}

	@Override
	public void blockSession() {
		if (getState() == SessionStatus.PAID) {
			throw new RuntimeException("cannot block from state PAID");
		}

		setState(SessionStatus.BLOCKED);
	}

	@Override
	public void unblockSession() {
		if (getState() == SessionStatus.PAID) {
			throw new RuntimeException("cannot unblock from state PAID");
		}

		setState(SessionStatus.NORMAL);
	}

	protected void setState(SessionStatus state) {
		if (state == null) {
			throw new IllegalArgumentException("cannot set the state of the manager to null");
		}
		
		this.state = state;
	}

	@Override
	public SessionStatus getState() {
		return state;
	}

	@Override
	public void notifyPaid() {
		if (getState() == SessionStatus.BLOCKED) {
			throw new RuntimeException("cannot set state from BLOCKED to PAID");
		}

		setState(SessionStatus.PAID);
	}

	@Override
	public void postTransactions() {
		if (getState() != SessionStatus.PAID) {
			throw new RuntimeException("cannot post transactions while not in the PAID state");
		}

		for (CardData card : records.keySet()) {
			// getting the list of records
			List<Pair<Long, Double>> arr = records.get(card);

			// posting them
			for (Pair<Long, Double> pair : arr) {
				issuer.postTransaction(card.getNumber(), pair.getKey(), pair.getValue());
			}
		}
	}

	@Override
	public void recordTransaction(CardData card, long holdnumber, double amount) {
		if (getState() != SessionStatus.NORMAL) {
			throw new RuntimeException("cannot record a transaction when BLOCKED");
		}

		Pair<Long, Double> pair = new Pair<>(holdnumber, amount);

		// creating the list if not present
		List<Pair<Long, Double>> ar = records.getOrDefault(card, new ArrayList<>());

		// adding the transaction
		ar.add(pair);

		// recording the transaction
		records.put(card, ar);
	}

	@Override
	public void notifyAttendant(String reason) {
		System.out.printf("[ATTENDANT NOTIFY]: %s", reason);
	}

	@Override
	public void printReceipt(Product product, BigDecimal payment, PaymentType type, CardData card) {
		 // Delegates the actual printing of the receipt to the pm (PrintManager) object.
    		// This allows for a centralized and modular printing functionality.
		pm.printReceipt(product, payment, type, card);

	}
  
	public void addCustomerBags(Item bags) {
		if (getState() != SessionStatus.NORMAL) {
			throw new RuntimeException("cannot add customer bags when not in a normal state");
		}

		this.om.addCustomerBags(bags);
	}

	@Override
	public void handleBagsTooHeavy() {
		this.om.handleBagsTooHeavy();
	}
}
