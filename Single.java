import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of single in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class Single extends Hand {

	/**
	 * Creates and returns an instance of the Single class.
	 */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand Single
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand Single
	 */
    public boolean isValid() {
        if (this.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Returns the type of hand "Single"
     *
     * @return a string value of the type of hand "Single"
	 */
    public String getType() {
        return "Single";
    }
}