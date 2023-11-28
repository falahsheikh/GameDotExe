package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.Software;

public class SoftwareTest {
	
	private Software software;
    private SelfCheckoutStation selfCheckoutStation;
    
    @Before
    public void setUp() {
        selfCheckoutStation = mock(SelfCheckoutStation.class);
        software = new Software(selfCheckoutStation);
    }
    
    
    
    @Test
    public void testWeightDiscrepancyWhenAddingItem() {
        // Mocking the scale and setting expected and current weights
        ElectronicScale scale = mock(ElectronicScale.class);
        software.scale = scale;
        software.expectedWeight = new Mass(1000); // Assuming expected weight is 1000 grams

        // Setting a different current weight to simulate weight discrepancy
        Mass currentWeight = new Mass(800);
        when(scale.getCurrentWeight()).thenReturn(currentWeight);

        // Triggering addItemViaBarcode()
        software.addItemViaBarcode();

        // Verifying that weight discrepancy messages are displayed
        assertFalse(software.allow);
    }
    
    @Test
    public void testWeightDiscrepancyWhenPayingViaCoin() {
        // Mocking the scale and setting expected and current weights
        ElectronicScale scale = mock(ElectronicScale.class);
        software.scale = scale;
        software.expectedWeight = new Mass(1500); // Assuming expected weight is 1500 grams

        // Setting a different current weight to simulate weight discrepancy
        Mass currentWeight = new Mass(1200);
        when(scale.getCurrentWeight()).thenReturn(currentWeight);

        // Triggering payViaCoin()
        software.payViaCoin();

        // Verifying that weight discrepancy messages are displayed
        assertFalse(software.allow);
    }
    
    @Test
    public void testWeightMatching() {
        // Mocking the scale and setting expected and current weights to be the same
        ElectronicScale scale = mock(ElectronicScale.class);
        software.scale = scale;
        software.expectedWeight = new Mass(500); // Assuming expected weight is 500 grams

        // Setting the current weight to match the expected weight
        Mass currentWeight = new Mass(500);
        when(scale.getCurrentWeight()).thenReturn(currentWeight);

        // Triggering addItemViaBarcode()
        software.addItemViaBarcode();

        // Verifying that allow is true as weights match
        assertTrue(software.allow);
    }
    
    @Test
    public void testWeightMatchingAfterAttendantApproval() {
        // Mocking the scale and setting expected and current weights to be different initially
        ElectronicScale scale = mock(ElectronicScale.class);
        software.scale = scale;
        software.expectedWeight = new Mass(1000); // Assuming expected weight is 1000 grams
        Mass currentWeight = new Mass(800);
        when(scale.getCurrentWeight()).thenReturn(currentWeight);

        // Attendant approves the weight
        software.attendantApprovesWeight();

        // Triggering addItemViaBarcode() after attendant approval
        software.addItemViaBarcode();

        // Verifying that allow is true as weights match after attendant approval
        assertTrue(software.allow);
    }

    @Test
    public void testWeightDiscrepancyWhenCustomerAddsAndRemovesItems() {
        // Mocking the scale and setting expected weight
        ElectronicScale scale = mock(ElectronicScale.class);
        software.scale = scale;
        software.expectedWeight = new Mass(2000); // Assuming expected weight is 2000 grams

        // Customer adds items to the scale
        Item item1 = new Item("Item1", new Mass(500));
        Item item2 = new Item("Item2", new Mass(700));
        Item item3 = new Item("Item3", new Mass(300));
        software.customerAddsItem(item1);
        software.customerAddsItem(item2);
        software.customerAddsItem(item3);

        // Triggering addItemViaBarcode()
        software.addItemViaBarcode();

        // Verifying that allow is false due to weight discrepancy
        assertFalse(software.allow);

        // Customer removes an item from the scale
        software.customerRemovesItem(item2);

        // Triggering addItemViaBarcode() after item removal
        software.addItemViaBarcode();

        // Verifying that allow is true as weights match after item removal
        assertTrue(software.allow);
    }

    @Test
    public void testCustomerRequestsNotToBagItem() {
        // Setting expected and current weights
        software.expectedWeight = new Mass(1200); // Assuming expected weight is 1200 grams
        software.currentWeight = new Mass(1200);

        // Customer requests not to bag the item
        software.customerRequestsDoNotBagItem(1200);

        // Verifying that allow is true as the customer explicitly requested not to bag the item
        assertTrue(software.allow);
    }

    

}
