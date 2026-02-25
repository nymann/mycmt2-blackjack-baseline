package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.ConsolePlayerDecisionDecider;
import com.jitterted.ebp.blackjack.adapter.out.ConsoleGameDisplay;
import com.jitterted.ebp.blackjack.application.BlackjackService;
import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayerDecisionDecider;
import com.jitterted.ebp.blackjack.domain.Deck;

public class Game {

    static void main() {
        GameDisplay gameDisplay = new ConsoleGameDisplay();
        PlayerDecisionDecider playerDecisionDecider = new ConsolePlayerDecisionDecider();
        BlackjackService blackjackService = new BlackjackService(new Deck(), gameDisplay, playerDecisionDecider);
        blackjackService.play();
    }
}
