package handler;

import org.springframework.web.socket.TextMessage;

public interface CoreHandler {

    void handleMessage(TextMessage message);

}
