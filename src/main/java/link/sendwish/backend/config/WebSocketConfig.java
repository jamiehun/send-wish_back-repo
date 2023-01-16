package link.sendwish.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /// Client에서 websocket 연결할 때 사용할 API 경로를 설정해주는 메서드
    /// messageBroker는 송신자에게 수신자의 이전 메시지 프로토콜을 변환해주는 모듈 중 하나
    /// 요청이 오면 해당하는 통신 채널로 전송
    @Override // messageBroker는 송신자에게 수신자의 이전 메세지 프로토콜 변환해주는 모듈 중 하나. 요청 오면 해당하는 통신 채널로 전송
    public void registerStompEndpoints(StompEndpointRegistry registry) { // 최초 소켓 연결시 endpoint
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // reacti-native에서 SockJS생성자를 통해 연결
    }

    // messageBroker가 잡아서 해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해줌
    // /queue : 1대1 메시징, /topic : 1대다 메시징
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 메세지 응답 prefix
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트에서 메세지 송신시 붙여줄 prefix
    }
}
