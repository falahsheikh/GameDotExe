// Liam Major 30223023

package stubbing;

import java.math.BigDecimal;
import java.util.List;

import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import managers.*;

public class StubbedPaymentManager extends PaymentManager {

	public StubbedSystemManager smStub;

	public BigDecimal notifyBalanceAddedValue;
	public boolean insertCoinCalled;
	public boolean insertBanknoteCalled;
	public boolean tenderChangeCalled;
	public boolean getCustomerPaymentCalled;

	public StubbedPaymentManager(StubbedSystemManager sm, CardIssuer issuer) {
		super(sm, issuer);

		smStub = sm;

		insertCoinCalled = false;
		insertBanknoteCalled = false;
		tenderChangeCalled = false;
		getCustomerPaymentCalled = false;
	}

	@Override
	public void notifyBalanceAdded(BigDecimal value) {
		notifyBalanceAddedValue = value;
	}

	public CardIssuer getIssuer() {
		return this.issuer;
	}

	public AbstractSelfCheckoutStation getMachine() {
		return machine;
	}

	private boolean configured;

	@Override
	public void configure(AbstractSelfCheckoutStation machine) {
		super.configure(machine);

		configured = true;
	}

	public boolean getConfigured() {
		return configured;
	}

	@Override
	public void insertCoin(Coin coin) {
		insertCoinCalled = true;
		super.insertCoin(coin);
	}

	@Override
	public void insertBanknote(Banknote banknote) {
		insertBanknoteCalled = true;
		super.insertBanknote(banknote);
	}

	@Override
	public void tenderChange() {
		tenderChangeCalled = true;
		super.tenderChange();
	}

	@Override
	public BigDecimal getCustomerPayment() {
		getCustomerPaymentCalled = true;
		return super.getCustomerPayment();
	}
}
