
package mariokart;

import student.TestCase;

/**
 * Tests every menu choice and error handling rule in ChallengerController.
 *
 * @author Preston Moore
 * @version 2026.11.12
 */
public class ChallengerControllerTest
    extends TestCase
{

    // ~ Fields ................................................................

    private ChallengerModel model;

    private TestView view;

    private ChallengerController controller;

    // ~ Setup .................................................................

    /**
     * Makes a fresh model, view, and controller before each test.
     */
    public void setUp()
    {

        model = new ChallengerModel();

        view = new TestView();

        controller = new ChallengerController(model, view);

    }

    // ~ Public Methods ........................................................


    /**
     * Tests that the constructor rejects a null model or view.
     */
    public void testConstructorInvalid()
    {

        Exception thrown = null;

        try
        {
            new ChallengerController(null, view);
        }
        catch (Exception e)
        {
            thrown = e;
        }

        assertNotNull(thrown);

        thrown = null;

        try
        {
            new ChallengerController(model, null);
        }
        catch (Exception e)
        {
            thrown = e;
        }

        assertNotNull(thrown);

    }


    /**
     * Tests option 1 adds a player to the waiting line.
     */
    public void testAddPlayer()
    {

        view.setChoices("1", "6");
        view.setNames("Alex");

        controller.run();

        assertEquals(1, model.getLine().length);
        assertEquals("Alex", model.getLine()[0]);

    }


    /**
     * Tests option 2 displays the waiting line.
     */
    public void testViewLine()
    {

        model.addPlayer("Alex");
        model.addPlayer("Jordan");

        view.setChoices("2", "6");

        controller.run();

        assertEquals(1, view.lineCount);
        assertEquals("Alex", view.lastLine[0]);
        assertEquals("Jordan", view.lastLine[1]);

    }


    /**
     * Tests option 3 starts a race and displays both racers.
     */
    public void testStartNextRace()
    {

        model.addPlayer("Alex");
        model.addPlayer("Jordan");

        view.setChoices("3", "6");

        controller.run();

        assertEquals("Alex", view.firstRacer);
        assertEquals("Jordan", view.secondRacer);
        assertEquals(1, view.raceCount);

    }


    /**
     * Tests option 4 records a winner and displays the champion.
     */
    public void testRecordWinner()
    {

        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();

        view.setChoices("4", "6");
        view.setNames("Jordan");

        controller.run();

        assertEquals("Jordan", model.getChampion());
        assertEquals("Jordan", view.lastChampion);

    }


    /**
     * Tests option 5 displays the current champion.
     */
    public void testViewChampion()
    {

        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        model.recordWinner("Alex");

        view.setChoices("5", "6");

        controller.run();

        assertEquals("Alex", view.lastChampion);

    }


    /**
     * Tests option 6 exits the game.
     */
    public void testQuitGame()
    {

        view.setChoices("6");

        controller.run();

        assertEquals(1, view.menuCount);
        assertEquals(0, model.getLine().length);

    }


    /**
     * Tests that invalid menu choices display an error and allow the user to
     * choose again.
     */
    public void testInvalidChoice()
    {

        view.setChoices("9", "abc", "", "6");

        controller.run();

        assertEquals(3, view.errorCount);
        assertEquals("Not a valid option.", view.lastError);
        assertEquals(4, view.menuCount);

    }


    /**
     * Tests that a null menu choice displays an error.
     */
    public void testNullChoice()
    {

        view.setChoices(null, "6");

        controller.run();

        assertEquals("Not a valid option.", view.lastError);
        assertEquals(2, view.menuCount);

    }


    /**
     * Tests that an invalid player name is rejected without changing the
     * waiting line.
     */
    public void testInvalidPlayer()
    {

        view.setChoices("1", "6");
        view.setNames("   ");

        controller.run();

        assertEquals(0, model.getLine().length);
        assertEquals("Please enter a valid name.", view.lastError);

    }


    /**
     * Tests that starting a race without enough players displays the model's
     * error message.
     */
    public void testNotEnoughPlayers()
    {

        model.addPlayer("Alex");

        view.setChoices("3", "6");

        controller.run();

        assertNotNull(view.lastError);
        assertEquals(1, model.getLine().length);
        assertEquals(2, view.menuCount);

    }


    /**
     * Tests that an invalid winner displays an error and does not change the
     * champion.
     */
    public void testInvalidWinner()
    {

        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();

        view.setChoices("4", "6");
        view.setNames("Sam");

        controller.run();

        assertEquals(
            "Winner must have participated in the recent race",
            view.lastError);

        // startNextRace() already made Alex champion; a bad winner
        // must not change that.
        assertEquals("Alex", model.getChampion());

    }

    // ~ Helper Classes ........................................................

    /**
     * Test version of ChallengerView that provides input and records output
     * without using the keyboard. This assumes ChallengerView can be extended
     * and has the methods listed in the project specification.
     */
    private static class TestView
        extends ChallengerView
    {

        private String[] choices;
        private String[] names;

        private int choiceIndex;
        private int nameIndex;

        private int menuCount;
        private int errorCount;
        private int lineCount;
        private int raceCount;

        private String lastError;
        private String lastChampion;
        private String[] lastLine;
        private String firstRacer;
        private String secondRacer;

        /**
         * Sets the menu choices for the test.
         *
         * @param values
         *            the menu choices.
         */
        public void setChoices(String... values)
        {

            choices = values;
            choiceIndex = 0;

        }


        /**
         * Sets the player names for the test.
         *
         * @param values
         *            the player names.
         */
        public void setNames(String... values)
        {

            names = values;
            nameIndex = 0;

        }


        /**
         * Records that the menu was displayed.
         */
        @Override
        public void showMenu()
        {

            menuCount++;

        }


        /**
         * Returns the next menu choice.
         *
         * @return the next choice.
         */
        @Override
        public String readChoice()
        {

            if (choiceIndex < choices.length)
            {
                return choices[choiceIndex++];
            }

            return "6";

        }


        /**
         * Returns the next player name.
         *
         * @return the next name.
         */
        @Override
        public String readName()
        {

            return names[nameIndex++];

        }


        /**
         * Records the waiting line.
         *
         * @param line
         *            the waiting players.
         */
        @Override
        public void showLine(String[] line)
        {

            lastLine = line;
            lineCount++;

        }


        /**
         * Records the two racers.
         *
         * @param champion
         *            the current champion.
         * @param challenger
         *            the next challenger.
         */
        @Override
        public void showRace(String champion, String challenger)
        {

            firstRacer = champion;
            secondRacer = challenger;
            raceCount++;

        }


        /**
         * Records the displayed champion.
         *
         * @param champion
         *            the champion's name.
         */
        @Override
        public void showChampion(String champion)
        {

            lastChampion = champion;

        }


        /**
         * Records the error message.
         *
         * @param message
         *            the error message.
         */
        @Override
        public void showError(String message)
        {

            lastError = message;
            errorCount++;

        }

    }

}
