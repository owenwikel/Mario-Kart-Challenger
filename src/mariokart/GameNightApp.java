
package mariokart;

/**
 * Starts the Mario Kart Challenger application by connecting the model, view,
 * and controller.
 *
 * @author Preston Moore
 * @version 2026.11.19
 */
public class GameNightApp
{
    /**
     * Starts the game and runs the menu until the user quits.
     *
     * @param args
     *            command-line arguments (unused)
     */
    public static void main(String[] args)
    {
        ChallengerModel model = new ChallengerModel();
        ChallengerView view = new ChallengerView();
        ChallengerController controller = new ChallengerController(model, view);
        controller.run();
    }
}
