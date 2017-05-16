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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Controller {

    @FXML
    private GridPane gameGrid;

    private Client client;
    private Player player;
    private Game game;
    private Boolean startTimer = false;

    @FXML
    private Circle color;

    @FXML
    private Label nom;

    @FXML
    private Label timer;

    @FXML
    private Label classement;

    private int nbMort = 0;

    /*public Controller() {
        this.game = new Game();
        this.connect();
    }*/

    public void init(String serverIP, String userName, Color userColor) {

        this.game = new Game();
        this.connect(serverIP,userName,userColor);
    }

    public void connect(String serverIP, String userName, Color userColor) {
        client = new Client("localhost", 1500, "Trawn", this);
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
                    startTimer = false;
                    break;
            }
        }
        if(o instanceof GameData) {
            synchronized (game) {
                this.game.setPlayers(((GameData) o).getPlayersData());
                this.updatePosition();
            }
        }
        if(o instanceof PlayerData) {
            this.player = new Player(((PlayerData) o).getUniqueId(), ((PlayerData) o).getPosition(), ((PlayerData) o).getColor(), ((PlayerData) o).getUsername(), ((PlayerData) o).isAlive());

            //Affiche la couleur du joueur sur l'interface graphique
            //Définition de Platform.runLater : Run the specified Runnable on the JavaFX Application Thread at some unspecified time in the future.
            Platform.runLater(()->color.setFill(Paint.valueOf(this.convertColortoHex(player.getColor()))));

            //Afficher le nom du joueur dans l'interface graphique
            Platform.runLater(()->nom.setText(this.player.getUsername()));
        }
    }

    public void showStartGame() {
        share.Util.print("The game will begin get ready !!!");
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
                            String texteClassement = "";
                            int classementInt = game.getPlayers().size() - nbMort;

                            switch(classementInt){
                                case 0:{
                                    texteClassement = "1er";
                                    break;
                                }
                                case 1:{
                                    texteClassement = "2ème";
                                    break;
                                }
                                case 2:{
                                    texteClassement = "3ème";
                                    break;
                                }
                                case 3:{
                                    texteClassement = "4ème";
                                    break;
                                }
                            }
                            String finalTexteClassement = texteClassement;
                            Platform.runLater(()->classement.setText(finalTexteClassement));
                        }
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

    private String convertColortoHex(Color color) {
        return String.format("#%06x", color.getRGB() & 0x00FFFFFF);
    }
}