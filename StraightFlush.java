import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Straight Flush in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class StraightFlush extends Hand {

	/**
	 * Creates and returns an instance of the StraightFlush class.
	 */
    public StraightFlush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand StraightFlush
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand StraightFlush
	 */
    public boolean isValid() {
        this.sort();
        if (this.size() == 5) {
            for (int i = 0; i < 4; i++) { // checks Straight
                if (this.getCard(i+1).getRank() - this.getCard(i).getRank() == 1) {
                    continue;
                } else {
                    return false;
                }
            }
            for (int i = 0; i < 4; i++) { // checks Flush
                if (this.getCard(i+1).getSuit() == this.getCard(i).getSuit()) {
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
	 * Returns the type of hand "StraightFlush"
     *
     * @return a string value of the type of hand "StraightFlush"
	 */
    public String getType() {
        return "StraightFlush";
    }
}