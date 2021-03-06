package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.controls.GameControl;
import it.polimi.ingsw.view.client.controls.PlayerViewState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link VBox} containing god cards and player names
 */
public class PlayerListView extends VBox {
    private final GameControl gameControl;
    private final List<Label> labels = new ArrayList<>();

    public PlayerListView(GameControl gameControl) {
        this.gameControl = gameControl;
        initView();
        bindGameControl();
    }

    private void initView() {
        for (PlayerViewState p : gameControl.getBoardViewState().getPlayers()) {
            this.getChildren().add(playerView(p.getUser(), p.getGod()));
        }
    }

    private void bindGameControl() {
        gameControl.setOnTurnChangeListener(this::onTurnChange);
    }

    private void onTurnChange(Integer integer, User user) {
        highlight(user);
    }

    private void highlight(User user) {
        labels.stream()
                .peek(l -> l.setStyle("-fx-font-size: 16pt;"))
                .filter(l -> l.getText().equals(user.getUsername()))
                .forEach(l -> l.setStyle("-fx-font-size: 16pt; -fx-underline: true;"));
    }

    private Node playerView(User user, GodIdentifier god) {
        ImageView godView = Resources.loadGodCard(this, god.getName())
                .orElse(Resources.loadGodCard(this));
        godView.setPreserveRatio(true);
        godView.fitHeightProperty().bind(
                prefHeightProperty()
                    .divide(gameControl.getBoardViewState().getSize())
                    .multiply(0.70)
        );
        godView.fitWidthProperty().bind(prefWidthProperty());
        Tooltip.install(godView, new Tooltip(god.getName() + "\n" + god.getDescription()));

        Label playerName = new Label(user.getUsername());
        playerName.setStyle("-fx-font-size: 16pt;");
        labels.add(playerName);

        VBox playerView = new VBox(godView, playerName);

        playerView.setAlignment(Pos.CENTER);
        playerView.setPadding(new Insets(10.0));
        return playerView;
    }
}
