package com.jitterted.ebp.blackjack.application;

import static org.assertj.core.api.Assertions.*;

import com.jitterted.ebp.blackjack.adapter.out.DummyGameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayerDecisionDecider;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlackjackServiceTest {

    private static final PlayerDecisionDecider ALWAYS_STAND = (playerHand, dealerHand) -> Action.STAND;

    // -- Characterization: initialDeal --

    @Test
    void initialDealGivesPlayerAndDealerTwoCardsEach() {
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TWO), // player 1
                new Card(Suit.SPADES, Rank.THREE), // dealer 1
                new Card(Suit.HEARTS, Rank.FOUR), // player 2
                new Card(Suit.SPADES, Rank.FIVE) // dealer 2
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();

        assertThat(service.playerHand().valueEquals(2 + 4)).isTrue();
        assertThat(service.dealerHand().valueEquals(3 + 5)).isTrue();
    }

    // -- Characterization: dealerTurn --

    @Test
    void dealerDrawsWhenHandValueIs16OrLess() {
        // Dealer starts with 10+6=16, must hit, draws 2 -> total 18 -> stands
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.TEN), // dealer 1
                new Card(Suit.HEARTS, Rank.EIGHT), // player 2
                new Card(Suit.SPADES, Rank.SIX), // dealer 2
                new Card(Suit.CLUBS, Rank.TWO) // dealer draws this (16 -> must hit)
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.dealerTurn();

        // Dealer should have drawn: 10 + 6 + 2 = 18
        assertThat(service.dealerHand().valueEquals(18)).isTrue();
    }

    @Test
    void dealerStandsWhenHandValueIs17OrMore() {
        // Dealer starts with 10+7=17, must stand
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.TEN), // dealer 1
                new Card(Suit.HEARTS, Rank.EIGHT), // player 2
                new Card(Suit.SPADES, Rank.SEVEN) // dealer 2
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.dealerTurn();

        // Dealer should stand at 17
        assertThat(service.dealerHand().valueEquals(17)).isTrue();
    }

    @Test
    void dealerSkipsTurnWhenPlayerIsBusted() {
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.SIX), // dealer 1 (dealer has 6+10=16, would draw)
                new Card(Suit.HEARTS, Rank.KING), // player 2
                new Card(Suit.SPADES, Rank.TEN), // dealer 2
                new Card(Suit.CLUBS, Rank.FIVE) // would be dealer's draw if not skipped
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();

        // Player has 10+K(10)=20, not busted.
        // To characterize the skip behavior, we need a busted player.
        // Let's draw an extra card to bust the player.
        service.playerHand().addCard(new Card(Suit.DIAMONDS, Rank.FIVE));
        assertThat(service.playerHand().isBusted()).isTrue(); // 20+5=25

        service.dealerTurn();

        // Dealer should NOT have drawn (skipped) - still 6+10=16
        assertThat(service.dealerHand().valueEquals(16)).isTrue();
    }

    // -- Characterization: outcome determination --

    @Test
    void playerBustsAndLoses() {
        // Player: 10 + 10 + 5 = 25 (busted)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.SEVEN), // dealer 1
                new Card(Suit.HEARTS, Rank.QUEEN), // player 2
                new Card(Suit.SPADES, Rank.TEN) // dealer 2
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.playerHand().addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertThat(service.playerHand().isBusted()).isTrue();
    }

    @Test
    void dealerBustsAndPlayerWins() {
        // Player: 10+8=18. Dealer: 10+6=16, draws 10 -> 26 (busted)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.TEN), // dealer 1
                new Card(Suit.HEARTS, Rank.EIGHT), // player 2
                new Card(Suit.SPADES, Rank.SIX), // dealer 2
                new Card(Suit.CLUBS, Rank.TEN) // dealer draws this
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.dealerTurn();

        assertThat(service.dealerHand().isBusted()).isTrue();
        assertThat(service.playerHand().isBusted()).isFalse();
    }

    @Test
    void playerBeatsDealer() {
        // Player: 10+9=19. Dealer: 10+8=18.
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.TEN), // dealer 1
                new Card(Suit.HEARTS, Rank.NINE), // player 2
                new Card(Suit.SPADES, Rank.EIGHT) // dealer 2
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.dealerTurn();

        assertThat(service.playerHand().beats(service.dealerHand())).isTrue();
    }

    @Test
    void playerAndDealerPush() {
        // Player: 10+8=18. Dealer: 10+8=18.
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.TEN), // dealer 1
                new Card(Suit.HEARTS, Rank.EIGHT), // player 2
                new Card(Suit.SPADES, Rank.EIGHT) // dealer 2
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.dealerTurn();

        assertThat(service.playerHand().pushes(service.dealerHand())).isTrue();
    }

    @Test
    void dealerBeatsPlayer() {
        // Player: 10+7=17. Dealer: 10+8=18.
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.TEN), // dealer 1
                new Card(Suit.HEARTS, Rank.SEVEN), // player 2
                new Card(Suit.SPADES, Rank.EIGHT) // dealer 2
                ));

        BlackjackService service = new BlackjackService(deck, new DummyGameDisplay(), ALWAYS_STAND);
        service.initialDeal();
        service.dealerTurn();

        assertThat(service.playerHand().isBusted()).isFalse();
        assertThat(service.dealerHand().isBusted()).isFalse();
        assertThat(service.playerHand().beats(service.dealerHand())).isFalse();
        assertThat(service.playerHand().pushes(service.dealerHand())).isFalse();
    }
}
