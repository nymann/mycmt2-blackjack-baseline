package com.jitterted.ebp.blackjack.application;

import static org.assertj.core.api.Assertions.*;

import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import java.util.List;
import org.junit.jupiter.api.Test;

class DealerPlayingStrategyTest {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;
    private final DealerPlayingStrategy strategy = new DealerPlayingStrategy();

    @Test
    void hitsWhenHandValueIs16OrLess() {
        Hand hand = new Hand(List.of(new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.SIX)));

        assertThat(strategy.decide(hand, new Hand())).isEqualTo(Action.HIT);
    }

    @Test
    void standsWhenHandValueIs17() {
        Hand hand = new Hand(List.of(new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.SEVEN)));

        assertThat(strategy.decide(hand, new Hand())).isEqualTo(Action.STAND);
    }

    @Test
    void standsWhenHandValueIsAbove17() {
        Hand hand = new Hand(List.of(new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.NINE)));

        assertThat(strategy.decide(hand, new Hand())).isEqualTo(Action.STAND);
    }
}
