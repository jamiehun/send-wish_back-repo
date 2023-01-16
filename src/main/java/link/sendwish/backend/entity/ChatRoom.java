package link.sendwish.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ChatRoom extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMessage> chatRoomMessages = new ArrayList<>();

    /// chatRoomMembers 리스트에 chatRoomMember 추가
    public void addMemberChatRoom(ChatRoomMember chatRoomMember) { this.chatRoomMembers.add(chatRoomMember); }

    public void deleteMemberChatRoom(ChatRoomMember chatRoomMember) {
        this.chatRoomMembers.remove(chatRoomMember);
    }

    public void addMessageChatRoom(ChatRoomMessage chatRoomMessage) { /// chatRoom에 있는 메시지들을 추가하는 메소드
        this.chatRoomMessages.add(chatRoomMessage);
    }

    public void deleteMessageChatRoom(ChatRoomMessage chatRoomMessage) {
        this.chatRoomMessages.remove(chatRoomMessage);
    }
}
