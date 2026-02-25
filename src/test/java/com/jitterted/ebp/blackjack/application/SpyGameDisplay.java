package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Outcome;

class SpyGameDisplay implements GameDisplay {

    private Outcome announcedOutcome = Outcome.PUSH; // overwritten by announceOutcome()
    private boolean outcomeCaptured = false;

    @Override
    public void announceOutcome(Outcome outcome) {
        this.announcedOutcome = outcome;
        this.outcomeCaptured = true;
    }

    Outcome announcedOutcome() {
        if (!outcomeCaptured) {
            throw new IllegalStateException("announceOutcome was never called");
        }
        return announcedOutcome;
    }

    @Override
    public void displayWelcomeScreen() {}

    @Override
    public void promptToStart() {}

    @Override
    public void showPlayerTurn(Hand playerHand, Hand dealerHand) {}

    @Override
    public void showFinalHands(Hand playerHand, Hand dealerHand) {}

    @Override
    public void resetScreen() {}
}
