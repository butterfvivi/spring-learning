package org.vivi.spring3.websocket.config;

import jakarta.websocket.Session;

public class WebSocketClient {

    private Session session;

    private String uri;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
