package it.polimi.ingsw.view.client;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.events.OnClientEventListener;
import it.polimi.ingsw.view.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Handles client side communication with the server.
 * It will launch events originating from the server through its {@link MessageDispatcher} and allows
 * forwarding client events to the server
 */
public class ServerHandler implements Runnable, OnClientEventListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private boolean running = true;
    private final MessageDispatcher dispatcher = new MessageDispatcher();

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    /**
     * Get the {@link MessageDispatcher} for this server handler, when a message from the server is received
     * it is converted into an event through this dispatcher, its listener can be set to handle the events.
     * @return this server handler's {@link MessageDispatcher}
     */
    public MessageDispatcher dispatcher() {
        return dispatcher;
    }

    @Override
    public void run() {
         dispatcher.setOnPingListener(() -> sendMessage(new PingMessage()));
        while (running) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());
                message.visit(dispatcher);
            } catch (NoSuchElementException e) {
                if (running) {
                    new ServerErrorMessage("Connection error", "The connection to the server has been lost")
                        .visit(dispatcher);
                }
                break;
            }
        }
        socketOut.close();
        socketIn.close();
        try {
            socket.close();
        } catch (IOException e) {
            CLI.error("Exception thrown while closing socket");
            e.printStackTrace();
        }
    }

    /**
     * Stop the {@link ServerHandler} as soon as possible
     */
    public void stop() {
        running = false;
        socketOut.close();
        socketIn.close();
    }

    @Override
    public boolean onSelectPlayerNumber(int size) {
        Message message = new SelectPlayerNumberMessage(size);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onAddUser(User user) {
        Message message = new AddUserMessage(user);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        Message message = new ChooseGodMessage(user, god);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        Message message = new PlacePawnsMessage(user, c1, c2);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        Message message = new CheckActionMessage(user, pawnId, actionIdentifier, coordinate);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        Message message = new ExecuteActionMessage(user, pawnId, actionIdentifier, coordinate);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onSelectGods(User user, List<GodIdentifier> selectedGods) {
        Message message = new SelectGodsMessage(user, selectedGods);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onChooseFirstPlayer(User self, User firstPlayer) {
        Message message = new ChooseFirstPlayerMessage(self, firstPlayer);
        sendMessage(message);
        return true;
    }

    /**
     * Serialize the message and send it through the outgoing socket
     */
    private void sendMessage(Message message){
        String serialized = Serializer.serializeMessage(message);
        synchronized (socketOut){
            socketOut.println(serialized);
            socketOut.flush();
        }
    }

}
