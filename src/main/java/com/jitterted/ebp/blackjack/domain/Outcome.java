package com.jitterted.ebp.blackjack.domain;

public enum Outcome {
    PLAYER_BUSTED,
    DEALER_BUSTED,
    PLAYER_WINS,
    PUSH,
    DEALER_WINS;

    public static Outcome determine(Hand playerHand, Hand dealerHand) {
        if (playerHand.isBusted()) {
            return PLAYER_BUSTED;
        } else if (dealerHand.isBusted()) {
            return DEALER_BUSTED;
        } else if (playerHand.beats(dealerHand)) {
            return PLAYER_WINS;
        } else if (playerHand.pushes(dealerHand)) {
            return PUSH;
        } else {
            return DEALER_WINS;
        }
    }
}
