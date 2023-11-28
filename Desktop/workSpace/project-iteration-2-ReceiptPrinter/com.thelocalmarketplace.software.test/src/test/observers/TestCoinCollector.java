// Liam Major 30223023
// André Beaulieu, UCID 30174544

package test.observers;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import observers.payment.CoinCollector;
import stubbing.StubbedPaymentManager;
import stubbing.StubbedSystemManager;

public class TestCoinCollector {

	// vars
	private StubbedPaymentManager pmStub;
	private CoinCollector cc;

	@Before
	public void setup() {
		// This stub will never talk to its SystemManager,
		// so we can declare it as null.
		pmStub = new StubbedSystemManager().pmStub;
		cc = new CoinCollector(pmStub);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void testNullPaymentManager() {
		new CoinCollector(null);
	}

	/***
	 * Testing when the system detects a new valid coin has been added
	 */
	@Test
	public void validCoinDetectedTest() {
		// We don't care who the validator is, so it may be null.
		cc.validCoinDetected(null, new BigDecimal(2.0f));

		// We expect the PaymentManager to know about an coin of value 3
		assertTrue("The Payment Manager did not recieve the correct coin value",
				pmStub.notifyBalanceAddedValue.floatValue() == 2.0f);
	}
}
