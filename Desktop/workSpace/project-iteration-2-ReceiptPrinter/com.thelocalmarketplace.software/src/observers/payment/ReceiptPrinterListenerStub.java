// Sheikh Falah Sheikh Hasan - 30175335


package observers.payment;

import com.jjjwelectronics.AbstractDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.printer.ReceiptPrinterListener;

import managers.PaymentManager;


public class ReceiptPrinterListenerStub implements ReceiptPrinterListener {
	
	private PaymentManager ref;
	
	public ReceiptPrinterListenerStub(PaymentManager paymentManager) {
		if(paymentManager == null) {
			throw new NullPointerException();
		}
		this.ref = paymentManager;	
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

	@Override
	public void thePrinterIsOutOfPaper() {
		this.ref.notifyPaper(false);
	}

	@Override
	public void thePrinterIsOutOfInk() {
		this.ref.notifyInk(false);
	}

	@Override
	public void thePrinterHasLowInk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void thePrinterHasLowPaper() {
		// TODO Auto-generated method stub
	}

	@Override
	public void paperHasBeenAddedToThePrinter() {
		this.ref.notifyPaper(true);		
	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		this.ref.notifyInk(true);		
	}

}

