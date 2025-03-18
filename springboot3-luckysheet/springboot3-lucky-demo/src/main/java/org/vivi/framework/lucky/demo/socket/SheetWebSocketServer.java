package org.vivi.framework.lucky.demo.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.vivi.framework.lucky.demo.dto.ResponseDTO;
import org.vivi.framework.lucky.demo.utils.CompressUtils;
import org.vivi.framework.lucky.demo.utils.HttpUtils;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SheetWebSocketServer extends WebSocketServer {
	// 存储连接及昵称
	private static Map<WebSocket, String> connMap = new ConcurrentHashMap<>();

	private static int onlineCount = 0;

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	public SheetWebSocketServer(int port) {
		super(new InetSocketAddress(port));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		String resource = conn.getResourceDescriptor();
		String name = HttpUtils.getParameter(resource, "name");
		if (name == null) {
			name = "" + onlineCount++;
		}
		connMap.put(conn, name);
		addOnlineCount(); // 在线数加1
		log.info("{} 加入,在线人数 = {}", name, connMap.size());

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		String name = connMap.remove(conn);
		subOnlineCount(); // 在线数减1
		log.info("{} 离开", name);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if (null != message && message.length() != 0) {
			try {
				if ("rub".equals(message)) {
					return;
				}
				String unMessage = CompressUtils.unCompressURI(message);
				if (log.isTraceEnabled())
					log.trace(unMessage);

				JSONObject jsonObject = JSON.parseObject(unMessage);
				// 广播
				String resource = conn.getResourceDescriptor();
				String name = HttpUtils.getParameter(resource, "name");
				connMap.forEach((socket, n) -> {
					if (conn == socket) {
						return;
					}
					if ("mv".equals(jsonObject.getString("t"))) {
						socket.send(JSON.toJSONString(new ResponseDTO(3, name, name, unMessage)));
					} else if (!"shs".equals(jsonObject.getString("t"))) {
						socket.send(JSON.toJSONString(new ResponseDTO(2, name, name, unMessage)));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception error) {
		log.error("发生错误");
		error.printStackTrace();
	}

	@Override
	public void onStart() {
		log.info("ws start http://127.0.0.1:{}", getAddress().getPort());
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		SheetWebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		SheetWebSocketServer.onlineCount--;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
