package edu.kh.project.chatting.config;

import java.net.http.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.chatting.handler.ChattingWebsocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration	// 서버 실행 시 메서드 모두 수행
@EnableWebSocket // 웹소켓을 사용하겠다라는 설정(활성화)
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer{

	// SessionHandshakeInterceptor 빈 의존성 주입 받기
	private final HandshakeInterceptor handshakeInterceptor;
	
	// 채팅 핸들러 의존성 주입받기
	private final ChattingWebsocketHandler chattingWebsocketHandler;
	
	// 웹소켓 핸들러를 등록하는 메서드
	@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
							// 핸들러 등록
			registry.addHandler(chattingWebsocketHandler, "/chattingSock")
			
							// 세션 가로채는 인터셉터 등록
							.addInterceptors(handshakeInterceptor)
							
							// 웹소케 요청을 허용할 주소 패턴
							.setAllowedOriginPatterns(
									"http://localhost/", 
									"http://127.0.0.1/", 
									"http://192.168.10.32/")
							// SockJS 지원 + 브라우저 호환성 증가
							.withSockJS();
		}
}
