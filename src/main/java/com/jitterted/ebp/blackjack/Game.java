package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.ConsolePlayerActionPrompter;
import com.jitterted.ebp.blackjack.adapter.out.ConsoleGameDisplay;
import com.jitterted.ebp.blackjack.application.BlackjackService;
import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.PlayingStrategy;

public class Game {

    static void main() {
        GameDisplay gameDisplay = new ConsoleGameDisplay();
        PlayingStrategy playerStrategy = new ConsolePlayerActionPrompter();
        BlackjackService blackjackService = new BlackjackService(new Deck(), gameDisplay, playerStrategy);
        blackjackService.play();
    }
}
