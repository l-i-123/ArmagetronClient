/**
 * @project     : Argmagetron
 * @file        : Game.java
 * @author(s)   : Thomas Lechaire, Kevin Pradervand, Elie N'Djoli Bohulu, Michael Brouchoud
 * @date        : 08.06.2017
 *
 * @brief        : Game class that manage local data for players
 */

package client;

import data.PlayerData;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;

    public Game() {
        this.players = new ArrayList<Player>();
    }

    /**
     * @fn public ArrayList<Player> getPlayers()
     *
     * @brief Return a list of all players
     *
     * @return ArrayList<Player> The list of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @fn add
     *
     * @brief methode to add a player in the players list
     *
     * @param player
     * @param void
     */
    public void add(Player player) {
        this.players.add(player);
    }

    /**
     * @fn setPlayers
     *
     * @brief methode that create the players list from the playersData send from the server
     *
     * @param playersData
     * @param void
     */
    public void setPlayers(ArrayList<PlayerData> playersData) {
        this.players.clear();
        for (PlayerData playerData : playersData) {
            this.players.add(new Player(playerData.getUniqueId(), playerData.getPosition(), playerData.getColor(), playerData.getUsername(), playerData.isAlive()));
        }
    }
}
