# Mario-Kart-Challenger
Mario Kart Challenger keeps game night fair. Players join a waiting line, the champion races whoever is next, and the winner stays. Losers can rejoin at the back, and new players can join anytime.

**Group 39 / CS 2114 Project 3:** Owen Wikel, Nishitha Kancharla,
Aryan Patil, Preston Moore

## How to Run

### Compile and run from a terminal
From the project folder, with the CS 2114 `student.jar` somewhere on your computer. If your copy doesn't bundle JUnit, add `junit.jar` to the same classpath. On macOS/Linux use `:` instead of `;` in the classpath.

```
javac -cp path/to/student.jar -d bin src/mariokart/*.java
java -cp bin mariokart.GameNightApp
java -cp "bin;path/to/student.jar" org.junit.runner.JUnitCore mariokart.LinkedQueueTest mariokart.ChallengerModelTest mariokart.ChallengerControllerTest mariokart.GameNightAppTest mariokart.ChallengerViewTest
```

In Eclipse or VS Code, add `student.jar` to the build path, then right-click a test file and choose Run As > JUnit Test (Eclipse) or Run Tests (VS Code).

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
| Model | `ChallengerModel` | Game state and rules (line, champion, challenger, raceActive, win streak) |
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

### Deliverable 4: model side of the change (Owen)
- **Classes the feature touched:** in the model, only `ChallengerModel` and `ChallengerModelTest`. This matched our D3 prediction: a new `streak` field set to 0 in the constructor, the streak update in `recordWinner`, and a new `getStreak()`. `Node`, `QueueInterface` and `LinkedQueue` did not change, and no `LinkedQueueTest` case had to change, so the feature did not leak into the queue.
- **What surprised us:** where the streak update goes in `recordWinner` matters. It has to come after every check that can throw, so a bad winner never changes the streak. It also has to come before `champion = name`, or there is no old champion left to compare with.
- **Model/view line:** on the model side the change did not cross the line. The model only gained `getStreak()`, which returns an `int`. The view receives the champion's name and the count from the controller, never the model itself.
- **What changed from the D3 plan:** `addPlayer` changed, even though D3 listed it under "should NOT change". This was not caused by the streak: `addPlayer` now enforces the name rules from our scope, which were already planned as a separate MVP fix in our D3 Next Steps.
- **Tests:** `ChallengerModelTest` now has a normal case and a bad-input case for each new behavior:
  - `testGetStreak`: the streak resets to 1 for a new champion and goes up when the champion wins again.
  - `testStreakInvalid`: the streak stays at 0 when `recordWinner` throws.
  - `testAddPlayerNameRules`: a 21-character name and "!!!!" are rejected, and the line stays empty.

  Compared with the D3 plan, we kept the smallest set of tests that still reaches every new branch, rather than every case we listed.

### Deliverable 4: view side of the change (Aryan)
- **Classes the feature actually touched on this side:**
  - `ChallengerView`: one new method, `showStreak(String champion, int streak)`.
  - Tests: `ChallengerViewTest`.
- **Compared with our D3 prediction:** this matched. D3 said the view would gain `showStreak`, and that is all that changed in the view. `showMenu`, `readChoice`, `readName`, `showLine`, `showRace`, `showChampion` and `showError` did not change, and neither did `GameNightApp`.
- **Did the change cross the model/view line?** No. The view learned one new thing, an `int` count, and it still never sees `ChallengerModel`. The view only decides how to word it ("race" vs. "races", and "No win streak yet." when there is no champion). It does not decide what the streak is.
- **What changed from the D3 plan:** the signature is exactly what D3 named. Two small additions: the wording switches between "race" and "races", and `showStreak(null, 0)` prints "No win streak yet." so option 5 works before the first race. On the test side, `testBadInputGameNight` now also types a 21-character name and "!!!!" through the real menu, which D3 Next Steps step 3 asked for.
- **New tests:**
  - `ChallengerViewTest.testShowStreak` and `testShowStreakNone`
  - `testFullGameNight` now checks the streak resets to 1 for a new champion and grows to 2 when the champion defends

### Deliverable 4: controller side of the change (Preston)
- **Classes the feature actually touched on this side:**
  - `ChallengerController`: only cases `"4"` and `"5"` of `run()`, which now call `view.showStreak(champion, model.getStreak())` after `showChampion`.
  - Tests: `ChallengerControllerTest`, whose fake `TestView` needed a `showStreak` override.
- **Compared with our D3 prediction:** this matched. D3 said the controller would call `showStreak` in cases 4 and 5, and nothing else in the controller changed.
- **What surprised us:**
  - The controller's fake `TestView` had to change too. Any time the view gains a method the controller calls, the test double has to gain it as well.
  - An earlier cleanup had inlined `view.showChampion(model.recordWinner(winner))`. Case 4 had to go back to saving the champion in a local variable so it could also be passed to `showStreak`.
- **Did the change cross the model/view line?** No. The controller reads `getStreak()` from the model and passes the plain number to the view. The view still never sees the model.
- **New tests:** `ChallengerControllerTest.testRecordWinnerShowsStreak`, `testInvalidWinnerShowsNoStreak` and `testViewChampionShowsStreak`.
- **Full game night by hand:** Preston ran `GameNightApp`, used all six menu options, and tried every bad-input case from the scope.

All 66 tests pass.

### Did the model really stand alone?
Yes. ChallengerModel has no Scanner or System.out. ChallengerModelTest only uses the model.
Nothing had to cross the line to make the view work. ChallengerView never imports or mentions ChallengerModel, LinkedQueue or Node. It only receives `String` and `String[]` values from the controller, and the controller turns model exceptions into message text before the view sees them.

### What happened to our unknowns?
The main thing we had to work out while building was how to write a singly linked queue with `front`, `back` and `size`. That is done in `LinkedQueue` and `Node`, which pass `LinkedQueueTest`, including emptying the queue and refilling it.

### What doesn't work yet?
- **Bad input:** fixed in D4. `addPlayer` now rejects names longer than 20 characters and names with no letters, and leaves the line unchanged.
- **Error wording:** not fixed yet. The code and `ChallengerModelTest` still use "You must have two racers to start the next race." The "Nobody is waiting to challenge Alex" message was planned but isn't in `startNextRace` yet.
- **Win streak display:** done. After option 4 or 5 the menu prints, for example, "Alex has won 2 races in a row."
- **Champion's name capitalization:** if the winner is typed as "alex", the champion is stored as "alex" instead of "Alex". The streak still counts correctly, because names are compared ignoring case, but the display uses the typed spelling.
- **Losers:** a player who loses leaves the game and has to use Add Player again. That is the MVP behavior. Automatic Rejoining is a stretch goal.

## Next Steps (Final Build)

### What is left for the final build?
In the order we will do them:
1. **Clearer "not enough players" message** (Owen): `startNextRace` says "Nobody is waiting to challenge Alex." when a champion exists, and `ChallengerModelTest` checks the new text.
2. **Keep the champion's original spelling** (Owen): `recordWinner` stores the racer's name as it was entered in the line ("Alex"), not as typed for the winner ("alex"). Add a test.
3. **Automatic Rejoining** (Nishitha): after a winner is recorded, the model puts the loser at the back of the line with `line.enqueue(loser)`. Add model tests (the loser is at the back, the winner is not in the line, and nothing is re-added when `recordWinner` throws).
4. **Show who rejoined** (Aryan, with Preston): once step 3 works, the view prints "Jordan rejoined the line." after a race. Preston passes the name in from the controller, and Aryan updates `testFullGameNight` and `testBadInputGameNight`.
5. **Full game night by hand and a demo rehearsal** (Preston, with Aryan): run every menu option and every bad-input case from the scope, then time the demo below.
6. **Final README** (Nishitha, with everyone adding their own rows): the reflection on Automatic Rejoining, plus the final attribution and GenAI note.

### Which stretch goals are we committing to?

| Stretch goal | Decision |
|---|---|
| Win Streak Tracker | **Done** (our D4 change feature). |
| Automatic Rejoining | **Committing** for the final build. It only adds one queue call in the model, and the view shows one extra line. |
| Leaderboard | **Dropping.** It needs win counts for every player and a ranking, which is a second data structure and a new menu option. |
| Character Selection | **Dropping.** It is mostly display and doesn't add queue logic. |

### What will our 3-minute demo show?
1. **Add players:** Alex, Jordan, Sam and Taylor (option 1). Also try "!!!!", a 21-character name and " alex " to show they are rejected without crashing.
2. **View Line** (option 2) to show FIFO order.
3. **Next Race** (option 3): Alex vs. Jordan. Press 3 again to show a second race can't start yet.
4. **Record Winner** (option 4): first type "Sam" to show a non-racer is rejected, then "Jordan". Jordan becomes champion with "1 race in a row."
5. **Next Race, then Record Winner with Jordan again:** the streak grows to 2. Show Champion (option 5) shows "Jordan has won 2 races in a row."
6. **Next Race:** Jordan vs. Taylor, and Taylor wins, so the streak resets to 1. If Automatic Rejoining is done, show Jordan back at the end of the line (option 2).
7. **Quit** (option 6). Then briefly show `ChallengerModelTest` passing to prove the model works without the view.

### Who does what?

| Remaining item | Owner | Why them |
|---|---|---|
| Clearer message and champion spelling in `ChallengerModel`, with tests | Owen Wikel | Wrote `ChallengerModel` and `ChallengerModelTest` |
| Automatic Rejoining in `recordWinner`, with model tests | Nishitha Kancharla | Wrote the queue the loser rejoins |
| "Rejoined the line" message in `ChallengerView`, and updated view and end-to-end tests | Aryan Patil | Wrote `ChallengerView`, `ChallengerViewTest` and the streak display |
| Passing the rejoined name from `ChallengerController`, and controller tests | Preston Moore | Wrote `ChallengerController`, its tests and the streak wiring |
| Full game night by hand and the demo rehearsal | Preston Moore, with Aryan Patil | Own the menu loop and the view |
| Final README reflection and attribution | Nishitha Kancharla, with everyone adding their own rows | Set up the README |

## Deliverable 3 Next Steps (our prediction, kept for comparison)

### What is left to finish the MVP?
In the order we will do them:
1. **Name rules in the model** (Owen, **done**): `ChallengerModel.addPlayer` rejects names longer than 20 characters and names with no letters (for example "!!!!"), and leaves the line unchanged.
2. **Clearer "not enough players" message** (Owen): `startNextRace` names who is missing, for example "Nobody is waiting to challenge Alex", instead of "You must have two racers…". Moved to the final build.
3. **Tests for 1 and 2** (Owen, **done for 1**): new cases in `ChallengerModelTest`. Aryan added the two new bad inputs to `ChallengerViewTest.testBadInputGameNight` (**done**).
4. **Full game night by hand** (Preston, with Aryan, **done**): run `GameNightApp`, use all six menu options, and try every bad-input case from the scope.

### Our change feature
- **Feature:** Win Streak Tracker. It counts how many races in a row the current champion has won, and resets when a new champion takes over. It adds model state (a streak count) and logic (when to add to it or reset it), and users reach it through the menu.
- **Classes and methods we expected to change:**
  - **ChallengerModel (model):** a new field `private int streak`, set to 0 in the constructor; `recordWinner` adds to or resets it; a new `getStreak()` getter.
  - **ChallengerView (view):** a new `showStreak(String champion, int streak)`.
  - **ChallengerController (controller):** in `run()`, cases `"4"` and `"5"` call `view.showStreak(champion, model.getStreak())` after `showChampion`.
- **What should NOT change:**
  - **Whole classes:** `Node`, `QueueInterface`, `LinkedQueue` and `GameNightApp`.
  - **In ChallengerModel:** `addPlayer`, `startNextRace` and `getLine`.
  - **In ChallengerView:** `showMenu`, `readChoice`, `readName`, `showLine` and `showRace`.

### Which stretch goals will we attempt?

| Stretch goal | Decision |
|---|---|
| Win Streak Tracker | **Committing.** It is our change feature. |
| Automatic Rejoining | **Committing after the Win Streak Tracker works.** |
| Leaderboard | **Dropping.** |
| Character Selection | **Dropping.** |

## Attribution

| File | Who worked on it | How |
|---|---|---|
| `QueueInterface.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `Node.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `LinkedQueue.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `LinkedQueueTest.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `ChallengerModel.java` | Owen Wikel | wrote, tested, GenAI-assisted, reviewed |
| `ChallengerModelTest.java` | Owen Wikel | wrote, GenAI-assisted GenAI-assisted, reviewed, ran |
| `ChallengerView.java` | Aryan Patil | wrote with GenAI assistance (including D4 `showStreak`), reviewed, tested |
| `ChallengerViewTest.java` | Aryan Patil | wrote with GenAI assistance (including D4 streak and new bad-input tests), reviewed, ran |
| `ChallengerController.java` | Preston Moore | wrote, reviewed, GenAI-assisted; added the D4 `showStreak` calls in cases 4 and 5 |
| `ChallengerControllerTest.java` | Preston Moore, Aryan Patil | Preston wrote, GenAI-assisted, and added the three D4 streak tests and the `TestView.showStreak` override; Aryan revised one assertion in `testInvalidWinner` (GenAI-assisted) |
| `GameNightApp.java` | Preston Moore | wrote, reviewed, GenAI-assisted |
| `GameNightAppTest.java` | Preston Moore | wrote, GenAI-assisted |

### GenAI Note

- **Owen:** Used GenAI to check my tests were sufficient.
- **Nishitha:** I used GenAI to help create my code and verify the test. Also to create a skeleton for the ReadMe file so it was easier to fill in.
- **Aryan:** I used GenAI (Claude) to write `ChallengerView`, `ChallengerViewTest` and the D4 `showStreak` display and its view tests, and to draft my Reflection and Next Steps sections. I reviewed everything, checked that the view never references the model, and ran all tests to confirm they pass.
- **Preston:** I used GenAI to validate that my tests covered everything. For D4, I wired the streak into the controller (cases 4 and 5), added the `TestView` override and the controller streak tests, and ran the full game night by hand.
