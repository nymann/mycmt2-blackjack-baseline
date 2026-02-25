package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

class TestHandFactory {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;

    static Hand createHand(Rank... ranks) {
        List<Card> cards = new ArrayList<>();
        for (Rank rank : ranks) {
            cards.add(new Card(DUMMY_SUIT, rank));
        }
        return new Hand(cards);
    }
}
