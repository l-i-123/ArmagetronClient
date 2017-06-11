/**
 * @project     : Argmagetron
 * @file        : Client.java
 * @author(s)   : Thomas Lechaire, Kevin Pradervand, Elie N'Djoli Bohulu, Michael Brouchoud
 * @date        : 08.06.2017
 *
 * @brief        : Client Class that generate the play ground window
 */

package client;

import data.ConnectionData;
import sample.Controller;
import share.Util;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private Thread thread;
    private boolean keepGoing;

    private String serverAddress;
    private String username;
    private int port;

    private Color userColor;

    private Controller controller;

    public Client(String serverAddress, int port, String username, Controller controller, Color userColor){
        this.userColor = userColor;
        this.serverAddress = serverAddress;
        this.port = port;
        this.username = username;
        this.controller = controller;
        this.keepGoing = true;
        this.thread = new Thread(this);
        this.connect();
    }

    /**
     * @fn connect
     *
     * @brief method that connect the client to the server
     */
    private void connect(){
        try{
            socket = new Socket(serverAddress, port);
        }catch(Exception e){
            Util.print("Error when trying to connect to server" + e);
            e.printStackTrace();
        }

        Util.print("Connecton accepted on port : " +  port);

        try{
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException e){
            Util.print("Error when trying to create input/output stream :" + e);
            e.printStackTrace();
        }

        this.thread.start();

        this.sendData(new ConnectionData(this.username, userColor));
    }

    /**
     * @fn void sendData(Object o)
     *
     * @brief Send Data to the server
     *
     * @param o The object to send
     */
    public void sendData(Object o) {
        if(!socket.isConnected()) {
            Util.print("Client disconnected");
            this.close();
        }

        try {
            output.writeObject(o);
            output.reset(); //To not send the same object
        }
        catch(IOException e) {
            Util.print("Exception when trying to send data to server :" + e);
            Util.print(e.toString());
        }
    }

    /**
     * @fn private void close()
     *
     * @brief Close socket and connection with the server
     *
     */
    private void close(){
        this.keepGoing = false;
        try {
            if(output != null) output.close();
        }
        catch (IOException e) {
            Util.print("Error when trying to close output stream : " + e);
            e.printStackTrace();
        }

        try {
            if(input != null) input.close();
        }
        catch (IOException e) {
            Util.print("Error when trying to close input stream : " + e);
            e.printStackTrace();
        }

        try {
            if(socket != null) socket.close();
        }
        catch (IOException e) {
            Util.print("Error when trying to close socket : " + e);
            e.printStackTrace();
        }
    }

    public void run() {
        while (this.keepGoing) {
            try {
                controller.processData(input.readObject());
            } catch (IOException e) {
                Util.print("Connection with server close : " + e.getMessage());
                this.close();
            } catch (ClassNotFoundException e) {
                Util.print("No known the received data : " + e);
                e.printStackTrace();
            }
        }
    }
}
