import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Flush in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class Flush extends Hand {

	/**
	 * Creates and returns an instance of the Flush class.
	 */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand Flush
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand Flush
	 */
    public boolean isValid() {
        this.sort();
        if (this.size() == 5) {
            for (int i = 0; i < 4; i++) { // checks Flush
                if (this.getCard(i).getSuit() == this.getCard(i+1).getSuit()) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Returns the type of hand "Flush"
     *
     * @return a string value of the type of hand "Flush"
	 */
    public String getType() {
        return "Flush";
    }
}