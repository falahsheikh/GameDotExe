// Liam Major 30223023
// André Beaulieu, UCID 30174544
// Nezla Annaisha 30123223

package managers;

import java.math.BigDecimal;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import observers.payment.BanknoteCollector;
import observers.payment.CoinCollector;
import observers.payment.PaymentType;
import observers.payment.ReceiptPrinterListenerStub;

import com.thelocalmarketplace.hardware.Product;


public class PaymentManager implements IPaymentManager, IPaymentManagerNotify {
	
	// hardware references
	protected AbstractSelfCheckoutStation machine;

	// object references
	protected SystemManager sm;
	protected CardIssuer issuer;
	

	// object ownership
	protected CoinCollector cc;
	protected BanknoteCollector bc;
	protected ReceiptPrinterListenerStub rpls;

	// vars
	protected BigDecimal payment;

	/**
	 * This controls everything relating to customer payment.
	 * 
	 * @param sm     a reference to the parent {@link SystemManager} object
	 * @param issuer a card issuer
	 * @throws InvalidArgumentSimulationException when either argument is null
	 */
	public PaymentManager(SystemManager sm, CardIssuer issuer) {
		// checking arguments
		if (sm == null) {
			throw new InvalidArgumentSimulationException("the system manager cannot be null");
		}

		if (issuer == null) {
			throw new InvalidArgumentSimulationException("the card issuer cannot be null");
		}

		// copying references
		this.sm = sm;
		this.issuer = issuer;

		// creating objects
		this.cc = new CoinCollector(this);
		this.bc = new BanknoteCollector(this);
		this.rpls = new ReceiptPrinterListenerStub(this);	
	}

	@Override
	public void configure(AbstractSelfCheckoutStation machine) {
		// saving reference
		this.machine = machine;

		// attaching observers
		this.machine.coinValidator.attach(this.cc);
		this.machine.banknoteValidator.attach(this.bc);
	}

	/**
	 * This is how you should tell the payment manager that there was payment added
	 * to the system.
	 * 
	 * @param value the value
	 */
	@Override
	public void notifyBalanceAdded(BigDecimal value) {
		this.payment = this.payment.add(value);
	}

	@Override
	public BigDecimal getCustomerPayment() {
		return this.payment;
	}

	@Override
	public void swipeCreditCard(Card creditCard) {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeDebitCard(Card debitCard) {
		// TODO Auto-generated method stub

	}

	public void notifyCreditCardSwipe(CardData creditcard) {
		// TODO Auto-generated method stub
	}

	public void notifyDebitCardSwipe(CardData creditcard) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insertCoin(Coin coin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertBanknote(Banknote banknote) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tenderChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public SessionStatus getState() {
		return sm.getState();
	}

	@Override
	public void blockSession() {
		sm.blockSession();
	}

	@Override
	public void unblockSession() {
		sm.unblockSession();
	}

	@Override
	public void notifyPaid() {
		sm.notifyPaid();
	}

	@Override
	public void recordTransaction(CardData card, long holdnumber, double amount) {
		sm.recordTransaction(card, holdnumber, amount);
	}

	@Override
	public void notifyAttendant(String reason) {
		sm.notifyAttendant(reason);
	}	


	@Override
	public void printReceipt(Product product, BigDecimal payment, PaymentType type, CardData card) {
		String receiptString;
		
   
		receiptString = "----- Receipt -----\n";
		receiptString += "Price: $" + product.getPrice();
		receiptString += "Payment: $" + payment;
		receiptString += "Payment Type: " + type;
		for(int i=0; i<receiptString.length();i++) {
			try {
				this.machine.printer.print(receiptString.charAt(i));
			} catch (EmptyDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OverloadedDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.machine.printer.cutPaper();
		
	}
	
	@Override
	public void notifyPaper(boolean hasPaper) {
		if(!hasPaper) {  
			// TODO: replace reference to MAXIMUM_PAPER with the constant called MAXIMUM_PAPER defined in AbstractReceiptPrinter.java
			int MAXIMUM_PAPER = this.machine.printer.paperRemaining()+1;
			try {
				this.machine.printer.addPaper(MAXIMUM_PAPER-this.machine.printer.paperRemaining());
			} catch (OverloadedDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	@Override
	public void notifyInk(boolean hasInk) {
		if(!hasInk) {
			// TODO: replace reference to MAXIMUM_INK with the constant called MAXIMUM_INK defined in AbstractReceiptPrinter.java
			int MAXIMUM_INK = this.machine.printer.inkRemaining()+1;
			try {
				this.machine.printer.addInk(MAXIMUM_INK-this.machine.printer.inkRemaining());
			} catch (OverloadedDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

}
