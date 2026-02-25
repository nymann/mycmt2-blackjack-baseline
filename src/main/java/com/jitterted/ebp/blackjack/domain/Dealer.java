package com.jitterted.ebp.blackjack.domain;

public class Dealer {
    private final Hand hand = new Hand();

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void dealFrom(Deck deck) {
        receiveCard(deck.draw());
    }

    public void playTurn(Deck deck) {
        while (hand.value() <= 16) {
            hand.drawFrom(deck);
        }
    }

    public Hand hand() {
        return hand;
    }

    public boolean isBusted() {
        return hand.isBusted();
    }
}
