package com.jitterted.ebp.blackjack.adapter.out;

import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Outcome;

public class DummyGameDisplay implements GameDisplay {
    @Override
    public void displayWelcomeScreen() {}

    @Override
    public void promptToStart() {}

    @Override
    public void showPlayerTurn(Hand playerHand, Hand dealerHand) {}

    @Override
    public void showFinalHands(Hand playerHand, Hand dealerHand) {}

    @Override
    public void announceOutcome(Outcome outcome) {}

    @Override
    public void resetScreen() {}
}
