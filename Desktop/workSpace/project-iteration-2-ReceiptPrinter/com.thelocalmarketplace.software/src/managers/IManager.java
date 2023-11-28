// Liam Major 30223023
// Nezla Annaisha 30123223

package managers;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

public interface IManager {
	/**
	 * This function uses any implementors {@link ISelfCheckout} and uses their
	 * references to attach observers.
	 * 
	 * @param machine the desired machine to observe
	 */
	void configure(AbstractSelfCheckoutStation machine);

	/**
	 * Gets the state of the manager.
	 * 
	 * @return the state
	 */
	SessionStatus getState();

	/**
	 * This method simply blocks the session, should only be used internally.
	 */
	void blockSession();

	/**
	 * This method unblocks the session.
	 */
	void unblockSession();

	/**
	 * This method notifies the attendant about a specific event or request, such as
	 * a "do not bag" request.
	 */
	void notifyAttendant(String reason);
}
