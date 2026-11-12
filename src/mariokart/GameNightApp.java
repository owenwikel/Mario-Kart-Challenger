
package mariokart;

/**
 * Starts the Mario Kart Challenger application. Creates the model, view, and
 * controller and connects them together before starting the game.
 *
 * @author Preston Moore
 * @version 2026.11.12
 */
public class GameNightApp
{

    // ~ Public Methods ........................................................

    /**
     * Main method that starts the Mario Kart Challenger game. Creates the
     * necessary objects and runs the controller.
     *
     * @param args
     *            command line arguments (not used).
     */
    public static void main(String[] args)
    {

        ChallengerModel model = new ChallengerModel();

        ChallengerView view = new ChallengerView();

        ChallengerController controller = new ChallengerController(model, view);

        controller.run();

    }

}
