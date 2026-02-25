package com.jitterted.ebp.blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class HandTest {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;

    @Test
    void handValueOf21IsNotBusted() {
        Hand hand = createHand(Rank.TEN, Rank.JACK, Rank.ACE); // 10+10+1=21

        assertThat(hand.isBusted()).isFalse();
    }

    @Test
    void handValueOf22IsBusted() {
        Hand hand = createHand(Rank.TEN, Rank.JACK, Rank.TWO); // 10+10+2=22

        assertThat(hand.isBusted()).isTrue();
    }

    @Test
    void higherValueHandBeatsLowerValueHand() {
        Hand higher = createHand(Rank.TEN, Rank.NINE); // 19
        Hand lower = createHand(Rank.TEN, Rank.EIGHT); // 18

        assertThat(higher.beats(lower)).isTrue();
    }

    @Test
    void lowerValueHandDoesNotBeatHigherValueHand() {
        Hand lower = createHand(Rank.TEN, Rank.EIGHT); // 18
        Hand higher = createHand(Rank.TEN, Rank.NINE); // 19

        assertThat(lower.beats(higher)).isFalse();
    }

    @Test
    void equalValueHandsPush() {
        Hand hand1 = createHand(Rank.TEN, Rank.EIGHT); // 18
        Hand hand2 = createHand(Rank.JACK, Rank.EIGHT); // 18

        assertThat(hand1.pushes(hand2)).isTrue();
    }

    @Test
    void differentValueHandsDoNotPush() {
        Hand hand1 = createHand(Rank.TEN, Rank.EIGHT); // 18
        Hand hand2 = createHand(Rank.TEN, Rank.NINE); // 19

        assertThat(hand1.pushes(hand2)).isFalse();
    }

    private Hand createHand(Rank... ranks) {
        List<Card> cards = new ArrayList<>();
        for (Rank rank : ranks) {
            cards.add(new Card(DUMMY_SUIT, rank));
        }
        return new Hand(cards);
    }
}
