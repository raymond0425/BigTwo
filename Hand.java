import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a subclass of the CardList class and is used to model a hand of cards.
 * 
 * @author Fung Ho Sang
 */
public abstract class Hand extends CardList {

    private int beatRank(Hand hand){
        String[] beatList = new String[] {"Straight", "Flush", "FullHouse", "Quad", "StraightFlush"};
        int beatRank = 0;
        for (int i = 0; i < 5; i++) {
            if (hand.getType() == beatList[i]) {
                beatRank = i;
            }
        }
        return beatRank;
    }

	/**
	 * Creates and returns an instance of the Hand class.
	 */
    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        for (int i = 0; i < cards.size(); i++) {
            this.addCard(cards.getCard(i));
        }
    }

    private CardGamePlayer player;

    /**
	 * Returns the player that played this hand
	 * 
	 * @return a CardGamePlayer object which is the player that played this hand
	 */
    public CardGamePlayer getPlayer() {
        return this.player;
    }

    /**
	 * Returns the top card of this hand
	 * 
	 * @return a Card object which is the top card of this hand
	 */
    public Card getTopCard() {
        return this.getCard(this.player.getNumOfCards() - 1);
    }

    /**
	 * Returns whether this hand beats the specified hand
     *
	 * @param hand the specified hand
	 * @return a boolean value of whether this hand beats the specified hand
	 */
    public boolean beats(Hand hand) {
        if (this.size() == 1 && hand.size() == 1) {
            if (this.getCard(0).compareTo(hand.getCard(0)) == 1) { // beats
                return true;
            } else { // if 0 or -1: not beats
                return false;
            }
        } else if (this.size() == 2 && hand.size() == 2) {
            if (this.getCard(1).compareTo(hand.getCard(0)) == 1 && this.getCard(1).compareTo(hand.getCard(1)) == 1) { // beats
                return true;
            } else { // if 0 or -1: not beats
                return false;
            }
        } else if (this.size() == 3 && hand.size() == 3) {
            if (this.getCard(2).compareTo(hand.getCard(2)) == 1) { // beats
                return true;
            } else { // if 0 or -1: not beats
                return false;
            }
        } else if (this.size() == 5 && hand.size() == 5) {
            if (beatRank(this) > beatRank(hand)) { // beats
                return true;
            } else if (beatRank(this) == beatRank(hand)) {
                if (this.getType() == "Straight" || this.getType() == "Flush" || this.getType() == "StraightFlush") {
                    if (this.getCard(4).compareTo(hand.getCard(4)) == 1) { // beats
                        return true;
                    } else { // if 0 or -1: not beats
                        return false;
                    }
                } else if (this.getType() == "FullHouse" || this.getType() == "Quad") {
                    if (this.getCard(2).compareTo(hand.getCard(2)) == 1) { // beats
                        return true;
                    } else { // if 0 or -1: not beats
                        return false;
                    }
                }
            } else { // if beatRank is lower -> not beats
                return false;
            }
        }
        return false;
    }

	/**
	 * Returns whether a BigTwo hand is valid in each subclass, to be overwritten
     *
     * @return a boolean value of whether a BigTwo hand is valid in each subclass
	 */
    public abstract boolean isValid(); 

    /**
	 * Returns the type of hand in each subclass, to be overwritten
     *
     * @return a string value of type of hand in each subclass
	 */
    public abstract String getType();
}