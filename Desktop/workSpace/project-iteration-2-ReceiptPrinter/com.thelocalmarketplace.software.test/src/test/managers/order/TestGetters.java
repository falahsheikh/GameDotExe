// Liam Major 30223023

package test.managers.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import powerutility.PowerGrid;
import stubbing.StubbedBarcodedProduct;
import stubbing.StubbedOrderManager;
import stubbing.StubbedPLUProduct;
import stubbing.StubbedStation;
import stubbing.StubbedSystemManager;

public class TestGetters {
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
	public void testGetProductsContainsAllAdded() {
		om.addProduct(new StubbedBarcodedProduct());

		List<Product> prods = om.getProducts();

		assertEquals(prods.size(), 1);

		// cheating because I know that I just added a barcoded product
		BarcodedProduct prod = (BarcodedProduct) prods.get(0);

		// asserting
		assertNotNull(prod);
		assertEquals(prod.getBarcode(), StubbedBarcodedProduct.BARCODE);
		assertEquals(prod.getDescription(), StubbedBarcodedProduct.DESCRIPTION);
		assertTrue(prod.getExpectedWeight() == StubbedBarcodedProduct.WEIGHT);
		assertEquals(prod.getPrice(), StubbedBarcodedProduct.PRICE);
	}

	@Test
	public void testGetProductsHasNonOnCreation() {
		List<Product> prods = om.getProducts();

		assertEquals(prods.size(), 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTotalPriceThrowsOnPLU() {
		om.addProduct(new StubbedPLUProduct());

		om.getTotalPrice();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testExpectedMassThrowsOnPLU() {
		om.addProduct(new StubbedPLUProduct());

		om.getExpectedMass();
	}

	@Test(expected = NullPointerSimulationException.class)
	public void testTotalPriceThrowsOnNull() {
		om.addProduct(null);

		om.getTotalPrice();
	}

	@Test(expected = NullPointerSimulationException.class)
	public void testExpectedMassThrowsOnNull() {
		om.addProduct(null);

		om.getExpectedMass();
	}

	@Test
	public void testExpectedMassEqualsProductMasses() {
		om.addProduct(new StubbedBarcodedProduct());

		BigDecimal mass = om.getExpectedMass();

		assertEquals(mass, new BigDecimal(StubbedBarcodedProduct.WEIGHT));
	}

	@Test
	public void testExpectedMassNotNull() {
		assertNotNull(om.getExpectedMass());
	}

	@Test
	public void testExpectedMassZeroWithNoItems() {
		assertEquals(om.getProducts().size(), 0);
		assertEquals(om.getExpectedMass(), BigDecimal.ZERO);
	}

	@Test
	public void testNoAdjustmentOnCreation() {
		assertEquals(om.getWeightAdjustment(), BigDecimal.ZERO);
	}
}
