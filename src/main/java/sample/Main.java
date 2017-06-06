package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.LoginController;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Main extends Application implements Observer{

    final Stage primaryStage = new Stage();
    Stage loginStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{

        // fenetre de login

        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/login/login.fxml"));
        Parent loginRoot = loaderLogin.load();
        loginStage.setTitle("login");
        loginStage.setScene(new Scene(loginRoot));
        loginStage.show();
        loginRoot.getScene().getRoot().requestFocus();
        LoginController loginController = loaderLogin.getController();
        loginController.addObserver(this);
        loginController.init();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void update(Observable o, Object arg) {
        if(o instanceof LoginController){
            if(((LoginController)o).checkForm()) {
                //fenetre de jeu
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    System.out.println(e);
                }
                primaryStage.setTitle("Hello World");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
                root.getScene().getRoot().requestFocus();
                Controller controller = loader.getController();
                controller.init(((LoginController)o).serverIP, ((LoginController)o).userName, convertColor(((LoginController)o).userColor));
                //fermeture de la fenetre de login
                loginStage.close();
            }
        }
    }

    private java.awt.Color convertColor(Color color){
        java.awt.Color awtColor = new java.awt.Color((float) color.getRed(),(float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
        return awtColor;
    }
}
