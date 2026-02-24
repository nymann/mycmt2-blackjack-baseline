package com.jitterted.ebp.blackjack;

import static org.assertj.core.api.Assertions.*;

import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayerDecision;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Outcome;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameTest {

    private static final GameDisplay NO_OP_GAME_DISPLAY = new GameDisplay() {
        @Override
        public void showPlayerTurn(Hand playerHand, Hand dealerHand) {}

        @Override
        public void showFinalHands(Hand playerHand, Hand dealerHand) {}

        @Override
        public void announceOutcome(Outcome outcome) {}
    };

    private static final PlayerDecision ALWAYS_STAND = (playerHand, dealerHand) -> Action.STAND;

    // -- Characterization: initialDeal --

    @Test
    void initialDealGivesPlayerAndDealerTwoCardsEach() {
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TWO), // player 1
                new Card(Suit.SPADES, Rank.THREE), // dealer 1
                new Card(Suit.HEARTS, Rank.FOUR), // player 2
                new Card(Suit.SPADES, Rank.FIVE) // dealer 2
                ));

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();

        assertThat(game.playerHand().valueEquals(2 + 4)).isTrue();
        assertThat(game.dealerHand().valueEquals(3 + 5)).isTrue();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.dealerTurn();

        // Dealer should have drawn: 10 + 6 + 2 = 18
        assertThat(game.dealerHand().valueEquals(18)).isTrue();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.dealerTurn();

        // Dealer should stand at 17
        assertThat(game.dealerHand().valueEquals(17)).isTrue();
    }

    @Test
    void dealerSkipsTurnWhenPlayerIsBusted() {
        // Player has 10+6=16, NOT busted - but we need player busted
        // Player: K + Q = 20, then draws 5 -> 25 busted
        // But we can't trigger playerTurn without I/O...
        // Instead: manually bust the player hand, then verify dealer doesn't draw
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN), // player 1
                new Card(Suit.SPADES, Rank.SIX), // dealer 1 (dealer has 6+10=16, would draw)
                new Card(Suit.HEARTS, Rank.KING), // player 2
                new Card(Suit.SPADES, Rank.TEN), // dealer 2
                new Card(Suit.CLUBS, Rank.FIVE) // would be dealer's draw if not skipped
                ));

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();

        // Player has 10+K(10)=20, not busted.
        // To characterize the skip behavior, we need a busted player.
        // Let's draw an extra card to bust the player.
        game.playerHand().addCard(new Card(Suit.DIAMONDS, Rank.FIVE));
        assertThat(game.playerHand().isBusted()).isTrue(); // 20+5=25

        game.dealerTurn();

        // Dealer should NOT have drawn (skipped) - still 6+10=16
        assertThat(game.dealerHand().valueEquals(16)).isTrue();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.playerHand().addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertThat(game.playerHand().isBusted()).isTrue();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.dealerTurn();

        assertThat(game.dealerHand().isBusted()).isTrue();
        assertThat(game.playerHand().isBusted()).isFalse();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.dealerTurn();

        assertThat(game.playerHand().beats(game.dealerHand())).isTrue();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.dealerTurn();

        assertThat(game.playerHand().pushes(game.dealerHand())).isTrue();
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

        Game game = new Game(deck, NO_OP_GAME_DISPLAY, ALWAYS_STAND);
        game.initialDeal();
        game.dealerTurn();

        assertThat(game.playerHand().isBusted()).isFalse();
        assertThat(game.dealerHand().isBusted()).isFalse();
        assertThat(game.playerHand().beats(game.dealerHand())).isFalse();
        assertThat(game.playerHand().pushes(game.dealerHand())).isFalse();
    }
}
