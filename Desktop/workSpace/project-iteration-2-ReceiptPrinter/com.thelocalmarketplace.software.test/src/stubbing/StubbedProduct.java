// Liam Major 30223023

package stubbing;

import com.thelocalmarketplace.hardware.Product;

public class StubbedProduct extends Product {

	/**
	 * Creates a stubbed product object.
	 * 
	 * @param price     the price of the product
	 * @param isPerUnit if it is sold per unit and not by weight
	 */
	public StubbedProduct(long price) {
		super(price, true);
	}

}
