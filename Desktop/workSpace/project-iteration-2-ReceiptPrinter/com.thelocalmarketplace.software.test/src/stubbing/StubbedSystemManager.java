// Liam Major 30223023
// Nezla Annaisha 30123223

package stubbing;

import java.math.BigDecimal;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import managers.OrderManager;
import managers.PaymentManager;
import managers.SessionStatus;
import managers.SystemManager;

public class StubbedSystemManager extends SystemManager {

	public static final CardIssuer issuer = new CardIssuer("testing", 10);
	public static final BigDecimal leniency = BigDecimal.ZERO;
	
	public StubbedPaymentManager pmStub;
	public StubbedOrderManager omStub;
	private String attendantNotification;
	
	public StubbedSystemManager() {
		this(issuer, leniency);
	}
	
	public StubbedSystemManager(CardIssuer i) {
		this(i, leniency);
	}
	
	public StubbedSystemManager(BigDecimal l) {
		this(issuer, l);
	}
	
	public StubbedSystemManager(CardIssuer i, BigDecimal l) {
		super(i, l);
		
		// creating stubbed managers
		omStub = new StubbedOrderManager(this, l);
		pmStub = new StubbedPaymentManager(this, i);
		
		// injecting stubbed managers for testing purposes
		this.om = omStub;
		this.pm = pmStub;
	}
	
	public void setPaymentManager(PaymentManager pm) {
		this.pm = pm;
	}
	
	public void setOrderManager(OrderManager om) {
		this.om = om;
	}
	
	public AbstractSelfCheckoutStation getMachine() {
		return machine;
	}

	@Override
    public void notifyAttendant(String reason) {
        this.attendantNotification = reason;
    }
	
	public String getAttendantNotification() {
        return this.attendantNotification;
    }
	
	@Override
	public void setState(SessionStatus s) {
		super.setState(s);
	}
}
