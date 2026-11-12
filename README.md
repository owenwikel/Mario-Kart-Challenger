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

**ChallengerControllerTest fix (Aryan):** `testInvalidWinner` expected `getChampion()` to be `null` after a bad winner was entered. But `startNextRace()` makes the first player in line the champion when the first race starts, so the champion is already "Alex". We changed the assertion to `assertEquals("Alex", model.getChampion())`. It still checks that a bad winner doesn't change the champion, and now all tests pass.

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
- **Bad input still allowed:** names longer than 20 characters and names with no letters (for example "!!!!") are accepted. The scope says they should be rejected, but `addPlayer` doesn't check for them yet.
- **Error wording:** "Next Race" without enough players says "You must have two racers…" instead of naming who is missing (for example "Nobody is waiting to challenge Alex").
- **Losers:** a player who loses leaves the game and has to use Add Player again. That is the MVP behavior. Automatic Rejoining is a stretch goal.

## Next Steps (Deliverable 4)

### What is left to finish the MVP?
In the order we will do them:
1. **Name rules in the model** (Owen): `ChallengerModel.addPlayer` rejects names longer than 20 characters and names with no letters (for example "!!!!"), and leaves the line unchanged. This is the last bad-input rule from our scope that isn't enforced.
2. **Clearer "not enough players" message** (Owen): `startNextRace` names who is missing, for example "Nobody is waiting to challenge Alex", instead of "You must have two racers…". Only the message text changes. The view already prints whatever message it gets, so it doesn't change.
3. **Tests for 1 and 2** (Owen): new cases in `ChallengerModelTest`. Then Aryan adds the two new bad inputs to `ChallengerViewTest.testBadInputGameNight` so they are also checked through the real menu.
4. **Full game night by hand** (Preston, with Aryan): run `GameNightApp`, use all six menu options, and try every bad-input case from the scope. Anything that crashes or reads badly goes back to the owner of that class.

### Our change feature
- **Feature:** Win Streak Tracker. It counts how many races in a row the current champion has won, and resets when a new champion takes over. It adds model state (a streak count) and logic (when to add to it or reset it), and users reach it through the menu. That makes it a non-trivial feature, not just a new label.
- **Classes and methods we expect to change:**
  - **ChallengerModel (model):**
    - a new field, `private int streak`, set to 0 in the constructor
    - `recordWinner(String name)`: if the winner is already the champion, `streak++`; otherwise `streak = 1`
    - a new `getStreak()` getter
  - **ChallengerView (view):** a new `showStreak(String champion, int streak)` that prints, for example, "Alex has won 3 in a row."
  - **ChallengerController (controller):** in `run()`, case `"4"` calls `view.showStreak(champion, model.getStreak())` after `showChampion`, and case `"5"` does the same.
- **What should NOT change:**
  - **Whole classes:** `Node`, `QueueInterface`, `LinkedQueue` and `GameNightApp`. The streak belongs to the champion, who isn't in the queue, and `main()` only connects the parts.
  - **In ChallengerModel:** `addPlayer`, `startNextRace` and `getLine`.
  - **In ChallengerView:** `showMenu`, `readChoice`, `readName`, `showLine` and `showRace`.

  If any of these does need to change, our model/view line is in the wrong place, and we'll explain why in the Deliverable 4 reflection.

### Which stretch goals will we attempt?

| Stretch goal | Decision |
|---|---|
| Win Streak Tracker | **Committing.** It is our change feature. |
| Automatic Rejoining | **Committing after the Win Streak Tracker works.** The model re-adds the loser to the back of the line in `recordWinner` (one `line.enqueue(loser)` call), so it reuses the queue we already have. |
| Leaderboard | **Dropping.** It needs win counts for every player and a ranking display, which is too much for one more deliverable. |
| Character Selection | **Dropping.** It is mostly extra display and adds little to the queue logic. |

### What new tests will we need?
- **`ChallengerModelTest`:**
  - **Name rules (Owen):** `addPlayer` rejects a 21-character name, accepts a 20-character name, rejects "!!!!" and "123", and leaves the line unchanged after each rejection.
  - **Clearer message (Owen):** the new "nobody is waiting" message names the champion.
  - **Win streak (Owen):**
    - `getStreak()` is 0 before any race.
    - The streak is 1 after the first race.
    - It goes up when the champion wins again.
    - It resets to 1 when the challenger wins.
    - It stays the same when `recordWinner` throws (no race active, or the winner wasn't racing).
  - **Automatic rejoining (Nishitha):** the loser is at the back of the line after `recordWinner`, and the winner is not in the line.
- **`ChallengerViewTest` (Aryan):**
  - `showStreak` prints the name and the count, and handles a null champion.
  - `testFullGameNight` checks the streak text after each race.
- **`ChallengerControllerTest` (Preston):**
  - After option 4 the streak is shown, and after a rejected winner it isn't.
  - Option 5 shows the streak.
  - The fake `TestView` needs a `showStreak` override.
- **`LinkedQueueTest` (Nishitha):** no new cases, because the queue doesn't change. If any queue test has to change, that tells us the feature leaked into the queue.

### Who does what?

| Remaining item | Owner | Why them |
|---|---|---|
| Name rules and clearer error message in `ChallengerModel`, and their tests | Owen Wikel | Wrote `ChallengerModel` and `ChallengerModelTest` |
| Win streak in `ChallengerModel` (`streak`, `recordWinner`, `getStreak`) and its tests | Owen Wikel | Same class and tests |
| `showStreak` in `ChallengerView`, and the new view and end-to-end tests | Aryan Patil | Wrote `ChallengerView` and `ChallengerViewTest` |
| Wiring the streak into `ChallengerController.run()` (cases 4 and 5), `TestView` and the controller tests | Preston Moore | Wrote `ChallengerController` and its tests |
| Full game night by hand with every bad-input case | Preston Moore, with Aryan Patil | Owns the menu loop and the view |
| Automatic Rejoining in `recordWinner`, and its tests | Nishitha Kancharla | Wrote the queue that the loser rejoins |
| Deliverable 4 README: comparing what actually changed with this prediction, and the attribution table | Nishitha Kancharla, with everyone adding their own rows | Set up the README skeleton |

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
| `ChallengerControllerTest.java` | Preston Moore, Aryan Patil | Preston wrote, GenAI-assisted; Aryan revised one assertion in `testInvalidWinner` (GenAI-assisted) |
| `GameNightApp.java` | Preston Moore | wrote, reviewed, GenAI-assisted |
| `GameNightAppTest.java` | Preston Moore | wrote, GenAI-assisted |

### GenAI Note

- **Owen:** Used GenAI to check my tests were sufficient.
- **Nishitha:** I used GenAI to help create my code and verify the test. Also to create a skeleton for the ReadMe file so it was easier to fill in
- **Aryan:** I used GenAI (Claude) to write `ChallengerView` and `ChallengerViewTest` so that they match the method names Preston's controller and its fake `TestView` already call. I also used it to draft my parts of the Reflection and the Next Steps section. I gave it what each person built so far and asked it to assign remaining work to the owner of each class. I also asked it which classes and methods the Win Streak Tracker would force us to edit. Its answer (ChallengerModel, ChallengerView and ChallengerController, but not the queue classes or GameNightApp) matched our earlier prediction and stays on the right side of the model/view line. I checked by hand that the view never references the model, that names are passed through untrimmed so the model does the validating, and that the tests pass.
- **Preston:** I used GenAI to validate that my tests covered everything.
