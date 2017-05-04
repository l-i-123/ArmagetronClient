package model;

import java.awt.*;

/**
 * Created by Kevin on 02.05.2017.
 */
public class Player {
    private short id;
    private String name;
    private short score;
    private Color color;
    public  Position position;

    //classe interne pour la position du joueur.
    public class Position {
        int x;
        int y;

        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public void goUp(){
            this.x ++;
        }

        public void goDown(){
            this.x --;
        }

        public void goRight(){
            this.y ++;
        }

        public void goLeft(){
            this.y --;
        }
    }

    //constructeurs
    public Player(){
        score = 0;

    }

    public Player( short id){
        this.id = id;
        score = 0;
    }

    //setteur
    public void setName(String name){
        this.name = name;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void initPosition(int x, int y){
        this.position = new Position(x,y);
    }

    public void resetScore(){
        this.score = 0;
    }

    public void incrementScore(){
        this.score ++;
    }

}
