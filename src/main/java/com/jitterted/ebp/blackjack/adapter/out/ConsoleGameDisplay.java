package com.jitterted.ebp.blackjack.adapter.out;

import static org.fusesource.jansi.Ansi.ansi;

import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Outcome;
import com.jitterted.ebp.blackjack.domain.Rank;
import java.util.stream.Collectors;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class ConsoleGameDisplay implements GameDisplay {

    public ConsoleGameDisplay() {
        AnsiConsole.systemInstall();
    }

    @Override
    public void displayWelcomeScreen() {
        System.out.println(ansi().bgBright(Ansi.Color.WHITE)
                .eraseScreen()
                .cursor(1, 1)
                .fgGreen()
                .a("Welcome to")
                .fgRed()
                .a(" JitterTed's")
                .fgBlack()
                .a(" BlackJack game"));
    }

    @Override
    public void promptToStart() {
        System.out.println(ansi().cursor(3, 1).fgBrightBlack().a("Hit [ENTER] to start..."));

        java.io.Console console = System.console();
        if (console != null) {
            console.readLine();
        }
    }

    @Override
    public void showPlayerTurn(Hand playerHand, Hand dealerHand) {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(displayCard(dealerHand.faceUpCard()));

        displayBackOfCard();

        System.out.println();
        System.out.println("Player has: ");
        displayHand(playerHand);
        System.out.println(" (" + playerHand.value() + ")");
    }

    @Override
    public void showFinalHands(Hand playerHand, Hand dealerHand) {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        displayHand(dealerHand);
        System.out.println(" (" + dealerHand.value() + ")");

        System.out.println();
        System.out.println("Player has: ");
        displayHand(playerHand);
        System.out.println(" (" + playerHand.value() + ")");
    }

    @Override
    public void announceOutcome(Outcome outcome) {
        String message =
                switch (outcome) {
                    case PLAYER_BUSTED -> "You Busted, so you lose.  \uD83D\uDCB8";
                    case DEALER_BUSTED -> "Dealer went BUST, Player wins! Yay for you!! \uD83D\uDCB5";
                    case PLAYER_WINS -> "You beat the Dealer! \uD83D\uDCB5";
                    case PUSH -> "Push: Nobody wins, we'll call it even.";
                    case DEALER_WINS -> "You lost to the Dealer. \uD83D\uDCB8";
                };
        System.out.println(message);
    }

    @Override
    public void resetScreen() {
        System.out.println(ansi().reset());
    }

    private void displayHand(Hand hand) {
        System.out.println(hand.cards().stream()
                .map(this::displayCard)
                .collect(Collectors.joining(ansi().cursorUp(6).cursorRight(1).toString())));
    }

    private String displayCard(Card card) {
        String[] lines = new String[7];
        lines[0] = "┌─────────┐";
        lines[1] = String.format("│%s%s       │", card.rank().display(), card.rank() == Rank.TEN ? "" : " ");
        lines[2] = "│         │";
        lines[3] = String.format("│    %s    │", card.suit().symbol());
        lines[4] = "│         │";
        lines[5] = String.format(
                "│       %s%s│", card.rank() == Rank.TEN ? "" : " ", card.rank().display());
        lines[6] = "└─────────┘";

        Ansi.Color cardColor = card.suit().isRed() ? Ansi.Color.RED : Ansi.Color.BLACK;
        return ansi().fg(cardColor).toString()
                + String.join(ansi().cursorDown(1).cursorLeft(11).toString(), lines);
    }

    private void displayBackOfCard() {
        System.out.print(ansi().cursorUp(7)
                .cursorRight(12)
                .a("┌─────────┐")
                .cursorDown(1)
                .cursorLeft(11)
                .a("│░░░░░░░░░│")
                .cursorDown(1)
                .cursorLeft(11)
                .a("│░ J I T ░│")
                .cursorDown(1)
                .cursorLeft(11)
                .a("│░ T E R ░│")
                .cursorDown(1)
                .cursorLeft(11)
                .a("│░ T E D ░│")
                .cursorDown(1)
                .cursorLeft(11)
                .a("│░░░░░░░░░│")
                .cursorDown(1)
                .cursorLeft(11)
                .a("└─────────┘"));
    }
}
