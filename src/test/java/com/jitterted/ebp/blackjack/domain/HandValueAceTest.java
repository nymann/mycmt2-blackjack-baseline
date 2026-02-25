package com.jitterted.ebp.blackjack.domain;

import static com.jitterted.ebp.blackjack.domain.TestHandFactory.createHand;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HandValueAceTest {

    @Test
    public void handWithOneAceAndOtherCardValuedLessThan10ThenAceIsValuedAt11() throws Exception {
        Hand hand = createHand(Rank.ACE, Rank.FIVE);

        assertThat(hand.valueEquals(11 + 5)).isTrue();
    }

    @Test
    public void handWithOneAceAndOtherCardsValuedAt10ThenAceIsValuedAt11() throws Exception {
        Hand hand = createHand(Rank.ACE, Rank.TEN);

        assertThat(hand.valueEquals(11 + 10)).isTrue();
    }

    @Test
    public void handWithOneAceAndOtherCardsValuedAs11ThenAceIsValuedAt1() throws Exception {
        Hand hand = createHand(Rank.ACE, Rank.EIGHT, Rank.THREE);

        assertThat(hand.valueEquals(1 + 8 + 3)).isTrue();
    }
}
