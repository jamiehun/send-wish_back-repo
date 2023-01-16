package link.sendwish.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    /// Client에서 websocket 연결할 때 사용할 API 경로를 설정해주는 메서드
    /// messageBroker는 송신자에게 수신자의 이전 메시지 프로토콜을 변환해주는 모듈 중 하나
    /// 요청이 오면 해당하는 통신 채널로 전송
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { /// 최초 소켓 연결시 endpoint
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS(); /// react-native에서 SockJS를 통해 연결
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // messageBroker가 잡아서 해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해줌
        // /queue : 1대1 메시징, /topic : 1대다 메시징
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 보낼 때 관련 경로 설정 (/app 붙어 있으면 브로커로 보내짐)
    }
}
