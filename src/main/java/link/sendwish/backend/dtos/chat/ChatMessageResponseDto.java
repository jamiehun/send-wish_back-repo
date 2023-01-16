package link.sendwish.backend.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto { /// 챗룸의 메시지를 담는 Dto (sender 포함)
    private Long chatRoomId;
    private String sender;
    private String message;
}
