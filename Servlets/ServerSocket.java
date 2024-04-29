//requires Tomcat to be added into Classpath of Project 
package finalproject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class ServerSocket {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connection opened: " + session.getId());
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received from " + session.getId() + ": " + message);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("Error on session " + session.getId() + ": " + error.getMessage());
    }

    private void broadcast(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}
