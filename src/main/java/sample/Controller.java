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
    private Boolean startTimer = false;
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
     * @brief Methode de connection du client au serveur
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
     * @brief Methode transmettant au client l'etat des touches gauche et droite
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
     * @brief Methode retournant un noeud par rapport à une position donnee
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
     * @brief Methode appele par l'objet Client lui permettant de transmettre au contrôleur les objet qui le concerne
     *
     * @param o
     */
    public void processData(Object o) {
        if(o instanceof GameStatData) {
            switch (((GameStatData) o).getType()) {
                case GameStatData.START_GAME:
                    this.showStartGame();
                    rebornAccount(((GameStatData) o).getTimeToWait());
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
                    startTimer = false;
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
     * @brief Methode appele en debut departie pour afficher textuellement le demarrage de la partie
     *
     */
    public void showStartGame() {
        share.Util.print("The game will begin get ready !!!");
        affichageTextuel.setText("The game will begin get ready !!!");
    }

    /**
     * @fn showEndGame
     *
     * @brief Methode appele à la fin de la partie pour indiquer textuellement que la partie est termine
     *
     */
    public void showEndGame(){
        affichageTextuel.setText("Game over !!!");
    }

    /**
     * @fn updatePosition
     *
     *@brief Methode mettant à jour les potision des joueurs
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
     * @brief compte a rebours avant le debut de la partie
     *
     * @param nbMilliseconde
     */
    public void rebornAccount(short nbMilliseconde){
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
     * @brief Methode permettant de setter les couleurs de chaque joueur dans l'interface graphique
     *
     */
    public void setColorPlayers(){
        //Iterator<Player> playerColorIterator = game.getPlayers().iterator();
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
     * @brief Methode d'affichage du classement
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
                texteClassement = "1er";
                break;
            case 1:
                texteClassement = "2ème";
                break;
            case 2:
                texteClassement = "3ème";
                break;
            case 3:
                texteClassement = "4ème";
                break;
        }
        String finalTexteClassement = texteClassement;
        Platform.runLater(()->classement.setText(finalTexteClassement));
    }

    /**
     * @fn getClient
     *
     * @briel Methode qui retourne le client
     *
     * @return
     */
    public Client getClient (){
        return this.client;
    }

    /**
     * @fn convertColorHex
     *
     * @brief Methode permettant de convertir les couleurs de type Color en type String
     *
     * @param color
     * @return String
     */
    private String convertColortoHex(Color color) {
        return String.format("#%06x", color.getRGB() & 0x00FFFFFF);
    }
}