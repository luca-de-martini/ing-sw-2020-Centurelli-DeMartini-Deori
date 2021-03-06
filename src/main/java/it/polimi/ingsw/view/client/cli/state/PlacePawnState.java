package it.polimi.ingsw.view.client.cli.state;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.cli.CLIClient;

import java.util.Optional;

/**
 * {@link InputHandlerState} that handles the phase in which a player chooses the starting position for its pawns
 */
public class PlacePawnState implements InputHandlerState {
    @Override
    public void handle(InputHandlerContext ctx, String line) {
        String[] tokens = line.split(" ");

        if(tokens.length == 2) {
            Optional<Coordinate> c1 = CLIClient.decodeCoordinate(tokens[0]);
            Optional<Coordinate> c2 = CLIClient.decodeCoordinate(tokens[1]);
            if (c1.isPresent() && c2.isPresent()) {
                ctx.getGameControl().setOnPlaceAttemptListener(this::onPlaceAttempt);
                ctx.getGameControl().placePawns(c1.get(), c2.get());
            } else {
                System.out.println("Invalid coordinate format!");
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    private void onPlaceAttempt(Boolean result) {
        if (!result) {
            CLI.clientInfo("Invalid starting position for pawns!");
        }
    }

    private void printUsage() {
        System.out.println("Choose a starting position for your pawns\nEXAMPLE:\nA1 E5");
        System.out.flush();
    }
}
