import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a subclass of the Card class and is used to model a card used in a
 * Big Two card game. It overrides the compareTo() method it inherits from the Card
 * class to reflect the ordering of cards used in a Big Two card game.
 * 
 * @author Fung Ho Sang
 */
public class BigTwoCard extends Card implements Comparable<Card>, Serializable{

    private static int convertRank (int rank) { // convert from Card rank to BigTwoCard rank
        if (rank >= 2) { // 3 - K
            return (rank - 2);
        } else if (rank == 0) { // A
            return 11;
        } else { // 2
            return 12;
        }
    }

    private static int reverseConvertRank (int rank) { // convert from BigTwoCard rank to Card rank
        if (rank <= 10) { // 3 - K
            return (rank + 2);
        } else if (rank == 11) { // A
            return 0;
        } else { // 2
            return 1;
        }
    }
    
	/**
	 * Creates and returns an instance of the BigTwoCard class.
     * @param suit an int value between 0 and 3 representing the suit of a BigTwoCard:
	 *             <p>
	 *             0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank an int value between 0 and 12 representing the rank of a BigTwoCard:
	 *             <p>
	 *             0 = '3', 1 = '4', 2 = '5', ..., 8 = 'J', 9 = 'Q', 10 = 'K', 11 =
	 *             'A', 12 = '2'
	 */
    public BigTwoCard(int suit, int rank) {
        super(suit, reverseConvertRank(rank));
    }

	/**
	 * Compares this card with the specified card for BigTwo order.
	 * 
	 * @param card the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is less
	 *         than, equal to, or greater than the specified card
	 */
    public int compareTo(Card card) {
        if (convertRank(this.rank) > convertRank(card.rank)) {
			return 1;
		} else if (convertRank(this.rank) < convertRank(card.rank)) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
    }
}