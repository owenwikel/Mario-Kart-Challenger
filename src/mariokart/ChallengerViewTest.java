
package mariokart;

import student.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests every method in ChallengerView by feeding it typed input and checking
 * what it prints. Also runs one full game night through the real view,
 * controller and model to check the MVP path end to end without crashing.
 *
 * @author Aryan Patil
 * @version 2026.11.12
 */
public class ChallengerViewTest
    extends TestCase
{

    // ~ Fields ................................................................

    private ByteArrayOutputStream output;

    // ~ Setup .................................................................

    /**
     * Makes a fresh output buffer before each test.
     */
    public void setUp()
    {

        output = new ByteArrayOutputStream();

    }

    // ~ Helper Methods ........................................................


    /**
     * Makes a view that reads the given text as if it were typed and prints
     * into the output buffer.
     *
     * @param typed
     *            the text the user "types".
     * @return the new view.
     */
    private ChallengerView makeView(String typed)
    {

        return new ChallengerView(
            new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8)),
            new PrintStream(output, true));

    }


    /**
     * Returns everything the view printed so far, with Windows line endings
     * changed to "\n" so the checks work on any computer.
     *
     * @return the printed text.
     */
    private String printed()
    {

        return output.toString().replace("\r\n", "\n");

    }

    // ~ Public Methods ........................................................


    /**
     * Tests that the constructor rejects null streams and that the no-argument
     * constructor works.
     */
    public void testConstructor()
    {

        Exception thrown = null;

        try
        {
            new ChallengerView(null, new PrintStream(output));
        }
        catch (Exception e)
        {
            thrown = e;
        }

        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;

        try
        {
            new ChallengerView(
                new ByteArrayInputStream(new byte[0]),
                null);
        }
        catch (Exception e)
        {
            thrown = e;
        }

        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);

        assertNotNull(new ChallengerView());

    }


    /**
     * Tests that the menu lists all six options.
     */
    public void testShowMenu()
    {

        makeView("").showMenu();

        String text = printed();

        assertTrue(text.contains("1. Add Player"));
        assertTrue(text.contains("2. View Line"));
        assertTrue(text.contains("3. Next Race"));
        assertTrue(text.contains("4. Record Winner"));
        assertTrue(text.contains("5. Show Champion"));
        assertTrue(text.contains("6. Quit"));

    }


    /**
     * Tests that readChoice returns each typed line exactly as typed, even
     * when it is not a valid option.
     */
    public void testReadChoice()
    {

        ChallengerView view = makeView("1\n9\nabc\n\n");

        assertEquals("1", view.readChoice());
        assertEquals("9", view.readChoice());
        assertEquals("abc", view.readChoice());
        assertEquals("", view.readChoice());

    }


    /**
     * Tests that readChoice returns "6" (Quit) when there is no more input,
     * so the menu loop cannot run forever.
     */
    public void testReadChoiceNoInput()
    {

        ChallengerView view = makeView("");

        assertEquals("6", view.readChoice());

    }


    /**
     * Tests that readName prompts for a name and returns the raw typed line,
     * spaces included, so the model can do the trimming.
     */
    public void testReadName()
    {

        ChallengerView view = makeView("  Alex  \n");

        assertEquals("  Alex  ", view.readName());
        assertTrue(printed().contains("Enter a player's name:"));

    }


    /**
     * Tests that readName returns null when there is no more input.
     */
    public void testReadNameNoInput()
    {

        ChallengerView view = makeView("");

        assertNull(view.readName());

    }


    /**
     * Tests that showLine prints the players numbered and in order.
     */
    public void testShowLine()
    {

        makeView("").showLine(new String[] { "Sam", "Taylor", "Chris" });

        String text = printed();

        assertTrue(text.contains("1. Sam"));
        assertTrue(text.contains("2. Taylor"));
        assertTrue(text.contains("3. Chris"));
        assertTrue(text.indexOf("Sam") < text.indexOf("Taylor"));
        assertTrue(text.indexOf("Taylor") < text.indexOf("Chris"));

    }


    /**
     * Tests that showLine handles an empty or null line without crashing.
     */
    public void testShowLineEmpty()
    {

        ChallengerView view = makeView("");

        view.showLine(new String[0]);
        view.showLine(null);

        assertTrue(printed().contains("Nobody is waiting in line."));

    }


    /**
     * Tests that showRace prints both racers.
     */
    public void testShowRace()
    {

        makeView("").showRace("Alex", "Jordan");

        String text = printed();

        assertTrue(text.contains("Alex (champion)"));
        assertTrue(text.contains("Jordan (challenger)"));

    }


    /**
     * Tests that showChampion prints the champion's name.
     */
    public void testShowChampion()
    {

        makeView("").showChampion("Alex");

        assertTrue(printed().contains("Champion: Alex"));

    }


    /**
     * Tests that showChampion handles there being no champion yet.
     */
    public void testShowChampionNone()
    {

        makeView("").showChampion(null);

        assertTrue(printed().contains("No champion yet."));

    }


    /**
     * Tests that showError prints the message it is given, and a fallback
     * message when it is given nothing.
     */
    public void testShowError()
    {

        ChallengerView view = makeView("");

        view.showError("Please enter a valid name.");

        assertTrue(printed().contains("Error: Please enter a valid name."));

        view.showError(null);
        view.showError("");

        assertTrue(printed().contains("something went wrong"));

    }


    /**
     * Runs a full game night through the real view, controller and model: add
     * three players, view the line, race, record a winner, race again, record
     * again, show the champion and quit. Checks it never crashes and ends with
     * the right champion.
     */
    public void testFullGameNight()
    {

        String typed = "1\nAlex\n" + "1\nJordan\n" + "1\nSam\n" + "2\n"
            + "3\n" + "4\nJordan\n" + "3\n" + "4\nsam\n" + "5\n" + "6\n";

        ChallengerModel model = new ChallengerModel();
        ChallengerController controller =
            new ChallengerController(model, makeView(typed));

        controller.run();

        String text = printed();

        assertEquals("sam", model.getChampion());
        assertEquals(0, model.getLine().length);
        assertTrue(text.contains("Alex (champion) vs. Jordan (challenger)"));
        assertTrue(text.contains("Jordan (champion) vs. Sam (challenger)"));
        assertTrue(text.contains("Champion: Jordan"));
        assertFalse(text.contains("Error:"));

    }


    /**
     * Runs the bad-input cases from our scope through the real view,
     * controller and model. Each one should print an error, go back to the
     * menu, and leave the line and the active race unchanged.
     */
    public void testBadInputGameNight()
    {

        String typed = "9\n" // not a menu option
            + "\n" // just Enter
            + "1\n   \n" // blank name
            + "1\nAlex\n" + "1\n alex \n" // duplicate name
            + "3\n" // not enough players
            + "4\nAlex\n" // no race in progress
            + "1\nJordan\n" + "3\n" + "3\n" // second Next Race
            + "4\nSam\n" // winner was not racing
            + "6\n";

        ChallengerModel model = new ChallengerModel();
        ChallengerController controller =
            new ChallengerController(model, makeView(typed));

        controller.run();

        String text = printed();

        int errors = text.split("Error:", -1).length - 1;

        assertEquals(8, errors);
        assertEquals(0, model.getLine().length);
        // The Alex vs. Jordan race is still active after the bad winner,
        // so the real winner can still be recorded.
        assertEquals("Jordan", model.recordWinner("Jordan"));

    }


    /**
     * Tests that the game ends cleanly if the input runs out in the middle of
     * a game night instead of looping forever.
     */
    public void testInputRunsOut()
    {

        ChallengerModel model = new ChallengerModel();
        ChallengerController controller =
            new ChallengerController(model, makeView("1\nAlex\n1\n"));

        controller.run();

        assertEquals(1, model.getLine().length);
        assertTrue(printed().contains("Error: Please enter a valid name."));

    }

}
