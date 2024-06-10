import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Quad in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class Quad extends Hand {

	/**
	 * Creates and returns an instance of the Quad class.
	 */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand Quad
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand Quad
	 */
    public boolean isValid() {
        this.sort();
        if (this.size() == 5) {
            if (this.getCard(0).getRank() == this.getCard(1).getRank()) { // in the form of AAAAB
                for (int i = 0; i < 3; i++) {
                    if (this.getCard(i).getRank() == this.getCard(i+1).getRank()) {
                        continue;
                    } else {
                        return false;
                    }
                }
            } else { // in the form of ABBBB
                for (int i = 1; i < 4; i++) {
                    if (this.getCard(i).getRank() == this.getCard(i+1).getRank()) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Returns the type of hand "Quad"
     *
     * @return a string value of the type of hand "Quad"
	 */
    public String getType() {
        return "Quad";
    }
}