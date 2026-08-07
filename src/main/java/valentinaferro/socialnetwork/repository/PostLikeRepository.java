package valentinaferro.socialnetwork.repository;

import valentinaferro.socialnetwork.entity.Post;
import valentinaferro.socialnetwork.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    List<PostLike> findByPost(Post post);
}
