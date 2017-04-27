package sample;

import static org.junit.Assert.*;

/**
 * Created by Whiterussian on 26.04.2017.
 */
public class ControllerTest {
    private Controller controller = new Controller();

    @org.junit.Test
    public void test1() throws Exception {
        assertTrue(controller.test());
    }

}