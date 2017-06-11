/**
 * @project     : Argmagetron
 * @file        : LoginController.java
 * @author(s)   : Thomas Lechaire, Kevin Pradervand, Elie N'Djoli Bohulu, Michael Brouchoud
 * @date        : 08.06.2017
 *
 * @brief        : Controller of the login windows
 */


package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.net.InetAddress;


public class LoginController extends java.util.Observable{

    @FXML
    private TextField nickName;
    @FXML
    private TextField serverIp;
    @FXML
    private ColorPicker colorChoice;
    @FXML
    private Button startButton;
    @FXML
    private Label errorMessage;

    public String userName;
    public String serverIP;
    public Color userColor;
    private boolean ipReachable = false;
    private boolean formOK = false;

    /**
     * @fn public void init()
     *
     * @brief Method that initate the LoginController
     *
     * @return void
     */
    public void init() {

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                userName = nickName.getText();
                serverIP = serverIp.getText();
                userColor = colorChoice.getValue();

                // test d'adresse IP valable
                if (checkName(userName)){
                    if(checkIP(serverIP)) {
                        if(checkColor(userColor)) {
                            formOK = true;
                            setChanged();
                            notifyObservers("");
                        }else{
                            errorMessage.setText("Vous avez choisi la couleur du terrain, mauvaise idee...");
                        }
                    }else{
                        if (ipReachable){
                            errorMessage.setText("Adresse IP non recevable");
                        }else{
                            errorMessage.setText("Server non accessible!");
                        }
                    }
                }else{
                    errorMessage.setText("Pseudo non autorise : entre 3 et 12 characteres svp");
                }
            }
        });
    }

    /**
     * @fn boolean checkIP (String ip)
     *
     * @brief Method to check if the IP address is correct or reachable
     *
     * @param ip
     * @return boolean
     */
    boolean checkIP (String ip) {
        if (ip.matches("^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}") | ip.matches("localhost")) {
            try {
                if (InetAddress.getByName(ip).isReachable(60)) {
                    ipReachable = true;
                    return true;
                }else{
                    return false;
                }
            }catch (Exception e ){ System.out.println(e);}
        }
        return false;
    }

    /**
     * @fn boolean checkName (String name)
     *
     * @brief Method to check if the name of the palyer is correct
     *
     * @param name
     * @return boolean
     */
    boolean checkName (String name) {
        return (name.matches("^.{3,12}$"));
    }

    /**
     * @fn boolean checkColor (Color color)
     *
     * @brief Methode to check if the player'scolor is different from white
     *
     * @param color
     * @return boolean
     */
    boolean checkColor (Color color) {
        return !(color == Color.WHITE);
    }

    /**
     * @fn public boolean checkForm()
     *
     * @brief Methode to check if the state's form
     *
     * @return boolean
     */
    public boolean checkForm(){
        return formOK;
    }

}
