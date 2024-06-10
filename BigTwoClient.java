import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class is used for modeling a Big Two client for the Big Two card game.
 * 
 * @author Fung Ho Sang
 */
public class BigTwoClient implements NetworkGame {

    /**
     * Public constructor for BigTwoClient class.
     */
    public BigTwoClient(BigTwo game, BigTwoGUI gui) {
        this.game = game;
        this.gui = gui;
    }

    private BigTwo game;
    private BigTwoGUI gui;
    private Socket sock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int playerID;
    private String playerName;
    private String serverIP;
    private int serverPort;

    /**
	 * Gets the player index of this client player
     * 
     * @return the player index of this client player
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**
	 * Sets the player index of this client player
	 * 
	 * @param playerID an int value representing the index of the active player
	 */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
	 * Gets the player name of this client player
     * 
     * @return the player name of this client player
     */
    public String getPlayerName() {
        return this.playerName;
    }
    
    /**
	 * Sets the player name of this client player
     *
	 * @param playerName the player name of this client player
	 */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    /**
	 * Gets the server IP for this client to connect to
     * 
     * @return the server IP for this client to connect to
     */
    public String getServerIP() {
        return this.serverIP;
    }

    /**
	 * Sets the server IP for this client to connect to
     *
	 * @param serverIP the server IP for this client to connect to
	 */
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * Gets the server port for this client to connect to
     * 
     * @return the server port for this client to connect to
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
	 * Sets the server port for this client to connect to
     *
	 * @param serverPort the server port for this client to connect to
	 */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
	 * Connects this client to the server
	 */
    public void connect() {
        try {
            if (serverPort == 0) {
                serverPort = 2396;
            }
            sock = new Socket(serverIP, serverPort);
            //(i) create an ObjectOutputStream for sending messages to the game server;
            oos = new ObjectOutputStream(sock.getOutputStream());
            // (ii) create a new thread for receiving messages from the game server
            ServerHandler serverHandler = new ServerHandler();
            Thread serverThread = new Thread(serverHandler);
            serverThread.start();
            sendMessage(new CardGameMessage(1, playerID, playerName)); // 1 = JOIN
            sendMessage(new CardGameMessage(4, playerID, playerName)); // 4 = READY
            gui.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Parse message from server
     *
     * @param message the message to be parsed
	 */
    public void parseMessage(GameMessage message) {
        try {
            if (message.getType() == 0) { // playerList
                setPlayerID(message.getPlayerID());
                gui.setActivePlayer(message.getPlayerID());
                //System.out.println(((String[])message.getData())[0]);
                for(int i=0;i<4;i++) {
                    if(((String[])message.getData())[i]!=null) {
                        game.getPlayerList().get(i).setName(((String[])message.getData())[i]);
                    }
                }
                gui.repaint();
            } else if(message.getType()== 1) { // join
                game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
                gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " joined the game!");
                gui.repaint();
            } if (message.getType() == 2) { // full
                gui.printMsg("The server is full and cannot be joined!");
                try {
                    sock.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                gui.repaint();
            } else if(message.getType()== 3) { // quit
                gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " left the game.");
                game.getPlayerList().get(message.getPlayerID()).setName("");
                if(!game.endOfGame()) {
                    gui.disable();
                    sendMessage(new CardGameMessage(4, -1, null)); // Send ready
                    for(int i=0;i<4;i++) {
                        game.getPlayerList().get(i).removeAllCards();
                    }
                    gui.repaint();
                }
                gui.repaint();
            } else if(message.getType() == 4) { // ready
                gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " is ready.");
            } else if (message.getType() == 5) { // start
                game.start((BigTwoDeck)message.getData());
                gui.repaint();
            } else if (message.getType() == 6) { // move
                game.checkMove(message.getPlayerID(),(int[])message.getData());
                gui.repaint();
            } else if (message.getType() == 7) { // msg
                gui.getChatArea().append(((String)message.getData()) + "\n");
                gui.repaint();
            } 
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
	 * Send message to server
     *
     * @param message the message to be sent
	 */
    public void sendMessage(GameMessage message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Server handler
	 */
    public class ServerHandler implements Runnable{
        /**
	    * run() method for server handler.
	    */
        @Override
        public void run() {
            try {
                ois = new ObjectInputStream(sock.getInputStream());
                while(true) {
                    parseMessage((CardGameMessage)ois.readObject());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}