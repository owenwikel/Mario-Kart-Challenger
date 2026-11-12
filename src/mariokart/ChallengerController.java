
package mariokart;

/**
 * Controls the Mario Kart Challenger game by connecting the model and view.
 * Handles menu choices, calls model methods, and displays errors through the
 * view.
 *
 * @author Preston Moore
 * @version 2026.11.12
 */
public class ChallengerController
{

    // ~ Fields ................................................................

    private final ChallengerModel model;

    private final ChallengerView view;

    // ~ Constructors ..........................................................

    /**
     * Creates a controller with the given model and view.
     *
     * @param model
     *            the model that holds the game state.
     * @param view
     *            the view that handles user input and output.
     * @throws IllegalArgumentException
     *             if model or view is null.
     */
    public ChallengerController(ChallengerModel model, ChallengerView view)
    {

        if (model == null || view == null)
        {
            throw new IllegalArgumentException(
                "Model and view cannot be null.");
        }

        this.model = model;
        this.view = view;

    }

    // ~ Public Methods ........................................................


    /**
     * Runs the game menu until the user chooses to quit. Each menu choice calls
     * the appropriate model method. Invalid choices and model exceptions are
     * displayed through the view.
     */
    public void run()
    {

        boolean running = true;

        while (running)
        {

            view.showMenu();
            String choice = view.readChoice();

            if (choice == null)
            {
                view.showError("Not a valid option.");
                continue;
            }

            try
            {

                switch (choice.trim())
                {

                    case "1":
                        model.addPlayer(view.readName());
                        break;

                    case "2":
                        view.showLine(model.getLine());
                        break;

                    case "3":
                        String[] racers = model.startNextRace();
                        view.showRace(racers[0], racers[1]);
                        break;

                    case "4":
                        String winner = view.readName();
                        String champion = model.recordWinner(winner);
                        view.showChampion(champion);
                        break;

                    case "5":
                        view.showChampion(model.getChampion());
                        break;

                    case "6":
                        running = false;
                        break;

                    default:
                        view.showError("Not a valid option.");
                        break;

                }

            }
            catch (IllegalArgumentException | IllegalStateException e)
            {

                view.showError(e.getMessage());

            }

        }

    }

}
