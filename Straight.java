import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Straight in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class Straight extends Hand {

	/**
	 * Creates and returns an instance of the Straight class.
	 */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand Straight
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand Straight
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
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Returns the type of hand "Straight"
     *
     * @return a string value of the type of hand "Straight"
	 */
    public String getType() {
        return "Straight";
    }
}