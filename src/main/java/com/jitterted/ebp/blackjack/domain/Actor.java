package com.jitterted.ebp.blackjack.domain;

public interface Actor {
    void receiveCard(Card card);

    Hand hand();

    boolean isBusted();
}
