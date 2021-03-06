package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.controls.CellViewState;
import it.polimi.ingsw.view.client.controls.PawnViewState;
import it.polimi.ingsw.view.client.controls.PlayerViewState;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Unicode representation of a {@link BoardViewState}
 */
public class CLIBoardView {
    private final int BOARD_SIZE = 5;
    private final int DIM_ROW = 8;
    private final String firstRow;
    private final String divisorRow;
    private final String lastRow;
    private final String letterRow;
    private final BoardViewState boardViewState = new BoardViewState();
    private final Map<PlayerViewState, String> playerSymbolMap = new HashMap<>();
    private final Stack<String> symbols= new Stack<>();
    // ╔ ╗ ║ ╚  ╝ ═ ╦ ╩ ╬ ╣ ╠

    public BoardViewState getBoardViewState() {
        return boardViewState;
    }

    public void newPlayer(User user) {
        PlayerViewState player = new PlayerViewState(user);
        playerSymbolMap.put(player, symbols.pop());
        boardViewState.addPlayer(player);
    }

    public CLIBoardView() {
        // Fixed row setup
        StringBuilder firstRow = new StringBuilder(" ╔");
        StringBuilder divisorRow = new StringBuilder(" ╠");
        StringBuilder lastRow = new StringBuilder(" ╚");
        StringBuilder letterRow = new StringBuilder("  ");
        for (int i = 1, j = 1; i < DIM_ROW * BOARD_SIZE ; i++) {
            if(i % DIM_ROW == 0 ) {
                firstRow.append("╦");
                divisorRow.append("╬");
                lastRow.append("╩");
            }else {
                firstRow.append("═");
                divisorRow.append("═");
                lastRow.append("═");
            }
            if(i% DIM_ROW == DIM_ROW/2) {
                letterRow.append((char)( j - 1 + 'A')); // Format i to uppercase letter
                j++;
            }
            else{
                letterRow.append(" ");
            }
        }
        firstRow.append("╗");
        divisorRow.append("╣");
        lastRow.append("╝");
        this.firstRow = firstRow.toString();
        this.divisorRow = divisorRow.toString();
        this.lastRow = lastRow.toString();
        this.letterRow = letterRow.toString();
        //Setting pawns color
        symbols.push(CLI.color("☻", Colors.GREEN));
        symbols.push(CLI.color("☻", Colors.RED));
        symbols.push(CLI.color("☻", Colors.BG_WHITE));
        symbols.push(CLI.color("☻", Colors.YELLOW));
        symbols.push(CLI.color("☻", Colors.PURPLE));
        symbols.push(CLI.color("☻", Colors.CYAN));
    }

    public String renderCell(CellViewState cell) {
        if(cell.getBuilding().hasDome()) {
            return "▓▓" + CLI.color("███", Colors.BLUE) + "▓▓,▓" +
                    CLI.color("█████", Colors.BLUE) + "▓,▓▓" +
                    CLI.color("███", Colors.BLUE) + "▓▓";
        }
        String top, fill, bot;
        switch (cell.getBuilding().getLevel()) {
            case LEVEL0 -> {
                top = "       ,   ";
                fill = "  ";
                bot = "  ,       ";
            }
            case LEVEL1 -> {
                top = "░░░░░░░,░░░";
                fill = "░░";
                bot = "░░,░░░░░░░";
            }
            case LEVEL2 -> {
                top = "███████,█░░";
                fill = "░░";
                bot = "░█,███████";
            }
            case LEVEL3 -> {
                top = "▓▓███▓▓,▓██";
                fill = "██";
                bot = "█▓,▓▓███▓▓";
            }
            default -> throw new IllegalStateException("Unexpected value: " + cell.getBuilding().getLevel());
        }
        return top +
                cell.getPawn().map(this::renderPawn).orElse(fill) +
                bot;
    }

    public String renderPawn(PawnViewState pawn) {
            return playerSymbolMap.get(pawn.getOwner()) + pawn.getId();
    }

    public String renderBoard() {
        StringBuilder builder = new StringBuilder("\n\n");
        builder.append(firstRow).append('\n');
        for (int i = BOARD_SIZE - 1; i >= 0 ; i--) {
            StringBuilder line1 = new StringBuilder(" ║");
            StringBuilder line2 = new StringBuilder().append((char)(i + 1 + '0')).append("║"); // format i to number starting from 1
            StringBuilder line3 = new StringBuilder(" ║");
            for(int j = 0; j < BOARD_SIZE; j++) {
                Coordinate c = new Coordinate(j,i);
                String s = renderCell(boardViewState.cellAt(c));
                String[] array = s.split(",");
                line1.append(array[0]).append("║");
                line2.append(array[1]).append("║");
                line3.append(array[2]).append("║");
            }
            builder.append(line1).append('\n')
                    .append(line2).append('\n')
                    .append(line3).append('\n');
            if(i != 0 ) {
                builder.append(divisorRow);
                builder.append('\n');
            }else
                builder.append(lastRow);
        }
        builder.append('\n').append(letterRow);
        return builder.toString();
    }
}
