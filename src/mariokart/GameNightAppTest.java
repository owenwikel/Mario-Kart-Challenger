
package mariokart;

import student.TestCase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests the application's real entry point with redirected console streams.
 *
 * @author Preston Moore
 * @version 2026.11.19
 */
public class GameNightAppTest
    extends TestCase
{
    /** Starting and quitting prints the menu and exits normally. */
    public void testMain()
    {
        String output = runApp("6\n", new String[0]);
        assertTrue(output.contains("===== Mario Kart Challenger ====="));
        assertTrue(output.contains("1. Add Player"));
        assertTrue(output.contains("6. Quit"));
        assertFalse(output.contains("Error:"));
    }


    /** Main connects the real model, controller, and view for a whole race. */
    public void testMainFullGame()
    {
        String output =
            runApp("1\nAlex\n1\nJordan\n2\n3\n4\nJordan\n5\n6\n", null);
        assertTrue(output.contains("1. Alex"));
        assertTrue(output.contains("2. Jordan"));
        assertTrue(
            output.contains("Race: Alex (champion) vs. Jordan (challenger)"));
        assertTrue(output.contains("Champion: Jordan"));
        assertFalse(output.contains("Error:"));
    }


    /** Invalid choices report errors and the application continues. */
    public void testMainInvalidChoiceRecovery()
    {
        String output = runApp("9\n6\n", new String[0]);
        assertTrue(output.contains("Error: Not a valid option."));
        assertEquals(
            2,
            countOccurrences(output, "===== Mario Kart Challenger ====="));
    }


    /** End-of-file is treated as Quit by the real view. */
    public void testMainEmptyInput()
    {
        String output = runApp("", new String[0]);
        assertEquals(
            1,
            countOccurrences(output, "===== Mario Kart Challenger ====="));
        assertFalse(output.contains("Error:"));
    }


    /** Captures console output and restores global streams even on failure. */
    private String runApp(String input, String[] args)
    {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream captured = new PrintStream(output, true);
        try
        {
            System.setIn(
                new ByteArrayInputStream(
                    input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(captured);
            GameNightApp.main(args);
        }
        finally
        {
            System.setIn(originalIn);
            System.setOut(originalOut);
            captured.close();
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }


    /** Counts literal occurrences in the captured output. */
    private int countOccurrences(String text, String target)
    {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(target, index)) >= 0)
        {
            count++;
            index += target.length();
        }
        return count;
    }
}
