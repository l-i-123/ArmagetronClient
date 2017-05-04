package sample;

import client.Client;
import data.ClientStatData;
import data.ConfigData;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.awt.*;

public class Controller {

    @FXML
    private GridPane gameGrid;

    private static int x;
    private static int y;

    private Client client;

    // if it is for connection
    private boolean connected;

    // the default port number
    private int defaultPort;
    private String defaultHost;

    public void setClient(Client client) {
        this.client = client;
    }

    public Controller() {

    }

    public boolean test() {
        return true;
    }

    public void connect() {
        client = new Client("localhost", 1500, "gamer", this);
        // test if we can start the Client
        if(!client.start())
            return;
        connected = true;
    }

    public void setConfigData(ConfigData configData) {
        this.x = configData.getStartPosition().x;
        this.y = configData.getStartPosition().y;
        getNodeByRowColumnIndex(x, y).setStyle("-fx-background-color: green;");
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.UP) {
            x--;
            client.sendData(new ClientStatData(ClientStatData.TURN_LEFT));
        }
        else if(keyEvent.getCode() == KeyCode.DOWN) {
            x++;
            client.sendData(new ClientStatData(ClientStatData.LOGIN));
        }
        else if(keyEvent.getCode() == KeyCode.LEFT) {
            y--;
            client.sendData(new ClientStatData(ClientStatData.LOGOUT));
        }
        else if(keyEvent.getCode() == KeyCode.RIGHT) {
            y++;
        }
        else if(keyEvent.getCode() == KeyCode.S) {
            setConfigData(new ConfigData(new Point(10, 15), Color.green));
        }

        getNodeByRowColumnIndex(x, y).setStyle("-fx-background-color: green;");
    }

    private Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = gameGrid.getChildren();

        for (Node node : childrens) {
            if(gameGrid.getRowIndex(node) == row && gameGrid.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
