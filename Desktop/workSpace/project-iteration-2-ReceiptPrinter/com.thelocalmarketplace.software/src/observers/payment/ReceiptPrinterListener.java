// Sheikh Falah Sheikh Hasan - 30175335


package observers.payment;

import java.math.BigDecimal;


import com.jjjwelectronics.printer;
import com.jjjwelectronics.AbstractDevice;
import com.jjjwelectronics.AbstractDeviceListener;
import com.jjjwelectronics.ReceiptPrinter;
import com.jjjwelectronics.ReceiptPrinterListener;
import com.jjjwelectronics.ReceiptPrinterListenerStub;

import managers.PaymentManager;

public class ReceiptPrinterListenerStub implements ReceiptPrinterListener {

	private boolean paperAdded;
	private boolean inkAdded;

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void outOfPaper(ReceiptPrinter printer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void outOfInk(ReceiptPrinter printer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paperAdded(ReceiptPrinter printer)
	{
		paperAdded = true;
	}

	@Override
	public void paperAdded(ReceiptPrinter printer) {
		inkAdded = true;
	}

}

