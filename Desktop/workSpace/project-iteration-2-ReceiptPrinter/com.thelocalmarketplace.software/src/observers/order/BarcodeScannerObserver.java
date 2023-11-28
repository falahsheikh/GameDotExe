// Jun Heo - 30173430
// Brandon Smith - 30141515
// Katelan Ng - 30144672
// Muhib Qureshi - 30076351
// Liam Major - 30223023

package observers.order;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import managers.OrderManager;

public class BarcodeScannerObserver implements BarcodeScannerListener {

	// object references
	private OrderManager ref;

	public BarcodeScannerObserver(OrderManager om) {
		// checking for null
		if (om == null) {
			throw new NullPointerSimulationException("OrderManager cannot be null.");
		}

		this.ref = om;
	}

	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		this.ref.notifyBarcodeScanned(barcodeScanner, barcode);
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

}
