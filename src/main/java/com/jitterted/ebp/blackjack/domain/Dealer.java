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

    public void playTurn(Deck deck, Hand opponentHand) {
        DealerPlayingStrategy strategy = new DealerPlayingStrategy();
        while (strategy.decide(hand, opponentHand) == Action.HIT) {
            receiveCard(deck.draw());
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

    private void dealRoundOfCards(Deck deck, Actor actor) {
        actor.receiveCard(deck.draw());
        this.receiveCard(deck.draw());
    }
}
