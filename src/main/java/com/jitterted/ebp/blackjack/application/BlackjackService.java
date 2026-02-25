package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayerDecisionDecider;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Dealer;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Outcome;
import com.jitterted.ebp.blackjack.domain.Player;

public class BlackjackService {

    private final Deck deck;
    private final GameDisplay gameDisplay;
    private final PlayerDecisionDecider playerDecisionDecider;
    private final Dealer dealer = new Dealer();
    private final Player player = new Player();

    public BlackjackService(Deck deck, GameDisplay gameDisplay, PlayerDecisionDecider playerDecisionDecider) {
        this.deck = deck;
        this.gameDisplay = gameDisplay;
        this.playerDecisionDecider = playerDecisionDecider;
    }

    public void play() {
        gameDisplay.displayWelcomeScreen();
        gameDisplay.promptToStart();

        initialDeal();

        playerTurn();
        dealerTurn();

        gameDisplay.showFinalHands(player.hand(), dealer.hand());
        determineOutcome();

        gameDisplay.resetScreen();
    }

    private void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        player.receiveCard(deck.draw());
        dealer.dealFrom(deck);
    }

    private void playerTurn() {
        while (!player.isBusted()) {
            gameDisplay.showPlayerTurn(player.hand(), dealer.hand());
            Action action = playerDecisionDecider.decide(player.hand(), dealer.hand());
            if (action == Action.STAND) {
                break;
            }
            player.receiveCard(deck.draw());
        }
    }

    private void dealerTurn() {
        if (!player.isBusted()) {
            dealer.playTurn(deck);
        }
    }

    private void determineOutcome() {
        Outcome outcome = Outcome.determine(player.hand(), dealer.hand());
        gameDisplay.announceOutcome(outcome);
    }
}
