// Liam Major 30223023

package test.managers.system;

import static org.junit.Assert.assertFalse;

import javax.naming.OperationNotSupportedException;

import org.junit.Before;
import org.junit.Test;

import managers.SessionStatus;
import stubbing.StubbedSystemManager;

/**
 * This tests that certain functions cannot execute from certain states.
 */

public class TestBlockedFunctions {
	// TODO implement tests for swiping cards

	private StubbedSystemManager sm;

	@Before
	public void setup() {
		sm = new StubbedSystemManager();
	}

	@Test
	public void testCannotInsertCoinWhenBlocked() {
		sm.setState(SessionStatus.BLOCKED);

		sm.insertCoin(null);

		assertFalse(sm.pmStub.insertCoinCalled);
	}

	@Test
	public void testCannotInsertCoinWhenPaid() {
		sm.setState(SessionStatus.PAID);

		sm.insertCoin(null);

		assertFalse(sm.pmStub.insertCoinCalled);
	}

	@Test
	public void testCannotInsertBanknoteWhenBlocked() {
		sm.setState(SessionStatus.BLOCKED);

		sm.insertBanknote(null);

		assertFalse(sm.pmStub.insertBanknoteCalled);
	}

	@Test
	public void testCannotInsertBanknoteWhenPaid() {
		sm.setState(SessionStatus.PAID);

		sm.insertBanknote(null);

		assertFalse(sm.pmStub.insertBanknoteCalled);
	}

	@Test
	public void testCannotTenderChangeWhenBlocked() {
		sm.setState(SessionStatus.BLOCKED);

		sm.tenderChange();

		assertFalse(sm.pmStub.tenderChangeCalled);
	}

	@Test
	public void testCannotTenderChangeWhenPaid() {
		sm.setState(SessionStatus.PAID);

		sm.tenderChange();

		assertFalse(sm.pmStub.tenderChangeCalled);
	}

	@Test
	public void testCannotAddItemWhenBlocked() throws OperationNotSupportedException {
		sm.setState(SessionStatus.BLOCKED);

		sm.addItemToOrder(null, null);

		assertFalse(sm.omStub.addItemToOrderCalled);
	}

	@Test
	public void testCannotAddItemWhenPaid() throws OperationNotSupportedException {
		sm.setState(SessionStatus.PAID);

		sm.addItemToOrder(null, null);

		assertFalse(sm.omStub.addItemToOrderCalled);
	}

	@Test(expected = RuntimeException.class)
	public void testCannotRecordTransactionFromBlocked() {
		sm.setState(SessionStatus.BLOCKED);

		sm.recordTransaction(null, 0, 0);
	}

	@Test(expected = RuntimeException.class)
	public void testCannotRecordTransactionFromPaid() {
		sm.setState(SessionStatus.PAID);

		sm.recordTransaction(null, 0, 0);
	}

	@Test(expected = RuntimeException.class)
	public void testCannotPostTransactionFromBlocked() {
		sm.setState(SessionStatus.BLOCKED);

		sm.postTransactions();
	}

	@Test(expected = RuntimeException.class)
	public void testCannotPostTransactionFromNormal() {
		sm.setState(SessionStatus.PAID);

		sm.postTransactions();
	}
	
	@Test(expected = RuntimeException.class)
	public void testCannotAddBagsWhenBlocked() {
		sm.setState(SessionStatus.BLOCKED);

		sm.addCustomerBags(null);
	}

	@Test(expected = RuntimeException.class)
	public void testCannotAddBagsWhenPaid() {
		sm.setState(SessionStatus.PAID);

		sm.addCustomerBags(null);
	}

}
