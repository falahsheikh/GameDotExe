package test.managers.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import managers.SessionStatus;
import stubbing.StubbedSystemManager;

public class TestOther {
	private StubbedSystemManager sm;

	@Before
	public void setup() {
		sm = new StubbedSystemManager();
	}

	@Test
	public void testStateOnCreation() {
		assertEquals(sm.getState(), SessionStatus.NORMAL);
	}

	@Test
	public void testStateNotNullOnCreation() {
		assertNotNull(sm.getState());
	}
}
