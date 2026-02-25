package com.jitterted.ebp.blackjack.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.jitterted.ebp.blackjack.adapter.out.SpyGameDisplay;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Outcome;
import com.jitterted.ebp.blackjack.domain.PlayingStrategy;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Suit;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlackjackServiceTest {

    private static final PlayingStrategy ALWAYS_STAND = (playerHand, dealerHand) -> Action.STAND;

    private static final PlayingStrategy ALWAYS_HIT = (playerHand, dealerHand) -> Action.HIT;

    // method ordering
    @Test
    void announcesPlayerBustedWhenPlayerBusts() {
        // Player: 10+8=18, hits, draws 10 -> 28 (busted)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.SEVEN),
                new Card(Suit.HEARTS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN)));
        SpyGameDisplay spy = new SpyGameDisplay();

        BlackjackService service = new BlackjackService(deck, spy, hitThenStand());
        service.play();

        assertThat(spy.announcedOutcome()).isEqualTo(Outcome.PLAYER_BUSTED);
    }

    // -- Outcome announcement --

    @Test
    void announcesDealerBustedWhenDealerBusts() {
        // Player: 10+8=18 (stands). Dealer: 10+6=16, draws 10 -> 26 (busted)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.SIX),
                new Card(Suit.CLUBS, Rank.TEN)));
        SpyGameDisplay spy = new SpyGameDisplay();

        BlackjackService service = new BlackjackService(deck, spy, ALWAYS_STAND);
        service.play();

        assertThat(spy.announcedOutcome()).isEqualTo(Outcome.DEALER_BUSTED);
    }

    @Test
    void announcesPlayerWinsWhenPlayerBeatsDealer() {
        // Player: 10+9=19 (stands). Dealer: 10+8=18 (stands at 18)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.NINE),
                new Card(Suit.SPADES, Rank.EIGHT)));
        SpyGameDisplay spy = new SpyGameDisplay();

        BlackjackService service = new BlackjackService(deck, spy, ALWAYS_STAND);
        service.play();

        assertThat(spy.announcedOutcome()).isEqualTo(Outcome.PLAYER_WINS);
    }

    @Test
    void announcesPushWhenPlayerAndDealerTie() {
        // Player: 10+8=18 (stands). Dealer: 10+8=18 (stands at 18)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.EIGHT)));
        SpyGameDisplay spy = new SpyGameDisplay();

        BlackjackService service = new BlackjackService(deck, spy, ALWAYS_STAND);
        service.play();

        assertThat(spy.announcedOutcome()).isEqualTo(Outcome.PUSH);
    }

    @Test
    void announcesDealerWinsWhenDealerBeatsPlayer() {
        // Player: 10+7=17 (stands). Dealer: 10+8=18 (stands at 18)
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.SPADES, Rank.EIGHT)));
        SpyGameDisplay spy = new SpyGameDisplay();

        BlackjackService service = new BlackjackService(deck, spy, ALWAYS_STAND);
        service.play();

        assertThat(spy.announcedOutcome()).isEqualTo(Outcome.DEALER_WINS);
    }

    @Test
    void dealerSkipsTurnWhenPlayerIsBusted() {
        // Player: 10+8=18, hits, draws 10 -> 28 (busted)
        // Dealer: 6+10=16, would need to draw but shouldn't
        Deck deck = new Deck(List.of(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.SIX),
                new Card(Suit.HEARTS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN)));
        SpyGameDisplay spy = new SpyGameDisplay();

        BlackjackService service = new BlackjackService(deck, spy, hitThenStand());
        service.play();

        // If dealer played their turn with 16, they'd draw and bust -> DEALER_BUSTED
        // But since player busted first, dealer skips -> PLAYER_BUSTED
        assertThat(spy.announcedOutcome()).isEqualTo(Outcome.PLAYER_BUSTED);
    }

    // -- Orchestration --

    private static PlayingStrategy hitThenStand() {
        return new PlayingStrategy() {
            private int calls = 0;

            @Override
            public Action decide(
                    com.jitterted.ebp.blackjack.domain.Hand playerHand,
                    com.jitterted.ebp.blackjack.domain.Hand dealerHand) {
                return calls++ == 0 ? Action.HIT : Action.STAND;
            }
        };
    }
    //
}
