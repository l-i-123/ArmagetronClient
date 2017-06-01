/**
 * @project     : Argmagetron
 * @file        : Game.java
 * @author(s)   : Kevin Pradervand
 * @date        : 18.05.2017
 *
 * @brief        : The class Game
 */
package client;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

public class GameTest {
    UUID uniquId = UUID.randomUUID();
    Point point = new Point(3, 6);
    Player player = new Player(uniquId, point, Color.BLUE, "Kevin", true);

    Game game = new Game();


    @Test
    public void getPlayers() throws Exception {
        game.add(player);
        Assert.assertNotNull(game.getPlayers());

    }


}