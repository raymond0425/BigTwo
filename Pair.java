import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Pair in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class Pair extends Hand {

	/**
	 * Creates and returns an instance of the Pair class.
	 */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand Pair
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand Pair
	 */
    public boolean isValid() {
        if (this.size() == 2) {
            if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
	 * Returns the type of hand "Pair"
     *
     * @return a string value of the type of hand "Pair"
	 */
    public String getType() {
        return "Pair";
    }
}