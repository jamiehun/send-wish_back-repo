package link.sendwish.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFriendAddRequestDto {
    private String memberNickname; // 친구를 추가할 본인의 memberNickname
    private String addMemberNickname; // 추가할 친구의 memberNickname
}
