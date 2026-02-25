package com.jitterted.ebp.blackjack.domain;

public class Dealer implements Actor {
    private final Hand hand = new Hand();

    @Override
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void playTurn(Deck deck) {
        while (hand.value() <= 16) {
            hand.drawFrom(deck);
        }
    }

    @Override
    public Hand hand() {
        return hand;
    }

    @Override
    public boolean isBusted() {
        return hand.isBusted();
    }
}
