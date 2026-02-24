package com.jitterted.ebp.blackjack;

import static org.fusesource.jansi.Ansi.ansi;

import com.jitterted.ebp.blackjack.adapter.in.ConsolePlayerDecision;
import com.jitterted.ebp.blackjack.adapter.out.ConsoleGameDisplay;
import com.jitterted.ebp.blackjack.application.port.GameDisplay;
import com.jitterted.ebp.blackjack.application.port.PlayerDecision;
import com.jitterted.ebp.blackjack.domain.Action;
import com.jitterted.ebp.blackjack.domain.Dealer;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Outcome;
import com.jitterted.ebp.blackjack.domain.Player;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Game {

    private final Deck deck;
    private final GameDisplay gameDisplay;
    private final PlayerDecision playerDecision;
    private final Dealer dealer = new Dealer();
    private final Player player = new Player();

    static void main() {
        AnsiConsole.systemInstall();
        displayWelcomeScreen();
        waitForEnterFromUser();

        GameDisplay gameDisplay = new ConsoleGameDisplay();
        PlayerDecision playerDecision = new ConsolePlayerDecision();
        Game game = new Game(new Deck(), gameDisplay, playerDecision);
        game.initialDeal();
        game.play();

        resetScreen();
    }

    private static void resetScreen() {
        System.out.println(ansi().reset());
    }

    private static void waitForEnterFromUser() {
        System.out.println(ansi().cursor(3, 1).fgBrightBlack().a("Hit [ENTER] to start..."));

        java.io.Console console = System.console();
        if (console != null) {
            console.readLine();
        }
    }

    private static void displayWelcomeScreen() {
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

    public Game(Deck deck, GameDisplay gameDisplay, PlayerDecision playerDecision) {
        this.deck = deck;
        this.gameDisplay = gameDisplay;
        this.playerDecision = playerDecision;
    }

    Hand playerHand() {
        return player.hand();
    }

    Hand dealerHand() {
        return dealer.hand();
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    public void play() {
        playerTurn();

        dealerTurn();

        gameDisplay.showFinalHands(player.hand(), dealer.hand());

        determineOutcome();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        player.receiveCard(deck.draw());
        dealer.dealFrom(deck);
    }

    private void determineOutcome() {
        if (player.isBusted()) {
            gameDisplay.announceOutcome(Outcome.PLAYER_BUSTED);
        } else if (dealer.isBusted()) {
            gameDisplay.announceOutcome(Outcome.DEALER_BUSTED);
        } else if (player.hand().beats(dealer.hand())) {
            gameDisplay.announceOutcome(Outcome.PLAYER_WINS);
        } else if (player.hand().pushes(dealer.hand())) {
            gameDisplay.announceOutcome(Outcome.PUSH);
        } else {
            gameDisplay.announceOutcome(Outcome.DEALER_WINS);
        }
    }

    void dealerTurn() {
        if (!player.isBusted()) {
            dealer.playTurn(deck);
        }
    }

    private void playerTurn() {
        while (!player.isBusted()) {
            gameDisplay.showPlayerTurn(player.hand(), dealer.hand());
            Action action = playerDecision.decide(player.hand(), dealer.hand());
            if (action == Action.STAND) {
                break;
            }
            player.receiveCard(deck.draw());
        }
    }
}
