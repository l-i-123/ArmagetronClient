/**
 * @project     : Argmagetron
 * @file        : Controller.java
 * @author(s)   : Kevin Pradervand
 * @date        : 18.05.2017
 *
 * @brief        : The class Controller
 */
package sample;

import org.junit.Test;
import org.junit.Assert;


import java.awt.*;

import static org.junit.Assert.*;

public class ControllerTest {
    private Controller controller = new Controller();

    @Test
    public void init() throws Exception {
        Assert.assertNotNull(controller);
    }

    @Test
    public void connect() throws Exception {
        /*controller.connect("localhost","Kevin", Color.blue);
        Assert.assertNotNull(controller.getClient());*/
    }
}