import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Container;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.OverlayLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

/**
 * This class is used for modeling a graphical user interface for the Big Two card game.
 * 
 * @author Fung Ho Sang
 */
public class BigTwoGUI implements CardGameUI {

	/**
 	 * Constructor of BigTwoGUI
 	 */
    public BigTwoGUI(BigTwo game) {
        this.game = game;
		playerList = game.getPlayerList();
		handsOnTable = game.getHandsOnTable();

        frame = new JFrame();
		frame.setTitle("Big Two");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu();
        gameMenu.setText("Game");
        JMenuItem connectMenuItem = new JMenuItem();
        connectMenuItem.addActionListener(new ConnectMenuItemListener());
        connectMenuItem.setText("Connect");
        gameMenu.add(connectMenuItem);
        JMenuItem quitMenuItem = new JMenuItem();
        quitMenuItem.addActionListener(new QuitMenuItemListener());
        quitMenuItem.setText("Quit");
        gameMenu.add(quitMenuItem);
        JMenu messageMenu = new JMenu();
        messageMenu.setText("Message");
        JMenuItem clearInformationMenuItem = new JMenuItem();
        clearInformationMenuItem.setText("Clear information box");
        clearInformationMenuItem.addActionListener(new ClearInformationMenuItemListener());
        messageMenu.add(clearInformationMenuItem);
        JMenuItem clearChatMenuItem = new JMenuItem();
        clearChatMenuItem.setText("Clear chat box");
        clearChatMenuItem.addActionListener(new ClearChatItemListener());
        messageMenu.add(clearChatMenuItem);
        menuBar.add(gameMenu);
        menuBar.add(messageMenu);
        frame.add(menuBar, BorderLayout.NORTH);

        playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
        passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		JPanel bottomPanel = new JPanel();
        bottomPanel.add(playButton, BorderLayout.WEST);
        bottomPanel.add(passButton, BorderLayout.EAST);
		frame.add(bottomPanel, BorderLayout.SOUTH);

		bigTwoPanel = new JPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
		panels = new JPanel[] {new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel()};
		playerAvatars = new JLabel[] {new JLabel(), new JLabel(), new JLabel(), new JLabel()};
        playerNames = new JLabel[] {new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()};
		for (int i=0; i<playerList.size(); i++) {
            playerNames[i].setHorizontalAlignment(JLabel.LEFT);
            playerNames[i].setForeground(new Color(120, 90, 40));
            playerNames[i].setFont(new Font("Verdana", Font.PLAIN, 12));
            playerNames[i].setText(playerList.get(i).getName());
            playerNames[i].setBorder(new EmptyBorder(0,5,0,0));
			playerAvatars[i].setIcon(new ImageIcon("avatars/avatar"+i+".png"));
			panels[i].setLayout(new BoxLayout(panels[i],BoxLayout.X_AXIS));
			panels[i].setBorder(new EmptyBorder(6,6,6,6));
            panels[i].setPreferredSize(new Dimension(590,120));
            playerAvatars[i].setBorder(new EmptyBorder(0,0,0,5));
            panels[i].add(playerAvatars[i]);
            //panels[i].add(playerNames[i]);
            panels[i].setForeground(Color.red);
			bigTwoPanel.add(panels[i]);
		}
        panels[4].setMaximumSize(new Dimension(590,120));
        playerNames[4].setHorizontalAlignment(JLabel.LEFT);
        playerNames[4].setForeground(new Color(120, 90, 40));
        playerNames[4].setFont(new Font("Verdana", Font.PLAIN, 12));
        playerNames[4].setText("Cards on table");
        panels[4].add(playerNames[4]);
		bigTwoPanel.add(panels[4]);
		frame.add(bigTwoPanel, BorderLayout.WEST);

		chatPanel = new JPanel();
		msgArea = new JTextArea();
        msgArea.setEnabled(false);
		chatArea = new JTextArea();
        chatArea.setEnabled(false);
		chatInput = new JTextField();
		chatInput.setPreferredSize(new Dimension(150, 30));
		chatInput.addActionListener(new ChatInputListener());
        JLabel messageLabel = new JLabel();
        messageLabel.setText("Message: ");
        JPanel chatboxPanel = new JPanel();
        chatboxPanel.add(messageLabel, BorderLayout.WEST);
        chatboxPanel.add(chatInput, BorderLayout.EAST);
		Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		
		chatPanel.setPreferredSize(new Dimension(230, 500));
        JScrollPane spMsgArea = new JScrollPane(msgArea); 
        JScrollPane spChatArea = new JScrollPane(chatArea); 
        spMsgArea.setBorder(blackBorder);
        spMsgArea.setPreferredSize(new Dimension(230,300));
        spMsgArea.setMaximumSize(new Dimension(230,300));
		spChatArea.setBorder(blackBorder);
        spChatArea.setPreferredSize(new Dimension(230,300));
        spChatArea.setMaximumSize(new Dimension(230,300));
		chatPanel.add(spMsgArea);
		chatPanel.add(spChatArea);
		chatPanel.add(chatboxPanel);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		frame.add(chatPanel, BorderLayout.EAST);

        frame.setSize(850, 650);
        frame.setVisible(true);
    }

    private final static int MAX_CARD_NUM = 13;
    private BigTwo game = null;
    private boolean[] selected = new boolean[MAX_CARD_NUM];
    private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
    private int activePlayer = -1;
    private Scanner scanner;
    private JFrame frame;
    private JPanel bigTwoPanel;
    private JButton playButton;
    private JButton passButton;
    private JTextArea msgArea;
    private JTextArea chatArea;
    private JTextField chatInput;
	private JPanel[] panels;
	private JLabel[] playerAvatars;
    private JLabel[] playerNames;
	private ArrayList<ArrayList<JLabel>> playerCards;
	private JPanel chatPanel;
    private JMenuBar menuBar;

	/**
	 * Gets the chat area.
	 */
	public JTextArea getChatArea() {
		return this.chatArea;
	}

    /**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= playerList.size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
    }

	/**
	 * Repaints the user interface.
	 */
	public void repaint() {
		for (int i = 0; i < 4; i++) {
			game.getPlayerList().get(i).sortCardsInHand();
		}
		this.game = game;
		playerList = game.getPlayerList();
		handsOnTable = game.getHandsOnTable();
		frame.setTitle("Big Two (" + game.getClient().getPlayerName() + ")");

        selected = new boolean[MAX_CARD_NUM];
		playerCards = new ArrayList<ArrayList<JLabel>>();
		playerCards.add(new ArrayList<JLabel>());
		playerCards.add(new ArrayList<JLabel>());
		playerCards.add(new ArrayList<JLabel>());
		playerCards.add(new ArrayList<JLabel>());
		for (int i = 0; i < 4; i++) {
			CardGamePlayer player = playerList.get(i);
			String name = player.getName();
			playerNames[i].setText(name);
			panels[i].removeAll();
			playerNames[i].setPreferredSize(new Dimension(80,20));
			playerNames[i].setMaximumSize(new Dimension(80,20));
			panels[i].add(playerNames[i]);
			panels[i].add(playerAvatars[i]);
			for (int j = 0; j < player.getNumOfCards(); j++) {
				if (game.getClient().getPlayerID() == i) {
					playerCards.get(i).add(new JLabel());
					playerCards.get(i).get(j).setIcon(new ImageIcon("cards/" + player.getCardsInHand().getCard(j).getRank() + player.getCardsInHand().getCard(j).getSuit() + ".gif"));
					if (j!=player.getNumOfCards()-1) {
						playerCards.get(i).get(j).setBorder(new EmptyBorder(0,0,0,-50));
					} else {
						playerCards.get(i).get(j).setBorder(new EmptyBorder(0,0,0,0));
					}
					panels[i].add(playerCards.get(i).get(j));
					playerCards.get(i).get(j).addMouseListener(new CardClickListener());
				} else if (activePlayer == -1) {
					System.out.println("<" + name + ">");
					System.out.print("    ");
					player.getCardsInHand().print(true, true);
				} else {
					playerCards.get(i).add(new JLabel());
					playerCards.get(i).get(j).setIcon(new ImageIcon("cards/back.gif"));
					if (j!=player.getNumOfCards()-1) {
						playerCards.get(i).get(j).setBorder(new EmptyBorder(0,0,0,-50));
					} else {
						playerCards.get(i).get(j).setBorder(new EmptyBorder(0,0,0,0));
					}
					panels[i].add(playerCards.get(i).get(j));
					playerCards.get(i).get(j).addMouseListener(new CardClickListener());
				}
			}
			if (name == "") {
				panels[i].setVisible(false);
			} else {
				panels[i].setVisible(true);
			}
			frame.validate();
			frame.repaint();
		}
		
		Hand lastHandOnTable;
		if (handsOnTable != null) {
			lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
		} else {
			lastHandOnTable = null;
		}
		panels[4].removeAll();
        panels[4].add(playerNames[4]);
        panels[4].setLayout(new BoxLayout(panels[4],BoxLayout.X_AXIS));
		if (lastHandOnTable != null) {
            JLabel[] tableCard = new JLabel[5];
			for (int i = 0; i < lastHandOnTable.size(); i++) {
                tableCard[i] = new JLabel();
                tableCard[i].setIcon(new ImageIcon("cards/" + lastHandOnTable.getCard(i).getRank() + lastHandOnTable.getCard(i).getSuit() + ".gif"));
                panels[4].add(tableCard[i]);
                panels[4].repaint();
            }
		}
		
        frame.validate();
		frame.repaint();
		enable();
    }

	/**
	 * Prints the end game text when game is over.
	 * 
	 * @param content the string content to be shown
	 * @param WinnerID the winner's index
	 */
	public void displayEndGameText(String content, int winnerID) {
		if (game.getClient().getPlayerID() == winnerID) {
			int res = JOptionPane.showOptionDialog(bigTwoPanel, content, "You win!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (res == JOptionPane.OK_OPTION) {
				game.getClient().sendMessage(new CardGameMessage(4, -1, null)); // 4 = READY
			}
		} else {
			int res = JOptionPane.showOptionDialog(bigTwoPanel, content, "You lose!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (res == JOptionPane.OK_OPTION) {
				game.getClient().sendMessage(new CardGameMessage(4, -1, null)); // 4 = READY
			}
		}
	}

	/**
	 * Prints the specified string to the message area of the card game user
	 * interface.
	 * 
	 * @param msg the string to be printed to the message area of the card game user
	 *            interface
	 */
	public void printMsg(String msg) {
        msgArea.append(msg+"\n");
    }

	/**
	 * Clears the message area of the card game user interface.
	 */
	public void clearMsgArea() {

    }

	/**
	 * Resets the card game user interface.
	 */
	public void reset() {

    }

	/**
	 * Enables user interactions.
	 */
	public void enable() {
        playButton.setEnabled(true);
        passButton.setEnabled(true);
    }

	/**
	 * Disables user interactions.
	 */
	public void disable() {
        playButton.setEnabled(false);
        passButton.setEnabled(false);
    }

	/**
	 * Disable ALL interactions (when game finished).
	 */
	public void disableAll() {
        playButton.setEnabled(false);
        passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
    }

	/**
	 * Prompts active player to select cards and make his/her move.
     */
	public void promptActivePlayer() {
		printMsg(playerList.get(activePlayer).getName() + "'s turn: ");
    }
     
    /**
	 * MouseListener of BigTwoPanel
     */
    class BigTwoPanel extends JPanel implements MouseListener {
        public void paintComponent(Graphics g) {
			super.paintComponent(g);
        }

		@Override
        public void mouseReleased(MouseEvent event) {

        }

        public void mousePressed(MouseEvent event) {}
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
        public void mouseClicked(MouseEvent event) {}

    } 
   
    /**
	 * MouseListener of clicking the cards
     */
	class CardClickListener extends JPanel implements MouseListener {
		@Override
        public void mouseReleased(MouseEvent event) {
			JLabel c = ((JLabel)(event.getComponent()));
			if (c.getIcon().toString() == "cards/back.gif") {
				return;
			}
			int rank = -1;
			int suit = -1;
			if (c.getIcon().toString().length() == 12) {
				rank = Integer.parseInt(c.getIcon().toString().substring(6,7));
				suit = Integer.parseInt(c.getIcon().toString().substring(7,8));
			} else if (c.getIcon().toString().length() == 13) {
				rank = 10 + Integer.parseInt(c.getIcon().toString().substring(7,8));
				suit = Integer.parseInt(c.getIcon().toString().substring(8,9));
			}
			if (c.getBorder().getBorderInsets(c).bottom==0 && c.getBorder().getBorderInsets(c).right==-50) { // pop inner card up
				c.setBorder(new EmptyBorder(0,0,10,-50));
                for (int i=0; i<playerList.get(activePlayer).getNumOfCards(); i++) {
				if (playerList.get(activePlayer).getCardsInHand().getCard(i).getRank() == rank && playerList.get(activePlayer).getCardsInHand().getCard(i).getSuit() == suit) {
					selected[i] = true;
				}
			}
			} else if (c.getBorder().getBorderInsets(c).bottom==10 && c.getBorder().getBorderInsets(c).right==-50) { // pop inner card down
				c.setBorder(new EmptyBorder(0,0,0,-50));
                for (int i=0; i<playerList.get(activePlayer).getNumOfCards(); i++) {
				if (playerList.get(activePlayer).getCardsInHand().getCard(i).getRank() == rank && playerList.get(activePlayer).getCardsInHand().getCard(i).getSuit() == suit) {
					selected[i] = false;
				}
			    }
			} else if (c.getBorder().getBorderInsets(c).bottom==0 && c.getBorder().getBorderInsets(c).right==0) { // pop rightmost card up
				c.setBorder(new EmptyBorder(0,0,10,0));
                for (int i=0; i<playerList.get(activePlayer).getNumOfCards(); i++) {
				if (playerList.get(activePlayer).getCardsInHand().getCard(i).getRank() == rank && playerList.get(activePlayer).getCardsInHand().getCard(i).getSuit() == suit) {
					selected[i] = true;
				}
			    }
			} else if (c.getBorder().getBorderInsets(c).bottom==10 && c.getBorder().getBorderInsets(c).right==0) { // pop rightmost card down
				c.setBorder(new EmptyBorder(0,0,0,0));
                for (int i=0; i<playerList.get(activePlayer).getNumOfCards(); i++) {
				if (playerList.get(activePlayer).getCardsInHand().getCard(i).getRank() == rank && playerList.get(activePlayer).getCardsInHand().getCard(i).getSuit() == suit) {
					selected[i] = false;
				}
			    }
			}
			frame.repaint();
        }
          

        public void mousePressed(MouseEvent event) {}
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
        public void mouseClicked(MouseEvent event) {}

    }

    /**
	 * ActionListener of play button
     */
    class PlayButtonListener implements ActionListener {

		/**
	 	 * Action performed of play button
		 * Note: if NO card is selected, playing is NOT allowed
     	 */
        public void actionPerformed(ActionEvent event) {
			if (getSelected() != null && activePlayer == game.getClient().getPlayerID()) { // card is selected and is current active player
            	game.makeMove(activePlayer, getSelected());
			}	
        }

    }

    /**
	 * ActionListener of pass button
     */
    class PassButtonListener implements ActionListener {
		
		/**
	 	 * Action performed of pass button
		 * Note: if card is selected, passing is still allowed
     	 */
        public void actionPerformed(ActionEvent event) {
			if (activePlayer == game.getClient().getPlayerID()) {
				game.makeMove(activePlayer, getSelected());
			}
        }

    }

	/**
	 * ActionListener of chat input JTextField
     */
	class ChatInputListener implements ActionListener {

		/**
	 	 * Action performed of chat input JTextField
     	 */
        public void actionPerformed(ActionEvent event) {
			game.getClient().sendMessage(new CardGameMessage(7,game.getClient().getPlayerID(),chatInput.getText()));
			chatInput.setText("");
            repaint();
        }

    }

    /**
	 * ActionListener of connect menu button
     */
    class ConnectMenuItemListener implements ActionListener {
		
        /**
         * Action performed of connect menu button
     	 */
        public void actionPerformed(ActionEvent event) {
            game.connectToServer();
        }

    }

    /**
	 * ActionListener of quit menu button
     */
    class QuitMenuItemListener implements ActionListener {
		/**
         * Action performed of quit menu button
     	 */
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    /**
	 * ActionListener of clear information menu button
     */
    class ClearInformationMenuItemListener implements ActionListener {
		/**
         * Action performed of clear information menu button
     	 */
        public void actionPerformed(ActionEvent event) {
            msgArea.setText("");
        }
    }

    /**
	 * ActionListener of clear chat menu button
     */
    class ClearChatItemListener implements ActionListener {
		/**
         * Action performed of clear chat menu button
     	 */
        public void actionPerformed(ActionEvent event) {
            chatArea.setText("");
        }
    }

    private int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	private void resetSelected() {
		for (int j = 0; j < selected.length; j++) {
			selected[j] = false;
		}
	}
}