package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {

}
