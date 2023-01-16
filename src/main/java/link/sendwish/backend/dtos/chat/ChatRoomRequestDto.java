package link.sendwish.backend.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDto {
    private String nickname; /// 채팅방 생성자
    private String title; /// 채팅방 제목
}
