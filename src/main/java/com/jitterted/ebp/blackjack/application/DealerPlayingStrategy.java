package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.application.port.PlayingStrategy;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Hand;

public class DealerPlayingStrategy implements PlayingStrategy {
    @Override
    public Action decide(Hand myHand, Hand opponentHand) {
        if (myHand.value() <= 16) {
            return Action.HIT;
        }
        return Action.STAND;
    }
}
