package link.sendwish.backend.repository;

import link.sendwish.backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
}
