package client;

import java.awt.*;
import java.util.UUID;

/**
 * Created by Michael on 06.05.2017.
 */
public class Player {
    private UUID uniqueId;
    private Point position;
    private Color color;
    private String username;

    public Player(UUID uniqueId,Point position, Color color, String username) {
        this.uniqueId = uniqueId;
        this.position = position;
        this.color = color;
        this.username = username;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Color getColor() {
        return color;
    }

    public String getUsername() {
        return username;
    }

    public Point getPosition() {
        return position;
    }
}
