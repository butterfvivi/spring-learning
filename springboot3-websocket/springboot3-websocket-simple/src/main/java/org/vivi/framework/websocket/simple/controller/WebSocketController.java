package org.vivi.framework.websocket.simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.vivi.framework.websocket.simple.service.WebSocketServer;

@RestController
public class WebSocketController {

    @Autowired
    private WebSocketServer webSocketServer;

    @GetMapping("/page")
    public ModelAndView page() {
        return new ModelAndView("webSocket");
    }

    /**
     * 跳转到websocketDemo.html页面，携带自定义的cid信息。
     * http://127.0.0.1:7000/socket/page
     * @param message
     * @param toUID
     * @return
     * @throws Exception
     */
    @RequestMapping("/push/{toUID}")
    public ResponseEntity<String> pushToClient(String message, @PathVariable String toUID) throws Exception {
        webSocketServer.sendInfo(toUID,message);
        return ResponseEntity.ok("Send Success!");
    }

}
