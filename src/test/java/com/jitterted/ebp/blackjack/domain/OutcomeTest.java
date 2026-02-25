package com.jitterted.ebp.blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class OutcomeTest {

    private static final Suit DUMMY_SUIT = Suit.CLUBS;

    @Test
    void playerBustedReturnPlayerBusted() {
        Hand playerHand = createHand(Rank.TEN, Rank.QUEEN, Rank.FIVE); // 25
        Hand dealerHand = createHand(Rank.TEN, Rank.EIGHT); // 18

        assertThat(Outcome.determine(playerHand, dealerHand)).isEqualTo(Outcome.PLAYER_BUSTED);
    }

    @Test
    void dealerBustedReturnDealerBusted() {
        Hand dealerHand = createHand(Rank.TEN, Rank.QUEEN, Rank.FIVE); // 25
        Hand playerHand = createHand(Rank.TEN, Rank.EIGHT); // 18

        Outcome outcome = Outcome.determine(playerHand, dealerHand);

        assertThat(outcome).isEqualTo(Outcome.DEALER_BUSTED);
    }

    @Test
    void playerHigherThanDealerReturnPlayerWins() {
        Hand playerHand = createHand(Rank.TEN, Rank.EIGHT);
        Hand dealerHand = createHand(Rank.TEN, Rank.SEVEN);

        Outcome outcome = Outcome.determine(playerHand, dealerHand);

        assertThat(outcome).isEqualTo(Outcome.PLAYER_WINS);
    }

    @Test
    void sameValueReturnPush() {
        Hand playerHand = createHand(Rank.TEN, Rank.NINE);
        Hand dealerHand = createHand(Rank.TEN, Rank.NINE);

        Outcome outcome = Outcome.determine(playerHand, dealerHand);

        assertThat(outcome).isEqualTo(Outcome.PUSH);
    }

    @Test
    void dealerHigherThanPlayerReturnDealerWins() {
        Hand playerHand = createHand(Rank.TEN, Rank.NINE);
        Hand dealerHand = createHand(Rank.TEN, Rank.JACK);

        Outcome outcome = Outcome.determine(playerHand, dealerHand);

        assertThat(outcome).isEqualTo(Outcome.DEALER_WINS);
    }

    private Hand createHand(Rank... ranks) {
        List<Card> cards = new ArrayList<>();
        for (Rank rank : ranks) {
            cards.add(new Card(DUMMY_SUIT, rank));
        }
        return new Hand(cards);
    }
}
