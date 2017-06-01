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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller {

    @FXML
    private GridPane gameGrid;

    private Client client;
    private Player player;
    private Game game;
    private Boolean startTimer = false;

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

    private ArrayList<Circle> colorPlayers ;

    @FXML
    private Label namePlayer1;
    @FXML
    private Label namePlayer2;
    @FXML
    private Label namePlayer3;
    @FXML
    private Label namePlayer4;

    private ArrayList<Label> labelPlayers;

    @FXML
    private Label timer;

    @FXML
    private Label classement;

    private int nbMort = 0;

    private boolean setPlayersInformation = false;

    /*public Controller() {
        this.game = new Game();
        this.connect();
    }*/

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

    public void connect(String serverIP, String userName, Color userColor) {
        client = new Client(serverIP, 1500, userName, this, userColor);
    }

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

    public void processData(Object o) {
        if(o instanceof GameStatData) {
            switch (((GameStatData) o).getType()) {
                case GameStatData.START_GAME:
                    this.showStartGame();
                    startTimer();
                    break;
                case GameStatData.END_GAME:
                    this.showEndGame();
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
                this.updatePosition();
            }
        }
        if(o instanceof PlayerData) {
            this.player = new Player(((PlayerData) o).getUniqueId(), ((PlayerData) o).getPosition(), ((PlayerData) o).getColor(), ((PlayerData) o).getUsername(), ((PlayerData) o).isAlive());

        }
    }

    public void showStartGame() {
        share.Util.print("The game will begin get ready !!!");
        affichageTextuel.setText("The game will begin get ready !!!");
    }

    public void showEndGame(){
        affichageTextuel.setText("Game over !!!");
    }

    private void updatePosition() {
        synchronized (this.gameGrid) {
            for (Player player : game.getPlayers()) {
                Node n = getNodeByRowColumnIndex(player.getPosition().x, player.getPosition().y);
                if (n != null) {
                    if(player.isAlive()) {
                        n.setStyle("-fx-background-color: " + this.convertColortoHex(player.getColor()) + ";");
                    }
                    else {
                        nbMort++;
                        n.setStyle("-fx-background-image: url('/img/skull.png');" +
                                    "-fx-background-position: center center;" +
                                    "-fx-background-repeat: stretch;" );

                        //Mise à jour du classement
                        if(player.getUniqueId().compareTo(this.player.getUniqueId()) == 0){
                            startTimer = false;
                            String texteClassement = "";
                            int classementInt = game.getPlayers().size() - nbMort;

                            switch(classementInt){
                                case 0:{
                                    texteClassement = "1er";
                                    break;
                                }
                                case 1:{
                                    texteClassement = "2eme";
                                    break;
                                }
                                case 2:{
                                    texteClassement = "3eme";
                                    break;
                                }
                                case 3:{
                                    texteClassement = "4eme";
                                    break;
                                }
                            }
                            String finalTexteClassement = texteClassement;
                            Platform.runLater(()->classement.setText(finalTexteClassement));
                        }
                        System.out.println("contenu variable nbMort : " + nbMort);
                    }
                }
            }
        }
    }

    private void startTimer(){
        startTimer = true;
        new Thread(){
            public int secondes;
            public int minutes;
            NumberFormat formatter = new DecimalFormat("00");
            public void run(){
                while(startTimer){
                    try {
                        Thread.sleep(1000);
                            secondes++;
                        if(secondes == 60){
                            minutes++;
                            secondes = 0;
                        }
                        Platform.runLater(()->timer.setText(formatter.format(minutes) + ":" + formatter.format(secondes)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

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

//        for(final Circle color : colorPlayers) {
//            Platform.runLater(()->color.setFill(Paint.valueOf(this.convertColortoHex(playerColorIterator.next().getColor()))));
//            Platform.runLater(()->color.setFill(Paint.valueOf(this.convertColortoHex(playerColorIterator.next().getColor()))));
//        }
    }

    public Client getClient (){
        return this.client;
    }

    private String convertColortoHex(Color color) {
        return String.format("#%06x", color.getRGB() & 0x00FFFFFF);
    }
}