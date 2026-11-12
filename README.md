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



### Did the model really stand alone?



### What happened to our unknowns?


### What doesn't work yet?


## Next Steps (Deliverable 4)

### What is left to finish the MVP?


### Our change feature


### Which stretch goals will we attempt?


| Win Streak Tracker | [Committing / Dropping] |
| Leaderboard | [Committing / Dropping] |
| Automatic Rejoining | [Committing / Dropping] |
| Character Selection | [Committing / Dropping] |

### What new tests will we need?




## Attribution

| File | Who worked on it | How |
|---|---|---|
| `QueueInterface.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `Node.java` | Nishitha Kancharla |wrote, reviewed, GenAI-assisted |
| `LinkedQueue.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted |
| `LinkedQueueTest.java` | Nishitha Kancharla | wrote, reviewed, GenAI-assisted|
| `ChallengerModel.java` | Owen Wikel | [fill in] |
| `ChallengerModelTest.java` | Owen Wikel | [fill in] |
| `ChallengerView.java` | [name(s)] | [fill in] |
| `ChallengerController.java` | [name(s)] | [fill in] |
| `GameNightApp.java` | [name(s)] | [fill in] |

### GenAI Note


- **Owen:** []
- **Nishitha:** []
- **Aryan:** []
- **Preston:** []
