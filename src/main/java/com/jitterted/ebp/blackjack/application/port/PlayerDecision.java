package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Hand;

/**
 * Input port: how player decisions enter the game.
 * Adapters implement this to get decisions from console, AI, web, etc.
 */
public interface PlayerDecision {

    Action decide(Hand playerHand, Hand dealerHand);
}
