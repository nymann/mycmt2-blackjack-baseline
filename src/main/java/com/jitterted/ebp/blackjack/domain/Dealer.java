package com.jitterted.ebp.blackjack.domain;

public class Dealer implements Actor {
    private final Hand hand = new Hand();

    @Override
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void initialDeal(Deck deck, Actor actor) {
        dealRoundOfCards(deck, actor);
        dealRoundOfCards(deck, actor);
    }

    @Override
    public Hand hand() {
        return hand;
    }

    @Override
    public boolean isBusted() {
        return hand.isBusted();
    }

    private void dealRoundOfCards(Deck deck, Actor actor) {
        actor.receiveCard(deck.draw());
        this.receiveCard(deck.draw());
    }
}
