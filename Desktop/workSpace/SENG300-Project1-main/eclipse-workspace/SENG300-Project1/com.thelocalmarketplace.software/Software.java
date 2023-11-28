package com.thelocalmarketplace.software;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;

import powerutility.PowerGrid;

public class Software implements ElectronicScaleListener{
	SelfCheckoutStation scs;
	boolean allow = false;
	Mass expectedWeight;
	Mass currentWeight;
	BigDecimal expectedWeightVal;
	BigDecimal currentWeightVal;
	ElectronicScale scale;
	
	
	public Software start(SelfCheckoutStation scs) {
		allow = true;
		this.scale = scs.baggingArea;
		return new Software(scs);
	}
	
	public Software(SelfCheckoutStation scs) {
		this.scs = scs;
	} 

	// add item via bar code - deals with expectedWeight
	public void addItemViaBarcode() {
		checkWeight();
		if (allow == true){
			// code goes here
		} else {
			weightDiscrepancy();
		}
	}
	
	// pay via coin 
	public void payViaCoin() {
		checkWeight();
		if (allow == true){
			// code goes here
		} else {
			weightDiscrepancy();
		}
	}
	
	// weight discrepancy code goes here
	public void weightDiscrepancy() {
		customerBlockMessage();
		attendantBlockMessage();
	}
	
	public void customerBlockMessage() {
		System.out.println("Weight Discrepancy: Please solve problem or call attendant");
	}
	
	public void attendantBlockMessage() {
		System.out.println("Customer Weight Discrepancy: Check on customer unless problem is solved");
	}
	
	public void checkWeight() {
		if ( expectedWeight.inGrams() == currentWeight.inGrams()) {
			allow = true;
		} else {
			allow = false;
		}
		
	}
	
	
	

	
	// customer or attendant actions
	
	public void customerRequestsDoNotBagItem(int itemWeight) {
		allow = true;
	}
	public void attendantApprovesWeight() {
		allow = true;
		currentWeight = expectedWeight;
	}
	
	// when a customer physically adds an item to the scale
	public void customerAddsItem(Item item) {
		currentWeightVal.add(item.getMass().inGrams());
		currentWeight = new Mass(currentWeightVal);
		theMassOnTheScaleHasChanged(scale, currentWeight);
	}
	
	// when a customer physically removes an item from the scale
	public void customerRemovesItem(Item item) {
		currentWeightVal.subtract(item.getMass().inGrams());
		currentWeight = new Mass(currentWeightVal);
		theMassOnTheScaleHasChanged(scale, currentWeight);
	}

	
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {}
	
}
