# Mario-Kart-Challenger
Mario Kart Challenger keeps game night fair. Players join a waiting line, the champion races whoever is next, and the winner stays. Losers can rejoin at the back, and new players can join anytime.

**Group 39 / CS 2114 Project 3:** Owen Wikel, Nishitha Kancharla,
Aryan Patil, Preston Moore

## How to Run

1. Open the project in Eclipse or VS Code.
2. Run `GameNightApp.main()`.
3. Use the text menu:

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

### Did the model really stand alone?
Yes. ChallengerModel has no Scanner or System.out. ChallengerModelTest only uses the model.

### What happened to our unknowns?
The main thing we had to work out while building was how to write a singly linked queue with `front`, `back` and `size`. That is done in : `LinkedQueue` and `Node` pass `LinkedQueueTest`, including emptying the queue and refilling it.

### What doesn't work yet?


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

| Win Streak Tracker | Committing (our change feature) |
| Leaderboard | Dropping (needs per-player win counts and a ranking display) |
| Automatic Rejoining | Might still include for the second prototype(the model would have to re-add the loser after each race) |
| Character Selection | Dropping  |

### What new tests will we need?


## Attribution

| File | Who worked on it | How |
|---|---|---|
| `QueueInterface.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `Node.java` | Nishitha Kancharla |wrote, reviewed, GenAI-assisted |
| `LinkedQueue.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `LinkedQueueTest.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted|
| `ChallengerModel.java` | Owen Wikel | wrote, tested |
| `ChallengerModelTest.java` | Owen Wikel | wrote, GenAI-assisted |
| `ChallengerView.java` | [name(s)] | [fill in] |
| `ChallengerController.java` | Preston Moore | wrote, reviewed, GenAI-assisted |
| `GameNightApp.java` | Preston Moore | wrote, reviewed, GenAI-assisted |

### GenAI Note

- **Owen:** Used GenAI to check my tests were sufficient.
- **Nishitha:** I used GenAI to help create my code and verify the test. Also to create a skeleton for the ReadMe file so it was easier to fill in
- **Aryan:** []
- **Preston:** I used GenAI to validate that my tests covered everything.
