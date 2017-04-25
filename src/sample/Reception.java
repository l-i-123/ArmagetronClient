package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by elien on 11.04.2017.
 */
public class Reception implements Runnable{
    private BufferedReader in;
    private Boolean arretThread = false;

    public Reception(BufferedReader in){
        this.in = in;
    }

    public void arretThread(){
        arretThread = true;
    }

    @Override
    public void run() {
        while(!arretThread){
            try{
                String message_distant;
                message_distant = in.readLine();
                System.out.println(message_distant);
            }catch (Exception e){
                System.out.println(e);
            }

        }

    }
}
