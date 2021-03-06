package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.model.player.Player;

/**
 * The user class represents the abstraction of a client that connects to the lobby
 * to play a game
 */
public class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User))
            return false;
        User u = (User) obj;
        return username.equals(u.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User: \"" + username + "\"";
    }

    public boolean matches(Player player) {
        return username.equals(player.getUsername());
    }
    public User(Player player) {
        this.username = player.getUsername();
    }
}
