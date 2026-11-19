
package mariokart;

/**
 * Connects the Mario Kart Challenger model and view. Handles menu choices,
 * delegates game rules to the model, and displays results through the view.
 *
 * @author Preston Moore
 * @author Aryan Patil (win streak display)
 * @version 2026.11.19
 */
public class ChallengerController
{
    private final ChallengerModel model;
    private final ChallengerView view;

    /**
     * Creates a controller with a model and a view.
     *
     * @param model
     *            the game state and rules
     * @param view
     *            the input and output interface
     * @throws IllegalArgumentException
     *             if either argument is null
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


    /**
     * Processes menu selections until the user quits. Invalid choices and model
     * validation errors are displayed without ending the menu loop.
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
                        view.showStreak(champion, model.getStreak());
                        break;
                    case "5":
                        view.showChampion(model.getChampion());
                        view.showStreak(
                            model.getChampion(),
                            model.getStreak());
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
