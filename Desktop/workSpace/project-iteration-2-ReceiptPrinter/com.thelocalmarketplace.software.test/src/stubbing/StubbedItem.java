// Liam Major 30223023

package stubbing;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;

public class StubbedItem extends Item {

	/**
	 * Creates an item with a mass of type {@link Mass}.
	 * 
	 * @param mass the mass for the item
	 */
	public StubbedItem(Mass mass) {
		super(mass);
	}

	/**
	 * Creates an item with a mass of type {@link double}.
	 * 
	 * @param mass the mass for the item
	 */
	public StubbedItem(double mass) {
		super(new Mass(mass));
	}

}
