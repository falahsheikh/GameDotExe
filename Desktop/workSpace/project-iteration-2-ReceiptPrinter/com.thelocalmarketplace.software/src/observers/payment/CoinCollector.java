// Jun Heo - 30173430
// Brandon Smith - 30141515
// Katelan Ng - 30144672
// Muhib Qureshi - 30076351
// Liam Major - 30223023

package observers.payment;

import java.math.BigDecimal;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import driver.Driver;
import managers.PaymentManager;

/**
 * This is the "Pay via Coin" use case.
 */

/**
 * <p>
 * This class is an observer that listens to the events emitted by a
 * {@link CoinValidator} object. The reason being is that there is no other way
 * to access the coins or cash inputed into the system from the
 * {@code SelfCheckoutStation}.
 * </p>
 * 
 * <p>
 * This class does nothing but listen for the {@link Coin}s emitted by a
 * {@link CoinValidator}.
 * </p>
 * 
 * @see CoinValidator
 * @see CoinValidatorObserver
 * @see Driver
 */
public class CoinCollector implements CoinValidatorObserver {

	// hardware references

	// object references
	private PaymentManager ref;

	// object ownership

	// vars

	/**
	 * Creates an observer to listen to the events emitted by a
	 * {@link CoinValidator}.
	 */
	public CoinCollector(PaymentManager pm) {
		// checking for null
		if (pm == null) {
			throw new NullPointerSimulationException("PaymentManager cannot be null.");
		}

		this.ref = pm;
	}

	/**
	 * This class does not listen for this event.
	 */
	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		return;
	}

	/**
	 * This class does not listen for this event.
	 */
	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		return;
	}

	/**
	 * This class does not listen for this event.
	 */
	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		return;
	}

	/**
	 * This class does not listen for this event.
	 */
	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		return;
	}

	/**
	 * Listens for the `validCoinDetected` event from a {@link CoinValidator} so
	 * that this object can infer the amount of coins or cash inputed into the
	 * {@link AbstractSelfCheckoutStation} and therefore its balance.
	 * 
	 * @see CoinValidator
	 * @see CoinValidatorObserver
	 */
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		this.ref.notifyBalanceAdded(value);
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		// TODO notify the parent object to dispense the invalid coin
	}

}
