package projet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by elien on 11.04.2017.
 */
public class Emission implements Runnable{
    private String messageClavier;
    private PrintWriter out;
    private Scanner sc = null;
    private Boolean arretThread = false;

    public Emission(PrintWriter out){
        this.out = out;
    }

    public void arretThread(){
        arretThread = true;
    }

    @Override
    public void run() {
        sc = new Scanner(System.in);
        while(!arretThread) {
            //Réception du message tapé au clavier
            messageClavier = sc.nextLine();
            //Envoie du méssage précédemment entré dans la commande
            out.println(messageClavier);
            out.flush();
        }
    }
}
