package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayingStrategy;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Dealer;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Outcome;
import com.jitterted.ebp.blackjack.domain.Player;

public class BlackjackService {

    private final Deck deck;
    private final GameDisplay gameDisplay;
    private final PlayingStrategy playerStrategy;
    private final Dealer dealer = new Dealer();
    private final Player player = new Player();

    public BlackjackService(Deck deck, GameDisplay gameDisplay, PlayingStrategy playerStrategy) {
        this.deck = deck;
        this.gameDisplay = gameDisplay;
        this.playerStrategy = playerStrategy;
    }

    public void play() {
        gameDisplay.displayWelcomeScreen();
        gameDisplay.promptToStart();

        dealer.initialDeal(deck, player);

        playerTurn();
        dealerTurn();

        gameDisplay.showFinalHands(player.hand(), dealer.hand());

        Outcome outcome = Outcome.determine(player.hand(), dealer.hand());
        gameDisplay.announceOutcome(outcome);

        gameDisplay.resetScreen();
    }

    private void playerTurn() {
        while (!player.isBusted()) {
            gameDisplay.showPlayerTurn(player.hand(), dealer.hand());
            Action action = playerStrategy.decide(player.hand(), dealer.hand());
            if (action == Action.STAND) {
                break;
            }
            player.receiveCard(deck.draw());
        }
    }

    private void dealerTurn() {
        if (!player.isBusted()) {
            PlayingStrategy dealerStrategy = new DealerPlayingStrategy();
            while (!dealer.isBusted()) {
                Action action = dealerStrategy.decide(dealer.hand(), player.hand());
                if (action == Action.STAND) {
                    break;
                }
                dealer.receiveCard(deck.draw());
            }
        }
    }
}
