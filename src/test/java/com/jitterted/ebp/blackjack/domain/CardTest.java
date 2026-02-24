package com.jitterted.ebp.blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CardTest {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;

    @Test
    public void withNumberCardHasNumericValueOfTheNumber() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.SEVEN);

        assertThat(card.rankValue()).isEqualTo(7);
    }

    @Test
    public void withValueOfQueenHasNumericValueOf10() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.QUEEN);

        assertThat(card.rankValue()).isEqualTo(10);
    }

    @Test
    public void withAceHasNumericValueOf1() throws Exception {
        Card card = new Card(DUMMY_SUIT, Rank.ACE);

        assertThat(card.rankValue()).isEqualTo(1);
    }

    @Test
    public void cardExposesRankAndSuit() {
        Card card = new Card(Suit.HEARTS, Rank.KING);

        assertThat(card.rank()).isEqualTo(Rank.KING);
        assertThat(card.suit()).isEqualTo(Suit.HEARTS);
        assertThat(card.suit().isRed()).isTrue();
    }
}
