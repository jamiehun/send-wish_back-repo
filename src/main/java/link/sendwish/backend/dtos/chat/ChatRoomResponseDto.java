package link.sendwish.backend.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long chatRoomId; /// chatRoom ID
    private String title; /// chatRoom title
}
