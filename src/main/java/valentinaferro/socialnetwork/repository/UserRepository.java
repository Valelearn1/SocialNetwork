package valentinaferro.socialnetwork.repository;

import valentinaferro.socialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
