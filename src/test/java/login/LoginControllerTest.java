/**
 * @project     : Argmagetron
 * @file        : LoginController.java
 * @author(s)   : Kevin Pradervand
 * @date        : 18.05.2017
 *
 * @brief        : The class LoginController
 */
package login;

import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginControllerTest {

    private LoginController loginController = new LoginController();

    @Test
    public void init() throws Exception {
        Assert.assertNotNull(loginController);
    }

    @Test
    public void checkIP() throws Exception {
        Assert.assertEquals(loginController.checkIP("localhost"), true);
        Assert.assertEquals(loginController.checkIP("8.8.8.8"), true);
        Assert.assertEquals(loginController.checkIP("1.1.1.1"), false);
        Assert.assertEquals(loginController.checkIP("salut mec"), false);
    }

    @Test
    public void checkName() throws Exception {
        Assert.assertEquals(loginController.checkName("coucou"), true );
        Assert.assertEquals(loginController.checkName("s"), false);
        Assert.assertEquals(loginController.checkName("salut me34"), true);
        Assert.assertEquals(loginController.checkName("aaaaaaaaaaaaaadv"), false);
    }

    @Test
    public void checkColor() throws Exception {
        /*Assert.assertEquals(loginController.checkColor(Color.AQUAMARINE), true);
        Assert.assertEquals(loginController.checkColor(Color.WHITE), false);*/
    }


}