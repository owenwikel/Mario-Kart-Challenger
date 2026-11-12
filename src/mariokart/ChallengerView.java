
package mariokart;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * The text view for Mario Kart Challenger. It does all of the input and
 * output: it shows the menu, reads menu choices and names, and prints the
 * waiting line, race matchups, the champion and error messages.
 *
 * The view holds no game rules and no reference to the model. It only reads
 * plain Strings from the user and prints plain values (String and String[])
 * that the controller hands it.
 *
 * @author Aryan Patil
 * @version 2026.11.12
 */
public class ChallengerView
{

    // ~ Fields ................................................................

    private final Scanner in;

    private final PrintStream out;

    // ~ Constructors ..........................................................

    /**
     * Creates a view that reads from the keyboard (System.in) and prints to
     * the console (System.out).
     */
    public ChallengerView()
    {

        this(System.in, System.out);

    }


    /**
     * Creates a view that reads from and prints to the given streams. This
     * lets tests feed typed input and check the printed output without a
     * keyboard.
     *
     * @param input
     *            where the user's typing comes from.
     * @param output
     *            where the view prints.
     * @throws IllegalArgumentException
     *             if input or output is null.
     */
    public ChallengerView(InputStream input, PrintStream output)
    {

        if (input == null || output == null)
        {
            throw new IllegalArgumentException(
                "Input and output cannot be null.");
        }

        this.in = new Scanner(input);
        this.out = output;

    }

    // ~ Public Methods ........................................................


    /**
     * Prints the main menu.
     */
    public void showMenu()
    {

        out.println();
        out.println("===== Mario Kart Challenger =====");
        out.println("1. Add Player");
        out.println("2. View Line");
        out.println("3. Next Race");
        out.println("4. Record Winner");
        out.println("5. Show Champion");
        out.println("6. Quit");
        out.print("Choose an option: ");
        out.flush();

    }


    /**
     * Reads one line for the menu choice. The view does not check whether the
     * choice is valid; the controller does that. If there is no more input
     * (for example, the input stream ended), it returns "6" so the game night
     * ends cleanly instead of looping forever.
     *
     * @return the line the user typed, or "6" if there is no more input.
     */
    public String readChoice()
    {

        if (!in.hasNextLine())
        {
            return "6";
        }

        return in.nextLine();

    }


    /**
     * Asks for a player's name and reads one line. The name is returned
     * exactly as typed; the model trims it and decides if it is valid.
     *
     * @return the line the user typed, or null if there is no more input.
     */
    public String readName()
    {

        out.print("Enter a player's name: ");
        out.flush();

        if (!in.hasNextLine())
        {
            return null;
        }

        return in.nextLine();

    }


    /**
     * Prints the players waiting in line, in order, numbered from 1.
     *
     * @param line
     *            the waiting players, front of the line first.
     */
    public void showLine(String[] line)
    {

        if (line == null || line.length == 0)
        {
            out.println("Nobody is waiting in line.");
            return;
        }

        out.println("Waiting:");

        for (int i = 0; i < line.length; i++)
        {
            out.println("  " + (i + 1) + ". " + line[i]);
        }

    }


    /**
     * Prints the matchup for the race that just started.
     *
     * @param champion
     *            the current champion.
     * @param challenger
     *            the player challenging the champion.
     */
    public void showRace(String champion, String challenger)
    {

        out.println("Race: " + champion + " (champion) vs. " + challenger
            + " (challenger)");
        out.println("When the race is over, choose 4 to record the winner.");

    }


    /**
     * Prints the current champion, or a note that there is none yet.
     *
     * @param champion
     *            the champion's name, or null if no race has been won yet.
     */
    public void showChampion(String champion)
    {

        if (champion == null)
        {
            out.println("No champion yet. Start a race with option 3.");
            return;
        }

        out.println("Champion: " + champion);

    }


    /**
     * Prints an error message so the user knows what went wrong. The game
     * then goes back to the menu.
     *
     * @param message
     *            the message to show.
     */
    public void showError(String message)
    {

        if (message == null || message.isEmpty())
        {
            out.println("Error: something went wrong. Please try again.");
            return;
        }

        out.println("Error: " + message);

    }

}
