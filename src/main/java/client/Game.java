package client;

import data.PlayerData;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;

    public Game() {
        this.players = new ArrayList<Player>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void add(Player player) {
        this.players.add(player);
    }

    public void setPlayers(ArrayList<PlayerData> playersData) {
        this.players.clear();
        for (PlayerData playerData : playersData) {
            this.players.add(new Player(playerData.getUniqueId(), playerData.getPosition(), playerData.getColor(), playerData.getUsername(), playerData.isAlive()));
        }
    }
}
