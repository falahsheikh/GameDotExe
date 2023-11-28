// Liam Major 30223023
// Nezla Annaisha 30123223

package test.managers.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import powerutility.PowerGrid;
import stubbing.StubbedItem;
import stubbing.StubbedOrderManager;
import stubbing.StubbedStation;
import stubbing.StubbedSystemManager;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import managers.SessionStatus;

public class TestDoNotBag {
	// machine
	private AbstractSelfCheckoutStation machine;

	// vars
	private StubbedOrderManager om;
	private StubbedSystemManager sm;

	@Before
	public void setup() {
		// configuring the hardware
		StubbedStation.configure();

		// creating the hardware
		machine = new StubbedStation().machine;
		machine.plugIn(PowerGrid.instance());
		machine.turnOn();

		// creating the stubs
		sm = new StubbedSystemManager(BigDecimal.ZERO);
		om = sm.omStub;

		// configuring the machine
		sm.configure(machine);
	}

	@Test
	public void testDoNotBagRequestSetsFlag() {
		Item item = new StubbedItem(2);
		om.onDoNotBagRequest(item);
		
		// check if the flag for no bagging request is set to true
		assertTrue(om.getNoBaggingRequest());
	}
	
    @Test(expected = InvalidArgumentSimulationException.class)
    public void testDoNotBagNullItem() {
        Item item = null;
        
        // call with a null item, expecting an exception
    	om.onDoNotBagRequest(item);
    }
    
    @Test
    public void testWeightAdjustmentInNormalState() {
        Item item = new StubbedItem(2);

        om.setWeightAdjustment(BigDecimal.valueOf(5));
        om.onDoNotBagRequest(item);

        // check if the weight adjustment has been updated correctly (5 + 2 = 7)
        assertEquals(BigDecimal.valueOf(7), om.getWeightAdjustment());
    }
    
    @Test
    public void testWeightAdjustmentInBlockedState() {
        Item item = new StubbedItem(2);

        // set state to a state other than NORMAL
        sm.setState(SessionStatus.BLOCKED);
        om.onDoNotBagRequest(item);

        // check if the weight adjustment remains unchanged
        assertEquals(BigDecimal.ZERO, om.getWeightAdjustment());
    }
	
    @Test
    public void testDoNotBagRequestNotifiesAttendant() {
        Item item = new StubbedItem(4);
        
        sm.setState(SessionStatus.NORMAL);
        om.onDoNotBagRequest(item);

        // check if the attendant is notified with the correct reason
        assertEquals("do not bag request was received", sm.getAttendantNotification());
    }
    
}
