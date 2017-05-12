package sample;

import client.Client;
import client.Game;
import client.Player;
import com.sun.org.apache.xpath.internal.SourceTree;
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
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.awt.*;

public class Controller {

    @FXML
    private GridPane gameGrid;

    private Client client;
    private Player player;
    private Game game;

    private Boolean setPlayerData = false;

    @FXML
    private Pane color;

    @FXML
    private Label nom;

    @FXML
    private Label timer;

    @FXML
    private Label classement;

    /*public Controller() {
        this.game = new Game();
        this.connect();
    }*/

    public void init() {
        this.game = new Game();
        this.connect();
    }

    public void connect() {
        client = new Client("localhost", 1500, "gamer", this);
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
                    break;
                case GameStatData.END_GAME:
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
            Platform.runLater(()->color.setStyle("-fx-background-color: " + this.convertColortoHex(player.getColor()) + ";"));

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
                        n.setStyle("-fx-background-image: url('/img/skull.png');" +
                                    "-fx-background-position: center center;" +
                                    "-fx-background-repeat: stretch;" );
                    }
                }
            }
        }
    }

    private String convertColortoHex(Color color) {
        return String.format("#%06x", color.getRGB() & 0x00FFFFFF);
    }
}
