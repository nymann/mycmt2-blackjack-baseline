package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.ConsolePlayerActionPrompter;
import com.jitterted.ebp.blackjack.adapter.out.ConsoleGameDisplay;
import com.jitterted.ebp.blackjack.application.BlackjackService;
import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayerActionPrompter;
import com.jitterted.ebp.blackjack.domain.Deck;

public class Game {

    static void main() {
        GameDisplay gameDisplay = new ConsoleGameDisplay();
        PlayerActionPrompter playerActionPrompter = new ConsolePlayerActionPrompter();
        BlackjackService blackjackService = new BlackjackService(new Deck(), gameDisplay, playerActionPrompter);
        blackjackService.play();
    }
}
