package com.esprow.interview.sklroman.stockmarket.ws;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/market/{clientId}")
public class WebSocketService {

    private static final Map<String, List<Session>> sessions = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(String message, Session session)
            throws IOException, InterruptedException {
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        if (sessions.containsKey(clientId)) {
            sessions.get(clientId).add(session);
        } else {
            List<Session> list = new ArrayList<>();
            list.add(session);
            sessions.put(clientId, list);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId) {
        if (sessions.containsKey(clientId)) {
            if (sessions.get(clientId).size() > 1) {
                sessions.get(clientId).remove(session);
            } else {
                sessions.remove(clientId);
            }
        }
    }

    public void sendMsg(String clientId, String msg) {
        if (sessions.containsKey(clientId)) {
            for (Session session : sessions.get(clientId)) {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(msg);
                }
            }
        }
    }
}
