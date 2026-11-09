package mariokart;
/**
 * Holds game state for Mario Kart Challenger: the waiting line,
 * the champion, the challenger, and whether a race is active.
 * Enforces all game rules.
 *
 * @author Owen Wikel (wikelog)
 * @version 2026.11.09
 */
public class ChallengerModel {
    //~ Fields ................................................................
    private LinkedQueue<String> line;
    private String champion;
    private String challenger;
    private boolean raceActive;
    //~ Constructors ..........................................................
    /**
     * Creates a new model with an empty line, no champion,
     * no challenger, and no active race.
     */
    public ChallengerModel() {
        champion = null;
        challenger = null;
        raceActive = false;
        line = new LinkedQueue<String>();
    }
    //~Public  Methods ........................................................
    /**
     * Trims name and adds it to the back of the line.
     * @param name the name of the player being added.
     * @throws IllegalArgumentException if user entered an invalid name.
     */
    public void addPlayer(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Please enter a valid name.");
        }
        name = name.strip();
        if (name.equals("")) {
            throw new IllegalArgumentException("Please enter a valid name.");
        }
        Object[] a = line.toArray();
        for (int i = 0; i < a.length; i++) {
            if (name.equalsIgnoreCase((String)a[i])) {
                throw new IllegalArgumentException("Please enter a name"
                    + " that is not already in the queue.");
            }
        }
        if (name.equalsIgnoreCase(challenger) ||
            name.equalsIgnoreCase(champion)) {
            throw new IllegalArgumentException("Please enter a name"
                + " that is not already playing.");
        }
        line.enqueue(name);
    }
    
    
    
    
    /**
     * Starts a race and returns {champion, challenger},
     * If there is no champion, the first two in line race.
     * @throws IllegalStateException if there is an active race or less than
     * 2 racers.
     * @return an Array containing the champion and challenger.
     */
    public String[] startNextRace() throws IllegalStateException {
        if (raceActive) {
            throw new IllegalStateException("You must wait for the current"
                + " race to finish. If there is no active race you must record"
                + " the winner of the previous race before starting another.");
        }
        if (champion == null) {
            if (line.size() < 2) {
                throw new IllegalStateException("You must have two racers to"
                    + " start the first race.");
            }
            champion = line.dequeue();
            challenger = line.dequeue();
        }
        else {
            if (line.isEmpty()) {
                throw new IllegalStateException("You must have two racers to"
                    + " start the next race.");
            }
            challenger = line.dequeue();
        }
        raceActive = true;
        return new String[]{this.champion, this.challenger};
    }
    /**
     * Ends the race, makes the winner champion and returns their name.
     * @param name the name of the winner.
     * @throws IllegalArgumentException if user records winner as
     * non-participant.
     * @return the name of the winner.
     * @throws IllegalStateException if race is active or name is null;
     */
    public String recordWinner(String name) throws IllegalArgumentException, 
    IllegalStateException {
        if (!raceActive) {
            throw new IllegalStateException("You must race before declaring"
                + " a winner");
        }
        if(name == null) {
            throw new IllegalArgumentException("Winner must have participated"
                + " in the recent race");
        }
        name = name.strip();
        if (!name.equalsIgnoreCase(champion) &&
            !name.equalsIgnoreCase(challenger)) {
            throw new IllegalArgumentException("Winner must have participated"
                + " in the recent race");
        }
        raceActive = false;
        champion = name;
        challenger = null;
        return name;
    }
    
    /**
     * Returns the current champion. 
     * @return the champion.
     */
    public String getChampion() {
        return this.champion;
    }
    
    /**
     * Returns the waiting players in order.
     * @return the queue in an Array of strings.
     */
    public String[] getLine() {
        Object[] o = this.line.toArray();
        String[] s = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            s[i] = (String)o[i];
        }
        return s;
    }


}
