/**
 * @project     : Argmagetron
 * @file        : LoginController.java
 * @author(s)   :  Thomas Léchaire, Kevin Pradervand, Elie N'Djoli Bohulu, Michaël Brouchoud
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


/**
 * Created by Kevin on 11.05.2017.
 */
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
    public boolean ipReachable = false;
    private boolean formOK = false;

    /**
     * @fn init
     *
     * @brief Methode that initate the LoginController
     *
     * @return void
     */
    public void init() {

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("tu as clique sur start!");

                userName = nickName.getText();
                System.out.println(userName);
                serverIP = serverIp.getText();
                System.out.println(serverIP);
                userColor = colorChoice.getValue();
                System.out.println(userColor);

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
                System.out.println("fin du clic");
                System.out.println(formOK);
            }
        });

        System.out.println("fin fin du clic");
    }

    /**
     * @fn checkIP
     *
     * @brief Methode to check if the IP address is correct or reachable
     *
     * @param ip
     * @return boolean
     */
    public boolean checkIP (String ip){
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
     * @fn checkName
     *
     * @brief Methode to check if the name of the palyer is correct
     *
     * @param name
     * @return boolean
     */
    public boolean checkName (String name){
        if (name.matches("^.{3,12}$")){
            return true;
        }
        return false;

    }

    /**
     * @fn checkColor
     *
     * @brief Methode to check if the player'scolor is different from white
     *
     * @param color
     * @return boolean
     */
    public boolean checkColor (Color color){
        if (color == Color.WHITE){
            return false;
        }
        return true;


    }
    /**
     * @fn checkForm
     *
     * @brief Methode to check if the state's form
     *
     * @return boolean
     */
    public boolean checkForm(){
        return formOK;
    }

}
