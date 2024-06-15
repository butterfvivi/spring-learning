//package org.vivi.spring3.iwebsocket.job;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.vivi.spring3.iwebsocket.service.WebSocketService;
//
//import java.util.List;
//
//@EnableScheduling
//@Configuration
//@Slf4j
//public class WebSocketJob {
//
//    @Autowired
//    private WebSocketService webSocketService;
//
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void cleanToken() {
//        //10s推送一次
//        List<String> allUser = webSocketService.getAllUser();
//        //自己可以定义不同用户发送的信息，这里不做演示了
//        webSocketService.sendOpenAllUserMessage(allUser, "waring！");
//    }
//
//}
