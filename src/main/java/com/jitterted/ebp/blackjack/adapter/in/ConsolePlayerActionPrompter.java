package com.jitterted.ebp.blackjack.adapter.in;

import com.jitterted.ebp.blackjack.application.port.PlayingStrategy;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Hand;
import java.util.Locale;
import java.util.Scanner;

public class ConsolePlayerActionPrompter implements PlayingStrategy {

    @Override
    public Action decide(Hand myHand, Hand opponentHand) {
        while (true) {
            System.out.println("[H]it or [S]tand?");
            Scanner scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
            String input = scanner.nextLine().toLowerCase(Locale.ROOT);
            if (input.startsWith("h")) {
                return Action.HIT;
            }
            if (input.startsWith("s")) {
                return Action.STAND;
            }
            System.out.println("You need to [H]it or [S]tand");
        }
    }
}
