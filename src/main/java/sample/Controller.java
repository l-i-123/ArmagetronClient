/**
 * @project     : Argmagetron
 * @file        : Controller.java
 * @author(s)   : Thomas Léchaire, Kevin Pradervand, Elie N'Djoli Bohulu, Michaël Brouchoud
 * @date        : 08.06.2017
 *
 * @brief        : Controller of the playing grid that display the game
 */
package sample;

import client.Client;
import client.Game;
import client.Player;
import data.ClientStatData;

import data.GameData;
import data.GameStatData;
import data.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller {

    private Client client;
    private Player player;
    private Game game;
    private ArrayList<Circle> colorPlayers ;
    private ArrayList<Label> labelPlayers;
    private boolean setPlayersInformation = false;
    private Boolean endGame = false;

    @FXML
    private GridPane gameGrid;
    @FXML
    private Circle colorPlayer1;
    @FXML
    private Circle colorPlayer2;
    @FXML
    private Circle colorPlayer3;
    @FXML
    private Circle colorPlayer4;
    @FXML
    private TextArea affichageTextuel;
    @FXML
    private Label namePlayer1;
    @FXML
    private Label namePlayer2;
    @FXML
    private Label namePlayer3;
    @FXML
    private Label namePlayer4;
    @FXML
    private Label classement;

    /**
     * @fn init
     *
     * @brief Initialisation methode
     *
     * @param serverIP
     * @param userName
     * @param userColor
     */
    public void init(String serverIP, String userName, Color userColor) {

        this.game = new Game();
        this.connect(serverIP,userName,userColor);
        this.colorPlayers = new ArrayList<>();
        this.colorPlayers.add(colorPlayer1);
        this.colorPlayers.add(colorPlayer2);
        this.colorPlayers.add(colorPlayer3);
        this.colorPlayers.add(colorPlayer4);

        this.labelPlayers = new ArrayList<>();
        labelPlayers.add(namePlayer1);
        labelPlayers.add(namePlayer2);
        labelPlayers.add(namePlayer3);
        labelPlayers.add(namePlayer4);

    }

    /**
     * @fn connect
     *
     * @brief Connection methode between client and server
     *
     * @param serverIP
     * @param userName
     * @param userColor
     */
    public void connect(String serverIP, String userName, Color userColor) {
        client = new Client(serverIP, 1500, userName, this, userColor);
    }

    /**
     * @fn onKeyPressed
     *
     * @brief methode to send to key pressed to the client object
     *
     * @param keyEvent
     */
    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.LEFT) {
            client.sendData(new ClientStatData(ClientStatData.TURN_LEFT, player.getUniqueId()));
            System.out.println("clique gauche");
        }
        else if(keyEvent.getCode() == KeyCode.RIGHT) {
            client.sendData(new ClientStatData(ClientStatData.TURN_RIGHT, player.getUniqueId()));
            System.out.println("Clique droit");
        }
    }
    
    /**
     * @fn getNodeByRowColumnIndex
     *
     * @brief Methode who return a selected node with the axe x and y
     *
     * @param row
     * @param column
     * @return Node
     */
    private Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;

        try {
            for (Node node : gameGrid.getChildren()) {
                if (gameGrid.getRowIndex(node) == row && gameGrid.getColumnIndex(node) == column) {
                    result = node;
                    break;
                }
            }
        } catch (NullPointerException e) {
            //TODO a mieux gerer
            share.Util.print("NullPointerException : " + e.getMessage());
        }

        return result;
    }

    /**
     * @fn processData
     *
     * @brief Methode to receive the object from the client
     *
     * @param o
     */
    public void processData(Object o) {
        if(o instanceof GameStatData) {
            switch (((GameStatData) o).getType()) {
                case GameStatData.START_GAME:
                    this.showStartGame();
                    countdown(((GameStatData) o).getTimeToWait());
                    break;
                case GameStatData.END_GAME:
                    this.showEndGame();
                    endGame = true;
                    break;
            }
        }
        if(o instanceof GameData) {
            synchronized (game) {
                this.game.setPlayers(((GameData) o).getPlayersData());
                if(!setPlayersInformation){
                    setColorPlayers();
                    setPlayersInformation = true;
                }
                if(endGame){
                    setClassement(((GameData) o).getPlayersData());
                }
                this.updatePosition();
            }
        }
        if(o instanceof PlayerData) {
            this.player = new Player(((PlayerData) o).getUniqueId(), ((PlayerData) o).getPosition(), ((PlayerData) o).getColor(), ((PlayerData) o).getUsername(), ((PlayerData) o).isAlive());
        }
    }

    /**
     * @fn showStartGame
     *
     * @brief Textual display about the start of the game
     *
     */
    public void showStartGame() {
        affichageTextuel.setText("The game will begin get ready !!!");
    }

    /**
     * @fn showEndGame
     *
     * @brief Textual display about the end of a game
     *
     */
    public void showEndGame(){
        affichageTextuel.setText("Game over !!!");
    }

    /**
     * @fn updatePosition
     *
     *@brief Methode who update the position's players
     *
     */
    private void updatePosition() {
        synchronized (this.gameGrid) {
            for (Player player : game.getPlayers()) {
                Node n = getNodeByRowColumnIndex(player.getPosition().x, player.getPosition().y);
                if (n != null) {
                    if(player.isAlive()) {
                        n.setStyle("-fx-background-color: " + this.convertColortoHex(player.getColor()) + ";");
                    }
                    else {
                        n.setStyle("-fx-background-image: url('/img/skull.png');" +
                                "-fx-background-position: center center;" +
                                "-fx-background-repeat: stretch;" );
                    }
                }
            }
        }
    }

    /**
     *
     * @fn rebornAccount
     *
     * @brief countdown for the begin of the game
     *
     * @param nbMilliseconde
     */
    public void countdown(short nbMilliseconde){
        new Thread(){
            public void run(){
                int nbSeconde = nbMilliseconde / 1000;
                int compteur = 0;
                while(compteur < nbSeconde){
                    try{
                        affichageTextuel.setText("The game is starting in : " + (nbSeconde - compteur));
                        compteur++;
                        Thread.sleep(1000);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
                affichageTextuel.setText("Good Luck !!!!");
            }
        }.start();
    }


    /**
     * @fn setColorPlayers
     *
     * @brief Players color setting
     *
     */
    public void setColorPlayers(){
        Iterator<Circle> playerColorIterator = colorPlayers.iterator();
        Iterator<Label> playerLabelIterator = labelPlayers.iterator();

        for(final Player player: game.getPlayers()){
            if(playerColorIterator.hasNext() && playerLabelIterator.hasNext()){
                Platform.runLater(()->playerColorIterator.next().setFill(Paint.valueOf(this.convertColortoHex(player.getColor()))));
                Platform.runLater(()->playerLabelIterator.next().setText(player.getUsername()));
            }
        }
    }

    /**
     * @fn setClassement
     *
     * @brief Classement display
     *
     * @param players liste des players
     */
    private void setClassement(ArrayList<PlayerData> players){
        int position = 0;
        String texteClassement = "";
        for(int i = 0; i < players.size();++i) {
            System.out.println(players.get(i).getUniqueId());
            System.out.println(player.getUniqueId());
            if (players.get(i).getUniqueId().equals(player.getUniqueId())) {
                position = i;
            }
        }
        switch (position){
            case 0:
                texteClassement = "1st";
                affichageTextuel.setText("HOUAA! What an amazing pilot!");
                break;
            case 1:
                texteClassement = "2nd";
                affichageTextuel.setText("Almost there!");
                break;
            case 2:
                texteClassement = "3rd";
                affichageTextuel.setText("Good Try.");
                break;
            case 3:
                texteClassement = "4th";
                affichageTextuel.setText("Shit happens...");
                break;
        }
        String finalTexteClassement = texteClassement;
        Platform.runLater(()->classement.setText(finalTexteClassement));
    }

    /**
     * @fn convertColorHex
     *
     * @brief Methode to convert Color object to String
     *
     * @param color
     * @return String
     */
    private String convertColortoHex(Color color) {
        return String.format("#%06x", color.getRGB() & 0x00FFFFFF);
    }
}