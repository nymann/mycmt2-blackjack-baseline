package com.jitterted.ebp.blackjack.domain;

public class Player {
    private final Hand hand = new Hand();

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public boolean isBusted() {
        return hand.isBusted();
    }

    public Hand hand() {
        return hand;
    }
}
