import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a subclass of the Deck class and is used to model a deck of cards
 * used in a Big Two card game. It overrides the initialize() method it inherits from the
 * Deck class to create a deck of Big Two cards.
 * 
 * @author Fung Ho Sang
 */
public class BigTwoDeck extends Deck {

	/**
	 * Initializes the deck of BigTwoCard.
	 */
    public void initialize() {
        this.removeAllCards();
        for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
                this.addCard(card);
			}
		}
    }

}