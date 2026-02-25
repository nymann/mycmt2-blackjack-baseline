# Inbox

- A Dealer is just a Player with a specific automated playing strategy — refactor to Strategy pattern so we just have Players
- Move more responsibilities to Dealer (e.g. dealing during player hits?) — Dealer deals, BlackjackService is just the orchestrator
- Extract a `Play` class to unify playerTurn/dealerTurn — takes an Actor, PlayingStrategy, and a display. Each Play encapsulates the turn loop: while not busted, make one play. Both player and dealer have display needs (player sees their options, dealer reveals the hole card)
