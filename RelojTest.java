

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RelojTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RelojTest
{
    /**
     * Default constructor for test class RelojTest
     */
    public RelojTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testInitials()
    {
        Reloj reloj1 = new Reloj(0);
        assertEquals(false, reloj1.isPaused());
    }

    @Test
    public void setCyclesPerSecond()
    {
        Reloj reloj1 = new Reloj(1000);
        reloj1.setCyclesPerSecond(1000);
        reloj1.reset();
        reloj1.setCyclesPerSecond(1000);
    }

    @Test
    public void isPaused()
    {
        Reloj reloj1 = new Reloj(0);
        assertEquals(false, reloj1.isPaused());
    }

    @Test
    public void hasElapsedCycle()
    {
        Reloj reloj1 = new Reloj(0);
        assertEquals(false, reloj1.hasElapsedCycle());
    }
}




