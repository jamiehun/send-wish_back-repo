package link.sendwish.backend.repository;

import link.sendwish.backend.entity.Member;
import link.sendwish.backend.entity.MemberCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCollectionRepository extends JpaRepository<MemberCollection, Long> {
    Optional<List<MemberCollection>> findAllByMember(Member member);
}