package test.managers.system;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import stubbing.StubbedStation;
import stubbing.StubbedSystemManager;

public class TestConfigures {

	private StubbedSystemManager sm;

	@Before
	public void setup() {
		sm = new StubbedSystemManager();
	}

	@Test
	public void testSystemManagerCopiesHardwareReference() {
		sm.configure(new StubbedStation());

		assertNotNull(sm.getMachine());
	}

	@Test
	public void testSystemManagerConfiguresChildManagers() {
		sm.configure(new StubbedStation());

		assertTrue(sm.omStub.getConfigured());
		assertTrue(sm.pmStub.getConfigured());
	}
}
