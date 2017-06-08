/**
 * @project     : Argmagetron
 * @file        : Player.java
 * @author(s)   : Thomas Léchaire, Kevin Pradervand, Elie N'Djoli Bohulu, Michaël Brouchoud
 * @date        : 08.06.2017
 *
 * @brief        : Player class
 */
package client;

import java.awt.*;
import java.util.UUID;

public class Player {
    private UUID uniqueId;
    private Point position;
    private Color color;
    private String username;
    private boolean isAlive;

    public Player(UUID uniqueId,Point position, Color color, String username, boolean isAlive) {
        this.uniqueId = uniqueId;
        this.position = position;
        this.color = color;
        this.username = username;
        this.isAlive = isAlive;
    }

    /**
     * @fn getUniqueId
     *
     * @brief Methode to get the UniqueID of the player
     *
     * @return UUID
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * @fn getColor
     *
     * @brief Methode that return the color of the player
     *
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @fn getUsername
     *
     * @brief Methode that return the player's username
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * @fn getPosition
     *
     * @brief Methode that return the player's position
     *
     * @return Point
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @fn isAlive
     *
     * @brief Methode that return the player state (alive or not)
     *
     * @return boolean
     */
    public boolean isAlive() {
        return isAlive;
    }
}
