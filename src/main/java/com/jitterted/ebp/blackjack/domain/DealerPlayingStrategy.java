package com.jitterted.ebp.blackjack.domain;

public class DealerPlayingStrategy implements PlayingStrategy {
    @Override
    public Action decide(Hand myHand, Hand opponentHand) {
        if (myHand.value() <= 16) {
            return Action.HIT;
        }
        return Action.STAND;
    }
}
