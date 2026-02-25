package com.jitterted.ebp.blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DealerTest {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;

    @Test
    void newDealerHandIsEmpty() {
        Dealer dealer = new Dealer();

        assertThat(dealer.hand().value()).isZero();
    }

    @Test
    void dealerReceivesCards() {
        Dealer dealer = new Dealer();

        dealer.receiveCard(new Card(DUMMY_SUIT, Rank.TEN));
        dealer.receiveCard(new Card(DUMMY_SUIT, Rank.SEVEN));

        assertThat(dealer.hand().valueEquals(17)).isTrue();
    }

    @Test
    void dealerIsBustedWhenHandExceeds21() {
        Dealer dealer = new Dealer();

        dealer.receiveCard(new Card(DUMMY_SUIT, Rank.TEN));
        dealer.receiveCard(new Card(DUMMY_SUIT, Rank.EIGHT));
        dealer.receiveCard(new Card(DUMMY_SUIT, Rank.FIVE));

        assertThat(dealer.isBusted()).isTrue();
    }
}
