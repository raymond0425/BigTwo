import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to model a hand of Full House in a Big Two card game
 * 
 * @author Fung Ho Sang
 */
public class FullHouse extends Hand {
    
	/**
	 * Creates and returns an instance of the FullHouse class.
	 */
    public FullHouse(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

	/**
	 * Returns whether this hand is a valid BigTwo hand FullHouse
     *
     * @return a boolean value of whether this hand is a valid BigTwo hand FullHouse
	 */
    public boolean isValid() {
        this.sort();
        if (this.size() == 5) {
            if (this.getCard(1).getRank() == this.getCard(2).getRank()) { // in the form of AAABB
                for (int i = 0; i < 2; i++) {
                    if (this.getCard(i).getRank() == this.getCard(i+1).getRank()) {
                        continue;
                    } else {
                        return false;
                    }
                }
                for (int i = 3; i < 4; i++) {
                    if (this.getCard(i).getRank() == this.getCard(i+1).getRank()) {
                        continue;
                    } else {
                        return false;
                    }
                }
            } else { // in the form of AABBB
                for (int i = 0; i < 1; i++) {
                    if (this.getCard(i).getRank() == this.getCard(i+1).getRank()) {
                        continue;
                    } else {
                        return false;
                    }
                }
                for (int i = 2; i < 4; i++) {
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
	 * Returns the type of hand "FullHouse"
     *
     * @return a string value of the type of hand "FullHouse"
	 */
    public String getType() {
        return "FullHouse";
    }
}