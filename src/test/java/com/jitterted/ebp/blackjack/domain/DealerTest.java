package com.jitterted.ebp.blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class DealerTest {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;

    @Test
    void dealerHitsWhenHandValueIs16OrLess() {
        // Dealer has 10+6=16, draws 2 -> total 18 -> stands
        Deck deck = new Deck(List.of(
                new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.SIX), new Card(DUMMY_SUIT, Rank.TWO)));

        Dealer dealer = new Dealer();
        dealer.receiveCard(deck.draw());
        dealer.receiveCard(deck.draw());
        dealer.playTurn(deck, new Hand());

        assertThat(dealer.hand().valueEquals(18)).isTrue();
    }

    @Test
    void dealerStandsWhenHandValueIs17() {
        Deck deck = new Deck(List.of(
                new Card(DUMMY_SUIT, Rank.TEN),
                new Card(DUMMY_SUIT, Rank.SEVEN),
                new Card(DUMMY_SUIT, Rank.ACE))); // should NOT be drawn

        Dealer dealer = new Dealer();
        dealer.receiveCard(deck.draw());
        dealer.receiveCard(deck.draw());
        dealer.playTurn(deck, new Hand());

        assertThat(dealer.hand().valueEquals(17)).isTrue();
    }

    @Test
    void dealerHitsMultipleTimesUntilStanding() {
        // Dealer has 2+3=5, draws 4->9, draws 3->12, draws 6->18 -> stands
        Deck deck = new Deck(List.of(
                new Card(DUMMY_SUIT, Rank.TWO),
                new Card(DUMMY_SUIT, Rank.THREE),
                new Card(DUMMY_SUIT, Rank.FOUR),
                new Card(DUMMY_SUIT, Rank.THREE),
                new Card(DUMMY_SUIT, Rank.SIX)));

        Dealer dealer = new Dealer();
        dealer.receiveCard(deck.draw());
        dealer.receiveCard(deck.draw());
        dealer.playTurn(deck, new Hand());

        assertThat(dealer.hand().valueEquals(18)).isTrue();
    }

    @Test
    void dealerCanBust() {
        // Dealer has 10+6=16, draws 10 -> 26 -> busted
        Deck deck = new Deck(List.of(
                new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.SIX), new Card(DUMMY_SUIT, Rank.TEN)));

        Dealer dealer = new Dealer();
        dealer.receiveCard(deck.draw());
        dealer.receiveCard(deck.draw());
        dealer.playTurn(deck, new Hand());

        assertThat(dealer.isBusted()).isTrue();
    }
}
