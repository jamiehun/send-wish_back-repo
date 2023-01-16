package link.sendwish.backend.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ChatMessage extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum MessageType { /// 메시지 타입 : 입장, 채팅
        ENTER, TALK
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String message;
}
