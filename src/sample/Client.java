package projet;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.Socket;
        import java.util.Scanner;


public class Client{

    //Déclaration des variables
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner sc = null;
    private String ipServeur = "";
    private Emission emission;
    private Reception reception;

    public Client(){

        try {
            //Obtention de l'adresse IP du serveur en demandant à l'utilisateur
            //de l'entrée dans la commande
            System.out.println("Entrez l'adresse IP du serveur :");

            sc = new Scanner(System.in);
            ipServeur = sc.nextLine();
            socket = new Socket(ipServeur,20000);


            //socket = new Socket("10.192.91.249",20000);
            //Création des liaisons in et out permettant au socket de communiquer avec l'exterieur
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                emission = new Emission(out);
                reception = new Reception(in);
                Thread t4 = new Thread(emission);
                t4.start();
                Thread t3 = new Thread(reception);
                t3.start();

//            socket.close();

        }catch (Exception e) {
            try {
                emission.arretThread();
                reception.arretThread();
                if(socket != null){
                    socket.close();
                    System.out.println("Connection perdu");
                }
                else{
                    System.out.println("serveur injoignable");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println("Exception socket.close");
            }
            //e.printStackTrace();
        }
    }
}
