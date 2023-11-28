// Liam Major 30223023

package managers;

import java.math.BigDecimal;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.Product;

import observers.payment.PaymentType;

/**
 * A unified interface of what the payment manager should and should not do.
 */
public interface IPaymentManager extends IManager {
	/**
	 * Returns the amount of money the customer has inputted into the system
	 * 
	 * @return the amount of money
	 */
	BigDecimal getCustomerPayment();

	/**
	 * Allows the customer to swipe their credit card
	 * 
	 * @param creditCard a credit card
	 */
	void swipeCreditCard(Card creditCard);

	/**
	 * Allows the customer to swipe their debit card
	 * 
	 * @param debitCard a debit card
	 */
	void swipeDebitCard(Card debitCard);

	/**
	 * Allows the customer to insert a coin into the system.
	 * 
	 * @param coin a coin
	 */
	void insertCoin(Coin coin);

	/**
	 * Allows the customer to insert a banknote into the system.
	 * 
	 * @param banknote a banknote
	 */
	void insertBanknote(Banknote banknote);

	/**
	 * TODO: implement this method
	 */
	void tenderChange();

	/**
	 * This method sets the internal state to {@code PAID}, should only be used
	 * internally.
	 */
	void notifyPaid();

	/**
	 * Stores a transaction for the manager.
	 * 
	 * @param card       the card data
	 * @param holdnumber the hold number
	 * @param amount     the price of the order
	 */
	void recordTransaction(CardData card, long holdnumber, double amount);

	/**
	 * Prints a receipt for a given set of products and payment details.
	 *
	 * @param products The products for which the receipt is generated.
	 * @param payment The total payment amount made for the products.
	 * @param type The payment type used (e.g., cash, credit card).
	 * @param card The card data associated with the payment (if applicable, otherwise null).
	 */
	public void printReceipt(Product products, BigDecimal payment, PaymentType type, CardData card);

	

}
