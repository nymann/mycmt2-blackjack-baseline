package com.jitterted.ebp.blackjack.domain;

/**
 * Strategy for deciding whether to hit or stand.
 * Implemented by console prompter (human), dealer AI, etc.
 */
public interface PlayingStrategy {

    Action decide(Hand myHand, Hand opponentHand);
}
