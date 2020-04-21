package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class Game {
    private List<Player> players;
    private Board board;
    private int turn = 0;
    private int eliminationTurn = turn;

    public Game(List<Player> players) {
        this.players = players;
    }

    public int getPlayerNumber() {
        return players.size() ;
    }

    public Player getPlayer ( int i){
        return players.get(i);
    }

    public int getTurn() {
        return turn;
    }

    public void elimination(Player player) throws IllegalStateException {
        if (players.size() > 0) {
            players.remove(player);
            eliminationTurn = turn;
            } else {
                throw new IllegalStateException();
            }
        }

    public void nextTurn() {
        turn++;
    }

    public Player getCurrentPlayer() {
        if (players.size() == 3) {
            return players.get(turn % 3);
        } else {
            return players.get((turn - eliminationTurn) % 2);
        }
    }
}
