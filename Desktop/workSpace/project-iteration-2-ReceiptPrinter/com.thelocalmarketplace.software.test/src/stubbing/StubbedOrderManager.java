// Liam Major 30223023

package stubbing;

import java.math.BigDecimal;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import com.jjjwelectronics.Item;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.Product;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import managers.OrderManager;
import managers.ScanType;
import managers.SessionStatus;

public class StubbedOrderManager extends OrderManager {

	private StubbedSystemManager smStub;
	public boolean addItemToOrderCalled;
	public boolean addCustomerBagsCalled;
	public boolean getTotalPriceCalled;
	public boolean getExpectedMassCalled;
	public boolean getProductsCalled;
	public boolean onAttendantOverrideCalled;
	public boolean onDoNotBagRequestCalled;

	public StubbedOrderManager(StubbedSystemManager sm, BigDecimal leniency) {
		super(sm, leniency);

		smStub = sm;

		addItemToOrderCalled = false;
		addCustomerBagsCalled = false;
		getTotalPriceCalled = false;
		getExpectedMassCalled = false;
		getProductsCalled = false;
		onAttendantOverrideCalled = false;
		onDoNotBagRequestCalled = false;
	}

	public BigDecimal getActualWeight() {
		return this.actualWeight;
	}

	@Override
	public BigDecimal getWeightAdjustment() {
		return super.getWeightAdjustment();
	}

	@Override
	public void setWeightAdjustment(BigDecimal a) {
		super.setWeightAdjustment(a);
	}

	public void addProduct(Product p) {
		this.products.add(p);
	}

	public void setState(SessionStatus s) {
		smStub.setState(s);
	}

	@Override
	public void checkWeightDifference(BigDecimal difference) {
		super.checkWeightDifference(difference);
	}

	public boolean getNoBaggingRequest() {
		return noBaggingRequested;
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
	public void addItemToOrder(Item item, ScanType method) throws OperationNotSupportedException {
		addItemToOrderCalled = true;
		super.addItemToOrder(item, method);
	}

	@Override
	public void addCustomerBags(Item bags) {
		addCustomerBagsCalled = true;
		super.addCustomerBags(bags);
	}

	@Override
	public BigDecimal getTotalPrice() {
		getTotalPriceCalled = true;
		return super.getTotalPrice();
	}

	@Override
	public BigDecimal getExpectedMass() {
		getExpectedMassCalled = true;
		return super.getExpectedMass();
	}

	@Override
	public List<Product> getProducts() throws NullPointerSimulationException {
		getProductsCalled = true;
		return super.getProducts();
	}
	
	@Override
	public void onAttendantOverride() {
		onAttendantOverrideCalled = true;
		super.onAttendantOverride();
	}

	@Override
	public void onDoNotBagRequest(Item item) {
		onDoNotBagRequestCalled = true;
		super.onDoNotBagRequest(item);
	}

}
