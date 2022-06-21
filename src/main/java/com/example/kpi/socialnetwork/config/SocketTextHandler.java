package com.example.kpi.socialnetwork.config;

import com.example.kpi.socialnetwork.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
//        var method = jsonObject.getString("method");
//        if (method.equals("tweet"))
//        {
//
//        }
        session.sendMessage(new TextMessage("Hi how may we help you?"));
    }

}
