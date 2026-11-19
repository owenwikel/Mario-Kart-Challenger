package mariokart;

import student.TestCase;

/**
 * Tests every method and rule in ChallengerModel.
 *
 * @author Owen Wikel
 * @version 2026.11.18
 */
public class ChallengerModelTest extends TestCase {
    //~ Fields ................................................................
    private ChallengerModel model;

    //~ Setup .................................................................
    /**
     * Makes a fresh model before each test.
     */
    public void setUp() {
        model = new ChallengerModel();
    }

    //~ Public Methods ........................................................
    /**
     * Tests addPlayer adds trimmed names in order.
     */
    public void testAddPlayer() {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.addPlayer("  Sam  ");
        String[] line = model.getLine();
        assertEquals(3, line.length);
        assertEquals("Alex", line[0]);
        assertEquals("Jordan", line[1]);
        assertEquals("Sam", line[2]);
    }

    /**
     * Tests addPlayer rejects null, blank, and duplicate names.
     */
    public void testAddPlayerInvalid() {
        model.addPlayer("Alex");
        Exception thrown = null;
        try {
            model.addPlayer(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            model.addPlayer("   ");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            model.addPlayer(" alex ");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(1, model.getLine().length);
    }

    /**
     * Tests addPlayer rejects a name over 20 characters and a name
     * with no letters.
     */
    public void testAddPlayerNameRules() {
        Exception thrown = null;
        try {
            model.addPlayer("abcdefghijklmnopqrstu");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            model.addPlayer("!!!!");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(0, model.getLine().length);
    }

    /**
     * Tests addPlayer rejects the champion and challenger.
     */
    public void testAddPlayerRacing() {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        Exception thrown = null;
        try {
            model.addPlayer("jordan");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            model.addPlayer("ALEX");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }

    /**
     * Tests a full game night: first race, a new champion, then the
     * next challenger comes from the line.
     */
    public void testStartNextRace() {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.addPlayer("Sam");
        String[] race = model.startNextRace();
        assertEquals("Alex", race[0]);
        assertEquals("Jordan", race[1]);

        assertEquals("Jordan", model.recordWinner("Jordan"));
        assertEquals("Jordan", model.getChampion());

        race = model.startNextRace();
        assertEquals("Jordan", race[0]);
        assertEquals("Sam", race[1]);
        assertEquals(0, model.getLine().length);
    }

    /**
     * Tests startNextRace rejects a second race, too few players,
     * and an empty line, and names the champion in the message.
     */
    public void testStartNextRaceInvalid() {
        model.addPlayer("Alex");
        Exception thrown = null;
        try {
            model.startNextRace();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(1, model.getLine().length);

        model.addPlayer("Jordan");
        model.startNextRace();
        thrown = null;
        try {
            model.startNextRace();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        model.recordWinner("Alex");
        thrown = null;
        try {
            model.startNextRace();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals("You must have two racers to start the next race.",
            thrown.getMessage());
    }

    /**
     * Tests recordWinner keeps the champion and rejects bad winners.
     */
    public void testRecordWinner() {
        assertNull(model.getChampion());
        Exception thrown = null;
        try {
            model.recordWinner("Alex");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.startNextRace();
        thrown = null;
        try {
            model.recordWinner(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            model.recordWinner("Sam");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);

        assertEquals("Alex", model.recordWinner("Alex"));
        assertEquals("Alex", model.getChampion());
    }

    /**
     * Tests the streak resets to 1 for a new champion and goes up
     * when the champion wins again.
     */
    public void testGetStreak() {
        model.addPlayer("Alex");
        model.addPlayer("Jordan");
        model.addPlayer("Sam");
        model.startNextRace();
        model.recordWinner("Jordan");
        assertEquals(1, model.getStreak());
        model.startNextRace();
        model.recordWinner("Jordan");
        assertEquals(2, model.getStreak());
    }

    /**
     * Tests the streak does not change when recordWinner throws.
     */
    public void testStreakInvalid() {
        Exception thrown = null;
        try {
            model.recordWinner("Alex");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(0, model.getStreak());
    }
}