package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;

/**
 * {@link BoardClickHandlerState} in which a player must wait for another player's actions
 */
public class WaitingState implements BoardClickHandlerState {
    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        ctx.getGameView().getInfoLabel().setText("Wait for your turn!");
    }

    @Override
    public void initState(BoardClickHandlerContext ctx) {
        Platform.runLater(()-> ctx.getGameView().getInfoLabel().setText("Wait for your turn!"));
        ctx.getGameControl().requestRedraw();
    }
}
