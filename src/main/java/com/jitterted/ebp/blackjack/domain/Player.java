package com.jitterted.ebp.blackjack.domain;

public class Player implements Actor {
    private final Hand hand = new Hand();

    @Override
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    @Override
    public boolean isBusted() {
        return hand.isBusted();
    }

    @Override
    public Hand hand() {
        return hand;
    }
}
