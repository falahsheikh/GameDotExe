// Liam Major 30223023

package observers.payment;

import java.math.BigDecimal;
import java.util.Currency;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.BanknoteValidator;
import com.tdc.banknote.BanknoteValidatorObserver;

import managers.PaymentManager;

public class BanknoteCollector implements BanknoteValidatorObserver {

	public BanknoteCollector(PaymentManager paymentManager) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void badBanknote(BanknoteValidator validator) {
		// TODO Auto-generated method stub

	}

}
