// Liam Major 30223023
//Katelan Ng 30144672
//Coverage: 94.6%

package test.observers;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import observers.order.ScaleObserver;
import stubbing.StubbedOrderManager;
import stubbing.StubbedSystemManager;

public class TestScaleObserver {

	// vars
	private StubbedOrderManager om;
	private StubbedSystemManager sm;
	private ScaleObserver so;

	@Before
	public void setup() {

		sm = new StubbedSystemManager();
		om = new StubbedOrderManager(sm, BigDecimal.ZERO);

		// setting the internal order manager
		sm.setOrderManager(om);

		so = new ScaleObserver(om);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void testNullOrderManager() {
		new ScaleObserver(null);
	}

	/**
	 * Testing that the mass observed by the scale was passed correctly to the
	 * OrderManager.
	 */
	@Test
	public void testNotifyOrderManagerMassChange() {
		Mass mass = new Mass(1);

		// We don't care who the validator is, so it may be null.
		so.theMassOnTheScaleHasChanged(null, mass);

		// ensure that the actual weight in the OrderManager has updated
		assertEquals(mass.inGrams(), om.getActualWeight());
	}
}
