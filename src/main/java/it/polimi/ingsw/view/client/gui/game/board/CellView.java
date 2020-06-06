package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.view.client.controls.CellViewState;
import it.polimi.ingsw.view.client.controls.PawnViewState;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.function.Function;

public class CellView extends StackPane {
    private final DoubleProperty cellWidthProperty = new SimpleDoubleProperty();
    private final Function<PawnViewState, ImageView> pawnRenderer;

    private final ImageView highlight;

    private final CellViewState viewState;

    public CellView(CellViewState viewState, Function<PawnViewState, ImageView> pawnRenderer) {
        this.viewState = viewState;
        this.pawnRenderer = pawnRenderer;

        minHeightProperty().bind(cellWidthProperty);
        maxHeightProperty().bind(cellWidthProperty);

        minWidthProperty().bind(cellWidthProperty);
        maxWidthProperty().bind(cellWidthProperty);

        highlight = Resources.loadCellHighlight(this);

        highlight.fitHeightProperty().bind(cellWidthProperty);
        highlight.fitWidthProperty().bind(cellWidthProperty);
        this.getChildren().add(highlight);
        highlight.setVisible(false);
    }

    public DoubleProperty cellWidthPropertyProperty() {
        return cellWidthProperty;
    }

    private ImageView renderPawn(PawnViewState pawn) {
        ImageView pawnView = pawnRenderer.apply(pawn);
        pawnView.fitHeightProperty().bind(cellWidthProperty);
        pawnView.fitWidthProperty().bind(cellWidthProperty);
        return pawnView;
    }

    public void updateView() {
        this.getChildren().clear();
        ImageView building = Resources.loadBuilding(this, viewState.getBuilding());

        building.fitHeightProperty().bind(cellWidthProperty);
        building.fitWidthProperty().bind(cellWidthProperty);

        this.getChildren().add(building);

        this.getChildren().add(highlight);
        highlight.setVisible(false);

        viewState.getPawn()
                .map(this::renderPawn)
                .ifPresent(this.getChildren()::add);
    }

    public void highlight(boolean on) {
        highlight.setVisible(on);
    }
}
