package client;

import data.ConfigData;
import sample.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Thomas on 26.04.2017.
 */
/*Classe client*/
public class Client{

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;

    //en cas d'utilisation de l'interface graphqiue
    private ClientGUI clientGui;

    private String serverAdress;
    //username et port de connexion
    private String username;
    private int port;

    private Controller controller;

    //Constructeur pour l'interface console
    public Client(String server, int port, String username){
        this.serverAdress = server;
        this.port = port;
        this.username = username;
    }

    //constructeur pour l'interface graphique
    public Client(String server, int port, String username, ClientGUI clientGui){
        this.serverAdress = server;
        this.port = port;
        this.username = username;
        //vaut null si en mode console
        this.clientGui = clientGui;
    }//Client

    public Client(String server, int port, String username, Controller controller){
        this.serverAdress = server;
        this.port = port;
        this.username = username;
        //vaut null si en mode console
        this.controller = controller;
        this.controller.setClient(this);
    }//Client


    /*
      pour démarre le client il est possible d'utiliser les commandes suivantes
      java Client
      java Client username
      java Client username port
      java Client username port serverAdress

      Le port par default est 24000
      Si pas de aucun username n'est spécifié "gamer" est utilisé par defaut
    */
    public static void main(String[] args){
        int port = 1500;
        String serverAdress = "localhost";
        String userName = "gamer";

        switch(args.length){
            case 3:
                serverAdress = args[2];
            case 2:
                try{
                    port = new Integer(args[1]);
                }catch(Exception e){
                    System.out.println("Mauvais numéro de port");
                    System.out.println("La commande est > java Client [login] [port] [serverAdresse]");
                    return;
                }
            case 1:
                userName = args[0];
            case 0:
                break;
            default:
                System.out.println("La commande est > java Client [login] [port] [serverAdresse]");
                return;
        }

        //Création du Client
        Client client = new Client(serverAdress, port, userName);
        //on test la connexion au server
        if(!client.start()){
            return;
        }

        //attente de message de l'utilisateur
        Scanner scanner = new Scanner(System.in);
        //boucle infinie pour l'attente des messages
        while (true){
            System.out.println(">");
            //lecture des messages
            String message = scanner.nextLine();

            //déconnexion si message = LOGOUT
            if(message.equalsIgnoreCase("LOGOUT")){
                client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                //break pour déconnecter
                break;
            }
            else if(message.equalsIgnoreCase("WHOIS")){
                client.sendMessage(new ChatMessage(ChatMessage.WHOIS,""));
            }
            else{
                client.sendMessage(new ChatMessage(ChatMessage.MESS, message));
            }
        }
        //fin et déconnexion
        client.disconnect();
    }

    public boolean start(){
        //tentative de connexion au server
        try{
            socket = new Socket(serverAdress, port);
        }catch(Exception e){
            display("Erreur de connexion au server");
            return false;
        }

        String message = "Connexion accepté " + socket.getInetAddress() + ":" + socket.getPort();
        display(message);

        //Création des 2 stream d'entrée et de sortie
        try{
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException e){
            display("Erreur lors de la connexion :" + e);
            return false;
        }

        //création du thread pour écouter le server
        new ListenFromServer().start();
        //envoi du nom d'utilisateur
        try{
            output.writeObject(username);
        }catch (IOException e){
            display("Exception lors du login : " + e);
            disconnect();
            return false;
        }
        //success
        return true;

    }//start


    private void display(String message){
        //ecrit dans la console
        if(clientGui == null){
            System.out.println(message);
        }else{
            clientGui.append(message + "\n"); //ecrit dans un jText
        }
    }//display

    //envoi du message au server
    public void sendMessage(Object msg){
        try {
            output.writeObject(msg);
        }catch (IOException e){
            display("Exception lors de l'envoi au serveur du message :" + e);
        }
    }

    public void sendData(Object msg){
        try {
            output.writeObject(msg);
        }catch (IOException e){
            display("Exception lors de l'envoi au serveur du message :" + e);
        }
    }

    //gestion de la deconnexion
    public void disconnect(){
        try{
            if(input != null)
                input.close();
        }catch(Exception e){}
        try{
            if(output != null)
                output.close();
        }catch(Exception e){}
        try{
            if(socket != null)
                socket.close();
        }catch(Exception e){}
        if(clientGui != null){
            clientGui.connectionFailed();
        }
    }//disconnect

    class ListenFromServer extends Thread{
        public void run(){
            while(true){
                try {

                    Object o = input.readObject();

                    if(o instanceof ChatMessage) {
                        String message = (String) o;

                        if (clientGui == null){
                            System.out.println(message);
                            System.out.println("> ");
                        }else {
                            clientGui.append(message);
                        }
                    }
                    else if(o instanceof ConfigData) {
                        controller.setConfigData((ConfigData)o);
                    }


                }catch (IOException e){
                    display("La connexion est fermée: ");
                    e.printStackTrace();
                    if(clientGui != null){
                        clientGui.connectionFailed();
                    }
                    break;
                }catch(ClassNotFoundException e2){}
            }
        }
    }
}
