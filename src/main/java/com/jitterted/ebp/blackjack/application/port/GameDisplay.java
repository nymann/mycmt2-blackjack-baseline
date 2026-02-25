package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Outcome;

/**
 * Output port: how game events leave the domain.
 * Adapters implement this to render on console, GUI, web, etc.
 */
public interface GameDisplay {

    void displayWelcomeScreen();

    void promptToStart();

    void showPlayerTurn(Hand playerHand, Hand dealerHand);

    void showDealerTurn(Hand playerHand, Hand dealerHand);

    void showFinalHands(Hand playerHand, Hand dealerHand);

    void announceOutcome(Outcome outcome);

    void resetScreen();
}
