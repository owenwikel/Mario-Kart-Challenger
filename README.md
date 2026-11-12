# Mario-Kart-Challenger
Mario Kart Challenger keeps game night fair. Players join a waiting line, the champion races whoever is next, and the winner stays. Losers can rejoin at the back, and new players can join anytime.

**Group 39 / CS 2114 Project 3:** Owen Wikel, Nishitha Kancharla,
Aryan Patil, Preston Moore

## How to Run

### The model on its own (no view)
Run the JUnit tests (they use `student.TestCase`, so have the CS 2114 `student.jar` on the build path):
- `LinkedQueueTest` and `ChallengerModelTest` exercise the queue and the model directly. No view is created.
- `ChallengerControllerTest` drives the controller with a fake view.
- `ChallengerViewTest` tests the text view with typed input, and also plays a full game night and every bad-input case through the real view, controller and model.

### The thin text view
1. Open the project in Eclipse or VS Code.
2. Run `GameNightApp.main()`. This builds the model, the `ChallengerView` text menu and the controller, then starts the menu.
3. Use the text menu (type the number and press Enter):

| Choice | Action |
|---|---|
| 1 | Add Player |
| 2 | View Line |
| 3 | Next Race |
| 4 | Record Winner |
| 5 | Show Champion |
| 6 | Quit |

## Project Structure

| Role | Class | Job |
|---|---|---|
| Model | `QueueInterface<T>` | The queue contract |
| Model | `Node<T>` | One link in the queue (data + next) |
| Model | `LinkedQueue<T>` | Hand-written singly linked FIFO waiting line |
| Model | `ChallengerModel` | Game state and rules (line, champion, challenger, raceActive) |
| View | `ChallengerView` | All input and output (Scanner and System.out) |
| Controller | `ChallengerController` | Menu loop; turns each choice into one model call |
| Controller | `GameNightApp` | `main()`; builds and connects model, view and controller |

## Reflection

### What changed from our specification?
recordWinner throws IllegalStateException when no race is active. Section 6 required this, but the Section 5 table left it out.
addPlayer and recordWinner also reject a null name.
recordWinner ignores capitalization and extra spaces in the winner’s name.

**ChallengerView (Aryan):**
- **Spec:** the view had one way to be built, reading from the keyboard. **Built:** a second constructor, `ChallengerView(InputStream, PrintStream)`, next to the no-argument one. **Why:** without it the only way to test the view was to swap out `System.in` and `System.out`. With it, `ChallengerViewTest` types input from a String and reads back what was printed.
- **Spec:** `readChoice()` and `readName()` just return what was typed. **Built:** that, plus handling for input that runs out. `readChoice()` returns `"6"` (Quit) and `readName()` returns `null`, which the model already rejects. **Why:** the controller treats a `null` choice as "Not a valid option" and shows the menu again, so if the input ended (for example a test that types too little), the menu loop would never stop.
- **Validation:** still lives only in the model, as the spec said. The view returns names exactly as typed, spaces included, and `ChallengerViewTest.testReadName` checks that it does not trim them. The only check in the view is that its own streams are not null.
- **Display:** `showChampion(null)` prints "No champion yet" and `showLine` prints "Nobody is waiting in line" for an empty line. The spec didn't say what to show before the first race.
- **Tests:** the spec only planned unit tests for each class. We added two end-to-end tests to `ChallengerViewTest` (`testFullGameNight`, `testBadInputGameNight`) that run typed input through the real view, controller and model. They show the MVP path and the scope's bad-input cases run without crashing.

### Did the model really stand alone?
Yes. ChallengerModel has no Scanner or System.out. ChallengerModelTest only uses the model.
Nothing had to cross the line to make the view work. ChallengerView never imports or mentions ChallengerModel, LinkedQueue or Node. It only receives `String` and `String[]` values from the controller, and the controller turns model exceptions into message text before the view sees them.

### What happened to our unknowns?
The main thing we had to work out while building was how to write a singly linked queue with `front`, `back` and `size`. That is done in : `LinkedQueue` and `Node` pass `LinkedQueueTest`, including emptying the queue and refilling it.

### What doesn't work yet?
- **Failing test:** `ChallengerControllerTest.testInvalidWinner` fails. It expects `getChampion()` to be `null` after the first race starts, but `startNextRace()` already makes the first player in line the champion. The controller works correctly, because the bad winner is rejected and the race stays active. The test's last assertion should be `assertEquals("Alex", model.getChampion())`.
- **Bad input still allowed:** names longer than 20 characters and names with no letters (for example "!!!!") are accepted. The scope says they should be rejected, but `addPlayer` doesn't check for them yet.
- **Error wording:** "Next Race" without enough players says "You must have two racers…" instead of naming who is missing (for example "Nobody is waiting to challenge Alex").
- **Losers:** a player who loses leaves the game and has to use Add Player again. That is the MVP behavior. Automatic Rejoining is a stretch goal.

## Next Steps (Deliverable 4)

### What is left to finish the MVP?
- Run one full game night with all six menu options and try every bad-input case from Section 6 by hand.

### Our change feature
- **Feature:** Win Streak Tracker. It counts consecutive wins for the current champion and resets when a new champion takes over.
- **Classes we expect to change:**
  - ChallengerModel (a streak field, updated in `recordWinner`, plus a getter)
  - ChallengerView (a method to show the streak)
  - ChallengerController (call that method after a winner is recorded)
- **Classes that should NOT change:** Node, QueueInterface, LinkedQueue and GameNightApp, because the queue only stores names and main() only connects the parts.

### Which stretch goals will we attempt?

| Stretch goal | Decision |
|---|---|
| Win Streak Tracker | Committing (our change feature) |
| Leaderboard | Dropping (needs per-player win counts and a ranking display) |
| Automatic Rejoining | Might still include for the second prototype(the model would have to re-add the loser after each race) |
| Character Selection | Dropping  |

### What new tests will we need?
- **ChallengerModel, the change feature:** the streak starts at 1 after the first race, goes up each time the champion wins again, and resets to 1 when the challenger wins. The streak doesn't change when `recordWinner` throws (no race active, or the winner wasn't racing). There's a getter for the streak before any race.
- **ChallengerModel, bad input:** `addPlayer` rejects a name over 20 characters and a name with no letters, and leaves the line unchanged.
- **ChallengerView:** the new `showStreak` method prints the champion and the streak, and handles there being no champion.
- **ChallengerController:** after option 4 the streak is shown. After a rejected winner, it is not shown.
- **Fix:** update the failing assertion in `ChallengerControllerTest.testInvalidWinner`.

## Attribution

| File | Who worked on it | How |
|---|---|---|
| `QueueInterface.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `Node.java` | Nishitha Kancharla |wrote, reviewed, GenAI-assisted |
| `LinkedQueue.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `LinkedQueueTest.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted|
| `ChallengerModel.java` | Owen Wikel | wrote, tested |
| `ChallengerModelTest.java` | Owen Wikel | wrote, GenAI-assisted |
| `ChallengerView.java` | Aryan Patil | wrote with GenAI assistance, reviewed, tested |
| `ChallengerViewTest.java` | Aryan Patil | wrote with GenAI assistance, reviewed, ran |
| `ChallengerController.java` | Preston Moore | wrote, reviewed, GenAI-assisted |
| `ChallengerControllerTest.java` | Preston Moore | wrote, GenAI-assisted |
| `GameNightApp.java` | Preston Moore | wrote, reviewed, GenAI-assisted |
| `GameNightAppTest.java` | Preston Moore | wrote, GenAI-assisted |

### GenAI Note

- **Owen:** Used GenAI to check my tests were sufficient.
- **Nishitha:** I used GenAI to help create my code and verify the test. Also to create a skeleton for the ReadMe file so it was easier to fill in
- **Aryan:** I used GenAI (Claude) to write `ChallengerView` and `ChallengerViewTest` so that they match the method names Preston's controller and its fake `TestView` already call. I also used it to draft my parts of the Reflection and Next Steps (what doesn't work yet, new tests). I checked by hand that the view never references the model, that names are passed through untrimmed so the model does the validating, and that the tests pass.
- **Preston:** I used GenAI to validate that my tests covered everything.
