package com.jitterted.ebp.blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;

    @Test
    void newPlayerHandIsEmpty() {
        Player player = new Player();
        assertThat(player.hand().valueEquals(0)).isTrue();
    }

    @Test
    void playerReceivesCardsFromDeck() {
        Deck deck = new Deck(List.of(new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.FIVE)));

        Player player = new Player();
        player.receiveCard(deck.draw());
        player.receiveCard(deck.draw());

        assertThat(player.hand().valueEquals(15)).isTrue();
    }

    @Test
    void playerIsBustedWhenHandExceeds21() {
        Deck deck = new Deck(List.of(
                new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.QUEEN), new Card(DUMMY_SUIT, Rank.FIVE)));

        Player player = new Player();
        player.receiveCard(deck.draw());
        player.receiveCard(deck.draw());
        player.receiveCard(deck.draw());

        assertThat(player.isBusted()).isTrue();
    }

    @Test
    void playerIsNotBustedAt21() {
        Deck deck = new Deck(List.of(new Card(DUMMY_SUIT, Rank.TEN), new Card(DUMMY_SUIT, Rank.ACE)));

        Player player = new Player();
        player.receiveCard(deck.draw());
        player.receiveCard(deck.draw());

        assertThat(player.isBusted()).isFalse();
    }
}
