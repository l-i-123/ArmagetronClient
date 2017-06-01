/**
 * @project     : Argmagetron
 * @file        : Player.java
 * @author(s)   : Kevin Pradervand
 * @date        : 18.05.2017
 *
 * @brief        : The class Player
 */
package client;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.UUID;


import static org.junit.Assert.*;

public class PlayerTest {
    UUID uniquId = UUID.randomUUID();
    Point point = new Point(3, 6);
    Player player = new Player(uniquId, point, Color.BLUE, "Kevin", true);


    @Test
    public void getUniqueId() throws Exception {
        Assert.assertEquals(player.getUniqueId(),uniquId);
    }

    @Test
    public void getColor() throws Exception {
        Assert.assertEquals(player.getColor(),Color.BLUE);
    }

    @Test
    public void getUsername() throws Exception {
        Assert.assertEquals(player.getUsername(),"Kevin");
    }

    @Test
    public void getPosition() throws Exception {
        Assert.assertEquals(player.getPosition(),point);
    }

    @Test
    public void isAlive() throws Exception {
        Assert.assertTrue(player.isAlive());
    }

}