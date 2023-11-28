// André Beaulieu, UCID 30174544
package observers.order;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import managers.OrderManager;

public class ScaleObserver implements ElectronicScaleListener {

	// hardware references

	// object references
	private OrderManager om;

	// object ownership

	// vars

	// Take keep the supplied scale as a pointer
	// and register yourself with that scale.
	public ScaleObserver(OrderManager om) {
		// Set orderManager is not already
		if (om == null) {
			throw new NullPointerSimulationException("OrderManager cannot be null.");
		}

		// copying reference
		this.om = om;
	}

	// If outside the bounds of expectation, shut the system down.
	// Once we're back within these bounds, re-enable the system
	// This only works if the WeightChecker is enabled.
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		this.om.notifyMassChanged(this, mass.inGrams());
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
	}

}
