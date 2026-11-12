
package mariokart;

import student.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests the GameNightApp class. Verifies that the application starts and exits
 * correctly.
 *
 * @author Preston Moore
 * @version 2026.11.12
 */
public class GameNightAppTest
    extends TestCase
{

    // ~ Public Methods ........................................................

    /**
     * Tests that main starts the application and exits when the user chooses
     * option 6.
     */
    public void testMain()
    {

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Exception thrown = null;

        try
        {

            System.setIn(
                new ByteArrayInputStream(
                    "6\n".getBytes(StandardCharsets.UTF_8)));

            System.setOut(new PrintStream(output));

            GameNightApp.main(new String[0]);

        }
        catch (Exception e)
        {

            thrown = e;

        }
        finally
        {

            System.setIn(originalIn);
            System.setOut(originalOut);

        }

        assertNull(thrown);
        assertTrue(output.size() > 0);

    }

}
