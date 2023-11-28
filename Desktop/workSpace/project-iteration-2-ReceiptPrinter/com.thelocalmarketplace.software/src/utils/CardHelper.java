// Liam Major 30223023

package utils;

import java.util.Calendar;
import java.util.Random;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;

public class CardHelper {

	public static final String ISSUER_NAME = "Blackrock";
	public static final long MAX_HOLDS = 100;

	public static final String[] BANKS = { "RBC", "Scotiabank" };

	public static CardIssuer createCardIssuer() {
		return createCardIssuer(ISSUER_NAME, MAX_HOLDS);
	}

	public static CardIssuer createCardIssuer(String name) {
		return createCardIssuer(name, MAX_HOLDS);
	}

	public static CardIssuer createCardIssuer(long maxHolds) {
		return createCardIssuer(ISSUER_NAME, maxHolds);
	}

	public static CardIssuer createCardIssuer(String name, long maxHolds) {
		return new CardIssuer(name, maxHolds);
	}

	public static Card createCard(CardIssuer issuer) {
		// creating a card issuer if none exists
		if (issuer == null) {
			throw new IllegalArgumentException("cannot add a card to a null card issuer");
		}

		Random r = new Random();

		// creating the fields of the card
		String ccv = String.valueOf(r.nextLong(100, 1_000));
		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.DAY_OF_MONTH, 20);
		String holder = "Joe Joeman";
		double limit = r.nextDouble(100, 10_000);

		// finding a non-used card number
		while (true) {
			try {
				String number = String.valueOf(r.nextLong(1, 1_000_000_000));

				// creating the card object
				Card card = new Card("debit", number, holder, ccv);

				// adding the card to the issuer
				issuer.addCardData(number, holder, expiry, ccv, limit);

				// returning the card to the caller
				return card;
			} catch (InvalidArgumentSimulationException e) {
				continue;
			}
		}
	}
}
