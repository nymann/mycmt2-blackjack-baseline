package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Hand;

/**
 * Strategy for deciding whether to hit or stand.
 * Implemented by console prompter (human), dealer AI, etc.
 */
public interface PlayingStrategy {

    Action decide(Hand playerHand, Hand dealerHand);
}
