import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This class implements the CardGame interface and is used to model a Big Two card
 * game.
 * 
 * @author Fung Ho Sang
 */
public class BigTwo {

	/**
	 * Creates and returns an instance of the BigTwo class.
	 */
    public BigTwo() {
        playerList = new ArrayList<CardGamePlayer>();
        for (int i = 0; i < 4; i++) {
            playerList.add(new CardGamePlayer(""));
        }
        ui = new BigTwoGUI(this);
        client = new BigTwoClient(this, ui);
    }

    private int numOfPlayers;
    private Deck deck = new Deck();
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private int currentPlayerIdx;
    private BigTwoGUI ui;
    private static BigTwoClient client;
    private static String[] ipAndPort;

    /**
     * Gets the client
     * 
     * @return the client
     */
    public BigTwoClient getClient() {
        return this.client;
    }

    public void connectToServer() {
        client.connect();
    }

    /**
	 * Returns the number of players
	 * 
	 * @return an int value representing the number of players in a BigTwo game.
	 */
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    /**
	 * Returns the deck of cards used in this BigTwo game.
	 * 
	 * @return the Deck object deck used in this BigTwo game.
	 */
    public Deck getDeck() {
        return this.deck;
    }

    /**
	 * Returns the ArrayList of CardGamePlayers in this BigTwo game.
	 * 
	 * @return the ArrayList of CardGamePlayers playerList in this BigTwo game.
	 */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return this.playerList;
    }

    /**
	 * Returns the ArrayList of Hands in this BigTwo game.
	 * 
	 * @return the ArrayList of Hands handsOnTable in this BigTwo game.
	 */
    public ArrayList<Hand> getHandsOnTable() {
        return this.handsOnTable;
    }

    /**
	 * Returns the index of current player in this BigTwo game.
	 * 
	 * @return the index of current player currentPlayerIdx in this BigTwo game.
	 */
    public int getCurrentPlayerIdx() {
        return this.currentPlayerIdx;
    }

    /**
	 * Starts the BigTwo game until end of game.
     *
	 * @param deck the deck of cards used in the BigTwo game
	 */
    public void start(Deck deck) {
        // remove all cards from the players and the table
        handsOnTable = new ArrayList<Hand>();
        for (int i = 0; i < 4; i++) {
            playerList.get(i).removeAllCards();
        }
        // distribute the cards to the players
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                playerList.get(i).addCard(deck.getCard(i*13+j));
            }
        }
        // identify the player who holds the Three of Diamonds
        // set both the currentPlayerIdx of the bigTwo object and the activePlayer of the BigTwoGUI object to the index of the player who holds the Three of Diamonds
        for (int i = 0; i < 4; i++) {
            playerList.get(i).sortCardsInHand();
            if (playerList.get(i).getCardsInHand().contains(new BigTwoCard(0, 0))) {
                currentPlayerIdx = i;
                ui.setActivePlayer(i);
                break;
            }
        }
        // call the repaint() method of the BigTwoGUI object to show the cards on the table
        playerList.get(currentPlayerIdx).sortCardsInHand();
        ui.repaint();
        ui.printMsg(playerList.get(currentPlayerIdx).getName() + "'s turn: ");
        // call the promptActivePlayer() method of the BigTwoGUI object to prompt user to select cards and make his/her move

        return;
    }

    private void nextPlayer(String handType) {
        //ui.repaint();
        String printStr = "";
        Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
        if (handType != "Pass" && lastHandOnTable != null) {
            for (int i = 0; i<lastHandOnTable.size(); i++) {
                printStr += "[" + lastHandOnTable.getCard(i).toString() + "] ";
            }
        }
        ui.printMsg("{"+handType+"} " + printStr);

        if (endOfGame()) {
            ui.repaint();
            String endGameText = "";
            int winnerID = 0;
            for (int i = 0; i < 4; i++) {
                if (playerList.get(i).getNumOfCards() == 0) {
                    endGameText += (playerList.get(i).getName() + " wins the game.\n");
                    winnerID = i;
                } else if (playerList.get(i).getNumOfCards() == 1) {
                    endGameText += (playerList.get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " card in hand.\n");
                } else {
                    endGameText += (playerList.get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " card in hand.\n");
                }
            }
            ui.disableAll();
            ui.displayEndGameText(endGameText, winnerID);
            return;
        }    

        playerList.get(currentPlayerIdx).sortCardsInHand();
        if (currentPlayerIdx == 3) {
            currentPlayerIdx = 0;
        } else {
            currentPlayerIdx = currentPlayerIdx + 1;
        }
        //System.out.println(currentPlayerIdx);
        ui.setActivePlayer(currentPlayerIdx);
        playerList.get(currentPlayerIdx).sortCardsInHand();
        ui.promptActivePlayer();
    }

    /**
	 * Makes the move of the player with the cards of indices cardIdx
     *
     * @param playerIdx the int value of the player making this move
	 * @param cardIdx the indices of cards of the player's hand
	 */
    public void makeMove(int playerIdx, int[] cardIdx) {
        client.sendMessage(new CardGameMessage(6,playerIdx,cardIdx));
    }

    /**
	 * Checks the move of the player with the cards of indices cardIdx
     *
     * @param playerIdx the int value of the player making this move
	 * @param cardIdx the indices of cards of the player's hand
	 */
    public void checkMove(int playerIdx, int[] cardIdx) {
        boolean isLastPlayer = false;
        if (handsOnTable.size() != 0) {
            isLastPlayer = handsOnTable.get(handsOnTable.size() - 1).getPlayer() == playerList.get(playerIdx);
        }
        if (cardIdx == null) {
            if (handsOnTable.size() == 0 || isLastPlayer) { // First hand or played last hand, must play some hand.
                ui.printMsg("Not a legal move!!!");
                return;
            } else {
                nextPlayer("Pass");
                return;
            }
        } else if (cardIdx.length == 1) {
            Single attemptSingle = new Single(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            if (handsOnTable.size() == 0) { // First hand (including diamond 3)
                if (attemptSingle.getCard(0).equals(new BigTwoCard(0,0))) {
                    handsOnTable.add(attemptSingle);
                    playerList.get(playerIdx).removeCards(attemptSingle);
                    nextPlayer("Single");
                    return;
                } else { // First hand no diamond 3
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptSingle.isValid() && attemptSingle.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                handsOnTable.add(attemptSingle);
                playerList.get(playerIdx).removeCards(attemptSingle);
                nextPlayer("Single");
                return;
            } else { // not valid or not beat
                ui.printMsg("Not a legal move!!!");
                return;
            }
        } else if (cardIdx.length == 2) {
            Pair attemptPair = new Pair(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            if (attemptPair.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                if (attemptPair.getCard(0).equals(new BigTwoCard(0,0))) {
                    handsOnTable.add(attemptPair);
                    playerList.get(playerIdx).removeCards(attemptPair);
                    nextPlayer("Pair");
                    return;
                } else { // First hand no diamond 3
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptPair.isValid() && attemptPair.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                handsOnTable.add(attemptPair);
                playerList.get(playerIdx).removeCards(attemptPair);
                nextPlayer("Pair");
                return;
            } else { // not valid or not beat
                ui.printMsg("Not a legal move!!!");
                return;
            }
        } else if (cardIdx.length == 3) {
            Triple attemptTriple = new Triple(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            if (attemptTriple.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                if (attemptTriple.getCard(0).equals(new BigTwoCard(0,0))) {
                    handsOnTable.add(attemptTriple);
                    playerList.get(playerIdx).removeCards(attemptTriple);
                    nextPlayer("Triple");
                    return;
                } else { // First hand no diamond 3
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptTriple.isValid() && attemptTriple.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                handsOnTable.add(attemptTriple);
                playerList.get(playerIdx).removeCards(attemptTriple);
                nextPlayer("Triple");
                return;
            } else { // not valid or not beat
                ui.printMsg("Not a legal move!!!");
                return;
            }
        } else if (cardIdx.length == 5) {
            StraightFlush attemptStraightFlush = new StraightFlush(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            Quad attemptQuad = new Quad(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            FullHouse attemptFullHouse = new FullHouse(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            Flush attemptFlush = new Flush(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            Straight attemptStraight = new Straight(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            if (attemptStraightFlush.isValid()) {
                if (attemptStraightFlush.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                    if (attemptStraightFlush.getCard(0).equals(new BigTwoCard(0,0))) {
                        handsOnTable.add(attemptStraightFlush);
                        playerList.get(playerIdx).removeCards(attemptStraightFlush);
                        nextPlayer("StraightFlush");
                        return;
                    } else { // First hand no diamond 3
                        ui.printMsg("Not a legal move!!!");
                        return;
                    }
                } else if (attemptStraightFlush.isValid() && attemptStraightFlush.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                    handsOnTable.add(attemptStraightFlush);
                    playerList.get(playerIdx).removeCards(attemptStraightFlush);
                    nextPlayer("StraightFlush");
                    return;
                } else { // not valid or not beat
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptQuad.isValid()) {
                if (attemptQuad.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                    if (attemptQuad.getCard(0).equals(new BigTwoCard(0,0))) {
                        handsOnTable.add(attemptQuad);
                        playerList.get(playerIdx).removeCards(attemptQuad);
                        nextPlayer("Quad");
                        return;
                    } else { // First hand no diamond 3
                        ui.printMsg("Not a legal move!!!");
                        return;
                    }
                } else if (attemptQuad.isValid() && attemptQuad.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                    handsOnTable.add(attemptQuad);
                    playerList.get(playerIdx).removeCards(attemptQuad);
                    nextPlayer("Quad");
                    return;
                } else { // not valid or not beat
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptFullHouse.isValid()) {
                if (attemptFullHouse.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                    if (attemptFullHouse.getCard(0).equals(new BigTwoCard(0,0))) {
                        handsOnTable.add(attemptFullHouse);
                        playerList.get(playerIdx).removeCards(attemptFullHouse);
                        nextPlayer("FullHouse");
                        return;
                    } else { // First hand no diamond 3
                        ui.printMsg("Not a legal move!!!");
                        return;
                    }
                } else if (attemptFullHouse.isValid() && attemptFullHouse.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                    handsOnTable.add(attemptFullHouse);
                    playerList.get(playerIdx).removeCards(attemptFullHouse);
                    nextPlayer("FullHouse");
                    return;
                } else { // not valid or not beat
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptFlush.isValid()) {
                if (attemptFlush.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                    if (attemptFlush.getCard(0).equals(new BigTwoCard(0,0))) {
                        handsOnTable.add(attemptFlush);
                        playerList.get(playerIdx).removeCards(attemptFlush);
                        nextPlayer("Flush");
                        return;
                    } else { // First hand no diamond 3
                        ui.printMsg("Not a legal move!!!");
                        return;
                    }
                } else if (attemptFlush.isValid() && attemptFlush.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                    handsOnTable.add(attemptFlush);
                    playerList.get(playerIdx).removeCards(attemptFlush);
                    nextPlayer("Flush");
                    return;
                } else { // not valid or not beat
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else if (attemptStraight.isValid()) {
                if (attemptStraight.isValid() && handsOnTable.size() == 0) { // First hand (including diamond 3)
                    if (attemptStraight.getCard(0).equals(new BigTwoCard(0,0))) {
                        handsOnTable.add(attemptStraight);
                        playerList.get(playerIdx).removeCards(attemptStraight);
                        nextPlayer("Straight");
                        return;
                    } else { // First hand no diamond 3
                        ui.printMsg("Not a legal move!!!");
                        return;
                    }
                } else if (attemptStraight.isValid() && attemptStraight.beats(handsOnTable.get(handsOnTable.size()-1)) || isLastPlayer) { // valid and beats
                    handsOnTable.add(attemptStraight);
                    playerList.get(playerIdx).removeCards(attemptStraight);
                    nextPlayer("Straight");
                    return;
                } else { // not valid or not beat
                    ui.printMsg("Not a legal move!!!");
                    return;
                }
            } else { // all 5-card types not match --> not valid
                ui.printMsg("Not a legal move!!!");
                return;
            }
        }
        return;
    }

    /**
	 * Returns the boolean value of whether the game has ended
     *
     * @return the boolean value of whether the game has ended
	 */
    public boolean endOfGame() {
        for (int i = 0; i < 4; i++) {
            if (playerList.get(i).getNumOfCards() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Starts the program
     *
     * @param args The user input args
	 */
    public static void main(String[] args) {
        // creates a Big Two card game
        BigTwo bigTwoGame = new BigTwo();

        // creates and shuffles a deck of cards
        BigTwoDeck deckOfCard = new BigTwoDeck();
        deckOfCard.initialize();
        deckOfCard.shuffle();

        client.setServerIP("127.0.0.1");
        client.setServerPort(2396);

        client.setPlayerName(JOptionPane.showInputDialog("Please enter your name"));
        
        client.connect();

        // starts the game with the deck of cards
        //bigTwoGame.start(deckOfCard);
    }

    /**
	 * Returns a valid hand from the specified list of cards of the player. Returns null if no
     * valid hand can be composed from the specified list of cards.
     *
     * @return a valid hand from the specified list of cards of the player. Returns null if no
     * valid hand can be composed from the specified list of cards.
	 */
    public static Hand composeHand(CardGamePlayer player, CardList cards) {
        Single mySingle = new Single(player, cards);
        Pair myPair = new Pair(player, cards);
        Triple myTriple = new Triple(player, cards);
        StraightFlush myStraightFlush = new StraightFlush(player, cards);
        Quad myQuad = new Quad(player, cards);
        FullHouse myFullHouse = new FullHouse(player, cards);
        Flush myFlush = new Flush(player, cards);
        Straight myStraight = new Straight(player, cards);
        if (mySingle.isValid()) {
            return mySingle;
        } else if (myPair.isValid()) {
            return myPair;
        } else if (myTriple.isValid()) {
            return myTriple;
        } else if (myStraightFlush.isValid()) {
            return myStraightFlush;
        } else if (myQuad.isValid()) {
            return myQuad;
        } else if (myFullHouse.isValid()) {
            return myFullHouse;
        } else if (myFlush.isValid()) {
            return myFlush;
        } else if (myStraight.isValid()) {
            return myStraight;
        }
        return null;
    }
}