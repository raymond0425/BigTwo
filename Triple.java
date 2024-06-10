import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Triple in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class Triple extends Hand {

	/**
	 * Creates and returns an instance of the Triple class.
	 */
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand Triple
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand Triple
	 */
    public boolean isValid() {
        if (this.size() == 3) {
            if ((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(1).getRank() ==  this.getCard(2).getRank())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
	 * Returns the type of hand "Triple"
     *
     * @return a string value of the type of hand "Triple"
	 */
    public String getType() {
        return "Triple";
    }
}