
package mariokart;

import student.TestCase;

/**
 * Tests menu dispatch, error recovery, and model/view integration.
 *
 * @author Preston Moore
 * @version 2026.11.19
 */
public class ChallengerControllerTest
    extends TestCase
{
    private ChallengerModel model;
    private TestView view;
    private ChallengerController controller;

    /** Creates fresh collaborators for each test. */
    public void setUp()
    {
        model = new ChallengerModel();
        view = new TestView();
        controller = new ChallengerController(model, view);
    }


    /** Rejects missing collaborators with the documented exception type. */
    public void testConstructorInvalid()
    {
        assertConstructorRejected(null, view);
        assertConstructorRejected(model, null);
        assertConstructorRejected(null, null);
    }


    private void assertConstructorRejected(ChallengerModel m, ChallengerView v)
    {
        try
        {
            new ChallengerController(m, v);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException expected)
        {
            assertEquals(
                "Model and view cannot be null.",
                expected.getMessage());
        }
    }


    /** Option 1 trims and queues a name. */
    public void testAddPlayer()
    {
        view.setChoices(" 1 ", "6");
        view.setNames("  Alex  ");
        controller.run();
        assertEquals(1, model.getLine().length);
        assertEquals("Alex", model.getLine()[0]);
        assertEquals(0, view.errorCount);
    }


    /** Option 2 displays the queue in FIFO order without changing it. */
    public void testViewLine()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        view.setChoices("2", "6");
        controller.run();
        assertEquals(1, view.lineCount);
        assertEquals(2, view.lastLine.length);
        assertEquals("Alex", view.lastLine[0]);
        assertEquals("Jordan", view.lastLine[1]);
        assertEquals(2, model.getLine().length);
    }


    /** Option 2 also works with an empty queue. */
    public void testViewEmptyLine()
    {
        view.setChoices("2", "6");
        controller.run();
        assertEquals(1, view.lineCount);
        assertEquals(0, view.lastLine.length);
        assertEquals(0, view.errorCount);
    }


    /** Option 3 starts a race and sends both racers to the view. */
    public void testStartNextRace()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.addPlayer("Sam");
        view.setChoices("3", "6");
        controller.run();
        assertEquals(1, view.raceCount);
        assertEquals("Alex", view.firstRacer);
        assertEquals("Jordan", view.secondRacer);
        assertEquals("Sam", model.getLine()[0]);
        assertEquals(0, view.errorCount);
    }


    /** Option 4 records the winner and shows the new champion. */
    public void testRecordWinner()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        view.setChoices("4", "6");
        view.setNames(" jordan ");
        controller.run();
        assertEquals("jordan", model.getChampion());
        assertEquals("jordan", view.lastChampion);
        assertEquals(1, view.championCount);
        assertEquals(1, model.getStreak());
        assertEquals(0, view.errorCount);
    }


    /** Option 5 shows the champion without changing game state. */
    public void testViewChampion()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        model.recordWinner("Alex");
        view.setChoices("5", "6");
        controller.run();
        assertEquals("Alex", view.lastChampion);
        assertEquals(1, view.championCount);
        assertEquals(1, model.getStreak());
    }


    /** Option 5 passes null to the view when no champion exists. */
    public void testViewChampionNone()
    {
        view.setChoices("5", "6");
        controller.run();
        assertEquals(1, view.championCount);
        assertNull(view.lastChampion);
        assertEquals(0, view.errorCount);
    }


    /** Option 6 quits immediately and ignores subsequent input. */
    public void testQuitGame()
    {
        view.setChoices("6", "1");
        controller.run();
        assertEquals(1, view.menuCount);
        assertEquals(1, view.choiceIndex);
        assertEquals(0, model.getLine().length);
    }


    /** Invalid options report errors and allow another menu selection. */
    public void testInvalidChoice()
    {
        view.setChoices("9", "abc", "", " 7 ", "6");
        controller.run();
        assertEquals(4, view.errorCount);
        assertEquals("Not a valid option.", view.lastError);
        assertEquals(5, view.menuCount);
    }


    /** A null choice is rejected, but does not stop the loop. */
    public void testNullChoice()
    {
        view.setChoices((String)null, "6");
        controller.run();
        assertEquals(1, view.errorCount);
        assertEquals("Not a valid option.", view.lastError);
        assertEquals(2, view.menuCount);
    }


    /** Blank names are rejected and the queue is unchanged. */
    public void testInvalidPlayer()
    {
        view.setChoices("1", "6");
        view.setNames("   ");
        controller.run();
        assertEquals(0, model.getLine().length);
        assertEquals(1, view.errorCount);
        assertEquals("Please enter a valid name.", view.lastError);
    }


    /** Null names (including end of input) are rejected. */
    public void testNullPlayerName()
    {
        view.setChoices("1", "6");
        view.setNames((String)null);
        controller.run();
        assertEquals(0, model.getLine().length);
        assertEquals("Please enter a valid name.", view.lastError);
    }


    /** Duplicate and invalid-format names do not alter the queue. */
    public void testOtherInvalidPlayerNames()
    {
        model.addPlayer("Alex");
        view.setChoices("1", "1", "1", "6");
        view.setNames(" alex ", "!!!!", "abcdefghijklmnopqrstu");
        controller.run();
        assertEquals(3, view.errorCount);
        assertEquals(1, model.getLine().length);
        assertEquals("Alex", model.getLine()[0]);
        assertEquals("Names must be 20 characters or fewer.", view.lastError);
    }


    /** Starting a race with too few players shows the exact model error. */
    public void testNotEnoughPlayers()
    {
        model.addPlayer("Alex");
        view.setChoices("3", "6");
        controller.run();
        assertEquals(
            "You must have two racers to start the first race.",
            view.lastError);
        assertEquals(1, view.errorCount);
        assertEquals(0, view.raceCount);
        assertEquals(1, model.getLine().length);
    }


    /** A second race cannot begin before recording the first winner. */
    public void testRaceAlreadyActive()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        view.setChoices("3", "3", "6");
        controller.run();
        assertEquals(1, view.raceCount);
        assertEquals(1, view.errorCount);
        assertTrue(view.lastError.contains("current race to finish"));
        assertEquals("Jordan", model.recordWinner("Jordan"));
    }


    /** Recording a winner before starting a race is rejected. */
    public void testRecordWinnerWithoutRace()
    {
        view.setChoices("4", "6");
        view.setNames("Alex");
        controller.run();
        assertEquals("You must race before declaring a winner", view.lastError);
        assertEquals(1, view.errorCount);
        assertEquals(0, view.championCount);
        assertNull(model.getChampion());
    }


    /** An unrelated winner cannot finish the active race. */
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
        assertEquals(1, view.errorCount);
        assertEquals(0, view.championCount);
        assertEquals("Alex", model.getChampion());
        assertEquals(0, model.getStreak());
        assertEquals("Jordan", model.recordWinner("Jordan"));
    }


    /** A null winner is rejected without ending the active race. */
    public void testNullWinner()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        view.setChoices("4", "6");
        view.setNames((String)null);
        controller.run();
        assertEquals(
            "Winner must have participated in the recent race",
            view.lastError);
        assertEquals(1, view.errorCount);
        assertEquals("Alex", model.recordWinner("Alex"));
    }


    /** An invalid winner can be followed by a valid one in the same run. */
    public void testRecoverAfterInvalidWinner()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        view.setChoices("3", "4", "4", "6");
        view.setNames("Sam", "Jordan");
        controller.run();
        assertEquals(1, view.errorCount);
        assertEquals(1, view.raceCount);
        assertEquals(1, view.championCount);
        assertEquals("Jordan", view.lastChampion);
        assertEquals(1, model.getStreak());
    }


    /** The menu can play multiple races with a returning champion. */
    public void testFullGameSequence()
    {
        view.setChoices("1", "1", "1", "2", "3", "4", "3", "4", "5", "6");
        view.setNames("Alex", "Jordan", "Sam", "Jordan", "Jordan");
        controller.run();
        assertEquals(10, view.menuCount);
        assertEquals(1, view.lineCount);
        assertEquals(2, view.raceCount);
        assertEquals("Jordan", view.lastChampion);
        assertEquals(3, view.championCount);
        assertEquals(2, model.getStreak());
        assertEquals(0, model.getLine().length);
        assertEquals(0, view.errorCount);
    }

    /** Option 4 shows the new champion's win streak after each race. */
    public void testRecordWinnerShowsStreak()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.addPlayer("Sam");
        view.setChoices("3", "4", "3", "4", "6");
        view.setNames("Jordan", "Jordan");
        controller.run();
        assertEquals(2, view.streakCount);
        assertEquals("Jordan", view.lastStreakChampion);
        assertEquals(2, view.lastStreak);
        assertEquals(0, view.errorCount);
    }


    /** A rejected winner shows an error, not a streak. */
    public void testInvalidWinnerShowsNoStreak()
    {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        view.setChoices("4", "6");
        view.setNames("Sam");
        controller.run();
        assertEquals(0, view.streakCount);
        assertEquals(1, view.errorCount);
        assertEquals(0, model.getStreak());
    }


    /** Option 5 shows the streak, or no streak before any race. */
    public void testViewChampionShowsStreak()
    {
        view.setChoices("5", "6");
        controller.run();
        assertEquals(1, view.streakCount);
        assertNull(view.lastStreakChampion);
        assertEquals(0, view.lastStreak);

        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        model.recordWinner("Alex");
        view.setChoices("5", "6");
        controller.run();
        assertEquals(2, view.streakCount);
        assertEquals("Alex", view.lastStreakChampion);
        assertEquals(1, view.lastStreak);
    }


    /** Fake view supplies scripted input and records observable output. */
    private static class TestView
        extends ChallengerView
    {
        private String[] choices = new String[0];
        private String[] names = new String[0];
        private int choiceIndex;
        private int nameIndex;
        private int menuCount;
        private int errorCount;
        private int lineCount;
        private int raceCount;
        private int championCount;
        private int streakCount;
        private int lastStreak;
        private String lastStreakChampion;
        private String lastError;
        private String lastChampion;
        private String[] lastLine;
        private String firstRacer;
        private String secondRacer;

        public void setChoices(String... values)
        {
            choices = values;
            choiceIndex = 0;
        }


        public void setNames(String... values)
        {
            names = values;
            nameIndex = 0;
        }


        @Override
        public void showMenu()
        {
            menuCount++;
        }


        @Override
        public String readChoice()
        {
            if (choiceIndex < choices.length)
            {
                return choices[choiceIndex++];
            }
            return "6";
        }


        @Override
        public String readName()
        {
            if (nameIndex < names.length)
            {
                return names[nameIndex++];
            }
            return null;
        }


        @Override
        public void showLine(String[] line)
        {
            lastLine = line;
            lineCount++;
        }


        @Override
        public void showRace(String champion, String challenger)
        {
            firstRacer = champion;
            secondRacer = challenger;
            raceCount++;
        }


        @Override
        public void showChampion(String champion)
        {
            lastChampion = champion;
            championCount++;
        }


        @Override
        public void showStreak(String champion, int streak)
        {
            lastStreakChampion = champion;
            lastStreak = streak;
            streakCount++;
        }


        @Override
        public void showError(String message)
        {
            lastError = message;
            errorCount++;
        }
    }
}
