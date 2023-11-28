//Katelan Ng 30144672
// Liam Major 30223023
//Coverage: 77.2%

package test.observers;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import observers.order.BarcodeScannerObserver;
import stubbing.StubbedOrderManager;
import stubbing.StubbedSystemManager;
import utils.DatabaseHelper;

public class TestBarcodeScannerObserver {

	// vars
	private StubbedOrderManager om;
	private StubbedSystemManager sm;
	private BarcodeScannerObserver bso;
	private BarcodedItem item;

	@Before
	public void setup() {
		// This stub will never talk to its SystemManager,
		// so we can declare it as null.
		sm = new StubbedSystemManager();
		om = new StubbedOrderManager(sm, BigDecimal.ZERO);

		// setting the internal order manager
		sm.setOrderManager(om);
		bso = new BarcodeScannerObserver(om);

		// creating a random item and putting in the database
		this.item = DatabaseHelper.createRandomBarcodedItem();
	}

	@Test(expected = NullPointerSimulationException.class)
	public void testNullPaymentManager() {
		new BarcodeScannerObserver(null);
	}

	/***
	 * Tests if the OrderManager actually received the item the hardware scanned.
	 * 
	 * This disregards most of the implementation of the method in OrderManager, the
	 * method `notifyBarcodeScanned` is tested in TestOrderManager.java
	 */
	@Test
	public void testNotifyOrderManagerBarcodeScanned() {
		// calling the function to notify that some barcode was scanned
		bso.aBarcodeHasBeenScanned(null, item.getBarcode());

		// getting the barcoded product
		BarcodedProduct prod = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(item.getBarcode());

		// ensuring that the item is present in the OrderManager
		assertTrue(om.getProducts().contains(prod));
	}
}
