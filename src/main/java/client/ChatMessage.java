package client;

import java.io.Serializable;

/**
 * Created by Thomas on 26.04.2017.
 */
public class ChatMessage implements Serializable {
    protected static final long serialVersionUID = 1112122200L;

    //Les différents type de message envoyé par le client.
    //WHOIS pour récupérer la liste des utilisateurs connectés
    //MESS pour un message normal
    //LOGOUT pour la déconnexion

    static final int WHOIS = 0, MESS = 1, LOGOUT = 2;
    private int type;
    private String message;

    //constructeur
    public ChatMessage(int type, String message){
        this.type = type;
        this.message = message;
    }

    int getType(){ return type;}

    String getMessage(){return message;}

}
